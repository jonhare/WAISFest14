package uk.ac.soton.ecs.waisfest14;

import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.util.iterator.TextLineIterable;

public class ColourAnalysisMapper extends Mapper<Text, BytesWritable, Text, FloatWritable> {
	TLongSet ids = new TLongHashSet(90000);
	FloatWritable fw = new FloatWritable();

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		// load the ids file
		for (final String line : new TextLineIterable(this.getClass().getResource("leaves.ids"))) {
			ids.add(Long.parseLong(line));
		}
	}

	@Override
	protected void map(Text key, BytesWritable value, Context context)
			throws IOException, InterruptedException
	{
		final long id = Long.parseLong(key.toString().trim());

		if (ids.contains(id)) {
			try {
				final MBFImage img = ImageUtilities.readMBF(new ByteArrayInputStream(value.getBytes()));
				final float outVal = processImage(img);
				fw.set(outVal);

				context.write(key, fw);
			} catch (final Exception e) {
				System.err.println(e);
			}
		}
	}

	private float processImage(MBFImage img) {
		final MBFImage hsv = ColourSpace.HSV.convertFromRGB(img);

		int green = 0;
		int red = 0;
		for (int y = 0; y < hsv.getHeight(); y++) {
			for (int x = 0; x < hsv.getWidth(); x++) {
				final float h = hsv.getBand(0).pixels[y][x] * 360;
				final float s = hsv.getBand(1).pixels[y][x];
				final float v = hsv.getBand(2).pixels[y][x];

				if (s < 0.2)
					continue;
				if (v < 0.2)
					continue;

				if (h > 0 && h <= 60 || h > 340) {
					red++;
				} else if (h > 60 && h <= 150) {
					green++;
				}
			}
		}

		return (float) (red - green) / (float) (red + green);
	}
}
