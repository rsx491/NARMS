#!/bin/bash

version=`grep '##' "Release notes.md" | head -n 1 | tr -d '#' | tr -d ' '`

mvn install:install-file -Dfile=LibRecognizer.aar -DgroupId=com.microblink -DartifactId=pdf417.mobi -Dversion=$version -Dpackaging=aar -DcreateChecksum=true -Djavadoc=./LibRecognizer-javadoc.jar