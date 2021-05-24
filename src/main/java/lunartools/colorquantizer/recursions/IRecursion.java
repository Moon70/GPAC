package lunartools.colorquantizer.recursions;

import java.util.ArrayList;

import lunartools.colorquantizer.cubes.ICube;

public interface IRecursion {

	public ArrayList<ICube> doRecursion(ICube cube, int paletteSize);

}
