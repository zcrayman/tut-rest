#!/bin/bash
# Used to rebuild all the templated docs

cat 1/README.ftl.md | fpp > 1/README.md

cd 2
cat README.ftl.md | fpp > README.md
cd ..
