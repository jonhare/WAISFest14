package uk.ac.soton.ecs.waisfest14;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.openimaj.hadoop.mapreduce.TextBytesJobUtil;
import org.openimaj.hadoop.sequencefile.SequenceFileUtility;

public class ColourAnalysisTool extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		final Path[] p = SequenceFileUtility.getFilePaths("hdfs://seurat/data/flickr-all-geo-16-46M-images.seq", "part");

		final Job job = TextBytesJobUtil.createJob(p, new Path("hdfs://seurat/data/leaves.txt"), null, this.getConf());
		job.setJarByClass(this.getClass());

		job.setMapperClass(ColourAnalysisMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FloatWritable.class);

		job.setReducerClass(Reducer.class);
		job.setNumReduceTasks(1);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new ColourAnalysisTool(), args);
	}
}
