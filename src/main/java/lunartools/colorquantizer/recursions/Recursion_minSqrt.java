package lunartools.colorquantizer.recursions;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.colorquantizer.cubes.ICube;

public class Recursion_minSqrt implements IRecursion{
	private static Logger logger = LoggerFactory.getLogger(Recursion_minSqrt.class);
	private ArrayList<ICube> cubes;

	public ArrayList<ICube> doRecursion(ICube cube, int paletteSize) {
		this.cubes=new ArrayList<ICube>();
		final int mincolors=(int)Math.sqrt(cube.getNumberOfColours());
		logger.trace("mincolors: "+mincolors);
		recursion(cube, paletteSize,mincolors);
		return this.cubes;
	}

	private void recursion(final ICube cube,final int level,final int mincolors) {
		if(cube.getColours().size()<=mincolors) {
			cubes.add(cube);
			return;
		}
		ICube cubeHi=cube.getChildCubeHi();
		ICube cubeLo=cube.getChildCubeLo();
		if(level==1) {
			if(cubeHi!=null && cubeHi.getNumberOfColours()>0) {
				cubes.add(cubeHi);
			}
			if(cubeLo!=null && cubeLo.getNumberOfColours()>0) {
				cubes.add(cubeLo);
			}
		}else {
			recursion(cubeHi, level>>1,mincolors);
			recursion(cubeLo, level>>1,mincolors);
		}
	}

}
