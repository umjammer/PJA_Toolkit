/*
 * @(#)ToolkitDemo.java   05/16/2000
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
 *
 * ***********************************************************************
 *
 * Copyright (C) 1996,1998 by Jef Poskanzer <jef@acme.com>. All rights reserved.
 * (for the use of Acme.JPM.Encoders.GifEncoder)
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * Visit the ACME Labs Java page for up-to-date versions of this and other
 * fine Java utilities: http://www.acme.com/java/
 */

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.MediaTracker;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.MemoryImageSource;
import java.awt.AWTError;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import Acme.JPM.Encoders.GifEncoderNoCM;
import com.eteks.filter.Web216ColorsFilter;

/**
 * Toolkit demo. This demo computes different images and save them in current directory.
 * See the source of the <code>main ()</code> method of this class to have a test example
 * of all the methods of the class <code>java.awt.Graphics</code>.
 * Only java classes and methods are used in this example (except to save images).<BR>
 * Set <code>awt.toolkit</code> System property to <code>com.eteks.awt.PJAToolkit</code> to compute
 * the images with PJA toolkit.<BR>
 * You may also try <code>com.eteks.servlet.TeksSurveyPie</code> servlet class.
 *
 * @version   1.1
 * @author    Emmanuel Puybaret
 * @see       com.eteks.awt.PJAToolkit
 * @see       com.eteks.awt.PJAGraphics	
 * @see       com.eteks.servlet.TeksSurveyPie	
 * @since     PJA1.1
 */
public class ToolkitDemo
{
  /**
   * An example of use of a toolkit.
   */
  public static void main (String args [])
  {
    System.out.println ("Toolkit demo");
    System.out.println ("\u00a9 Copyright 2000-2001 eTeks <info@eteks.com>.");
    System.out.println ("\u00a9 Copyright 1996,1998 by Jef Poskanzer <jef@acme.com>.\n");
    System.out.println ("Toolkit class in use is " + Toolkit.getDefaultToolkit ().getClass ().getName ());

    // Create an array of the drawers to be tested
    GraphicsDrawer drawers [] = {new GraphicsArcDrawer (),
                                 new GraphicsPolygonDrawer (),
                                 new GraphicsTextDrawer (),
                                 new GraphicsImageDrawer ()};

    try
    {
      // Get a different prefix for each toolkit
      String prefix = Toolkit.getDefaultToolkit ().getClass ().getName ().indexOf ("PJA") >= 0
                          ? "PJAToolkit"
                          : "NativeToolkit";
      ComputeImageFiles (drawers, new ImageBuilder (), prefix);
    }
    catch (AWTError e)
    {
      // This error can be thrown by getDefaultToolkit () if it doesn't succeed to get a toolkit
      System.out.println (e + "\nThis demo requires a toolkit to run.");
    }

    System.exit (0);
  }

  protected static void ComputeImageFiles (GraphicsDrawer drawers [],
                                           ImageBuilder   imageBuilder,
                                           String         filePrefix)
  {
    for (int i = 0, y = 0; i < drawers.length; i++)
    {
      // Create an image and draw into it
      Image image = imageBuilder.createImage (drawers [i].getWidth (), drawers [i].getHeight ());

      // Draw in the image
      try
      {
        System.out.println ("Computing " + drawers [i] + " image (instance of " + image.getClass ().getName () + ")");
        Graphics gc = image.getGraphics ();
        try
        {
          if (gc instanceof Graphics2D)
          {
            // If Java2D available, compute the nicest image possible          
            ((Graphics2D)gc).setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D)gc).setRenderingHint (RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            ((Graphics2D)gc).setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);          
          }
        }
        catch (LinkageError e)
        { } // Graphics2D not found    
        drawers [i].paint (gc);
      }
      catch (AWTError error)
      {
        System.out.println (error + "\nIn case of an error related to font, you should run first PJAFontCapture to generate at least one .pjaf font file.");
      }

