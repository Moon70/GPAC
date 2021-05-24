package lunartools.colorquantizer.dither;

import java.util.ArrayList;

import lunartools.ColourRGBG;
import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

/**
 * Atkinson dithering.
 * <br>Published in the mid 1980´s by Peter Atkinson.
 * 
 * @author Thomas Mattel
 */
public class AtkinsonDithering implements IDitheringAlgorithm{

	@Override
	public void dither(int[] pixeldata,int width,int height,Palette palette,ArrayList<ICube> cubes) {
		int[] pixeldataR = new int[pixeldata.length];
		int[] pixeldataG = new int[pixeldata.length];
		int[] pixeldataB = new int[pixeldata.length];
		for(int i=0;i<pixeldata.length;i++) {
			pixeldataR[i]=((pixeldata[i]>>13)&0b11111111000);
			pixeldataG[i]=((pixeldata[i]>>5) &0b11111111000);
			pixeldataB[i]=((pixeldata[i]<<3) &0b11111111000);
		}

		int red,green,blue,deltaRed,deltaGreen,deltaBlue;
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {

				red=pixeldataR[y*width+x]>>3;
				if(red>255) {
					red=255;
				}else if(red<0) {
					red=0;
				}
				green=pixeldataG[y*width+x]>>3;
				if(green>255) {
					green=255;
				}else if(green<0) {
					green=0;
				}
				blue=pixeldataB[y*width+x]>>3;
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
					pixeldataR[(y)*width+x+2]+=deltaRed;
					pixeldataG[(y)*width+x+2]+=deltaGreen;
					pixeldataB[(y)*width+x+2]+=deltaBlue;
				}
	
				if(x<width-1) {
					pixeldataR[(y)*width+x+1]+=deltaRed;
					pixeldataG[(y)*width+x+1]+=deltaGreen;
					pixeldataB[(y)*width+x+1]+=deltaBlue;
					if(y<height-1) {
						pixeldataR[(y+1)*width+x+1]+=deltaRed;
						pixeldataG[(y+1)*width+x+1]+=deltaGreen;
						pixeldataB[(y+1)*width+x+1]+=deltaBlue;
					}				
				}

				if(y<height-1) {
					pixeldataR[(y+1)*width+x]+=deltaRed;
					pixeldataG[(y+1)*width+x]+=deltaGreen;
					pixeldataB[(y+1)*width+x]+=deltaBlue;
					if(x>0) {
						pixeldataR[(y+1)*width+x-1]+=deltaRed;
						pixeldataG[(y+1)*width+x-1]+=deltaGreen;
						pixeldataB[(y+1)*width+x-1]+=deltaBlue;
					}
				}
	
				if(y<height-2) {
					pixeldataR[(y+2)*width+x]+=deltaRed;
					pixeldataG[(y+2)*width+x]+=deltaGreen;
					pixeldataB[(y+2)*width+x]+=deltaBlue;
				}

			}
		}
	}

}
