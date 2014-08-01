<?php

/*********************************************************
* First argument is the results of			 *
* join_images_to_meta.php.				 *
* Second argument is a .csv containing the latitude and  *
* longitude to search nearby (X points either way).	 *
* Third argument is an int representing the search	 *
* distance.						 *
* Outputs those that pass the filter to a .csv file.	 *
*********************************************************/

/*********************************************************
* Output .csv format:					 *
* id,col_val,lat,long,date_taken,date_uploaded		 *
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

$img_meta = fopen($img_meta, 'r');
$results_file = fopen($output, 'w');
while(($line = fgetcsv($img_meta)) !== FALSE)
{
	$image_tags = explode(', ', mb_strtolower($line[17], 'UTF-8'));
	$image_tags = str_replace(array('[', ']'), '', $image_tags);
	foreach($tags_array as $tag)
	{
		if(in_array($tag, $image_tags))
		{
			fputcsv($results_file, $line);
			print_r($line);
			break;
		}
	}
}
fclose($results_file);
fclose($img_meta);
