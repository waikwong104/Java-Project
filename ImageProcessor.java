import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.util.Random;

public class ImageProcessor {
	
	// Create a clone of a buffered image
	// (The BufferedImage class describes an Image with an accessible buffer of image data.)
	public static BufferedImage copy(BufferedImage img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return bi;
	}

	// Create a clone of a buffered image
	// (Another implementation)
/*
	public static BufferedImage copy(BufferedImage img) {
		 ColorModel cm = img.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = img.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
*/
	
	// Convert an input color image to grayscale image
	public static BufferedImage convertToGrayScale(BufferedImage src) {
		// Make a copy of the source image as the target image
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		
		// Scan through each row of the image
		for (int j = 0; j < height; j++) {
			// Scan through each column of the image
			for (int i = 0; i < width; i++) {
				// Get an integer pixel in the default RGB color model
				int pixel = target.getRGB(i, j);
				// Convert the single integer pixel value to RGB color
				Color oldColor = new Color(pixel);

				int red = oldColor.getRed(); 	// get red value
				int green = oldColor.getGreen();	// get green value
				int blue = oldColor.getBlue(); 	// get blue value

				// Convert RGB to grayscale using formula
				// gray = 0.299 * R + 0.587 * G + 0.114 * B
				double grayVal = 0.299 * red + 0.587 * green + 0.114 * blue;

				// Assign each channel of RGB with the same value
				Color newColor = new Color((int) grayVal, (int) grayVal, (int) grayVal);

				// Get back the integer representation of RGB color and assign it back to the original position
				target.setRGB(i, j, newColor.getRGB());
			}
		}
		// return the resulting image in BufferedImage type
		return target;
	}

	// Invert the color of an input image
	public static BufferedImage invertColor(BufferedImage src) {
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = target.getRGB(i, j);
				Color oldColor = new Color(pixel);

				int red = oldColor.getRed();
				int green = oldColor.getGreen();
				int blue = oldColor.getBlue();
				
				// invert the color of each channel
				Color newColor = new Color(255 - red, 255 - green, 255 - blue);
				
				target.setRGB(i, j, newColor.getRGB());
			}
		}
		return target;
	}

	// Adjust the brightness of an input image
	public static BufferedImage adjustBrightness(BufferedImage src, int amount) {
		BufferedImage target = copy(src);
		int width = target.getWidth();
		int height = target.getHeight();
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = target.getRGB(i, j);
				Color oldColor = new Color(pixel);

				int red = oldColor.getRed();
				int green = oldColor.getGreen();
				int blue = oldColor.getBlue();

				int newRed = (red + amount > 255) ? 255 : red + amount;
				int newGreen = (green + amount > 255) ? 255 : green + amount;
				int newBlue = (blue + amount > 255) ? 255 : blue + amount;

				newRed = (newRed < 0) ? 0 : newRed;
				newGreen = (newGreen < 0) ? 0 : newGreen;
				newBlue = (newBlue < 0) ? 0 : newBlue;

				Color newColor = new Color(newRed, newGreen, newBlue);

				target.setRGB(i, j, newColor.getRGB());
			}
		}
		return target;
	}

	// Apply a blur effect to an input image by random pixel movement
	public static BufferedImage blur(BufferedImage src, int offset) {
		int[][] result = GetRGBEachPixel1(src);
		int width = src.getWidth();
		int height = src.getHeight();
		float offsetX, offsetY;

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Random randomGen = new Random();
				int RNumber = randomGen.nextInt(offset);
				offsetX = RNumber - offset / 2;
				offsetY = RNumber - offset / 2;
				int oldCol = (int) (col + (offsetX));
				int oldRow = (int) (row + (offsetY));
				// if(oldCol < width && oldCol >= 0 && oldRow < height && oldRow >= 0) {
				if (oldCol > width-1) {
					oldCol = width-1;
				}
				if (oldCol < 0) {
					oldCol = 0;
				}
				if (oldRow > height-1) {
					oldRow = height-1;
				}
				if (oldRow < 0) {
					oldRow = 0;
				}
				outputImage.setRGB(col, row, result[oldCol][oldRow]);
			}
		}
		return outputImage;
	}

	private static int[][] GetRGBEachPixel1(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[col][row] = image.getRGB(col, row);
			}
		}
		return result;
	}
	
	
	
	// Scale (resize) an image
	public static BufferedImage scale(BufferedImage src, int tWidth, int tHeight) {
		// TODO: add your implementation
		BufferedImage target = copy(src);
		int w = src.getWidth();
		int h = src.getHeight();
		
	    BufferedImage scale = new BufferedImage(w, h, src.TYPE_INT_ARGB);  
	    Graphics g = scale.getGraphics();  
	    // To resize the image
	    g.drawImage(target,0,0,tWidth, tHeight,0,0,w,h,null);  
	    g.dispose();  
	    return scale;
	}
	
	
	
	
	
	// Rotate an image by angle degrees clockwise
	public static BufferedImage rotate(BufferedImage src, double angle) {
		 double angle2 = Math.toRadians(angle);

		    int w = src.getWidth();
		    int h = src.getHeight();

		    double sin = Math.abs(Math.sin(angle2));
		    double cos = Math.abs(Math.cos(angle2));
		    //To calculate the newWidth and newHeight
		    int newWidth = (int) Math.floor(w * cos + h * sin);
		    int newHeight = (int) Math.floor(h * cos + w * sin);
		    //Save a newWidth and newHeight of image 
		    BufferedImage result = new BufferedImage(newWidth, newHeight,src.getType());
		    Graphics2D g = result.createGraphics();
		    g.translate((newWidth - w) / 2, (newHeight - h) / 2);
		    //To rotate the image
		    g.rotate(angle2, w / 2, h / 2);
		    g.drawRenderedImage(src, null);

		    return result;
	}
	
	
	
	// Apply a swirl effect to an input image
	public static BufferedImage swirl(BufferedImage src, double degree) {
		float midX, midY, dx, dy;
		double theta, radius;
		int[][] result = GetRGBEachPixel(src);
		int width = src.getWidth();
		int height = src.getHeight();
		midX = width / 2;
		midY = height / 2;

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {		//set new RGB of each pixel
				dx = col - midX;						//computer each pixel
				dy = row - midY;

				theta = Math.atan2(dy, dx);

				radius = Math.sqrt(dx * dx + dy * dy);
				int oldCol = (int) (midX + radius * Math.cos(theta + degree * radius));
				int oldRow = (int) (midY + radius * Math.sin(theta + degree * radius));
				if (oldCol < width && oldCol >= 0 && oldRow < height && oldRow >= 0) {
					outputImage.setRGB(col, row, result[oldCol][oldRow]);
				}
			}
		}

		return outputImage; // temporary for passing compilation (remove it after added your code)
	}

	private static int[][] GetRGBEachPixel(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();							//get the width and height of image
		int[][] result = new int[height][width];				//create array for getRGB of each pixel
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[col][row] = image.getRGB(col, row);
			}
		}

		return result;
	}

	// Apply an effect to preserve partial colors of an image 
	public static BufferedImage preserveColor(BufferedImage src, boolean[][] mask, int colorVal, 
			int rgValue, int gbValue, int brValue) {
		
		// TODO: add your implementation
		
		return null; // temporary for passing compilation (remove it after added your code)
	}

	// Perform edge detection for an input image
	public static BufferedImage detectEdges(BufferedImage src) {
		
		// TODO: add your implementation
		
		return null; // temporary for passing compilation (remove it after added your code)
	}
}