package lunartools.colorquantizer;

import java.util.ArrayList;

import lunartools.ColourRGBG;

/**
 * The colour palette built by the quantizer.
 * 
 * @author Thomas Mattel
 */
public class Palette {
	private ArrayList<ColourRGBG> colours=new ArrayList<ColourRGBG>();

	public void addColour(ColourRGBG colour) {
		colours.add(colour);
	}

	public ColourRGBG[] findNeighbours(ColourRGBG colour) {
		ColourRGBG resultLo=null;
		ColourRGBG resultHi=null;
		int deltaRed;
		int deltaGreen;
		int deltaBlue;
		int minDeltaLo=Integer.MAX_VALUE;
		int minDeltaHi=Integer.MAX_VALUE;
		for(int i=0;i<colours.size();i++) {
			ColourRGBG currentColour=colours.get(i);
			deltaRed=colour.getRed()-currentColour.getRed();
			deltaGreen=colour.getGreen()-currentColour.getGreen();
			deltaBlue=colour.getBlue()-currentColour.getBlue();
			int delta=deltaRed*deltaRed+deltaGreen*deltaGreen+deltaBlue*deltaBlue;
			if(delta<800) {
				if(colour.getGrey()<currentColour.getGrey()) {
					if(minDeltaHi>delta) {
						minDeltaHi=delta;
						resultHi=currentColour;
					}
				}else {
					if(minDeltaLo>delta) {
						minDeltaLo=delta;
						resultLo=currentColour;
					}
				}
			}
		}
		return new ColourRGBG[] {resultLo,resultHi};
	}

	public ColourRGBG[] findNeighboursOLD(ColourRGBG colour) {
		ColourRGBG leftFriendIndex=null;
		ColourRGBG rightFriendIndex=null;
		int deltaRed;
		int deltaGreen;
		int deltaBlue;
		int deltaTotal;
		int leftFriend=Integer.MAX_VALUE;
		int rightFriend=Integer.MAX_VALUE;
		for(int i=0;i<colours.size();i++) {
			ColourRGBG currentColour=colours.get(i);
			if(colour.getColour()==currentColour.getColour()) {
				continue;
			}
			deltaRed=Math.abs(colour.getRed()-currentColour.getRed());
			deltaGreen=Math.abs(colour.getGreen()-currentColour.getGreen());
			deltaBlue=Math.abs(colour.getBlue()-currentColour.getBlue());

			if(deltaRed<(16) && deltaGreen<(16) && deltaBlue<(16)) {
				deltaTotal=deltaRed+deltaGreen+deltaBlue;
				if(colour.getGrey()>=colours.get(i).getGrey()) {
					if(leftFriend>deltaTotal) {
						leftFriend=deltaTotal;
						leftFriendIndex=currentColour;
					}
				}
				if(colour.getGrey()<=colours.get(i).getGrey()) {
					if(rightFriend>deltaTotal) {
						rightFriend=deltaTotal;
						rightFriendIndex=currentColour;
					}
				}
			}
		}
		return new ColourRGBG[] {leftFriendIndex,rightFriendIndex};
	}

	public ColourRGBG findClosestColour(ColourRGBG colour) {
		ColourRGBG result=null;
		int deltaRed;
		int deltaGreen;
		int deltaBlue;
		int minDelta=Integer.MAX_VALUE;
		for(int i=0;i<colours.size();i++) {
			ColourRGBG currentColour=colours.get(i);
			deltaRed=colour.getRed()-currentColour.getRed();
			deltaGreen=colour.getGreen()-currentColour.getGreen();
			deltaBlue=colour.getBlue()-currentColour.getBlue();
			int delta=deltaRed*deltaRed+deltaGreen*deltaGreen+deltaBlue*deltaBlue;
			if(minDelta>delta) {
				minDelta=delta;
				result=currentColour;
			}
		}
		return result;
	}

	public ColourRGBG findClosestColour(int red,int green,int blue) {
		ColourRGBG result=null;
		int deltaRed;
		int deltaGreen;
		int deltaBlue;
		int minDelta=Integer.MAX_VALUE;
		for(int i=0;i<colours.size();i++) {
			ColourRGBG currentColour=colours.get(i);
			deltaRed=red-currentColour.getRed();
			deltaGreen=green-currentColour.getGreen();
			deltaBlue=blue-currentColour.getBlue();
			int delta=deltaRed*deltaRed+deltaGreen*deltaGreen+deltaBlue*deltaBlue;
			if(minDelta>delta) {
				minDelta=delta;
				result=currentColour;
			}
		}
		return result;
	}

	public ColourRGBG getColour(int index) {
		return colours.get(index);
	}

}
