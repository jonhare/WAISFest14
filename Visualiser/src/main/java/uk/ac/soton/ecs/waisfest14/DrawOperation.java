package uk.ac.soton.ecs.waisfest14;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.math.util.Interpolation;
import org.openimaj.util.data.Context;
import org.openimaj.util.function.Operation;
import org.openimaj.vis.general.DotPlotVisualisation;
import org.openimaj.vis.general.DotPlotVisualisation.ColouredDot;
import org.openimaj.vis.world.WorldMap;

public final class DrawOperation implements Operation<Context>, WindowProcessListener
{
	final MBFImage img;
	SimpleDateFormat df;
	MBFImage layer;
	int size = 3;
	float damp = 0.90f;

	float lonMin;
	float lonMax;
	float latMin;
	float latMax;
	private MBFImage map;

	public DrawOperation(MBFImage img, boolean drawMap) {
		this(img, -90, 90, -180, 180, drawMap);
		// this(img, 45, 60, -15, 15, drawMap); // UK
		// this(img, 40, 50, -80, -65, drawMap); // New England
	}

	public DrawOperation(MBFImage img, float latMin, float latMax, float lonMin, float lonMax, boolean drawMap) {
		this.img = img;
		this.layer = img.clone();
		this.df = new SimpleDateFormat("yyyy-MM-dd");

		if (drawMap) {
			final WorldMap<ColouredDot> wp = new WorldMap<ColouredDot>(img.getWidth(), img.getHeight() - 40,
					new DotPlotVisualisation(), (int) lonMin, (int) lonMax, (int) latMin, (int) latMax);
			wp.getAxesRenderer().setDrawXAxis(false);
			wp.getAxesRenderer().setDrawYAxis(false);
			wp.getAxesRenderer().setAxisPaddingBottom(0);
			wp.getAxesRenderer().setAxisPaddingTop(0);
			wp.getAxesRenderer().setAxisPaddingLeft(0);
			wp.getAxesRenderer().setAxisPaddingRight(0);

			wp.setDefaultCountryLandColour(RGBColour.BLACK);
			wp.setSeaColour(RGBColour.BLACK);
			wp.setDefaultCountryOutlineColour(RGBColour.GRAY);

			wp.updateVis();
			this.map = wp.getVisualisationImage();
			this.map = this.map.paddingSymmetric(0, 0, 0, 40);
		}

		this.latMin = 90 - latMax;
		this.latMax = 90 - latMin;
		this.lonMin = lonMin + 180;
		this.lonMax = lonMax + 180;
	}

	@Override
	public void perform(Context ctx) {
		final double x = (Double) ctx.get(CSVStream.LONGITUDE) + 180 - lonMin;
		final double y = 90 - (Double) ctx.get(CSVStream.LATITUDE) - latMin;

		final float deltaLon = lonMax - lonMin;
		final float deltaLat = latMax - latMin;

		final int xx = (int) (x * (1.0 * img.getWidth() / deltaLon)) - 1;
		final int yy = (int) (y * (1.0 * (img.getHeight() - 40) / deltaLat)) - 1;

		float v = ctx.getTyped(CSVStream.COLOUR_VALUE);
		v = Interpolation.lerp(v, -1f, 0.4f, 1f, 0f);
		final Float[] colour = ColourSpace.HSV.convertToRGB(new Float[] { v, 1f, 1f });

		if (xx >= 0 && xx < img.getWidth() && yy >= 0 && yy < img.getHeight()) {
			this.layer.drawPoint(new Point2dImpl(xx, yy), colour, size);

		}
	}

	@Override
	public void windowDrawn(Context object) {
		layer.multiplyInplace(damp);
		layer.drawShapeFilled(new Rectangle(0, img.getHeight() - 40, img.getWidth(), 40), RGBColour.BLACK);
		layer.drawText(df.format(new Date((Long) object.get("start"))), 0, img.getHeight(),
				HersheyFont.ROMAN_SIMPLEX, 18, RGBColour.WHITE);
		img.addInplace(layer);
		img.addInplace(this.map);
	}
}
