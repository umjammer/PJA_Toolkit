/*
 * @(#)PJANativeToolkitComparison.java   05/16/2000
 *
 * Copyright (c) 2000-2001 Emmanuel PUYBARET / eTeks <info@eteks.com>. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Visit eTeks web site for up-to-date versions of this file and other
 * Java tools and tutorials : http://www.eteks.com/
 */

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Component;
import java.awt.Button;
import java.awt.Label;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Canvas;
import java.awt.Insets;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Event;
import java.awt.Window;
import java.awt.AWTError;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.eteks.awt.PJAImage;
import com.eteks.awt.PJAGraphicsManager;
import com.eteks.tools.awt.GridBagConstraints2;
import com.eteks.tools.fontcapture.PJAFontCapture;

/**
 * Pure Java AWT Toolkit demo. This demo allows to compare at screen the rendering
 * of the PJAToolkit and of native toolkit. A speed meter dialog box is also launched
 * to compare the average duration for drawing between PJA and native toolkit.
 * This is Java 1.0 compatible code.
 *
 * @version   1.1
 * @author    Emmanuel Puybaret
 * @see       ToolkitDemo
 * @see       com.eteks.awt.PJAToolkit
 * @see       com.eteks.awt.PJAGraphics	
 * @since     PJA1.1
 */
public class PJANativeToolkitComparison extends ToolkitDemo
{
  /**
   * An example of use for Pure Java AWT Toolkit.
   */
  public static void main (String args [])
  {
    // First display an information dialog box
    showFirstDialog ();

    Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();

    // Create an array of the drawers to be tested
    GraphicsDrawer drawers [] = {new GraphicsArcDrawer (),
                                 new GraphicsPolygonDrawer (),
                                 new GraphicsTextDrawer (),
                                 new GraphicsImageDrawer ()};

    // Dummy frame to create dialogs
    for (int i = 0; i < drawers.length; i++)
    {
      final Dimension      drawerSize = new Dimension (drawers [i].getWidth (), drawers [i].getHeight ());
      final GraphicsDrawer drawer = drawers [i];

      // Create a PJA image and draw into it (this will use PJAGraphics methods)
      Image PJAToolkitImage = new PJAImage (drawer.getWidth (), drawer.getHeight ());
      try
      {
        drawer.paint (PJAToolkitImage.getGraphics ());
      }
      catch (Error error)
      {
        System.out.println (error + "\nIn case of an error related to font, run PJAFontCapture to generate at least one .pjaf font file.");
      }

      // Draw the PJA image instance at screen in a canvas
      final Image nativeImage = Toolkit.getDefaultToolkit ().createImage (PJAToolkitImage.getSource ());
      Canvas PJACanvas = new Canvas ()
        {
          public Dimension preferredSize ()
          {
            return drawerSize;
          }

          public void paint (Graphics gc)
          {
            gc.drawImage (nativeImage, 0, 0, this);
          }
        };
      // Use this canvas in a first window
      Frame PJAFrame = new Frame ("PJA Toolkit " + drawer);
      PJAFrame.add ("Center", PJACanvas);
      PJAFrame.pack ();
      PJAFrame.move (0, screenSize.height * i / drawers.length);
      PJAFrame.show ();

      // Draw directly with native Toolkit in a second canvas
      Canvas nativeCanvas = new Canvas ()
        {
          public Dimension preferredSize ()
          {
            return drawerSize;
          }

          public void paint (Graphics gc)
          {
            drawer.paint (gc);
          }
        };
      // Use this canvas in a second window
      Frame nativeFrame = new Frame ("Native Toolkit " + drawer);
      nativeFrame.add ("Center", nativeCanvas);
      nativeFrame.pack ();
      nativeFrame.move (screenSize.width / 2, screenSize.height * i / drawers.length);
      nativeFrame.show ();
    }

    // Show a dialog that compares drawing speed
    showSpeedMeterDialog (drawers);
  }