      try
      {
        String fileName = filePrefix + drawers [i] + ".gif";
        System.out.println ("Saving image to " + fileName);

        // Save the image in GIF with Acme GifEncoder
        OutputStream out = new FileOutputStream (fileName);
        // Image is saved using Web216ColorsFilter filter to be sure to have less than 256 colors
        new GifEncoderNoCM (new FilteredImageSource (image.getSource (),
                                                     new Web216ColorsFilter ()),
                            out).encode ();
        out.close ();
      }
      catch (IOException e)
      {
        System.err.println ("Image image" + drawers [i] + " couldn't be saved");
      }
    }

  }

  protected static class ImageBuilder
  {
    public Image createImage (int width, int height)
    {
      // Java 1.0 simplest way to create a buffered image
      Frame frame = new Frame ();
      frame.addNotify ();
      return frame.createImage (width, height);
    }
  }

  protected static interface GraphicsDrawer
  {
    public int getWidth ();
    public int getHeight ();
    public void paint (Graphics gc);
  }

  // Arcs and ovals exemples
  protected static class GraphicsArcDrawer implements GraphicsDrawer
  {
    public int getWidth ()
    {
      return 71 * 6;
    }

    public int getHeight ()
    {
      return 81;
    }

    public void paint (Graphics gc)
    {
      int startAngles [] = {30, 30, 30, 30, 160, 240};
      int angles      [] = {42, 120, 200, 270, 280, 240};
      int widths      [] = {69, 69, 69, 69, 69, 69};
      int heights     [] = {50, 80, 70, 70, 35, 70};

      gc.setColor (Color.white);
      gc.fillRect (0, 0, getWidth (), getHeight ());
      for (int i = 0, x = 0; i < widths.length; x += widths [i++] + 1)
      {
        gc.setColor (Color.lightGray);
        gc.fillOval (x, 0, widths [i], heights [i]);
        gc.setColor (Color.darkGray);
        gc.drawOval (x, 0, widths [i], heights [i]);
        gc.setColor (Color.red);
        gc.fillArc (x, 0, widths [i], heights [i], startAngles [i], angles [i]);
        gc.setColor (Color.blue);
        gc.drawArc (x, 0, widths [i], heights [i], startAngles [i], angles [i]);
        gc.drawArc (x + 1, 1, widths [i] - 2, heights [i] - 2, startAngles [i], angles [i]);
      }
    }

    public String toString ()
    {
      return "ArcDrawer";
    }
  }

  // Lines and polygons exemples
  protected static class GraphicsPolygonDrawer implements GraphicsDrawer
  {
    public int getWidth ()
    {
      return 400;
    }

    public int getHeight ()
    {
      return 101;
    }

    public void paint (Graphics gc)
    {
      gc.setColor (Color.white);
      gc.fillRect (0, 0, getWidth (), getHeight ());
      
      gc.setColor (Color.yellow.darker ());
      gc.fillRoundRect (2, 2, 95, 95, 25, 18);
      gc.setColor (Color.darkGray);
      gc.drawRoundRect (2, 2, 95, 95, 25, 18);
      for (int angle = 0; angle < 360; angle += 15)
      {
        gc.setColor (new Color (angle * 255 / 360, angle * 255 / 360, angle * 255 / 360));
        Graphics gc2 = gc.create ();
        gc2.translate ((int)(angle / 25 * Math.cos (angle * Math.PI / 180.)), (int)(angle / 25 * Math.sin (angle * Math.PI / 180.)));
        gc2.drawLine (50, 50, (int)(50 + 35 * Math.cos (angle * Math.PI / 180.)), (int)(50 + 35 * Math.sin (angle * Math.PI / 180.)));
        gc2.dispose ();
      }

      gc.setColor (Color.yellow.darker ());
      gc.fill3DRect (102, 2, 294, 96, false);
      gc.draw3DRect (101, 1, 295, 97, true);
      Polygon polygons [] = {new Polygon (new int [] {107, 150, 200, 160, 107}, new int [] {5, 5, 25, 49, 49}, 5),
                             new Polygon (new int [] {202, 225, 260, 300, 300, 260, 225, 202}, new int [] {5, 49, 7, 49, 5, 49, 4, 49}, 8),
                             new Polygon (new int [] {302, 330, 302, 350, 390, 390, 360, 302}, new int [] {6, 25, 47, 32, 47, 6, 17, 6}, 8)};
      for (int i = 0; i < polygons.length; i++)
      {
        gc.setColor (Color.pink);
        gc.fillPolygon (polygons [i]);

        Graphics gc2 = gc.create ();
        gc2.translate (0, 45);
        gc2.setColor (Color.orange);
        gc2.fillPolygon (polygons [i]);
        gc2.setColor (Color.darkGray);
        gc2.drawPolygon (polygons [i]);
        gc2.dispose ();
      }
    }

    public String toString ()
    {
      return "PolygonDrawer";
    }
  }

  // Text exemples
  protected static class GraphicsTextDrawer implements GraphicsDrawer
  {
    public int getWidth ()
    {
      return 300;
    }

    public int getHeight ()
    {
      return 170;
    }

    public void paint (Graphics gc)
    {
      gc.setColor (Color.white);
      gc.fillRect (0, 0, getWidth (), getHeight ());

      String defaultFontName = gc.getFont ().getName ();
      int fontStyle [] = {Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN,
                          Font.ITALIC, Font.BOLD, Font.ITALIC | Font.BOLD};
      int fontSize  [] = {9, 10, 12, 16, 24, 12, 12, 12};
      for (int i = 0, y = 0; i < fontStyle.length; i++)
      {
        String text = defaultFontName;
        if ((fontStyle [i] & Font.BOLD) != 0)
          text += (fontStyle [i] & Font.ITALIC) != 0 ? " bolditalic " : " bold ";
        else
          text += (fontStyle [i] & Font.ITALIC) != 0 ? " italic " : " plain ";
        text += fontSize [i];

        // Using "" as font name set it to default font
        gc.setFont (new Font ("", fontStyle [i], fontSize [i]));

        // Draw first rectangles around with FontMetrics
        FontMetrics metrics = gc.getFontMetrics ();
        int    textWidth = metrics.stringWidth (text);
        gc.setColor (Color.cyan.darker ());
        gc.drawRect (5, y, textWidth, metrics.getHeight ());
        gc.drawRect (5, y + metrics.getLeading (), textWidth, metrics.getAscent ());

        // Draw text in rectangles
        gc.setColor (Color.black);
        gc.drawString (text, 5, y + metrics.getAscent () + metrics.getLeading ());

        y += metrics.getHeight () + 2;
      }
    }

    public String toString ()
    {
      return "TextDrawer";
    }
  }

  // Image exemples
  protected static class GraphicsImageDrawer implements GraphicsDrawer
  {
    public int getWidth ()
    {
      return 250;
    }

    public int getHeight ()
    {
      return 100;
    }

    public void paint (Graphics gc)
    {
      gc.setColor (Color.white);
      gc.fillRect (0, 0, getWidth (), getHeight ());

      Rectangle imagePositions [] = {new Rectangle (0, 0, 150, 100),
                                     new Rectangle (140, 10, 80, 70),
                                     new Rectangle (130, 65, 90, 25)};
      Image      images [] = new Image [3];
      // Build a colorfull MemoryImageSource image
      Dimension imageSize = new Dimension (100, 100);
      int [ ]    pixels = new int [imageSize.width * imageSize.height];
      for (int i = 0; i < imageSize.height; i++)
        for (int j = 0; j < imageSize.width; j++)
          pixels [i * imageSize.width + j] =
                192 << 24                            // Transparency
              | ((j * 255 / imageSize.width) << 16)  // Red
              | ((i * 255 / imageSize.height) << 8)  // Green
              | 0;                                   // Blue

      // Creation of an offscreen image
      Toolkit toolkit = Toolkit.getDefaultToolkit ();
      images [0] = toolkit.getImage ("photo.jpg");
      images [1] = toolkit.createImage (new MemoryImageSource (imageSize.width, imageSize.height,
                                                               pixels, 0, imageSize.width));
      images [2] = toolkit.getImage ("logoeteks.gif");
      ImageObserver observer = new ImageObserver ()
                                     {
                                       public boolean imageUpdate (Image img, int flags,
                                                                   int x, int y, int w, int h)
                                       {
                                         return (flags & (ALLBITS | ABORT)) == 0;
                                       }
                                     };
      for (int i = 0; i < images.length; i++)
      {        
        try
        {
          MediaTracker imageTracker = new MediaTracker (new Frame ());
          imageTracker.addImage (images [i], 0);
          imageTracker.waitForID (0);
        }
        catch (Exception e)
        { }
        gc.drawImage (images [i], imagePositions [i].x, imagePositions [i].y,
                                  imagePositions [i].width, imagePositions [i].height, observer);
      }

      gc.copyArea (210, 0, 10, 60, 12, 0);
      if (gc.getFont () != null)
      {
        gc.setFont (new Font (gc.getFont ().getName (), Font.ITALIC | Font.BOLD, 20));
        gc.setXORMode (Color.red);
        gc.clipRect (10, 47, 250, 12);
        gc.drawString ("XOR Clipped text", 5, 60);
      }
    }

    public String toString ()
    {
      return "ImageDrawer";
    }
  }
}

