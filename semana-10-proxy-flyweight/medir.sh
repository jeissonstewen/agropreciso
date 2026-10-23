#!/bin/sh
# Ejecuta el banco de medicion que sustenta la decision de NO implementar
# el patron Flyweight.
# Uso, desde semana-10-proxy-flyweight/:  ./medir.sh
set -e
rm -rf out/medicion
mkdir -p out/medicion
javac -d out/medicion medicion/*.java
java -cp out/medicion BancoMemoriaFlyweight
