package lunartools.colorquantizer.dither;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import lunartools.ColourRGBG;
import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

/**
 * An own algorithm, work in progress.
 * 
 * @author Thomas Mattel
 */
public class SimpleDithering1 implements IDitheringAlgorithm{

	/**
	 * Replacing colours:
	 * <br>-alternating with the closest darker and closest brighter pixel
	 * <br>-the start pixel (darker or brighter) is alterating each line, too.
	 */
	@Override
	public void dither(int[] pixeldata,int width,int height,Palette palette,ArrayList<ICube> cubes) {
		ICube cube;
		int[] pixelHash1=new int[0x1000000];
		int[] pixelHash2=new int[0x1000000];
		for(int i=0;i<cubes.size();i++) {
			cube=cubes.get(i);
			replaceCubeColorsHash(cube,palette,pixelHash1,pixelHash2);
		}

		int index=0;
		for(int y=0;y<height;y++) {
			if((y&1)==0) {
				for(int x=0;x<width>>1;x++) {
					pixeldata[index]=pixelHash1[pixeldata[index++]];
					pixeldata[index]=pixelHash2[pixeldata[index++]];
				}
			}else {
				for(int x=0;x<width>>1;x++) {
					pixeldata[index]=pixelHash2[pixeldata[index++]];
					pixeldata[index]=pixelHash1[pixeldata[index++]];
				}
			}
		}
	}

	/**
	 * The cube is split in three parts.
	 * <br>-the pixel of the left third are replaced alterating with hi and center pixel
	 * <br>-the pixel of the center third are replaced with center pixel
	 * <br>-the pixel of the right third are replaced alterating with lo and center pixel
	 * 
	 * @param cube One cube that represents one colour of the palette
	 * @param palette The palette, one entry is the average colour of one cube
	 * @param pixelcount1 Pixel hashtable lo (left)
	 * @param pixelcount2 Picel hashtable hi (right)
	 */
	private void replaceCubeColorsHash(ICube cube,Palette palette,int[] pixelcount1,int[] pixelcount2) {
		TreeSet<ColourRGBG> set=new TreeSet<ColourRGBG>(new Comparator<ColourRGBG>() {
			@Override
			public int compare(ColourRGBG color1, ColourRGBG color2) {
				return color1.getGrey()>color2.getGrey()?1:-1;
			}
		});
		for(int x=0;x<cube.getColours().size();x++) {
			set.add(cube.getColours().get(x));
		}

		int pixelCenter=cube.getAveragePixel();
		int pixelHi;
		int pixelLo;

		int third=set.size()/3;
		int counter=0;

		Iterator<ColourRGBG> iterator=set.descendingIterator();
		while(iterator.hasNext()) {
			counter++;
			ColourRGBG color=iterator.next();
			int pixelToReplace=color.getColour();
			pixelCenter=palette.findClosestColour(color).getColour();
			ColourRGBG[] neighbours=palette.findNeighbours(color);
			if(neighbours[0]==null) {
				pixelLo=pixelCenter;
			}else {
				pixelLo=neighbours[0].getColour();
			}
			if(neighbours[1]==null) {
				pixelHi=pixelCenter;
			}else {
				pixelHi=neighbours[1].getColour();
			}

			if(counter<=third) {
				pixelcount1[pixelToReplace]=pixelHi;
				pixelcount2[pixelToReplace]=pixelCenter;
			}else if(counter>=set.size()-third) {
				pixelcount1[pixelToReplace]=pixelCenter;
				pixelcount2[pixelToReplace]=pixelLo;
			}else {
				pixelcount1[pixelToReplace]=pixelCenter;
				pixelcount2[pixelToReplace]=pixelCenter;
			}
		}
	}

}
