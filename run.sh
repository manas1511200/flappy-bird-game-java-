#!/bin/bash
javac *.java
chmod +r hit.wav
chmod +r jump.wav
chmod +r point.wav
java App.java
rm -f *.class


