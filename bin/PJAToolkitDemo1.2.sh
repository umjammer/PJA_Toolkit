#!/bin/sh
# With JDK <= 1.1, use PJAToolkitDemo.sh
#
# pja.jar must be in bootclasspath or Toolkit.getDefaultToolkit ()
# and GraphicsEnvironment.getLocalGraphicsEnvironment () will fail to
# load com.eteks.awt.PJAToolkit and com.eteks.java2d.PJAGraphicsEnvironment classes.
#
# Check java.awt.fonts path : default is set to /.../jdk.../jre/lib/fonts
#   where Lucida True Type fonts delivered with JDK 1.2 are.
#   You can add other directories to this path separated by :
#
# user.home system property must be set to the directory where lib/font.properties file
# will be found during GraphicsEnvironment.getLocalGraphicsEnvironment () call.
# ../lib/font.properties file delivered with PJA uses Lucida True Type default fonts.
#
# Under JDK 1.2, you may have to add an other True Type font file in java.awt.fonts path
# if a "No fonts were found in ..." message appears when you use
# GraphicsEnvironment.getLocalGraphicsEnvironment ().getAvailableFontFamilyNames () or
# GraphicsEnvironment.getLocalGraphicsEnvironment ().getAllFonts () methods.
# This font file can be a copy of one of Lucida True Type default fonts (this is due
# to a bug that doesn't take into account all the fonts already used in lib/font.properties)
#
# Run this command in the bin directory of pja or check user.home directory
#
java -Xbootclasspath/a:../lib/pja.jar \
     -Dawt.toolkit=com.eteks.awt.PJAToolkit \
     -Djava.awt.graphicsenv=com.eteks.java2d.PJAGraphicsEnvironment \
     -Djava2d.font.usePlatformFont=false \
     -Djava.awt.fonts=/usr/local/jdk1.2.2/jre/lib/fonts \
     -Duser.home=.. \
     -classpath ../lib/pjatools.jar \
   ToolkitDemo
