package com.xdlogic.jphotoedit;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager.*;
import javax.swing.border.BevelBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.plaf.metal.*;

import javax.imageio.*;

import java.io.IOException;
import java.io.File;

import java.util.*;

import java.net.URL;
import java.util.Vector;

import java.beans.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// class for Input Output Image
class IOImage{
    public  BufferedImage bii, bio;
    public BufferedImage getBii(){ return bii; };
    public BufferedImage getBio(){ return bio; };
    private  String ext;
    public File iofile;

    public BufferedImage createImage(BufferedImage iimage, int width, int height, int type){
        bii = iimage;
        bio = bii;
        bio = bii;
        return bio;
    }

    public BufferedImage readImage(String sfile){
        bii = null;
        try{
            iofile = new File(sfile);
            bii = ImageIO.read(iofile);
            bio = ImageIO.read(iofile);
            MediaTracker tracker = new MediaTracker( new Component() {});
            tracker.addImage(bii, 0);
            tracker.addImage(bio, 1);
            tracker.waitForID(0);
            tracker.waitForID(1);
        }catch (InterruptedException e){

        }catch (IOException e){

        }
        return bii;
    }

    public boolean writeImage(String sfile, BufferedImage wii){
        try{
            String sofile = sfile.substring(0, sfile.indexOf("."));
            ext = sfile.substring(sfile.indexOf(".")+1, sfile.length());
            File ofile = new File (sofile + "_modified." + ext);
            ImageIO.write(wii, ext, ofile);
        } catch (IOException e){
            System.err.println(e);
            return false;
        }

        return true;
    }

    public String getExt(){return ext;}
}
//class for Operation to be performed on an Image
class OpImage extends Canvas{
    private BufferedImage bii;

    public OpImage(BufferedImage img){
        bii = img;
    }

    public BufferedImage quantiseImage(int numBits, BufferedImage qii, BufferedImage qio) {
        int n = 8 - numBits;
        float scale = 255.0f / (255 >> n);
        byte[] tableData = new byte[256];
        for (int i = 0; i < 256; ++i)
            tableData[i] = (byte) Math.round(scale*(i >> n));
        LookupOp lookup = new LookupOp(new ByteLookupTable(0, tableData), null);

        BufferedImage result = lookup.filter(qii, qio);
        return result;
    }

    public static BufferedImage scaleImage(BufferedImage bimg, int m)
    {
        int w, h;

        w = m * bimg.getWidth();
        h = m *  bimg.getHeight();
        BufferedImage scaledImage = new BufferedImage(w, h, bimg.getType());

        int q11, q21, q12, q22;
        int x, y, x1, y1, x2, y2;
        for(y = 0; y < h; y++){
            for(x = 0; x < w; x++){
                if(x>0){
                    x1 = x-1;
                }else
                    x1 = 0;
                if(y>0){
                    y1 = y-1;
                }else{
                    y1 = 0;
                }
                if(x < (w-1))
                    x2 = x+1;
                else
                    x2 = w-1;
                if (y < (h-1)){
                    y2 = y+1;
                }else{
                    y2 = h-1;
                }
                Color pixel1, pixel2, pixel3, pixel4;
                pixel1 = new Color(bimg.getRGB(x1/m,y1/m));

                int R1 = (x2-x)*(y2-y);
                pixel2 = new Color(bimg.getRGB(x2/m,y1/m));
                int R2 = (x-x1)*(y2-y);
                pixel3 = new Color(bimg.getRGB(x1/m,y2/m));
                int R3 = (x2-x)*(y-y1);
                pixel4 = new Color(bimg.getRGB(x2/m,y2/m));
                int R4 = (x-x1)*(y-y1);

                int[] ARGB1 = ARGB(pixel1);
                int[] ARGB2 = ARGB(pixel2);
                int[] ARGB3 = ARGB(pixel3);
                int[] ARGB4 = ARGB(pixel4);

                int ALPHA = (ARGB1[0]*R1 + ARGB2[0]*R2 + ARGB3[0]*R3 + ARGB4[0]*R4)/((x2-x1)*(y2-y1));
                int RED = (ARGB1[1]*R1 + ARGB2[1]*R2 + ARGB3[1]*R3 + ARGB4[1]*R4)/((x2-x1)*(y2-y1));
                int GREEN = (ARGB1[2]*R1 + ARGB2[2]*R2 + ARGB3[2]*R3 + ARGB4[2]*R4)/((x2-x1)*(y2-y1));
                int BLUE = (ARGB1[3]*R1 + ARGB2[3]*R2 + ARGB3[3]*R3 + ARGB4[3]*R4)/((x2-x1)*(y2-y1));
                Color clr = new Color(RED,GREEN,BLUE,ALPHA);
                scaledImage.setRGB(x,y, clr.getRGB());
            }
        }
        return scaledImage;
    }

