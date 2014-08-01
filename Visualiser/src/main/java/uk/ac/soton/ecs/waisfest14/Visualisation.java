package uk.ac.soton.ecs.waisfest14;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.FImage;
import org.openimaj.util.data.Context;
import org.openimaj.util.function.Operation;
import org.openimaj.util.stream.Stream;

public class Visualisation {
	public static void main(String[] args) throws IOException {
		new CSVStream(new File("/Users/jsh2/Desktop/results_sorted.csv"))
				.transform(new TimeWindow(16 * 24 * 60 * 60 * 1000L))
				.forEach(new Operation<Context>() {

					@Override
					public void perform(Context ctx) {
						final FImage image = new FImage(360, 180);

						@SuppressWarnings("unchecked")
						final Stream<Context> win = ((Stream<Context>) ctx.get(TimeWindow.WINDOW));

						win.forEach(new Operation<Context>() {

							@Override
							public void perform(Context ctx) {
								final double x = (Double) ctx.get(CSVStream.LONGITUDE) + 180;
								final double y = 90 - (Double) ctx.get(CSVStream.LATITUDE);

								final int xx = (int) (x * (1.0 * img.getWidth() / 360)) - 1;
								final int yy = (int) (y * (1.0 * img.getHeight() / 180)) - 1;

								float v = ctx.getTyped(CSVStream.COLOUR_VALUE);
								v = (1 - v) / 2;

								if (xx >= 0 && xx < image.getWidth() && yy >= 0 && yy < image.getHeight()) {
									image.pixels[yy][xx] += v;

								}
							}
						});

					}
				});
	}
}
