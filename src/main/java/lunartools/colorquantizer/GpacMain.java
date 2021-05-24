package lunartools.colorquantizer;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class GpacMain{

	public static void main(String[] args) {
		try {
			if(args.length<1) {
				printUsage();
				return;
			}
			File sourceFile=new File(args[0]);
			if(!sourceFile.exists()) {
				System.err.println("File does not exist: "+sourceFile);
				return;
			}
			File destinationFile=null;
			if(args.length>1) {
				destinationFile=new File(args[1]);
				destinationFile.getParentFile().mkdirs();
			}else {
				String filename=sourceFile.getName();
				destinationFile=new File(sourceFile.getParentFile(),filename.substring(0,1+filename.lastIndexOf('.'))+"gif");
			}

			System.out.println("converting: "+sourceFile);
			BufferedImage image=new GPAC().quantizeColours(sourceFile,256);

			System.out.println("writing: "+destinationFile);
			ImageIO.write(image, "GIF", destinationFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("\tgpac filepath [destination filepath]");
		System.out.println("filepath: filepath to an image file");
		System.out.println("gpac converts the image to GIF format, saves the result either using the given destination filepath, or using the same folder, same filename but .gif extension");
	}

}
