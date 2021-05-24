package lunartools.colorquantizer.dither;

import java.util.ArrayList;

import lunartools.ColourRGBG;
import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

/**
 * Floyd-Steinberg dithering.
 * <br>Published 1976 by Robert W. Floyd and Louis Steinberg.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering">Floyd-Steinberg dithering at Wikipedia</a>
 * @author Thomas Mattel
 */
public class FloydSteinbergDithering implements IDitheringAlgorithm{

	@Override
	public void dither(int[] pixeldata,int width,int height,Palette palette,ArrayList<ICube> cubes) {
		int[] pixeldataR = new int[pixeldata.length];
		int[] pixeldataG = new int[pixeldata.length];
		int[] pixeldataB = new int[pixeldata.length];
		for(int i=0;i<pixeldata.length;i++) {
			pixeldataR[i]=(pixeldata[i]>>12)&0b111111110000;
			pixeldataG[i]=(pixeldata[i]>>4) &0b111111110000;
			pixeldataB[i]=(pixeldata[i]<<4) &0b111111110000;
		}

		int red,green,blue,deltaRed,deltaGreen,deltaBlue;
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {

				red=pixeldataR[y*width+x]>>4;
				if(red>255) {
					red=255;
				}else if(red<0) {
					red=0;
				}
				green=pixeldataG[y*width+x]>>4;
				if(green>255) {
					green=255;
				}else if(green<0) {
					green=0;
				}
				blue=pixeldataB[y*width+x]>>4;
				if(blue>255) {
					blue=255;
				}else if(blue<0) {
					blue=0;
				}
	
				ColourRGBG newPixel=palette.findClosestColour(red,green,blue);
				pixeldata[y*width+x]=newPixel.getColour();
	
				deltaRed=red-newPixel.getRed();
				deltaGreen=green-newPixel.getGreen();
				deltaBlue=blue-newPixel.getBlue();
	
				if(x<width-1) {
					pixeldataR[(y)*width+x+1]+=deltaRed*7;
					pixeldataG[(y)*width+x+1]+=deltaGreen*7;
					pixeldataB[(y)*width+x+1]+=deltaBlue*7;
					if(y<height-1) {
						pixeldataR[(y+1)*width+x+1]+=deltaRed*1;
						pixeldataG[(y+1)*width+x+1]+=deltaGreen*1;
						pixeldataB[(y+1)*width+x+1]+=deltaBlue*1;
					}				
				}
				if(y<height-1) {
					pixeldataR[(y+1)*width+x]+=deltaRed*5;
					pixeldataG[(y+1)*width+x]+=deltaGreen*5;
					pixeldataB[(y+1)*width+x]+=deltaBlue*5;
					if(x>0) {
						pixeldataR[(y+1)*width+x-1]+=deltaRed*3;
						pixeldataG[(y+1)*width+x-1]+=deltaGreen*3;
						pixeldataB[(y+1)*width+x-1]+=deltaBlue*3;
					}
				}
			}
		}
	}

}