    public static BufferedImage shrinkImage(BufferedImage bimg, int m)
    {
        int w, h;

        w = bimg.getWidth()/m;
        h = bimg.getHeight()/m;
        BufferedImage scaledImage = new BufferedImage(w, h, bimg.getType());

        int q11, q21, q12, q22;
        int x, y, x1, y1, x2, y2;
        for(y = 0; y < h; y++){
            for(x = 0; x < w; x++){
                if(x>0){
                    x1 = x-1;
                }else
                    x1 = 0;
                if(y>0){
                    y1 = y-1;
                }else{
                    y1 = 0;
                }
                if(x < (w-1))
                    x2 = x+1;
                else
                    x2 = w-1;
                if (y < (h-1)){
                    y2 = y+1;
                }else{
                    y2 = h-1;
                }
                Color pixel1, pixel2, pixel3, pixel4;
                pixel1 = new Color(bimg.getRGB(x1*m,y1*m));

                int R1 = (x2-x)*(y2-y);
                pixel2 = new Color(bimg.getRGB(x2*m,y1*m));
                int R2 = (x-x1)*(y2-y);
                pixel3 = new Color(bimg.getRGB(x1*m,y2*m));
                int R3 = (x2-x)*(y-y1);
                pixel4 = new Color(bimg.getRGB(x2*m,y2*m));
                int R4 = (x-x1)*(y-y1);

                int[] ARGB1 = ARGB(pixel1);
                int[] ARGB2 = ARGB(pixel2);
                int[] ARGB3 = ARGB(pixel3);
                int[] ARGB4 = ARGB(pixel4);

                int ALPHA = (ARGB1[0]*R1 + ARGB2[0]*R2 + ARGB3[0]*R3 + ARGB4[0]*R4)/((x2-x1)*(y2-y1));
                int RED = (ARGB1[1]*R1 + ARGB2[1]*R2 + ARGB3[1]*R3 + ARGB4[1]*R4)/((x2-x1)*(y2-y1));
                int GREEN = (ARGB1[2]*R1 + ARGB2[2]*R2 + ARGB3[2]*R3 + ARGB4[2]*R4)/((x2-x1)*(y2-y1));
                int BLUE = (ARGB1[3]*R1 + ARGB2[3]*R2 + ARGB3[3]*R3 + ARGB4[3]*R4)/((x2-x1)*(y2-y1));
                Color clr = new Color(RED,GREEN,BLUE,ALPHA);
                scaledImage.setRGB(x,y, clr.getRGB());
            }
        }
        return scaledImage;
    }


    public static int[] ARGB(Color p){
        int a,  r,  g,  b;
        //get alpha
        a = p.getAlpha();

        //get red
        r = p.getRed();

        //get green
        g = p.getGreen();

        //get blue
        b = p.getBlue();

        int[] RGBA = new int[4];
        RGBA[0] = a;
        RGBA[1] = r;
        RGBA[2] = g;
        RGBA[3] = b;
        return RGBA;
    }

