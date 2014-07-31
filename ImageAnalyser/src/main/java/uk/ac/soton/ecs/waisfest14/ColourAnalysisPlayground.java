package uk.ac.soton.ecs.waisfest14;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;

public class ColourAnalysisPlayground {
	public static void main(String[] args) throws MalformedURLException, IOException {
		final MBFImage img = ImageUtilities.readMBF(new URL(
				// "http://degas.ecs.soton.ac.uk/~jsh2/leaves/3991474519_71731ba247.jpg"));
				"http://degas.ecs.soton.ac.uk/~jsh2/leaves/6985831989_00dbbe8af6.jpg"));
		final MBFImage hsv = ColourSpace.HSV.convertFromRGB(img);

		DisplayUtilities.display(img);
		DisplayUtilities.display(hsv);

		int green = 0;
		int red = 0;
		for (int y = 0; y < hsv.getHeight(); y++) {
			for (int x = 0; x < hsv.getWidth(); x++) {
				final float h = hsv.getBand(0).pixels[y][x] * 360;
				final float s = hsv.getBand(1).pixels[y][x];
				final float v = hsv.getBand(2).pixels[y][x];

				img.setPixel(x, y, RGBColour.BLACK);
				if (s < 0.2)
					continue;
				if (v < 0.2)
					continue;

				if (h > 0 && h <= 60 || h > 340) {
					red++;
					img.setPixel(x, y, RGBColour.RED);
				} else if (h > 60 && h <= 150) {
					img.setPixel(x, y, RGBColour.GREEN);
					green++;
				}
			}
		}

		DisplayUtilities.display(img);

		final float ndvi = (float) (red - green) / (float) (red + green);

		System.out.println(ndvi);
	}
}
