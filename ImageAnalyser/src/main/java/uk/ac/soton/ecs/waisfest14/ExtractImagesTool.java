package uk.ac.soton.ecs.waisfest14;

import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.openimaj.hadoop.mapreduce.TextBytesJobUtil;
import org.openimaj.hadoop.sequencefile.SequenceFileUtility;
import org.openimaj.util.iterator.TextLineIterable;

public class ExtractImagesTool extends Configured implements Tool {
	public static class Map extends Mapper<Text, BytesWritable, Text, BytesWritable> {
		TLongSet ids = new TLongHashSet(90000);

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			// load the ids file
			for (final String line : new TextLineIterable(new File("ids.txt"))) {
				ids.add(Long.parseLong(line));
			}
		}

		@Override
		protected void map(Text key, BytesWritable value, Context context)
				throws IOException, InterruptedException
		{
			final long id = Long.parseLong(key.toString().trim());

			if (ids.contains(id)) {
				context.write(key, value);
			}
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		final String inputImages = args[0];
		final String outputImages = args[1];
		final String idsFile = args[2];

		final Path[] p = SequenceFileUtility.getFilePaths(inputImages, "part");
		final Job job = TextBytesJobUtil.createJob(p, new Path(outputImages), null, this.getConf());
		job.setJarByClass(this.getClass());

		job.setMapperClass(Map.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(BytesWritable.class);

		job.setNumReduceTasks(0);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		DistributedCache.createSymlink(job.getConfiguration());
		DistributedCache.addCacheFile(new URI(idsFile + "#ids.txt"), job.getConfiguration());

		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new ExtractImagesTool(), args);
	}
}
