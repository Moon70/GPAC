package lunartools.colorquantizer;

public class Color{
	int color;
	int red;
	int green;
	int blue;
	int grey;

	Color(int color) {
		this.color=color&0xffffff;
		this.red=(color>>16)&0xff;
		this.green=(color>>8) & 0xff;
		this.blue=color & 0xff;

		this.grey=(this.red+this.green+this.blue)/3;
	}

	int leftFriendIndex;
	int rightFriendIndex;
	void calcBestFriends(Color[] colors) {
		leftFriendIndex=-1;
		rightFriendIndex=-1;
		int deltaRed;
		int deltaGreen;
		int deltaBlue;
		int deltaTotal;
		int leftFriend=Integer.MAX_VALUE;
		int rightFriend=Integer.MAX_VALUE;
		for(int i=0;i<colors.length;i++) {
			if(colors[i]==null || color==colors[i].color) {
				continue;
			}
			deltaRed=Math.abs(red-colors[i].red);
			deltaGreen=Math.abs(green-colors[i].green);
			deltaBlue=Math.abs(blue-colors[i].blue);

			if(deltaRed<(16) && deltaGreen<(16) && deltaBlue<(16)) {
				deltaTotal=deltaRed+deltaGreen+deltaBlue;
				if(grey>=colors[i].grey) {
					if(leftFriend>deltaTotal) {
						leftFriend=deltaTotal;
						leftFriendIndex=i;
					}
				}
				if(grey<=colors[i].grey) {
					if(rightFriend>deltaTotal) {
						rightFriend=deltaTotal;
						rightFriendIndex=i;
					}
				}
			}
		}
	}

}