  private static void showFirstDialog ()
  {
    // Dialog box for copyrights and to run Font capture
    // if no default font is available
    final Dialog  firstDialog = new Dialog (new Frame (), "PJA Toolkit", true)
      {
        // This demo uses old Event model to be able to run with JDK 1.0
        public boolean action (Event evt, Object arg)
        {
          // This is a trick to be sure Window.dispose () is called in JDK 1.0
          ((Window)this).dispose ();
          return true;
        }
      };
    final boolean noPJAFont = PJAGraphicsManager.getDefaultGraphicsManager ().getDefaultFont () == null;
    Component     object;
    GridBagLayout layout = new GridBagLayout ();
    firstDialog.setLayout (layout);
    firstDialog.add (object = new Label ("Pure Java AWT Toolkit comparison demo."));
    layout.setConstraints (object, new GridBagConstraints2 (GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (5, 5, 0, 5), 0, 0));
    firstDialog.add (object = new Label ("\u00a9 Copyright 2000-2001 eTeks <info@eteks.com>."));
    layout.setConstraints (object, new GridBagConstraints2 (GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (0, 5, 20, 5), 0, 0));
    if (noPJAFont)
    {
      firstDialog.add (object = new Label ("No default font : run first PJA Font capture utility."));
      layout.setConstraints (object, new GridBagConstraints2 (GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (5, 10, 0, 10), 0, 0));
      firstDialog.add (object = new Label ("(command line : java com.eteks.tools.fontcapture.PJAFontCapture)"));
      layout.setConstraints (object, new GridBagConstraints2 (GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (0, 10, 0, 10), 0, 0));
    }
    firstDialog.add (object = new Button (noPJAFont ? "Run Font capture" : " Ok "));
    layout.setConstraints (object, new GridBagConstraints2 (GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (20, 0, 10, 0), 0, 0));
    firstDialog.setResizable (false);
    firstDialog.pack ();
    // This is a trick to be sure Window.show () is called in JDK 1.0
    ((Window)firstDialog).show ();
    // Run Font capture
    if (noPJAFont)
    {
      PJAFontCapture.showCaptureFontDialog ("Continue demo");
      PJAGraphicsManager.getDefaultGraphicsManager ().loadFonts ();
    }
  }

