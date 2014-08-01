package uk.ac.soton.ecs.waisfest14;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.util.data.Context;
import org.openimaj.util.function.Operation;
import org.openimaj.util.stream.Stream;
import org.openimaj.video.xuggle.XuggleVideoWriter;

public class Visualisation {
	public static void main(String[] args) throws IOException {
		final MBFImage image = FullScreenDemo.createImage();
		final JFrame window = FullScreenDemo.display(image, "Tree Colours");

		final DrawOperation pointOp = new DrawOperation(image, true);

		final XuggleVideoWriter xvw = new XuggleVideoWriter("/Users/jsh2/Desktop/leaves-8day.mp4", image.getWidth(),
				image.getHeight(), 10);

		new CSVStream(new File("/Users/jsh2/Desktop/results_sorted.csv"))
				.transform(new TimeWindow(8 * 24 * 60 * 60 * 1000L))
				.forEach(new Operation<Context>() {

					@Override
					public void perform(Context ctx) {
						image.fill(RGBColour.BLACK);

						@SuppressWarnings("unchecked")
						final Stream<Context> win = ((Stream<Context>) ctx.get(TimeWindow.WINDOW));

						win.forEach(pointOp);

						pointOp.windowDrawn(ctx);

						FullScreenDemo.update(window, image);
						xvw.addFrame(image);
					}
				});
		xvw.close();
	}
}
