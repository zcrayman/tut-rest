#!/bin/bash
# Used to rebuild all the templated docs

#this finds all directories containing README.ftl.md and creates a bash array
doc_locations=($(
find . -type f -name 'README.ftl.md' |sed 's#\(.*\)/.*#\1#' |sort -u
));

echo "Converting ..."

echo "SIDEBAR.ftl.md -> SIDEBAR.md"
cat SIDEBAR.ftl.md | fpp > SIDEBAR.md

ORIG=`pwd`

for loc in "${doc_locations[@]}";
do
  echo " $loc/README.ftl.md -> $loc/README.md"
  if [[ "$loc" == *1* ]]; then
    cp -R initial $loc/
  fi
  cd $loc
  cat README.ftl.md | fpp > README.md
  if [[ "$loc" == *1* ]]; then
    rm -rf initial
  fi
  cd $ORIG
done

