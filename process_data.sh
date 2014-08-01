#!/bin/sh
php join_id_to_meta.php leaves-ndvi1.txt leaves_meta_data.csv results.csv
sort -t, -n -k 7 results.csv > results_sorted.csv
cut -d, -f2,7 results_sorted.csv > results_cut.csv
awk -F, '$3 > 0' results_sorted.csv > results_above_equator.csv
awk -F, '$3 < 0' results_sorted.csv > results_below_equator.csv
php images_in_area.php results_sorted.csv new_england_lat_long.txt 5 results_in_new_england.csv
cut -d, -f2,7 results_in_new_england.csv > results_in_new_england_cut.csv
