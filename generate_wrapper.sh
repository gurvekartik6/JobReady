#!/bin/sh
# Run this script once after cloning to generate the Gradle wrapper jar
# Requires Gradle to be installed: https://gradle.org/install/
gradle wrapper --gradle-version=8.2
echo "Gradle wrapper generated successfully"
