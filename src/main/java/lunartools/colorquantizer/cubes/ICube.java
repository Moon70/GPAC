package lunartools.colorquantizer.cubes;

import java.util.ArrayList;

import lunartools.ColourRGBG;

public interface ICube {

	public void addColor(ColourRGBG color);

	public ArrayList<ColourRGBG> getColours();

	public int getNumberOfColours();

	public int getAveragePixel();

	public ICube getChildCubeHi();

	public ICube getChildCubeLo();

	public int getScore();

	public boolean isFinal();

}
