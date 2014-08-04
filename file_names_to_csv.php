<?php

/*********************************************************
* Takes a directory full of NASA images - writes a CSV	 *
* file containing tuples of image-start-date,		 *
* image-end-date and image-name (image-date calculated	 *
* from the file names, not meta-data.			 *
* First argument is the directory of images.		 *
* Second argument is the output file.			 *
*********************************************************/

$dir = $argv[1];
$output = $argv[2];

$lines = array();
$files = array_diff(scandir($dir), array('.', '..', '.DS_Store'));
foreach($files as $file)
{
	$file_name = $file;
	$start_date = substr($file, 15, 10);
	$start_date = strtotime($start_date);
	$line = array(
			$start_date,
			$file_name
		     );
	$lines[] = $line;
}

$results_file = fopen($output, 'w');
$i = 0;
for($i = 0; $i < sizeof($lines); $i++)
{
	$line = $lines[$i];
	if(isset($lines[$i+1]))
	{
		$end_date = $lines[$i+1][0] - 1;
	}
	else
	{
		$end_date = $line[0] + (86400 * 16) - 1;
	}
	$line = array(
			$line[0],
			$end_date,
			$line[1]
		     );
	fputcsv($results_file, $line);
}
fclose($results_file);
