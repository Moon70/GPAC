package lunartools.colorquantizer.dither;

import java.util.ArrayList;

import lunartools.ColourRGBG;
import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

/**
 * Jarvis, Judice and Ninke dithering.
 * <br>Publisher 1976.
 * 
 * @author Thomas Mattel
 */
public class JarvisJudiceNinkeDithering implements IDitheringAlgorithm{

	@Override
	public void dither(int[] pixeldata,int width,int height,Palette palette,ArrayList<ICube> cubes) {
		int[] pixeldataR = new int[pixeldata.length];
		int[] pixeldataG = new int[pixeldata.length];
		int[] pixeldataB = new int[pixeldata.length];
		for(int i=0;i<pixeldata.length;i++) {
			pixeldataR[i]=((pixeldata[i]>>16)&0b11111111)*48;
			pixeldataG[i]=((pixeldata[i]>>8) &0b11111111)*48;
			pixeldataB[i]=((pixeldata[i])	&0b11111111)*48;
		}

		int red,green,blue,deltaRed,deltaGreen,deltaBlue;
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {

				red=pixeldataR[y*width+x]/48;
				if(red>255) {
					red=255;
				}else if(red<0) {
					red=0;
				}
				green=pixeldataG[y*width+x]/48;
				if(green>255) {
					green=255;
				}else if(green<0) {
					green=0;
				}
				blue=pixeldataB[y*width+x]/48;
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
					pixeldataR[(y)*width+x+2]+=deltaRed*5;
					pixeldataG[(y)*width+x+2]+=deltaGreen*5;
					pixeldataB[(y)*width+x+2]+=deltaBlue*5;

					if(y<height-1) {
						pixeldataR[(y+1)*width+x+2]+=deltaRed*3;
						pixeldataG[(y+1)*width+x+2]+=deltaGreen*3;
						pixeldataB[(y+1)*width+x+2]+=deltaBlue*3;
					}
					if(y<height-2) {
						pixeldataR[(y+2)*width+x+2]+=deltaRed*1;
						pixeldataG[(y+2)*width+x+2]+=deltaGreen*1;
						pixeldataB[(y+2)*width+x+2]+=deltaBlue*1;
					}
				}

				if(x<width-1) {
					pixeldataR[(y)*width+x+1]+=deltaRed*7;
					pixeldataG[(y)*width+x+1]+=deltaGreen*7;
					pixeldataB[(y)*width+x+1]+=deltaBlue*7;

					if(y<height-1) {
						pixeldataR[(y+1)*width+x+1]+=deltaRed*5;
						pixeldataG[(y+1)*width+x+1]+=deltaGreen*5;
						pixeldataB[(y+1)*width+x+1]+=deltaBlue*5;
					}				
					if(y<height-2) {
						pixeldataR[(y+2)*width+x+1]+=deltaRed*3;
						pixeldataG[(y+2)*width+x+1]+=deltaGreen*3;
						pixeldataB[(y+2)*width+x+1]+=deltaBlue*3;
					}
				}

				if(y<height-1) {
					pixeldataR[(y+1)*width+x]+=deltaRed*7;
					pixeldataG[(y+1)*width+x]+=deltaGreen*7;
					pixeldataB[(y+1)*width+x]+=deltaBlue*7;

					if(x>0) {
						pixeldataR[(y+1)*width+x-1]+=deltaRed*5;
						pixeldataG[(y+1)*width+x-1]+=deltaGreen*5;
						pixeldataB[(y+1)*width+x-1]+=deltaBlue*5;
					}
					if(x>1) {
						pixeldataR[(y+1)*width+x-2]+=deltaRed*3;
						pixeldataG[(y+1)*width+x-2]+=deltaGreen*3;
						pixeldataB[(y+1)*width+x-2]+=deltaBlue*3;
					}
				}

				if(y<height-2) {
					pixeldataR[(y+2)*width+x]+=deltaRed*5;
					pixeldataG[(y+2)*width+x]+=deltaGreen*5;
					pixeldataB[(y+2)*width+x]+=deltaBlue*5;

					if(x>0) {
						pixeldataR[(y+2)*width+x-1]+=deltaRed*3;
						pixeldataG[(y+2)*width+x-1]+=deltaGreen*3;
						pixeldataB[(y+2)*width+x-1]+=deltaBlue*3;
					}
					if(x>1) {
						pixeldataR[(y+2)*width+x-2]+=deltaRed*1;
						pixeldataG[(y+2)*width+x-2]+=deltaGreen*1;
						pixeldataB[(y+2)*width+x-2]+=deltaBlue*1;
					}
				}

			}
		}
	}

}
