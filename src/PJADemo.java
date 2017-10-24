/*
 * @(#)PJADemo.java   05/16/2000
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
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Font;                      // Used only for PLAIN, ITALIC and BOLD constant values
import java.awt.image.FilteredImageSource; // Don't need java.awt.image.ColorModel instance
import java.awt.AWTError;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import Acme.JPM.Encoders.GifEncoderNoCM;
import com.eteks.filter.Web216ColorsFilter;
import com.eteks.awt.PJAImage;
import com.eteks.awt.PJAGraphicsExtension;

/**
 * Pure Java AWT demo.
 * <P>See the source of the <code>main ()</code> method of this class to have a test example
 * of all the methods of the class <code>java.awt.Graphics</code>. This exemple gives the same result as
 * <code>ToolkitDemo</code> class <code>main ()</code> method but doesn't need any Toolkit and awt library.
 *
 * @version   1.1
 * @author    Emmanuel Puybaret
 * @see       ToolkitDemo	
 * @see       com.eteks.awt.PJAGraphics	
 * @since     PJA1.1
 */
public class PJADemo extends ToolkitDemo
{
  /**
   * An example of use for Pure Java AWT without Toolkit.
   */
  public static void main (String args [])
  {
    // Use a security manager that disallows awt library
    System.setSecurityManager (new NoAWTSecurityManager ());

    System.out.println ("Pure Java AWT demo");
    System.out.println ("\u00a9 Copyright 2000-2001 eTeks <info@eteks.com>.");
    System.out.println ("\u00a9 Copyright 1996,1998 by Jef Poskanzer <jef@acme.com>.\n");

    try
    {
      // Check NoAWTSecurityManager is active
      new java.awt.Rectangle ();
      System.out.println ("Can access to Rectangle class, NoAWTSecurityManager may not be active.\n");
    }
    catch (Throwable e)
    {
      System.out.println ("Can't access to Rectangle class, because NoAWTSecurityManager is active.\n");
    }

    // Create an array of the drawers to be tested
    GraphicsDrawer drawers [] = {new PJAGraphicsArcDrawer (),
                                 new PJAGraphicsPolygonDrawer (),
                                 new PJAGraphicsTextDrawer ()};

    ComputeImageFiles (drawers,
                       new PJAImageBuilder (),
                       "PJADemo");

    System.exit (0);
  }

  // A restrictive SecurityManager that prevents the use of awt library
  private static class NoAWTSecurityManager extends SecurityManager
  {
    public void checkLink (String lib)
    {
      System.out.println ("Checking library " + lib);

      // Prevent the use of awt
      if  ("awt".equals  (lib))
        throw new SecurityException  ("AWT");
    }

    // Allow all the rest
    public void checkCreateClassLoader () { }
    public void checkAccess (Thread g) { }
    public void checkAccess (ThreadGroup g) { }
    public void checkExit (int status) { }
    public void checkExec (String cmd) { }
    public void checkRead (java.io.FileDescriptor fd) { }
    public void checkRead (String file) { }
    public void checkRead (String file, Object context) { }
    public void checkWrite (java.io.FileDescriptor fd) { }
    public void checkWrite (String file) { }
    public void checkDelete (String file) { }
    public void checkConnect (String host, int port) { }
    public void checkConnect (String host, int port, Object context) { }
    public void checkListen (int port) { }
    public void checkAccept (String host, int port) { }
    public void checkMulticast (java.net.InetAddress maddr) { }
    public void checkMulticast (java.net.InetAddress maddr, byte ttl) { }
    public void checkPropertiesAccess () { }
    public void checkPropertyAccess (String key) { }
    public void checkPropertyAccess (String key, String def) { }
    public boolean checkTopLevelWindow (Object window) { return true; }
    public void checkPrintJobAccess () { }
    public void checkSystemClipboardAccess () { }
    public void checkAwtEventQueueAccess () { }
    public void checkPackageAccess (String pkg) { }
    public void checkPackageDefinition (String pkg) { }
    public void checkSetFactory () { }
    public void checkMemberAccess (Class clazz, int which) { }
    public void checkSecurityAccess (String provider) { }
  }

