package lunartools.colorquantizer.dither;

import java.util.ArrayList;

import lunartools.colorquantizer.Palette;
import lunartools.colorquantizer.cubes.ICube;

public interface IDitheringAlgorithm {

	public void dither(int[] pixeldata,int width,int height,Palette palette,ArrayList<ICube> cubes);

}
