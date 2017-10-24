#!/bin/sh
# Requires JDK >= 1.4 java compiler

PJASRC="src/com/eteks/awt/PJAComponentPeer.java \
   src/com/eteks/awt/PJAFontData.java \
   src/com/eteks/awt/PJAFontMetrics.java \
   src/com/eteks/awt/PJAFontPeer.java \
   src/com/eteks/awt/PJAFramePeer.java \
   src/com/eteks/awt/PJAGraphics.java \
   src/com/eteks/awt/PJAGraphicsExtension.java \
   src/com/eteks/awt/PJAGraphicsManager.java \
   src/com/eteks/awt/PJAImage.java \
   src/com/eteks/awt/PJALightweightPeer.java \
   src/com/eteks/awt/PJAMenuComponentPeer.java \
   src/com/eteks/awt/PJAToolkit.java \
   src/com/eteks/awt/image/GIFDecoder.java \
   src/com/eteks/awt/image/Web216ColorModel.java \
   src/com/eteks/awt/servlet/PJARedirectServlet.java \
   src/com/eteks/awt/servlet/PJAServlet.java \
   src/com/eteks/java2d/PJABufferedImage.java \
   src/com/eteks/java2d/PJAGraphicsConfiguration.java \
   src/com/eteks/java2d/PJAGraphicsDevice.java \
   src/com/eteks/java2d/PJAGraphicsEnvironment.java \
   src/com/eteks/java2d/PJAGraphicsManager2D.java"

PJATOOLSSRC="src/ToolkitDemo.java \
   src/PJADemo.java \
   src/PJANativeToolkitComparison.java \
   src/Acme/IntHashtable.java \
   src/Acme/JPM/Encoders/GifEncoder.java \
   src/Acme/JPM/Encoders/ImageEncoder.java \
   src/Acme/JPM/Encoders/GifEncoderNoCM.java \
   src/Acme/JPM/Encoders/ImageEncoderNoCM.java \
   src/com/eteks/filter/Web216ColorsFilter.java \
   src/com/eteks/servlet/DefaultToolkitTest.java \
   src/com/eteks/servlet/PJAServletTest.java \
   src/com/eteks/servlet/TeksSurveyPie.java \
   src/com/eteks/tools/awt/GridBagConstraints2.java \
   src/com/eteks/tools/fontcapture/PJAFontCapture.java"

PJADOCSRC="com.eteks.awt \
   com.eteks.awt.image \
   com.eteks.awt.servlet \
   com.eteks.java2d \
   com.eteks.tools.fontcapture \
   com.eteks.tools.awt \
   com.eteks.filter \
   com.eteks.servlet"

# pja.jar build
echo compiling $PJASRC

mkdir lib/tmp

javac -target 1.1 -d lib/tmp -classpath lib/servlet.jar $PJASRC
jar cvfM lib/pja.jar -C lib/tmp/ .

rm -r -f lib/tmp

# pjatools.jar build
echo compiling $PJATOOLSSRC

mkdir lib/tmp

javac -target 1.1 -d lib/tmp -classpath lib/servlet.jar:lib/pja.jar $PJATOOLSSRC
jar cvfM lib/pjatools.jar -C lib/tmp/ .

rm -r -f lib/tmp

# javadoc build
javadoc -sourcepath src -classpath lib/servlet.jar -d doc -version -author -windowtitle "PJA Toolkit" -header "<A HREF='http://www.eteks.com'><FONT SIZE='+1'>http://www.eteks.com</FONT></A>" -footer "<P><CENTER>&copy; 1997-2003 eTeks - All rights reserved</CENTER>" $PJADOCSRC