  private static class PJAImageBuilder extends ImageBuilder
  {
    public Image createImage (int width, int height)
    {
      // Create a PJAImage (can't create PJABufferedImage instances because it requires awt library)
      return new PJAImage (width, height);
    }
  }

  // Arcs and ovals exemples
  private static class PJAGraphicsArcDrawer extends GraphicsArcDrawer
  {
    // Paint rewriten to avoid use of Color, Font and FontMetrics classes
    public void paint (Graphics gc)
    {
      int startAngles [] = {30, 30, 30, 30, 160, 240};
      int angles      [] = {42, 120, 200, 270, 280, 240};
      int widths      [] = {69, 69, 69, 69, 69, 69};
      int heights     [] = {50, 80, 70, 70, 35, 70};

      // gc.setColor (Color.white);
      ((PJAGraphicsExtension)gc).setColor (255, 255, 255);
      gc.fillRect (0, 0, getWidth (), getHeight ());
      for (int i = 0, x = 0; i < widths.length; x += widths [i++] + 1)
      {
        // gc.setColor (Color.lightGray);
        ((PJAGraphicsExtension)gc).setColor (192, 192, 192);
        gc.fillOval (x, 0, widths [i], heights [i]);
        // gc.setColor (Color.darkGray);
        ((PJAGraphicsExtension)gc).setColor (64, 64, 64);
        gc.drawOval (x, 0, widths [i], heights [i]);
        // gc.setColor (Color.red);
        ((PJAGraphicsExtension)gc).setColor (255, 0, 0);
        gc.fillArc (x, 0, widths [i], heights [i], startAngles [i], angles [i]);
        // gc.setColor (Color.blue);
        ((PJAGraphicsExtension)gc).setColor (0, 0, 255);
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
  private static class PJAGraphicsPolygonDrawer extends GraphicsPolygonDrawer
  {
    // Paint rewriten to avoid use of Color, Font and FontMetrics classes
    public void paint (Graphics gc)
    {
      // gc.setColor (Color.white);
      ((PJAGraphicsExtension)gc).setColor (255, 255, 255);
      gc.fillRect (0, 0, getWidth (), getHeight ());

      // gc.setColor (Color.yellow.darker ());
      ((PJAGraphicsExtension)gc).setColor (178, 178, 0);
      gc.fillRoundRect (2, 2, 95, 95, 25, 18);
      // gc.setColor (Color.darkGray);
      ((PJAGraphicsExtension)gc).setColor (64, 64, 64);
      gc.drawRoundRect (2, 2, 95, 95, 25, 18);
      for (int angle = 0; angle < 360; angle += 15)
      {
        // gc.setColor (new Color (angle * 255 / 360, angle * 255 / 360, angle * 255 / 360));
        ((PJAGraphicsExtension)gc).setColor (angle * 255 / 360, angle * 255 / 360, angle * 255 / 360);
        Graphics gc2 = gc.create ();
        gc2.translate ((int)(angle / 25 * Math.cos (angle * Math.PI / 180.)), (int)(angle / 25 * Math.sin (angle * Math.PI / 180.)));
        gc2.drawLine (50, 50, (int)(50 + 35 * Math.cos (angle * Math.PI / 180.)), (int)(50 + 35 * Math.sin (angle * Math.PI / 180.)));
        gc2.dispose ();
      }

      // gc.setColor (Color.yellow.darker ());
      ((PJAGraphicsExtension)gc).setColor (178, 178, 0);
      gc.fill3DRect (102, 2, 294, 96, false);
      gc.draw3DRect (101, 1, 295, 97, true);

      Polygon polygons [] = {new Polygon (new int [] {107, 150, 200, 160, 107}, new int [] {5, 5, 25, 49, 49}, 5),
                             new Polygon (new int [] {202, 225, 260, 300, 300, 260, 225, 202}, new int [] {5, 49, 7, 49, 5, 49, 4, 49}, 8),
                             new Polygon (new int [] {302, 330, 302, 350, 390, 390, 360, 302}, new int [] {6, 25, 47, 32, 47, 6, 17, 6}, 8)};
      for (int i = 0; i < polygons.length; i++)
      {
        // gc.setColor (Color.pink);
        ((PJAGraphicsExtension)gc).setColor (255, 175, 175);
        gc.fillPolygon (polygons [i]);

        Graphics gc2 = gc.create ();
        gc2.translate (0, 45);
        // gc2.setColor (Color.orange);
        ((PJAGraphicsExtension)gc2).setColor (255, 200, 0);
        gc2.fillPolygon (polygons [i]);
        // gc2.setColor (Color.darkGray);
        ((PJAGraphicsExtension)gc2).setColor (64, 64, 64);
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
  private static class PJAGraphicsTextDrawer extends GraphicsTextDrawer
  {
    // Paint rewriten to avoid use of Color, Font and FontMetrics classes
    public void paint (Graphics gc)
    {
      // gc.setColor (Color.white);
      ((PJAGraphicsExtension)gc).setColor (255, 255, 255);
      gc.fillRect (0, 0, getWidth (), getHeight ());

      // String defaultFontName = gc.getFont ().getName ();
      String defaultFontName = ((PJAGraphicsExtension)gc).getFontName ();
      // Font.PLAIN, Font.ITALIC and Font.BOLD can be used because they are constant
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

        // gc.setFont (new Font ("", fontStyle [i], fontSize [i]));
        // Using "" as font name set it to default font
        ((PJAGraphicsExtension)gc).setFont ("", fontStyle [i], fontSize [i]);

        // Draw first rectangles around with FontMetrics
        // FontMetrics metrics = gc.getFontMetrics ();
        // int    textWidth = metrics.stringWidth (text);
        // gc.setColor (Color.cyan.darker ());
        // gc.drawRect (5, y, textWidth, metrics.getHeight ());
        // gc.drawRect (5, y + metrics.getLeading (), textWidth, metrics.getAscent ());
        int    textWidth = ((PJAGraphicsExtension)gc).getStringWidth (text);
        ((PJAGraphicsExtension)gc).setColor (0, 178, 178);
        int fontHeight = ((PJAGraphicsExtension)gc).getFontAscent () + ((PJAGraphicsExtension)gc).getFontDescent () + ((PJAGraphicsExtension)gc).getFontLeading ();
        gc.drawRect (5, y, textWidth, fontHeight);
        gc.drawRect (5, y + ((PJAGraphicsExtension)gc).getFontLeading (), textWidth, ((PJAGraphicsExtension)gc).getFontAscent ());

        // Draw text in rectangles
        // gc.setColor (Color.black);
        ((PJAGraphicsExtension)gc).setColor (0, 0, 0);
        // gc.drawString (text, 5, y + metrics.getAscent () + metrics.getLeading ());
        gc.drawString (text, 5, y + ((PJAGraphicsExtension)gc).getFontAscent () + ((PJAGraphicsExtension)gc).getFontLeading ());

        // y += metrics.getHeight () + 2;
        y += fontHeight + 2;
      }
    }

    public String toString ()
    {
      return "TextDrawer";
    }
  }

  // Image examples can't work : they need java.awt.image.ColorModel class !
  // class GraphicsImageDrawer implements GraphicsDrawer ...
  // v1.2 : GIF images can be drawn : they use now com.eteks.awt.image.GIFDecoder
  //        which produces an image with default RGB color model if ColorModel class 
  //        can't be loaded. 
  //        To get a GIF image use PJAGraphicsManager.getDefaultGraphicsManager ().getImage ("xxxx.gif");
}

