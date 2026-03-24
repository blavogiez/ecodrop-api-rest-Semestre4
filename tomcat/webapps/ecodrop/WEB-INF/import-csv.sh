#!/bin/bash

csvpath=$1

mkdir -p out
SRC=src
LIB=../../../lib

javac -cp "$LIB/postgresql-42.7.8.jar" -sourcepath $SRC $SRC/utils/Import.java -d out/
java -cp "out/:$LIB/postgresql-42.7.8.jar" utils.Import "$csvpath"
rm -rf out
