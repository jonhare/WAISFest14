package uk.ac.soton.ecs.waisfest14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.openimaj.util.data.Context;
import org.openimaj.util.stream.AbstractStream;

public class CSVStream extends AbstractStream<Context> {
	public static String FLICKR_ID = "flickrId";
	public static String COLOUR_VALUE = "colourValue";
	public static String LATITUDE = "latitude";
	public static String LONGITUDE = "longitude";
	public static String TAKEN_DATE = "takenDate";
	public static String UPLOADED_DATE = "uploadedDate";
	public static String TIME_SINCE_EPOCH = "timeSinceEpoch";

	public static String CSV_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))";

	BufferedReader reader;
	String nextLine = null;

	public CSVStream(File file) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
	}

	@Override
	public boolean hasNext() {
		if (nextLine != null)
			return true;

		try {
			nextLine = reader.readLine();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return nextLine != null;
	}

	@Override
	public Context next() {
		if (hasNext()) {
			final Context ret = createContext();
			nextLine = null;
			return ret;
		}
		throw new NoSuchElementException();
	}

	private Context createContext() {
		try {
			final String[] parts = nextLine.split(CSV_REGEX);

			final Context ctx = new Context();

			ctx.put(FLICKR_ID, Long.parseLong(parts[0]));
			ctx.put(COLOUR_VALUE, Float.parseFloat(parts[1]));
			ctx.put(LATITUDE, Double.parseDouble(parts[2]));
			ctx.put(LONGITUDE, Double.parseDouble(parts[3]));
			ctx.put(TAKEN_DATE, parts[4]);
			ctx.put(UPLOADED_DATE, parts[5]);
			ctx.put(TIME_SINCE_EPOCH, Long.parseLong(parts[6]));
			return ctx;
		} catch (final Exception e) {
			System.out.println(nextLine);
			throw new RuntimeException(e);
		}
	}

}
