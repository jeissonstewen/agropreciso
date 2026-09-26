#!/bin/sh
# Compila el código y las pruebas y ejecuta JUnit 5.
# Uso, desde semana-7-adapter-bridge/:  ./pruebas.sh
set -e
JUNIT=../lib/junit-platform-console-standalone-1.11.4.jar

rm -rf out
mkdir -p out/src out/test

javac -d out/src src/*.java
javac -cp "$JUNIT:out/src" -d out/test test/*.java

java -jar "$JUNIT" execute --class-path "out/src:out/test" --scan-class-path --details=tree
