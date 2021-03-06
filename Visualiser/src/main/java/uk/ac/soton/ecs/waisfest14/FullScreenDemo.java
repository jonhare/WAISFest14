package uk.ac.soton.ecs.waisfest14;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.openimaj.image.DisplayUtilities.ImageComponent;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;

public class FullScreenDemo
{
	public static JFrame display(MBFImage img, String title) {
		return display(ImageUtilities.createBufferedImageForDisplay(img), img, title);
	}

	private static JFrame display(BufferedImage img, MBFImage original, String title) {
		// final JFrame f = DisplayUtilities.makeFrame(title);
		final JFrame f = new JFrame(title);
		f.setResizable(false);
		f.setUndecorated(true);

		final ImageComponent c = new ImageComponent();
		if (img != null)
			c.setImage(img);
		c.setOriginalImage(original);
		c.setSize(img.getWidth(), img.getHeight());
		c.setPreferredSize(new Dimension(c.getWidth(), c.getHeight()));

		c.removeMouseListener(c);
		c.removeMouseMotionListener(c);
		c.setShowPixelColours(false);
		c.setShowXYPosition(false);
		c.setAllowZoom(false);
		c.setAutoscrolls(false);
		c.setAllowPanning(false);

		f.add(c);

		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(dim);
		f.setPreferredSize(dim);
		f.pack();

		final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		gd.setFullScreenWindow(f);
		f.setVisible(true);

		return f;
	}

	public static MBFImage createImage() {
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		return new MBFImage(dim.width, dim.height, ColourSpace.RGB);
	}

	static FImage map;

	public static JFrame update(JFrame wind, MBFImage image) {
		// if (map == null)
		// map = StaticWorldMap.getMap(image.getWidth(),
		// image.getHeight()).getBand(1);
		//
		// for (int y = 0; y < image.getHeight(); y++) {
		// for (int x = 0; x < image.getWidth(); x++) {
		// if (map.pixels[y][x] != 1) {
		// image.setPixel(x, y, RGBColour.DARK_GRAY);
		// }
		// }
		// }
		return update(wind, image, ImageUtilities.createBufferedImageForDisplay(image));
	}

	private static JFrame update(JFrame wind, MBFImage image, BufferedImage original) {
		final ImageComponent cmp = ((ImageComponent) wind.getContentPane().getComponent(0));
		cmp.setImage(original);
		cmp.setOriginalImage(image);
		return wind;
	}
}