    public static BufferedImage getGrayLevelImg(BufferedImage imgin){
        BufferedImage img = imgin;
        int[] graylevel = new int[257];
        boolean hasAlpha = false;
        if(imgin.getAlphaRaster() != null){
            hasAlpha = true;
        }
        for(int i = 0; i < 257; i++){
            graylevel[i] = 0;
        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        //convert to grayscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);
                int a = 0;
                if(hasAlpha)
                a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                //calculate average
                int avg = (r+g+b)/3;
                Color clr;
                if(hasAlpha)
                clr = new Color(avg, avg, avg, a);
                else
                clr = new Color(avg, avg, avg);
                int n = graylevel[avg];
                img.setRGB(x, y, clr.getRGB());
                graylevel[avg] = n+1;
                //System.out.println(x + " " + y + " " + avg);
            }
        }
        return img;
    }

    public static int[] getGrayLevel(BufferedImage imgin){
        BufferedImage img = imgin;
        int[] graylevel = new int[257];

        for(int i = 0; i < 257; i++){
            graylevel[i] = 0;
        }
        boolean hasAlpha = false;
        if(imgin.getAlphaRaster() != null)
            hasAlpha = true;
        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        //convert to grayscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);
                int a = 0;
                if(hasAlpha)
                a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                //calculate average
                int avg = (r+g+b)/3;

                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
                int n = graylevel[avg];
                graylevel[avg] = n+1;
                //System.out.println(x + " " + y + " " + avg);
            }
        }
        return graylevel;

    }

    public static BufferedImage histogram(int[] graylevel){
        //width and hieght are bounds for the coordinates
        //histogram is the value.
        int width = 256*2;
        int maxlevel=0;
        for(int i=0; i < graylevel.length; i++){
            int temp = graylevel[i];
            if(maxlevel > temp){
                continue;
            }else
                maxlevel = temp;
        }
        int height = maxlevel/16;
        System.out.println(maxlevel);
        BufferedImage histo = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        int rgb=new Color(255,255,255,255).getRGB();
        int black = new Color(0,0,0,0).getRGB();
        int i=0;
        for(int x = 0; x < histo.getWidth()-1; x++){



            int ibar = (maxlevel/8 - graylevel[i]/8)/2;
            int yy=0;
            for(int y = 0; y < ibar-1; y++){
                //for(int y = height; y < ibar-1; y--){
                if(ibar <= maxlevel && x < histo.getWidth()){
                    histo.setRGB(x, y, rgb);
                    histo.setRGB(x+1, y, black);
                    yy = y;
                }
            }
            x++;
            if(i == 255)
                break;
            i++;

        }
        return histo;
    }

    public static BufferedImage getEqlBuffImg(BufferedImage img, int[] graylevel){
        BufferedImage bi = img;
        int[] h = graylevel;
        int mass = bi.getWidth() * bi.getHeight();
        int k = 0;
        long sum = 0;
        int pixel[];
        boolean hasAlpha = false;
        BufferedImage bio;

        if(img.getAlphaRaster()!=null){
            hasAlpha = true;
            bio = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        else
            bio = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        //calculate the scale factor
        float scale = (float) 255.0 / mass;
        //calculte cdf
        for (int x = 0; x < h.length; x++) {
            sum += h[x];
            int value = (int) (scale * sum);
            if (value > 255) {
                value = 255;
            }
            h[x] = value;
        }
        for (int ii = 0; ii < img.getWidth(); ii++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int a = 0, r=0,g=0,b=0;
                int p = bi.getRGB(ii,j);
                if(hasAlpha){
                    a = (p>>24)&0xff;
                    r = (p>>16)&0xff;
                    g = (p>>8)&0xff;
                    b = (p>>0)&0xff;
                    k = h[r] + h[g] +h[b];
                    k = k/3;
                }
                else{
                    pixel = bi.getRaster().getPixel(ii, j, new int[3]);
                //set the new value
                    k = h[pixel[0]];
                }
                Color color;
                if(hasAlpha){
                    color = new Color(k,k,k,a);
                }
                else
                    color = new Color(k, k, k);
                int rgba = color.getRGB();
                bio.setRGB(ii, j, rgba);
            }
        }
        return bio;
    }

    public static BufferedImage getSubtractedBuffImg(BufferedImage imgTarget, BufferedImage imgBackground, Vector<int[]> xycoordFG){

        int width = imgTarget.getWidth();
        int height = imgTarget.getHeight();
        boolean hasAlpha = false;
        BufferedImage tempImg;
        if (imgTarget.getAlphaRaster() != null){
            tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color p1 = new Color(imgTarget.getRGB(x,y));
                Color p2 = new Color(imgBackground.getRGB(x,y));

                int a1, r1, g1, b1;
                if(hasAlpha);
                a1 = Math.abs(p1.getAlpha() - p2.getAlpha());
                r1 = Math.abs(p1.getRed() - p2.getRed());
                g1 = Math.abs(p1.getGreen() - p2.getGreen());
                b1 = Math.abs(p1.getBlue() - p2.getBlue());

                if(r1 > 90 || g1 > 90 || b1 > 90){
                    //System.out.println(x + " " + y + " " + r1 + " " + g1 + " " + b1);
                    r1 = 255; b1 = 255; g1 = 255;
                    int[] arr = {x, y};
                    xycoordFG.add(arr);
                }

                if(x < imgTarget.getWidth()/8 &&  x > 0){
                    r1 = g1 = b1 = 0;
                }
                if( x > (imgTarget.getWidth()/8)*3 &&  x > imgTarget.getWidth()/2)
                    r1 = g1 = b1 = 0;
                Color clr = new Color(r1, g1, b1);

                tempImg.setRGB(x, y, clr.getRGB());
            }
        }
        return tempImg;
    }

    public static Vector<int[]> calcxcoord1(BufferedImage imgTarget, Vector<int[]> xycoordFG){

        int width = imgTarget.getWidth();
        int height = imgTarget.getHeight();
        boolean hasAlpha = false;
        if(imgTarget.getAlphaRaster() != null)
            hasAlpha = true;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color p1 = new Color(imgTarget.getRGB(x,y));

                int a1, r1, g1, b1;
                if(hasAlpha)
                a1 = Math.abs(p1.getAlpha());
                r1 = Math.abs(p1.getRed());
                g1 = Math.abs(p1.getGreen());
                b1 = Math.abs(p1.getBlue());

                if(r1 > 90 || g1 > 90 || b1 > 90){
                    //System.out.println(x + " " + y + " " + r1 + " " + g1 + " " + b1);
                    r1 = 255; b1 = 255; g1 = 255;
                    int[] arr = {x, y};
                    xycoordFG.add(arr);
                }
            }
        }
        return xycoordFG;
    }

    public static BufferedImage getSubtractedBuffImg2(BufferedImage imgTarget, BufferedImage imgBackground, Vector<int[]> xycoordBG){

        int width = imgTarget.getWidth();
        int height = imgTarget.getHeight();
        boolean hasAlpha = false;
        BufferedImage tempImg;

        if (imgTarget.getAlphaRaster() != null){
            tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color p1 = new Color(imgTarget.getRGB(x,y));
                Color p2 = new Color(imgBackground.getRGB(x,y));

                int a1, r1, g1, b1;
                if(hasAlpha)
                a1 = Math.abs(p1.getAlpha() - p2.getAlpha());
                r1 = Math.abs(p1.getRed() - p2.getRed());
                g1 = Math.abs(p1.getGreen() - p2.getGreen());
                b1 = Math.abs(p1.getBlue() - p2.getBlue());

                if(r1 > 90 || g1 > 90 || b1 > 90){
                    //System.out.println(x + " " + y + " " + r1 + " " + g1 + " " + b1);
                    r1 = 255; b1 = 255; g1 = 255;
                    int[] arr = {x, y};
                    xycoordBG.add(arr);
                }

                if(x < (imgTarget.getWidth()/8)*5 &&  x > 0){
                    r1 = g1 = b1 = 0;
                }
                if( x > (imgTarget.getWidth()/8)*7 &&  x < imgTarget.getWidth())
                    r1 = g1 = b1 = 0;
                Color clr = new Color(r1, g1, b1);

                tempImg.setRGB(x, y, clr.getRGB());
            }
        }
        return tempImg;
    }

    public static Vector<int[]> calcxcoord2(BufferedImage imgTarget, Vector<int[]> xycoordBG){

        int width = imgTarget.getWidth();
        int height = imgTarget.getHeight();
        boolean hasAlpha = false;
        if(imgTarget.getAlphaRaster() != null)
            hasAlpha = true;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color p1 = new Color(imgTarget.getRGB(x,y));

                int a1, r1, g1, b1;
                if(hasAlpha)
                a1 = Math.abs(p1.getAlpha());
                r1 = Math.abs(p1.getRed());
                g1 = Math.abs(p1.getGreen());
                b1 = Math.abs(p1.getBlue());

                if(r1 > 90 || g1 > 90 || b1 > 90){
                    //System.out.println(x + " " + y + " " + r1 + " " + g1 + " " + b1);
                    r1 = 255; b1 = 255; g1 = 255;
                    int[] arr = {x, y};
                    xycoordBG.add(arr);
                }
            }
        }
        return xycoordBG;
    }

    public static BufferedImage combineImage(BufferedImage imgTarget, BufferedImage imgBackground, int[] arr1, int[] arr2){

        int width = imgTarget.getWidth();
        int height = imgTarget.getHeight();

        int width2 = imgBackground.getWidth();
        int height2 = imgBackground.getHeight();

        if(width != width2 || height != height2){
            System.out.println("Out of bounds detected");
            if(width > width2)
                width = width2;
            if(height > height2)
                height = height2;
        }
        boolean hasAlpha = false;
        BufferedImage tempImg;

        if (imgTarget.getAlphaRaster() != null){
            tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color p1 = new Color(imgTarget.getRGB(x,y));
                Color p2 = new Color(imgBackground.getRGB(x,y));

                int a1, r1, g1, b1;
                if(hasAlpha)
                a1 = Math.abs(p1.getAlpha() + p2.getAlpha());
                r1 = Math.abs(p1.getRed() + p2.getRed());
                g1 = Math.abs(p1.getGreen() + p2.getGreen());
                b1 = Math.abs(p1.getBlue() + p2.getBlue());

                if(r1 >= 254 || g1 >= 254 || b1 >= 254){
                    r1 = g1 = b1 = 254;
                }

                if(x >= arr1[4] && y == arr1[5] ) {
                    //System.out.println(x + " " + y + " " + arr1[4] + " " + arr1[5] + " " + arr2[3] + " " + arr2[5]);
                    r1 = 250;
                    g1 = 0;
                    b1 = 0;
                }

                Color clr = new Color(r1, g1, b1);
                tempImg.setRGB(x, y, clr.getRGB());
            }
        }
        System.out.println("Xfmid : " + arr2[4] + " Xsmid : " + arr1[4] + " pixels travelled : " + (arr2[4] - arr1[4]));
        System.out.println("conversion to realworld units 1meters/1.2pixels");
        double var1 =  (arr2[4] - arr1[4]) / 1.2;
        System.out.println(var1 + " meters traveled.");
        System.out.println("time elapsed: " + 2.5 + "s");
        double var2 = ((arr2[4] - arr1[4]))/(1.2 * 2.5);
        System.out.println( var2 + " m/s");
        return tempImg;
    }

    public BufferedImage testWritableRaster(BufferedImage ibi){
        WritableRaster ras = (WritableRaster) ibi.getData();
        int pixel;
        boolean hasAlpha = false;
        if(ibi.getAlphaRaster() != null)
            hasAlpha = true;
        for(int ii=0; ii < ibi.getHeight(); ii++){
            for(int i=0; i < ibi.getWidth(); i++){
                pixel = ibi.getRGB(i,ii);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel >> 0) & 0xff;

                System.out.println(red + " " + green + " " + blue + " " + alpha);
            }

        }
        if(hasAlpha)
        System.out.println(ibi.getAlphaRaster());
        else
        System.out.println("no alpha");
        return ibi;
    }

    public BufferedImage linearMapping(BufferedImage bimg, float slope, float bias){
        int w, h;
        w = bimg.getWidth();
        h = bimg.getHeight();

        boolean hasAlpha = false;
        BufferedImage mappedImage;

        if(bimg.getAlphaRaster() != null){
            mappedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            mappedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        WritableRaster input = bimg.getRaster();
        WritableRaster output = mappedImage.getRaster();

        for(int y=0; y<h; y++){
            for(int x=0; x<w; x++){
                int alpha = 0;
                int pixel = bimg.getRGB(x,y);
                int red=0;
                int green=0;
                int blue=0;
                Color oclr;
                if(hasAlpha){
                    alpha = (pixel >> 24) & 0xff;
                    red = (pixel >> 16) & 0xff;
                    green = (pixel >> 8) & 0xff;
                    blue = (pixel >> 0) & 0xff;
                    red = Clamp(slope*red + bias);
                    green = Clamp(slope*green + bias);
                    blue = Clamp(slope*blue + bias);
                    oclr = new Color (red, green, blue, alpha);
                    if(alpha <10){
                        mappedImage.setRGB(x, y, pixel);
                    }else
                        mappedImage.setRGB(x, y, oclr.getRGB());
                }
                else{
                    red = (pixel >> 16) & 0xff;
                    green = (pixel >> 8) & 0xff;
                    blue = (pixel >> 0) & 0xff;
                    //if(hasAlpha)
                    //alpha = Clamp(slope*alpha + bias);
                    red = Clamp(slope*red + bias);
                    green = Clamp(slope*green + bias);
                    blue = Clamp(slope*blue + bias);
                    oclr = new Color (red, green, blue);
                    mappedImage.setRGB(x, y, oclr.getRGB());
                }

            }
        }
        return mappedImage;
    }

    public BufferedImage nonLinearMapping(BufferedImage bimg, float gain){
        int w, h;
        w = bimg.getWidth();
        h = bimg.getHeight();

        boolean hasAlpha = false;
        BufferedImage mappedImage;

        if(bimg.getAlphaRaster() != null){
            mappedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            mappedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        WritableRaster input = bimg.getRaster();
        WritableRaster output = mappedImage.getRaster();
        double f255 = 255.0f;
        gain =(float) Math.sqrt(f255);
        for(int y=0; y<h; y++){
            for(int x=0; x<w; x++){
                int alpha = 0;
                int pixel = bimg.getRGB(x,y);

                if(hasAlpha)
                alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel >> 0) & 0xff;
                //if(hasAlpha)
                //alpha = Clamp(gain*alpha);
                red = Clamp(gain*red);
                green = Clamp(gain*green);
                blue = Clamp(gain*blue);
                Color oclr;
                if(hasAlpha)
                oclr = new Color (red, green, blue, alpha);
                else
                oclr = new Color (red, green, blue);
                mappedImage.setRGB(x, y, oclr.getRGB());
            }
        }
        return mappedImage;
    }

    public int Clamp(float value){
        return Math.min(Math.max(Math.round(value),0), 255);
    }

    public BufferedImage invertPixels(BufferedImage bimg){
        int w, h;
        w = bimg.getWidth();
        h = bimg.getHeight();

        byte[] table = new byte[256];
        for(int i = 0; i < 256; i++){
            table[i] = (byte)(255 - i);
        }
        ByteLookupTable invertTable = new ByteLookupTable(0, table);
        LookupOp invertOp = new LookupOp(invertTable, null);
        BufferedImage invertedImage = invertOp.filter(bimg, null);

        return invertedImage;
    }

    public BufferedImage invertPixelsNQ(BufferedImage bimg){
        for(int y = 0; y < bimg.getHeight(); y++){
            for(int x = 0; x < bimg.getWidth(); x++){
                bimg.setRGB(x,y, 1 - bimg.getRGB(x,y));
            }
        }
        return bimg;
    }
    public BufferedImage paddImage(BufferedImage bimg){

        if(bimg.getWidth() == bimg.getHeight())
            return bimg;

        int paddedw = bimg.getWidth() + 2;
        int paddedh = bimg.getHeight() + 2;
        boolean hasAlpha = false;
        BufferedImage paddedImage;
        if(bimg.getAlphaRaster() != null)
            paddedImage = new BufferedImage(paddedw, paddedh, BufferedImage.TYPE_INT_ARGB);
        else
            paddedImage = new BufferedImage(paddedw, paddedh, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < paddedh; y++){
            for(int x = 0; x < paddedw; x++){
                if( ( (x>1) && (y>1) ) &&
                   ( (x<(paddedw-1)) && (y>1) ) &&
                   ( (x>1) && (y<=(paddedh-1) ) ) &&
                   ( (x<(paddedw-1)) && (y<(paddedh-1)))){

                    paddedImage.setRGB(x,y, bimg.getRGB(x-1,y-1));
                    continue;
                }else if((x==0||y==0)&&(x<paddedw-2)&&(y<(paddedh-2))){
                    paddedImage.setRGB(x, y, bimg.getRGB(x,y));
                    continue;
                }else if((x==(paddedw-2)||y==(paddedh-2))&&(x<paddedw-2)&&(y<(paddedh-2))){
                    paddedImage.setRGB(x+1,y+1, bimg.getRGB(x,y));
                    continue;
                }
            }
        }

        return paddedImage;
    }

    public BufferedImage edgeDetection(BufferedImage bimg, int msize, int padd, int idx, int[][] Mx, int[][] My, int op){
        int MSIZE = msize;
        int halfm = (int)Math.floor(MSIZE/2);
        boolean hasAlpha = false;
        BufferedImage oimg;
        if(bimg.getAlphaRaster() != null){
            oimg = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_ARGB);

            hasAlpha = true;
        }else{
            oimg = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_RGB);

        }

        for(int y = halfm; y < oimg.getHeight() - halfm; y++){
            for(int x = halfm; x < oimg.getWidth() - halfm; x++){

                int p=0, p1=0, psum = 0;

                for(int k = halfm - padd; k < halfm+padd; k++){
                    for(int j = halfm - padd; j < halfm+padd; j++){
                        //Prewitt Sobel
                        if(op == 0){
                            p = p + ((bimg.getRGB(x+1, y) - bimg.getRGB(x-1,y)))*Mx[j+halfm][k+halfm];
                            p1 = p1 + ((bimg.getRGB(x, y+1) - bimg.getRGB(x, y+1)))*My[j+halfm][k+halfm];
                            if(p < 0) p = -p;
                            if(p1 < 0) p1 = -p1;
                            psum += p + p1;
                        }
                        //Grayscal + gradient combination
                        if(op == 1){
                            p = p + (bimg.getRGB(x+j,y) - bimg.getRGB(x,y))*Mx[j+halfm][k+halfm];
                            p1 = p1 + (bimg.getRGB(x,y+k) - bimg.getRGB(x, y))*My[j+halfm][k+halfm];
                            if(p < 0) p = -p;
                            if(p1 < 0) p1 = -p1;
                            psum += p + p1;
                        }
                        //iii
                        if(op == 2){
                            p = p + ((bimg.getRGB(x+1,y) - bimg.getRGB(x-1,y)))*Mx[j+halfm][k+halfm];
                            p1 = p1 + ((bimg.getRGB(x,y+1) - bimg.getRGB(x,y-1)))*My[j+halfm][k+halfm];
                            if(p < 0) p = -p;
                            if(p1 < 0) p1 = -p1;
                            psum += p + p1;
                        }
                        //laplacian
                        if(op == 3){
                            p = p + ((bimg.getRGB(x+1/2, y) - bimg.getRGB(x-1/2,y)))*Mx[j+halfm][k+halfm];
                            p1 = p1 + ((bimg.getRGB(x, y+1/2) - bimg.getRGB(x, y+1/2)))*My[j+halfm][k+halfm];
                            if(p < 0) p = -p;
                            if(p1 < 0) p1 = -p1;
                            psum += p + p1;
                        }

                    }
                }
                oimg.setRGB(x,y, psum);
            }
        }
        return oimg;
    }

    public BufferedImage sinusoidalNoise(BufferedImage bimg, int amp, int u, int v){
        int w, h;
        w = bimg.getWidth();
        h = bimg.getHeight();
        int halfm = 3/2;
        int padd = 1;
        boolean hasAlpha = false;
        BufferedImage mappedImage;

        if(bimg.getAlphaRaster() != null){
            mappedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            mappedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        WritableRaster input = bimg.getRaster();
        WritableRaster output = mappedImage.getRaster();

        for(int y = halfm; y < h - halfm; y++){
            for(int x = halfm; x < w - halfm; x++){
                int p = 0;
                int p1 = 0;
                for(int k = halfm - padd; k < halfm+padd; k++){
                    for(int j = halfm - padd; j < halfm+padd; j++){
                        p1 += bimg.getRGB(x,y) + bimg.getRGB(x, y);
                        p += (int)(Math.sin(bimg.getRGB(x+j,y)) + Math.sin(bimg.getRGB(x,y+k)));

                    }
                }
                mappedImage.setRGB(x,y, p - p1);
            }
        }
        return mappedImage;
    }

    public BufferedImage gaussianNoise(BufferedImage image, BufferedImage output) {
        Raster source = image.getRaster();
        WritableRaster out = output.getRaster();
        double stdDev = 10;
        int currVal;                    // the current value
        double newVal;                  // the new "noisy" value
        double gaussian;                // gaussian number
        int bands  = out.getNumBands(); // number of bands
        int width  = image.getWidth();  // width of the image
        int height = image.getHeight(); // height of the image
        java.util.Random randGen = new java.util.Random();

        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                gaussian = randGen.nextGaussian();

                for (int b=0; b<bands; b++) {
                    newVal = stdDev * gaussian;
                    currVal = source.getSample(i, j, b);
                    newVal = newVal + currVal;
                    if (newVal < 0)   newVal = 0.0;
                    if (newVal > 255) newVal = 255.0;

                    out.setSample(i, j, b, (int)(newVal));
                }
            }
        }

        return output;
    }

    public BufferedImage saltandpepperNoise(BufferedImage bimg) {

        double amount = 20;

        int w, h;
        w = bimg.getWidth();
        h = bimg.getHeight();

        boolean hasAlpha = false;
        BufferedImage outputImage;

        if(bimg.getAlphaRaster() != null){
            outputImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            hasAlpha = true;
        }
        else
            outputImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        int ii = 0;

        outputImage = bimg;
        int white = 255;
        int black = 0;
        if(hasAlpha){
            white =(white<<24) | (white<<16) | (white<<8) | white;
            black = (black<<24) | (black<<16) | (black<<8) | black;
        }else{
            white = (white<<16) | (white<<8) | white;
            black = (black<<16) | (black<<8) | black;
        }

        java.util.Random rand = new java.util.Random();

        while(ii < (int)(w*h*0.20)){



            int x = rand.nextInt((int)(w-ii) + ii);
            //rand.setSeed(System.currentTimeMillis());
            int y = rand.nextInt((int)(h-ii) + ii);
            //rand.setSeed(System.currentTimeMillis());
            int ran;
            ran = rand.nextInt(3);
            if(ran == 2){
                outputImage.setRGB(x,y, white);
            }else if(ran == 1 )
                outputImage.setRGB(x,y, black);
            else
                outputImage.setRGB(x,y, bimg.getRGB(x,y));
            //System.out.println(x + " " + y + " " + ran );

            ii++;
        }
        return outputImage;
    }

    public BufferedImage meanFilter(BufferedImage bimg) {

        Color[] pixel=new Color[9];
        int[] R=new int[9];
        int[] B=new int[9];
        int[] G=new int[9];

        BufferedImage img=bimg;
        for(int i=1;i<img.getWidth()-1;i++)
            for(int j=1;j<img.getHeight()-1;j++)
            {
               pixel[0]=new Color(img.getRGB(i-1,j-1));
               pixel[1]=new Color(img.getRGB(i-1,j));
               pixel[2]=new Color(img.getRGB(i-1,j+1));
               pixel[3]=new Color(img.getRGB(i,j+1));
               pixel[4]=new Color(img.getRGB(i+1,j+1));
               pixel[5]=new Color(img.getRGB(i+1,j));
               pixel[6]=new Color(img.getRGB(i+1,j-1));
               pixel[7]=new Color(img.getRGB(i,j-1));
               pixel[8]=new Color(img.getRGB(i,j));
               for(int k=0;k<9;k++){
                   R[k]=pixel[k].getRed();
                   B[k]=pixel[k].getBlue();
                   G[k]=pixel[k].getGreen();
               }
               Arrays.sort(R);
               Arrays.sort(G);
               Arrays.sort(B);
               img.setRGB(i,j,new Color(R[4],B[4],G[4]).getRGB());
            }

            return img;
    }
}

