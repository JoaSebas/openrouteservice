#!/bin/sh
while [ ! -f  $1/ors.run ]
do
  echo "DEBUG: Waiting in folder: $1"
  sleep 3
done
mvn -B test --file $1/openrouteservice-api-tests/pom.xml
res=$?
rm $1/ors.run
if [ "$res" -ne 0 ] ; then
  echo 'API tests failed!'
  exit $res
fi
