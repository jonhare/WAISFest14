package uk.ac.soton.ecs.waisfest14;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.openimaj.util.data.Context;
import org.openimaj.util.function.Function;
import org.openimaj.util.stream.AbstractStream;
import org.openimaj.util.stream.CollectionStream;
import org.openimaj.util.stream.Stream;

public class TimeWindow implements Function<Stream<Context>, Stream<Context>> {
	public static final String WINDOW = "window";
	private long windowLength;

	public TimeWindow(Long windowLength) {
		this.windowLength = windowLength;
	}

	@Override
	public Stream<Context> apply(final Stream<Context> inner) {
		return new AbstractStream<Context>() {

			@Override
			public boolean hasNext() {
				return inner.hasNext();
			}

			@Override
			public Context next() {
				Context item = inner.next();

				Long itemtime = item.getTyped(CSVStream.TIME_SINCE_EPOCH);
				long currentWindowStartTime = itemtime;
				final DateTime startDate = new DateTime(currentWindowStartTime * 1000);
				// System.out.println("Start date is: " + startDate + " "+
				// currentWindowStartTime * 1000);
				currentWindowStartTime = startDate.withMillisOfDay(0).getMillis();
				final ArrayList<Context> currentWindow = new ArrayList<Context>();
				currentWindow.add(item);
				long end = 0;
				while (inner.hasNext()) {
					itemtime = (Long) item.getTyped(CSVStream.TIME_SINCE_EPOCH) * 1000;
					// System.out.println("Item date is: " + new
					// DateTime(itemtime) + " " + itemtime);

					item = inner.next();
					if (itemtime - currentWindowStartTime >= TimeWindow.this.windowLength) {
						// System.out.println("Found window end: " +
						// windowLength);
						currentWindow.add(item);
						end = itemtime;
						break;
					}
					currentWindow.add(item);
				}

				final Context retcontext = new Context();
				retcontext.put("start", currentWindowStartTime);
				retcontext.put("end", end);
				retcontext.put("windowsize", windowLength);
				retcontext.put(WINDOW, new CollectionStream<Context>(currentWindow));

				return retcontext;
			}
		};
	}
}
