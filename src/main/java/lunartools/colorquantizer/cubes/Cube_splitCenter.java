package lunartools.colorquantizer.cubes;

import java.util.ArrayList;

import lunartools.ColourRGBG;

public class Cube_splitCenter implements ICube{
	private ArrayList<ColourRGBG> colors=new ArrayList<ColourRGBG>();

	private int minR=Integer.MAX_VALUE;
	private int maxR=Integer.MIN_VALUE;
	private int minG=Integer.MAX_VALUE;
	private int maxG=Integer.MIN_VALUE;
	private int minB=Integer.MAX_VALUE;
	private int maxB=Integer.MIN_VALUE;

	private long totalR;
	private long totalG;
	private long totalB;

	private Cube_splitCenter childCubeHi;
	private Cube_splitCenter childCubeLo;

	private boolean flagFinal;
	private int score;

	@Override
	public void addColor(ColourRGBG color) {
		colors.add(color);
		totalR+=color.getRed();
		totalG+=color.getGreen();
		totalB+=color.getBlue();

		if(minR>color.getRed()) {
			minR=color.getRed();
		}
		if(maxR<color.getRed()) {
			maxR=color.getRed();
		}

		if(minG>color.getGreen()) {
			minG=color.getGreen();
		}
		if(maxG<color.getGreen()) {
			maxG=color.getGreen();
		}

		if(minB>color.getBlue()) {
			minB=color.getBlue();
		}
		if(maxB<color.getBlue()) {
			maxB=color.getBlue();
		}
		score=-1;
	}

	@Override
	public ArrayList<ColourRGBG> getColours(){
		return colors;
	}

	@Override
	public int getNumberOfColours() {
		return colors.size();
	}

	@Override
	public int getAveragePixel() {
		int r=(int)(totalR/colors.size());
		int g=(int)(totalG/colors.size());
		int b=(int)(totalB/colors.size());
		return ((r<<16)|(g<<8)|b);
	}

	@Override
	public ICube getChildCubeHi() {
		if(childCubeHi==null) {
			split();
		}
		return childCubeHi;
	}

	@Override
	public ICube getChildCubeLo() {
		if(childCubeLo==null) {
			split();
		}
		return childCubeLo;
	}

	private void split() {
		childCubeHi=new Cube_splitCenter();
		childCubeLo=new Cube_splitCenter();

		final int lenR=maxR-minR;
		final int lenG=maxG-minG;
		final int lenB=maxB-minB;

		if(lenR>lenG) {
			if(lenR>=lenB) {
				splitR(lenR);
			}else {
				if(lenG>=lenB) {
					splitG(lenG);
				}else {
					splitB(lenB);
				}
			}
		}else {
			if(lenG>=lenB) {
				splitG(lenG);
			}else {
				splitB(lenB);
			}
		}

		flagFinal=childCubeHi.getNumberOfColours()==0 || childCubeLo.getNumberOfColours()==0;
	}

	private void splitR(int lenR) {
		int cutR=minR+(lenR>>1);
		for(int i=0;i<colors.size();i++) {
			ColourRGBG color=colors.get(i);
			if(color.getRed()<cutR) {
				childCubeHi.addColor(color);
			}else {
				childCubeLo.addColor(color);
			}
		}
	}

	private void splitG(int lenG) {
		int cutG=minG+(lenG>>1);
		for(int i=0;i<colors.size();i++) {
			ColourRGBG color=colors.get(i);
			if(color.getGreen()<cutG) {
				childCubeHi.addColor(color);
			}else {
				childCubeLo.addColor(color);
			}
		}
	}

	private void splitB(int lenB) {
		int cutB=minB+(lenB>>1);
		for(int i=0;i<colors.size();i++) {
			ColourRGBG color=colors.get(i);
			if(color.getBlue()<cutB) {
				childCubeHi.addColor(color);
			}else {
				childCubeLo.addColor(color);
			}
		}
	}

	@Override
	public int getScore() {
		if(score==-1) {
			int biggestLen;

			final int lenR=maxR-minR;
			final int lenG=maxG-minG;
			final int lenB=maxB-minB;

			if(lenR>lenG) {
				if(lenR>=lenB) {
					biggestLen=lenR;
				}else {
					if(lenG>=lenB) {
						biggestLen=lenG;
					}else {
						biggestLen=lenB;
					}
				}
			}else {
				if(lenG>=lenB) {
					biggestLen=lenG;
				}else {
					biggestLen=lenB;
				}
			}
			score=biggestLen*colors.size();
		}
		return score;
	}

	@Override
	public boolean isFinal() {
		return flagFinal;
	}

}
