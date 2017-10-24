REM  With JDK <= 1.1, use PJAToolkitDemo.bat
REM 
REM pja.jar must be in bootclasspath or Toolkit.getDefaultToolkit () 
REM and GraphicsEnvironment.getLocalGraphicsEnvironment () will fail to 
REM load com.eteks.awt.PJAToolkit and com.eteks.java2d.PJAGraphicsEnvironment classes.
REM
java -Xbootclasspath/a:..\lib\pja.jar -Dawt.toolkit=com.eteks.awt.PJAToolkit -Djava.awt.graphicsenv=com.eteks.java2d.PJAGraphicsEnvironment -classpath ..\lib\pjatools.jar ToolkitDemo
