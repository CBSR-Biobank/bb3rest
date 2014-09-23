#!/bin/sh

HOST="aicml-med.cs.ualberta.ca"
#HOST="localhost"

curl -i -H "Accept: application/json" "http://$HOST:9900/patients/EDM001/study"

curl -i -H "Accept: application/json" "http://$HOST:9900/patients/EDM001/specimens/counts"

curl -i -H "Accept: application/json" "http://$HOST:9900/patients/EDM001/specimens/aliquots"
