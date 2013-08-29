#!/bin/bash
# Used to rebuild all the templated docs

#this finds all directories containing README.ftl.md and creates a bash array
doc_locations=($(
find . -type f -name 'README.ftl.md' |sed 's#\(.*\)/.*#\1#' |sort -u
));

echo "Converting ..."

ORIG=`pwd`

for loc in "${doc_locations[@]}";
do
  echo " $loc/README.ftl.md -> $loc/README.md"
  cd $loc
  cat README.ftl.md | fpp > README.md
  cd $ORIG
done

