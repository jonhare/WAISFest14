<?php

/*********************************************************
* First argument is the results from the Hadoop process, *
* giving Flickr ID/Colour Value pairs, as a .tsv.        *
* Second argument is the meta-deta of the Flickr images  *
* as a .csv.						 *
* Links the IDs and the relevant meta-data, then outputs *
* as a .csv to the third argument.			 *
* When calculating the date from the epoch, uses the	 *
* taken date if it is available, otherwise uses the	 *
* upload date.						 *
*********************************************************/

/*********************************************************
* Output .csv format:					 *
* id,col_val,lat,long,date_taken,date_uploaded,		 *
* date_from_epoch
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

$id_col_val = fopen($id_col_val, 'r');
$results_file = fopen($output, 'w');
while(($line = fgetcsv($id_col_val, 0, "\t")) !== FALSE)
{
	$match = $img_meta_array[trim($line[0])];
	if(trim($line[1]) !== 'NaN')
	{
		if(isset($match['date_taken']))
		{
			$time_from_epoch = strtotime($match['date_taken']);
		}
		else
		{
			$time_from_epoch = strtotime($match['date_uploaded']);
		}
		if($time_from_epoch != NULL && $time_from_epoch <= time() && $time_from_epoch >= 0 && $match['lat'] != NULL && $match['long'] != NULL)
		{
			$result = array(

					'id'			=> trim($line[0]),
					'col_val'		=> trim($line[1]),
					'lat'			=> $match['lat'],
					'long'			=> $match['long'],
					'date_taken'		=> $match['date_taken'],
					'date_uploaded'		=> $match['date_uploaded'],
					'date_from_epoch'	=> $time_from_epoch
				       );
			fputcsv($results_file, $result);
		}
	}
}
fclose($results_file);
fclose($id_col_val);
