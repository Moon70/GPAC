package lunartools.colorquantizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GpacMain{

	public static void main(String[] args) {
		try {
			if(args.length!=1) {
				printUsage();
				return;
			}
			File file=new File(args[0]);
			if(!file.exists()) {
				System.err.println("File does not exist: "+file);
				return;
			}
			
			BufferedImage imageEdit=getBufferedImage(file);

			System.out.println("converting: "+file);
			new GPAC_experimental().quantizeColors(imageEdit,256);
			
			String filename=file.getName();
			File fileGif=new File(file.getParentFile(),filename.substring(0,1+filename.lastIndexOf('.'))+"gif");
			ImageIO.write(imageEdit, "GIF", fileGif);
			System.out.println("created: "+fileGif);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("\tgpac filepath");
		System.out.println("filepath: filepath to an image file");
		System.out.println("gpac converts the image to GIF format, saves the result using same folder, same filename but .gif extension");
		
	}

	private static BufferedImage getBufferedImage(File file) throws IOException {
		BufferedImage imageByte=ImageIO.read(file);
		BufferedImage imageInt=new BufferedImage(imageByte.getWidth(null),imageByte.getHeight(null),BufferedImage.TYPE_INT_RGB);
		imageInt.getGraphics().drawImage(imageByte,0,0,null);
		imageByte.flush();
		return imageInt;
	}
	
}
