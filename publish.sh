#!bin/bash 
./gradlew clean build bintrayUpload  -PbintrayUser=${1}  -PbintrayKey=${2}   -PdryRun=false 
