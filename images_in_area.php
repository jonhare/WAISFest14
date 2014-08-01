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
$lat_long = $argv[2];
$search_radius = $argv[3];
$output = $argv[4];

$lat_long = fopen($lat_long, 'r');
$line = fgetcsv($lat_long);
$lat = $line[0];
$long = $line[1];
$lat_max = $lat + $search_radius;
$lat_min = $lat - $search_radius;
$long_max = $long + $search_radius;
$long_min = $long - $search_radius;

$img_meta = fopen($img_meta, 'r');
$results_file = fopen($output, 'w');
while(($line = fgetcsv($img_meta)) !== FALSE)
{
	if(($line[2] >= $lat_min && $line[2] <= $lat_max) && ($line[3] >= $long_min && $line[3] <= $long_max))
	{
		fputcsv($results_file, $line);
	}
}
fclose($results_file);
