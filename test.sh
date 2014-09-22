#!/bin/sh

HOST="aicml-med.cs.ualberta.ca"
#HOST="localhost"

curl -i -H "Accept: application/json" "http://$HOST:9900/patients/1102/specimens/counts"

curl -i -H "Accept: application/json" "http://$HOST:9900/patients/1105/specimens/counts"

curl -i -H "Accept: application/json" "http://$HOST:9900/patients/1107/specimens/counts"
