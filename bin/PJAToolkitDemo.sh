#!/bin/sh
# With JDK >= 1.2, use PJAToolkitDemo1.2.sh
java -classpath $CLASSPATH:../lib/pja.jar:../lib/pjatools.jar -Djava.awt.fonts=. -Dawt.toolkit=com.eteks.awt.PJAToolkit ToolkitDemo