  private static void showSpeedMeterDialog (final GraphicsDrawer drawers [])
  {
    final Dialog  speedMeterDialog = new Dialog (new Frame (), "Average drawing duration", false)
      {
        // This demo uses old Event model to be able to run with JDK 1.0
        public boolean action (Event evt, Object arg)
        {
          System.exit (0);
          return true;
        }
      };
    // Build the speed meter dialog with all its labels
    GridBagLayout layout = new GridBagLayout ();
    speedMeterDialog.setLayout (layout);
    // Left column titles panel
    Panel titlePanel = new Panel ();
    titlePanel.setLayout (new GridLayout (drawers.length + 1, 1, 0, 10));
    // Speed values table panel
    Panel speedPanel = new Panel ();
    speedPanel.setLayout (new GridLayout (drawers.length + 1, 3, 10, 10));
    titlePanel.add (new Label (""));
    speedPanel.add (new Label ("PJA (ms)", Label.CENTER));
    speedPanel.add (new Label ("Native (ms)", Label.CENTER));
    speedPanel.add (new Label ("Ratio (PJA/Native)", Label.CENTER));

    Label PJAAverageDurationLabel    [] = new Label [drawers.length];
    Label nativeAverageDurationLabel [] = new Label [drawers.length];
    Label rationLabel []                = new Label [drawers.length];
    for (int i = 0; i < drawers.length; i++)
    {
      titlePanel.add (new Label (drawers [i].toString ()));
      PJAAverageDurationLabel [i] = new Label ("00000 ", Label.RIGHT);
      PJAAverageDurationLabel [i].setForeground (Color.green);
      PJAAverageDurationLabel [i].setBackground (Color.black);
      speedPanel.add (PJAAverageDurationLabel [i]);
      nativeAverageDurationLabel [i] = new Label ("00000 ", Label.RIGHT);
      nativeAverageDurationLabel [i].setForeground (Color.red);
      nativeAverageDurationLabel [i].setBackground (Color.black);
      speedPanel.add (nativeAverageDurationLabel [i]);
      rationLabel [i] = new Label ("      1 ", Label.RIGHT);
      rationLabel [i].setForeground (Color.lightGray);
      rationLabel [i].setBackground (Color.black);
      speedPanel.add (rationLabel [i]);
    }
    speedMeterDialog.add (titlePanel);
    layout.setConstraints (titlePanel, new GridBagConstraints2 (0, 0, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (10, 10, 10, 10), 0, 0));    
    speedMeterDialog.add (speedPanel);
    layout.setConstraints (speedPanel, new GridBagConstraints2 (1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (10, 0, 10, 10), 0, 0));    
    Button button = new Button (" Quit ");
    speedMeterDialog.add (button);
    layout.setConstraints (button, new GridBagConstraints2 (0, 1, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets (10, 0, 10, 0), 0, 0));
    speedMeterDialog.setResizable (false);
    speedMeterDialog.pack ();
    // This is a trick to be sure Window.show () is called in JDK 1.0
    ((Window)speedMeterDialog).show ();

    final long PJATotalDuration    [] = new long [drawers.length];
    final long nativeTotalDuration [] = new long [drawers.length];
    // Dummy frame to create images
    Frame frame = new Frame ();
    frame.addNotify ();
    for (int drawingCount = 1; ; drawingCount++)
    {
      long currentTime = System.currentTimeMillis ();

      for (int i = 0, y = 0; i < drawers.length; i++)
        try
        {
          // Create a PJA image and draw into it (this will use PJAGraphics methods)
          Image PJAToolkitImage = new PJAImage (drawers [i].getWidth (), drawers [i].getHeight ());
          PJATotalDuration [i] += getDrawingDuration (PJAToolkitImage.getGraphics (), drawers [i]);
          PJAToolkitImage.flush ();
          // Update PJA duration label
          PJAAverageDurationLabel [i].setText (String.valueOf (PJATotalDuration [i] / drawingCount) + " ");

          if (!Boolean.getBoolean ("com.eteks.awt.useframe"))
          {
            // Create a toolkit off screen image and draw into it (this will use default toolkit Graphics methods)
            // Java 1.0 simpliest way to create a buffered image
            Image nativeImage = frame.createImage (drawers [i].getWidth (), drawers [i].getHeight ());
            nativeTotalDuration [i] += getDrawingDuration (nativeImage.getGraphics (), drawers [i]);
            nativeImage.flush ();
          }
          else
          {
            // With Java 1.2, off screen images are managed with java.awt.image.BufferedImage class
            // which doesn't use any native graphics resources (Windows GDI or X11). Thus, to really
            // compare with native drawing, you can define the system property com.eteks.awt.useframe to true
            // at command line (-Dcom.eteks.awt.useframe=true) to measure the duration of drawing at screen.
            // This isn't the default for 2 reasons :
            // - First, the next lines show and dispose multiple frames which makes an unpleasant
            // flickering effect at screen for a demo.
            // - Second, as PJA first goal is to generate off screen images, it's logical to compare
            // with off screen native computation.
            final int j = i;
            class SynchronizedCanvas extends Canvas
            {
              Object paintLock = new Object (); // Lock used to synchronized on drawing
             
              public Dimension preferredSize ()
              {
                return new Dimension (drawers [j].getWidth (), drawers [j].getHeight ());
              }
             
              public void paint (final Graphics gc)
              {
                if (paintLock != null)
                  synchronized (paintLock)
                  {
                    nativeTotalDuration [j] += getDrawingDuration (gc, drawers [j]);
                    // Notify drawing is finished
                    Object lock = paintLock;
                    paintLock = null;
                    lock.notifyAll ();
                  }
              }
            };
            // Create a frame in which drawing is done
            SynchronizedCanvas nativeCanvas = new SynchronizedCanvas ();
            Frame nativeFrame = new Frame ();
            nativeFrame.add ("Center", nativeCanvas);
            nativeFrame.pack ();
            // Put it under speed meter dialog to be sure there will be no clipping
            nativeFrame.move (0, speedMeterDialog.location ().y + speedMeterDialog.size ().height + 20);
            synchronized (nativeCanvas.paintLock)
            {
              try
              {
                nativeFrame.show ();
                // Wait that paint () is done before disposing the window
                if (nativeCanvas.paintLock != null)
                  nativeCanvas.paintLock.wait ();
                // Just for user to check the image is complete
                Thread.sleep (200);
                nativeFrame.dispose ();
              }
              catch (InterruptedException e)
              { }
            }
          }
          // Update native duration label
          nativeAverageDurationLabel [i].setText (String.valueOf (nativeTotalDuration [i] / drawingCount) + " ");

          // Update ratio label
          rationLabel [i].setText (nativeTotalDuration [i] != 0
                                     ? String.valueOf (PJATotalDuration [i] / nativeTotalDuration [i]) + " "
                                     : "");
        }
        catch (Error error)
        {
          System.out.println (error + "\nIn case of an error related to font, run PJAFontCapture to generate at least one .pjaf font file.");
        }

      try
      {
        long ellapsedTime = System.currentTimeMillis () - currentTime;
        // Let a chance to your system to breath a little :
        // If ellapsedTime was less than 0.5 s then wait next second
        //                                     otherwise wait 1 s
        Thread.sleep (ellapsedTime < 500
                       ? (1000 - ellapsedTime)
                       : 1000);
      }
      catch (InterruptedException e)
      {
        System.exit (0);
      }
    }
  }

  private static long getDrawingDuration (Graphics       gc,
                                          GraphicsDrawer drawer)
  {
    System.gc ();

    // Get current time, draw in image and return duration
    long currentTime = System.currentTimeMillis ();
    drawer.paint (gc);
    Toolkit.getDefaultToolkit ().sync ();

    return System.currentTimeMillis () - currentTime;
  }
}

