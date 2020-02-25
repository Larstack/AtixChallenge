#!/bin/bash

clear

if [ -z "$1" ] || [ -z "$2" ]
then
  echo "You must provide parameters S and M as arguments"
  exit 1
fi

echo "Compiling project.."
mvn -DskipTests clean install -U > /dev/null

echo "Starting monitor.."
java -jar target/atix-challenge-0.0.1.jar $1 $2






