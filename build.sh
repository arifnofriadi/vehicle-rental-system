#!/bin/bash
set -e
find src -name "*.java" > sources.txt
javac -cp lib/sqlite-jdbc-3.41.2.2.jar -d out @sources.txt
java -cp "out:lib/sqlite-jdbc-3.41.2.2.jar" Main