//class for image view
class ImageView extends JLabel implements Scrollable{
    private BufferedImage image;
    private Dimension viewSize;

    public ImageView(BufferedImage img){
        image = img;
        int width = Math.min(256, image.getWidth(null));
        int height = Math.min(256, image.getHeight(null));
        viewSize = new Dimension(width, height);
        setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    }

    public void paintComponent(Graphics g){
        g.drawImage(image, 0, 0, this);
    }

    public void setViewSize(Dimension newSize){
        viewSize.setSize(newSize);
    }
    @Override
    public Dimension getPreferredScrollableViewportSize(){
        return viewSize;
    }
    @Override
    public int getScrollableUnitIncrement(Rectangle rect, int orient, int dir) {
        return 1;
    }
    @Override
    public int getScrollableBlockIncrement(Rectangle rect, int orient, int dir) {
        if (orient == SwingConstants.HORIZONTAL)
            return image.getWidth()/10;
        else
            return image.getHeight()/10;
    }
    @Override
    public boolean getScrollableTracksViewportWidth(){
        return false;
    }
    @Override
    public boolean getScrollableTracksViewportHeight(){
        return false;
    }
}

/* ImageFilter.java is used by FileChooserDemo2.java. */
class ImageFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                extension.equals(Utils.tif) ||
                extension.equals(Utils.gif) ||
                extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg) ||
                extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
}

