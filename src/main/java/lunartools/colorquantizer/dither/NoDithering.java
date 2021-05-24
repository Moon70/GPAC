package lunartools.colorquantizer.dither;

import java.util.ArrayList;

import lunartools.ColourRGBG;
import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

/**
 * No dithering
 * <br>Each pixel is replaced with the closest pixel of the palette
 * 
 * @author Thomas Mattel
 */
public class NoDithering implements IDitheringAlgorithm{

	@Override
	public void dither(int[] pixeldata,int width,int height, Palette palette,ArrayList<ICube> cubes) {
		ICube cube;
		int[] pixelHash=new int[0x1000000];
		for(int i=0;i<cubes.size();i++) {
			cube=cubes.get(i);
			updatePixelHash(cube,pixelHash,palette);
		}
		for(int index=0;index<pixeldata.length;index++) {
			pixeldata[index]=pixelHash[pixeldata[index]];
		}
	}

	private void updatePixelHash(ICube cube,int[] pixelHash, Palette palette) {
		ArrayList<ColourRGBG> colours=cube.getColours();
		for(int x=0;x<colours.size();x++) {
			int pixelToReplace=colours.get(x).getColour();
			int closestPixel=palette.findClosestColour(colours.get(x)).getColour();
			pixelHash[pixelToReplace]=closestPixel;
		}
	}

}
