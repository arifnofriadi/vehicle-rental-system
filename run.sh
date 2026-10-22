#!/usr/bin/env bash
set -e

echo "Compiling Java files..."
mkdir -p bin

# Kompilasi seluruh file java di folder src/ (pemisah classpath ':' untuk Linux/macOS)
javac -encoding UTF-8 -cp "lib/*:src" -d bin $(find src -name "*.java")

echo "Menjalankan aplikasi..."
java -cp "bin:lib/*" Main