//Image preview
class ImagePreview extends JComponent
implements PropertyChangeListener {
    ImageIcon thumbnail = null;
    File file = null;

    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 50));
        addPropertyChangeListener(this);
    }

    public void loadImage() {
        if (file == null) {
            thumbnail = null;
            return;
        }

        //Don't use createImageIcon (which is a wrapper for getResource)
        //because the image we're trying to load is probably not one
        //of this program's own resources.
        ImageIcon tmpIcon = new ImageIcon(file.getPath());
        if (tmpIcon != null) {
            if (tmpIcon.getIconWidth() > 90) {
                thumbnail = new ImageIcon(tmpIcon.getImage().
                                          getScaledInstance(90, -1,
                                                            Image.SCALE_DEFAULT));
            } else { //no need to miniaturize
                thumbnail = tmpIcon;
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = true;

            //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            update = true;
        }

        //Update the preview accordingly.
        if (update) {
            thumbnail = null;
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        if (thumbnail == null) {
            loadImage();
        }
        if (thumbnail != null) {
            int x = getWidth()/2 - thumbnail.getIconWidth()/2;
            int y = getHeight()/2 - thumbnail.getIconHeight()/2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}

//class image file view
class ImageFileView extends FileView {
    ImageIcon jpgIcon = Utils.createImageIcon("images/jpgIcon.gif");
    ImageIcon gifIcon = Utils.createImageIcon("images/gifIcon.gif");
    ImageIcon tiffIcon = Utils.createImageIcon("images/tiffIcon.gif");
    ImageIcon pngIcon = Utils.createImageIcon("images/pngIcon.png");

    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(Utils.gif)){
                type = "GIF Image";
            } else if (extension.equals(Utils.tiff) ||
                       extension.equals(Utils.tif)) {
                type = "TIFF Image";
            } else if (extension.equals(Utils.png)){
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = Utils.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                icon = jpgIcon;
            } else if (extension.equals(Utils.gif)) {
                icon = gifIcon;
            } else if (extension.equals(Utils.tiff) ||
                       extension.equals(Utils.tif)) {
                icon = tiffIcon;
            } else if (extension.equals(Utils.png)) {
                icon = pngIcon;
            }
        }
        return icon;
    }
}

