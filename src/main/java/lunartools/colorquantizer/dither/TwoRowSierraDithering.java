package lunartools.colorquantizer.dither;

import java.util.ArrayList;

import lunartools.ColourRGBG;
import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

/**
 * Two-Row-Sierra dithering.
 * Published 1989-1990 by Frankie Sierra.
 * 
 * @author Thomas Mattel
 */
public class TwoRowSierraDithering implements IDitheringAlgorithm{

	@Override
	public void dither(int[] pixeldata,int width,int height,Palette palette,ArrayList<ICube> cubes) {
		int[] pixeldataR = new int[pixeldata.length];
		int[] pixeldataG = new int[pixeldata.length];
		int[] pixeldataB = new int[pixeldata.length];
		for(int i=0;i<pixeldata.length;i++) {
			pixeldataR[i]=((pixeldata[i]>>12)&0b111111110000);
			pixeldataG[i]=((pixeldata[i]>>4) &0b111111110000);
			pixeldataB[i]=((pixeldata[i]<<4) &0b111111110000);
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
	
	
				if(x<width-2) {
					pixeldataR[(y)*width+x+2]+=deltaRed*3;
					pixeldataG[(y)*width+x+2]+=deltaGreen*3;
					pixeldataB[(y)*width+x+2]+=deltaBlue*3;
					if(y<height-1) {
						pixeldataR[(y+1)*width+x+2]+=deltaRed;
						pixeldataG[(y+1)*width+x+2]+=deltaGreen;
						pixeldataB[(y+1)*width+x+2]+=deltaBlue;
					}
				}
	
				if(x<width-1) {
					pixeldataR[(y)*width+x+1]+=deltaRed<<2;
					pixeldataG[(y)*width+x+1]+=deltaGreen<<2;
					pixeldataB[(y)*width+x+1]+=deltaBlue<<2;
					if(y<height-1) {
						pixeldataR[(y+1)*width+x+1]+=deltaRed<<1;
						pixeldataG[(y+1)*width+x+1]+=deltaGreen<<1;
						pixeldataB[(y+1)*width+x+1]+=deltaBlue<<1;
					}				
				}
	
				if(y<height-1) {
					pixeldataR[(y+1)*width+x]+=deltaRed*3;
					pixeldataG[(y+1)*width+x]+=deltaGreen*3;
					pixeldataB[(y+1)*width+x]+=deltaBlue*3;
					if(x>0) {
						pixeldataR[(y+1)*width+x-1]+=deltaRed<<1;
						pixeldataG[(y+1)*width+x-1]+=deltaGreen<<1;
						pixeldataB[(y+1)*width+x-1]+=deltaBlue<<1;
					}
					if(x>1) {
						pixeldataR[(y+1)*width+x-2]+=deltaRed;
						pixeldataG[(y+1)*width+x-2]+=deltaGreen;
						pixeldataB[(y+1)*width+x-2]+=deltaBlue;
					}
				}

			}
		}
	}

}
