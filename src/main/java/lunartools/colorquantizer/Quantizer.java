package lunartools.colorquantizer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ColourRGBG;
import lunartools.ImageTools;
import lunartools.colorquantizer.cubes.Cube_splitCenter;
import lunartools.colorquantizer.cubes.ICube;
import lunartools.colorquantizer.dither.IDitheringAlgorithm;
import lunartools.colorquantizer.recursions.IRecursion;
import lunartools.colorquantizer.recursions.Recursion_minSqrt;

/**
 * Colour quantizer based on Median Cut algorithm.
 * 
 * @author Thomas Mattel
 */
public class Quantizer {
	private static Logger logger = LoggerFactory.getLogger(Quantizer.class);
	private GPAC gpac;
	private ArrayList<ICube> cubes=new ArrayList<ICube>();

	Quantizer(GPAC gpac) {
		this.gpac=gpac;
	}

	/** for research purpose only */
	private ICube getCubeInstance() {
		return new Cube_splitCenter();
		//return new Cube_splitNew();
	}

	/** for research purpose only */
	private IRecursion getRecursionInstance() {
		//return new Recursion_min100();
		return new Recursion_minSqrt();
	}

	/**
	 * Quantizes the given BufferedImage to the given number of colours.
	 * <br>The BufferedImage must contain a <code>DataBufferInt</code>.
	 * <br><b>It does not yet work with a <code>DataBufferByte</code></b>.
	 * 
	 * @param bufferedImage The BufferedImage to quantize.
	 * @param numberOfColours The number of colours.
	 */
	void quantizeColors(BufferedImage bufferedImage,int numberOfColours) {
		final int[] pixeldata=ImageTools.getRgbIntsFromBufferedImage(bufferedImage);
		int width=bufferedImage.getWidth();
		int height=bufferedImage.getHeight();
		Palette palette=quantizeColors(ImageTools.createPixelCountArray(pixeldata),width,height,numberOfColours);
		if(palette!=null) {
			IDitheringAlgorithm ditheringAlgorithm=gpac.getDitheringAlgorithm();
			ditheringAlgorithm.dither(pixeldata, width, height,palette,cubes);
		}
		ImageTools.writeRgbIntsToBufferedImage(pixeldata,bufferedImage);
	}

	/**
	 * Quantizes the pixel of the given ArrayList to the given number of colours.
	 * <br>One entry of the ArrayList is an int array, containing the pixel in int RGB format of a BufferedImage.
	 * <br>This method is used to quantize multiple images to one common colour palette.
	 * <br>All images (int arrays of the list) must have the same width and height.
	 * 
	 * @param arraylistImagesPixelInts A list of image data in int RGB format to quantize.
	 * @param width The width of all images.
	 * @param height Ther height of all images.
	 * @param numberOfColours The number of colours.
	 */
	void quantizeColors(ArrayList<int[]> arraylistImagesPixelInts,int width,int height,int numberOfColours) {
		int[] pixelcount=new int[0x1000000];
		for(int k=0;k<arraylistImagesPixelInts.size();k++) {
			int[] imageRgbInts=arraylistImagesPixelInts.get(k);
			for(int i=0;i<imageRgbInts.length;i++) {
				pixelcount[imageRgbInts[i]]++;
			}
		}

		Palette palette=quantizeColors(pixelcount,width,height,numberOfColours);
		if(palette!=null) {
			IDitheringAlgorithm ditheringAlgorithm=gpac.getDitheringAlgorithm();
			for(int k=0;k<arraylistImagesPixelInts.size();k++) {
				int[] imageRgbInts=arraylistImagesPixelInts.get(k);
				ditheringAlgorithm.dither(imageRgbInts, width, height,palette,cubes);
			}
		}
	}

	private Palette quantizeColors(int[] pixelcount,int width,int height,int paletteSize) {
		logger.debug("analyzing pixeldata "+width+"x"+height+"...");
		ICube cube=createFirstCubeFromPixelCountArray(pixelcount);
		if(cube.getColours().size()<=paletteSize) {
			logger.trace("number of colours: "+cube.getNumberOfColours()+", no quantizing necessary");
			return null;
		}
		logger.trace("number of colours: "+cube.getNumberOfColours()+", quantizing to: "+paletteSize);
		IRecursion recursion=getRecursionInstance();
		this.cubes=recursion.doRecursion(cube, paletteSize>>1);
		logger.trace("number of cubes after recursion: "+cubes.size());
		splitCubesToPaletteSize(paletteSize);
		logger.trace("number of cubes after quantizing: "+cubes.size());
		return calcPalette(cubes);
	}

	/** create first cube containing all colours */
	private ICube createFirstCubeFromPixelCountArray(int[] pixelcount) {
		ICube cube=getCubeInstance();
		for(int i=0;i<pixelcount.length;i++) {
			if(pixelcount[i]>0) {
				cube.addColor(new ColourRGBG(i));
			}
		}
		return cube;
	}

	private void splitCubesToPaletteSize(int paletteSize) {
		while(cubes.size()<paletteSize) {
			int maxScore=0;
			ICube cubeSplit=null;
			for(int i=0;i<cubes.size();i++) {
				ICube cube=cubes.get(i);
				if(cube!=null && !cube.isFinal() && cube.getScore()>maxScore) {
					cubeSplit=cube;
					maxScore=cube.getScore();
				}
			}
			if(cubeSplit==null) {
				return;
			}
			ICube cubeHi=cubeSplit.getChildCubeHi();
			ICube cubeLo=cubeSplit.getChildCubeLo();
			if(!cubeSplit.isFinal()) {
				cubes.remove(cubeSplit);
				cubes.add(cubeHi);
				cubes.add(cubeLo);
			}
		}
	}

	private Palette calcPalette(ArrayList<ICube> cubes) {
		Palette colorsPalette=new Palette();
		for(int i=0;i<cubes.size();i++) {
			ICube cube=cubes.get(i);
			colorsPalette.addColour(new ColourRGBG(cube.getAveragePixel()));
		}
		return colorsPalette;
	}

}