//class utils
class Utils {
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

//class spring utilities
class SpringUtilities {
    /**
     * A debugging utility that prints to stdout the component's
     * minimum, preferred, and maximum sizes.
     */
    public static void printSizes(Component c) {
        System.out.println("minimumSize = " + c.getMinimumSize());
        System.out.println("preferredSize = " + c.getPreferredSize());
        System.out.println("maximumSize = " + c.getMaximumSize());
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component is as big as the maximum
     * preferred width and height of the components.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeGrid(Container parent,
                                int rows, int cols,
                                int initialX, int initialY,
                                int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;

        //Calculate Springs that are the max of the width/height so that all
        //cells have the same size.
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
        getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
        getHeight();
        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                                  parent.getComponent(i));

            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }

        //Apply the new width/height Spring. This forces all the
        //components to have the same size.
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                                  parent.getComponent(i));

            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }

        //Then adjust the x/y constraints of all the cells so that they
        //are aligned in a grid.
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                                  parent.getComponent(i));
            if (i % cols == 0) { //start of new row
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { //x position depends on previous component
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
                                     xPadSpring));
            }

            if (i / cols == 0) { //first row
                cons.setY(initialYSpring);
            } else { //y position depends on previous row
                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
                                     yPadSpring));
            }
            lastCons = cons;
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH,
                            Spring.sum(
                                       Spring.constant(yPad),
                                       lastCons.getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST,
                            Spring.sum(
                                       Spring.constant(xPad),
                                       lastCons.getConstraint(SpringLayout.EAST)));
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                                  int row, int col,
                                                                  Container parent,
                                                                  int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                   getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                    getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
