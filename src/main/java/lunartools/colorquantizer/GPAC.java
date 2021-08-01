package lunartools.colorquantizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ImageTools;
import lunartools.colorquantizer.dither.AtkinsonDithering;
import lunartools.colorquantizer.dither.BurkesDithering;
import lunartools.colorquantizer.dither.FloydSteinbergDithering;
import lunartools.colorquantizer.dither.JarvisJudiceNinkeDithering;
import lunartools.colorquantizer.dither.SierraDithering;
import lunartools.colorquantizer.dither.SierraLiteDithering;
import lunartools.colorquantizer.dither.StuckiDithering;
import lunartools.colorquantizer.dither.TwoRowSierraDithering;
import lunartools.colorquantizer.dither.SimpleDithering1;
import lunartools.colorquantizer.dither.NoDithering;
import lunartools.colorquantizer.dither.IDitheringAlgorithm;

/**
 * GPAC - Gif Piece A Change.
 * <br>A colour quantizer, based on the 'Median Cut' algorithm.
 * <br>Before saving an image as GIF using Java API, use this API to reduce the number of colours
 * to 256 to get a better image quality.
 * 
 * @author Thomas Mattel
 */
public class GPAC {
	private static Logger logger = LoggerFactory.getLogger(GPAC.class);
	@SuppressWarnings("unused")
	private QuantizerAlgorithm quantizerAlgorithm = QuantizerAlgorithm.MEDIAN_CUT;
	private DitheringAlgorithm ditheringAlgorithm = DitheringAlgorithm.NO_DITHERING;

	/**
	 * Select the quantizer algorithm.
	 * <br>Currently, only one algorithm is implemented: Median Cut
	 * <br><b>Therefore, this method is useless at the moment</b>
	 * @param quantizerAlgorithm
	 */
	public GPAC setQuantizerAlgorithm(QuantizerAlgorithm quantizerAlgorithm) {
		this.quantizerAlgorithm=quantizerAlgorithm;
		logger.debug("Quantizer algorithm selected: {}",quantizerAlgorithm);
		return this;
	}

	/**
	 * Select the dithering algorithm.
	 * 
	 * @see NoDithering
	 * @see SimpleDithering1
	 * @see FloydSteinbergDithering
	 * @see JarvisJudiceNinkeDithering
	 * @see StuckiDithering
	 * @see AtkinsonDithering
	 * @see BurkesDithering
	 * @see SierraDithering
	 * @see TwoRowSierraDithering
	 * @see SierraLiteDithering
	 * @param ditheringAlgorithm
	 */
	public GPAC setDitheringAlgorithm(DitheringAlgorithm ditheringAlgorithm) {
		this.ditheringAlgorithm=ditheringAlgorithm;
		logger.debug("Dithering algorithm selected: {}",ditheringAlgorithm);
		return this;
	}

	IDitheringAlgorithm getDitheringAlgorithm() {
		switch(ditheringAlgorithm) {
		case NO_DITHERING:
			return new NoDithering();
		case SIMPLE_DITHERING1:
			return new SimpleDithering1();
		case FLOYD_STEINBERG:
			return new FloydSteinbergDithering();
		case JARVIS_JUDICE_NINKE:
			return new JarvisJudiceNinkeDithering();
		case STUCKI:
			return new StuckiDithering();
		case ATKINSON:
			return new AtkinsonDithering();
		case BURKES:
			return new BurkesDithering();
		case SIERRA:
			return new SierraDithering();
		case TWO_ROW_SIERRA:
			return new TwoRowSierraDithering();
		case SIERRA_LITE:
			return new SierraLiteDithering();
		default:
			return null;
		}
	}

	/**
	 * Returns a BufferesImage read from the given file, quantized to the given number of colours.
	 * 
	 * @param file
	 * @param numberOfColours
	 * @return
	 * @throws IOException
	 */
	public BufferedImage quantizeColours(File file,int numberOfColours) throws IOException {
		BufferedImage bufferedImage=ImageTools.createBufferedImage_intRGB(file);
		quantizeColours(bufferedImage,numberOfColours);
		return bufferedImage;
	}

	/**
	 * Changes the pixeldata of the given <code>BufferedImage</code> so that the image
	 * uses at most <code>numberOfColours</code> colours.
	 * 
	 * @param bufferedImage The bufferedImage to be changed
	 * @param numberOfColours The number of colours used after quantizing
	 */
	public void quantizeColours(BufferedImage bufferedImage,int numberOfColours) {
		new Quantizer(this).quantizeColors(bufferedImage,numberOfColours);
	}

	/**
	 * Changes the pixeldata of the given int array (RGB format) so that it
	 * uses at most <code>numberOfColours</code> colours.
	 * 
	 * @param pixelcount
	 * @param paletteSize
	 */
	public void quantizeColours(ArrayList<int[]> arraylistImagesPixelInts,int width,int height,int numberOfColours) {
		new Quantizer(this).quantizeColors(arraylistImagesPixelInts,width,height,numberOfColours);
	}

}
