<?php

/*********************************************************
* Takes a directory full of NASA images - writes a CSV	 *
* file containing pairs of image-date to image-name	 *
* (image-date calculated from the file names, not meta-	 *
* data.							 *
* First argument is the directory of images.		 *
* Second argument is the output file.			 *
*********************************************************/

$dir = $argv[1];
$output = $argv[2];

$files = array_diff(scandir($dir), array('.', '..', '.DS_Store'));;
$results_file = fopen($output, 'w');
foreach($files as $file)
{
	$file_name = $file;
	$date = substr($file, 15, 10);
	$date = strtotime($date);
	$line = array(
			$date,
			$file_name
		     );
	fputcsv($results_file, $line);
}
fclose($results_file);
