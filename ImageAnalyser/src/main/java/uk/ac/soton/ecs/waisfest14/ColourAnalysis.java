package uk.ac.soton.ecs.waisfest14;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;

public class ColourAnalysis {
	public static void main(String[] args) throws MalformedURLException, IOException {
		final MBFImage img = ImageUtilities.readMBF(new URL(
				"http://degas.ecs.soton.ac.uk/~jsh2/leaves/3991474519_71731ba247.jpg"));
		final MBFImage hsv = ColourSpace.HSV.convertFromRGB(img);

		for (int y = 0; y < hsv.getHeight(); y++) {
			for (int x = 0; x < hsv.getWidth(); x++) {
				final float h = hsv.getBand(0).pixels[y][x];
				final float s = hsv.getBand(1).pixels[y][x];
				final float v = hsv.getBand(2).pixels[y][x];

			}
		}
	}
}
