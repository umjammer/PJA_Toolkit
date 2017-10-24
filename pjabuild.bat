REM Requires JDK >= 1.4 java compiler

@echo off

SET PJASRC=src\com\eteks\awt\*.java src\com\eteks\awt\image\*.java src\com\eteks\awt\servlet\PJAServlet.java src\com\eteks\java2d\*.java

SET PJATOOLSSRC=src\ToolkitDemo.java src\PJADemo.java src\PJANativeToolkitComparison.java src\Acme\IntHashtable.java src\Acme\JPM\Encoders\*.java  src\com\eteks\filter\Web216ColorsFilter.java src\com\eteks\servlet\DefaultToolkitTest.java src\com\eteks\servlet\PJAServletTest.java src\com\eteks\servlet\TeksSurveyPie.java src\com\eteks\tools\awt\GridBagConstraints2.java src\com\eteks\tools\fontcapture\PJAFontCapture.java 
                                                            
SET PJADOCSRC=com.eteks.awt com.eteks.awt.image com.eteks.awt.servlet com.eteks.java2d com.eteks.tools.fontcapture com.eteks.tools.awt com.eteks.filter com.eteks.servlet                                                                         

REM
REM pja.jar build
REM
echo Compiling %PJASRC%

md lib\tmp1
javac -target 1.1 -d lib\tmp1 -classpath lib\servlet.jar %PJASRC%
jar cfM0 lib\pja.jar -C lib\tmp1\ .

REM
REM pjatools.jar build
REM
echo Compiling %PJATOOLSSRC%

md lib\tmp2                                                                                                  
javac -target 1.1 -d lib\tmp2 -classpath lib\servlet.jar;lib\pja.jar %PJATOOLSSRC%
jar cfM0 lib\pjatools.jar -C lib\tmp2\ . 

REM
REM javadoc build
REM
javadoc -sourcepath src -classpath lib\servlet.jar -d doc -version -author -windowtitle "PJA Toolkit" -header "<A HREF='http://www.eteks.com'><FONT SIZE='+1'>http://www.eteks.com</FONT></A>" -footer "<P><CENTER>&copy; 1997-2003 eTeks - All rights reserved</CENTER>" %PJADOCSRC%
