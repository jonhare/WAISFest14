<?php

/*********************************************************
* From the meta-data of all the images filters based on  *
* their tags.						 *
* The first argument is the meta-data .csv to look	 *
* through.						 *
* The second is a single line .csv of tags.		 *
* The third is the output file.				 *
*********************************************************/

/*********************************************************
* Outputs in the same format as the meta-data input.	 *
*********************************************************/


$img_meta = $argv[1];
$tags = $argv[2];
$output = $argv[3];

$tags_array = array();
$tags = fopen($tags, 'r');
$line = fgetcsv($tags);
foreach($line as $tag)
{
	$tags_array[] = $tag;
}
fclose($tags);

$i = 0;
$img_meta = fopen($img_meta, 'r');
$results_file = fopen($output, 'w');
while(($line = fgetcsv($img_meta)) !== FALSE)
{
	$i++;
	$image_tags = explode(', ', mb_strtolower($line[17], 'UTF-8'));
	$image_tags = str_replace(array('[', ']'), '', $image_tags);
	foreach($tags_array as $tag)
	{
		if(in_array($tag, $image_tags))
		{
			fputcsv($results_file, $line);
			break;
		}
	}
	if($i % 1000 === 0) {
		echo $i, ' rows processed.', "\n";
	}
}
fclose($results_file);
fclose($img_meta);
