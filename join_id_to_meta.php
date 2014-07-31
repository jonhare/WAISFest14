<?php

/*********************************************************
* First argument is the results from the Hadoop process, *
* giving Flickr ID/Colour Value pairs, as a .tsv.        *
* Second argument is the meta-deta of the Flickr images  *
* as a .csv.						 *
* Links the IDs and the relevant meta-data, then outputs *
* as a .csv to the third argument.			 *
*********************************************************/

/*********************************************************
* Output .csv format:					 *
* id,col_val,lat,long,date_taken,date_uploaded
*********************************************************/

$id_col_val = $argv[1];
$img_meta = $argv[2];
$output = $argv[3];

$img_meta_array = array();
$img_meta = fopen($img_meta, 'r');
while(($line = fgetcsv($img_meta)) !== FALSE)
{
	$img_meta_array[trim($line[2])] = array(
						'lat'		=> trim($line[15]),
						'long'		=> trim($line[16]),
						'date_taken'	=> trim($line[11]),
						'date_uploaded'	=> trim($line[10])
					       );
}
fclose($img_meta);

$results = array();
$id_col_val = fopen($id_col_val, 'r');
while(($line = fgetcsv($id_col_val, 0, "\t")) !== FALSE)
{
	$match = $img_meta_array[$line[0]];
	$results[] = array(
				'id'		=> trim($line[0]),
				'col_val'	=> trim($line[1]),
				'lat'		=> $match['lat'],
				'long'		=> $match['long'],
				'date_taken'	=> $match['date_taken'],
				'date_uploaded'	=> $match['date_uploaded']
			  );
}
fclose($id_col_val);

$results_file = fopen($output, 'w');
foreach($results as $result)
{
	fputcsv($results_file, $result);
}
fclose($results_file);
