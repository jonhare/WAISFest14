<?php

/*********************************************************
* Takes an sorted results file, counts the number of	 *
* results from each month, and outputs them as a .csv	 *
* file.							 *
*********************************************************/

$input_data = $argv[1];
$output = $argv[2];
date_default_timezone_set('Europe/London');

$month_year = array();
for($y = 1970; $y <= 2012; $y++)
{
	for($m = 1; $m <= 12; $m++)
	{
		$month_year[$m.'-'.$y] = 0;
	}
}

$input_data = fopen($input_data, 'r');
while(($line = fgetcsv($input_data)) !== FALSE)
{
	$date = $line[6];
	$date = new DateTime("@$date");
	$month_year[$date->format('m-Y')]++;
}
fclose($input_data);

$results_file = fopen($output, 'w');
foreach($month_year as $month => $count)
{
	fputcsv($results_file, array($month, $count), ',');
}
fclose($results_file);
