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

class MainUI extends JFrame implements ActionListener{
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;
    JCheckBoxMenuItem cbMenuItem;
    JDesktopPane desktop;
    Vector<IOImage> vio = new Vector<IOImage>();
    Vector<JInternalFrame> vjif = new Vector<JInternalFrame>();
    Vector<JLabel> vjlbl = new Vector<JLabel>();
    Vector<JPanel> vpnl = new Vector<JPanel>();
    String filename, msg = "Montezuftware";
    File file;
    JInternalFrame frame;
    JLabel jLabel;
    JPanel jPanel;

    JInternalFrame lmJIF;
    JPanel lmPanel;
    JTextField textField1;
    int txtFint1;
    int lmhandler = -1;
    JTextField textField2;
    int txtFint2;
    String btnstr1;
    String btnstr2;
    JButton jbtn1;
    JButton jbtn2;
    JButton smsbtn;
    JTextField[] matTF;
    static int handler, count=0;
    static int imgtgtHandle = -2;
    static int imgbgHandle = -3;
    int MATSIZE=0;
    static Vector<int[]> xycoordFG = new Vector<int[]>();
    static Vector<int[]> xycoordBG = new Vector<int[]>();
    static int[] midpoint1;
    static int[] midpoint2;

    static int isApplied = -1;
    JLabel statusLabel = new JLabel(msg);

    int[][] Mx  = {
    {-1,0,1},
    {-2,0,2},
    {-1,0,1}
    };

    int[][] My = {
    {-1,-2,-1},
    {0,0,0},
    {1,2,1}
    };

    int[][] Mz = {
    {0,1,0},
    {1,-4,1},
    {0,1,0}
    };

    public static final String INTERNAL_FRAME_FOCUS_EVENT_PROPERTY = "selected";
    // Specify the look and feel to use by defining the LOOKANDFEEL constant
    // Valid values are: null (use the default), "Metal", "System", "Motif",
    // and "GTK"
    final static String LOOKANDFEEL = "Metal";

    // If you choose the Metal L&F, you can also choose a theme.
    // Specify the theme to use by defining the THEME constant
    // Valid values are: "DefaultMetal", "Ocean",  and "Test"
    final static String THEME = "Test";

    public MainUI(){

    }

    private static void initLookAndFeel() {
        String lookAndFeel = null;

        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("Metal")) {
                //lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                //  an alternative way to set the Metal L&F is to replace the
                // previous line with:
                lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
            }

            else if (LOOKANDFEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            }

            else if (LOOKANDFEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }

            else if (LOOKANDFEEL.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }

            else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                                   + LOOKANDFEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            try {
                UIManager.setLookAndFeel(lookAndFeel);

                // If L&F = "Metal", set the theme

                if (LOOKANDFEEL.equals("Metal")) {
                    if (THEME.equals("DefaultMetal"))
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    else if (THEME.equals("Ocean"))
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                    else
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());

                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                }
            }

            catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"
                                   + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            }

            catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                                   + lookAndFeel
                                   + ") on this platform.");
                System.err.println("Using the default look and feel.");
            }

            catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                                   + lookAndFeel
                                   + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }
    public void createAndShowGUI(){

        initLookAndFeel();
        JFrame framecasg;
        framecasg = new JFrame();
        framecasg.setLayout(new BorderLayout());
        framecasg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JFrame.setDefaultLookAndFeelDecorated(true);
        desktop = new JDesktopPane(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage bg = ImageIO.read(new File("images/background.jpg"));
                    g.setColor(Color.darkGray);
                    g.fillRect(0, 0, bg.getWidth()*10, bg.getHeight()*10);
                    //g.drawImage(bg, 100, 100, bg.getWidth(), bg.getHeight(), null);
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        getContentPane().add(desktop);

        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        getContentPane().add(statusLabel, java.awt.BorderLayout.SOUTH);

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                                                             "Open and save image files.");
        menuBar.add(menu);

        menuItem = new JMenuItem("Open Image");
        menuItem.setMnemonic(KeyEvent.VK_O);
        //menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                                       KeyEvent.VK_O, 2));

        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("New Image");
        menuItem.setMnemonic(KeyEvent.VK_N);
        //menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                                       KeyEvent.VK_N, 2));

        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save Image");
        menuItem.setMnemonic(KeyEvent.VK_S);
        //menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                                       KeyEvent.VK_S, 2));

        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Reset Workspace");
        menuItem.setMnemonic(KeyEvent.VK_R);
        //menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                                       KeyEvent.VK_R, 2));

        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Quit Application");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        //menuItem.addActionListener(this);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                                                       KeyEvent.VK_Q, 2));

        menuItem.addActionListener(this);
        menu.add(menuItem);

        menu = new JMenu("Selections");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                                                             "Image file editing menu");
        menuItem = new JMenuItem("Set selected source image");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Set selected background image");
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu("Scale Image");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                                                             "Image file editing menu");
        menuItem = new JMenuItem("Quantize Image");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Enlarge Image 2x");
        menuItem.setMnemonic(KeyEvent.VK_2);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Enlarge Image 3x");
        menuItem.setMnemonic(KeyEvent.VK_3);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Shrink Image 2x");
        menuItem.setMnemonic(KeyEvent.VK_8);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 2));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Shrink Image 3x");
        menuItem.setMnemonic(KeyEvent.VK_8);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 2));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu("Simple Edit Options");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                                                             "Image file editing menu");

        menuItem = new JMenuItem("Convert To Grayscale");
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Convert To Equalized Grayscale");
        menuItem.setMnemonic(KeyEvent.VK_U);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Get Histogram for this grayscale image");
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Subtract first source image");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Subtract second source image");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Combine source images");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu.addSeparator();
        menuItem = new JMenuItem("Computations");
        menu.add(menuItem);
        menuBar.add(menu);
        menu.addSeparator();

        menuItem = new JMenuItem("Calculate XY-Bounds of starting point image");
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);
        menuItem = new JMenuItem("Calculate XY-Bounds of ending point image");
        menuItem.setMnemonic(KeyEvent.VK_Y);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Compute starting midpoint");
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 2));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Compute ending midpoint");
        menuItem.setMnemonic(KeyEvent.VK_Y);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 2));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("test WritableRaster");
        menuItem.setMnemonic(KeyEvent.VK_Y);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 2));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu("L-NL Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                                                             "L-NL Edit");
        menuItem = new JMenuItem("Linear mapping");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Non-Linear mapping");
        menuItem.setMnemonic(KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Invert pixels");
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Non quantise invert pixels");
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu.addSeparator();
        menuItem = new JMenuItem("Convolution");
        menu.add(menuItem);
        menuBar.add(menu);
        menu.addSeparator();

        /*
        menuItem = new JMenuItem("Set matrix");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);
        */
        menuItem = new JMenuItem("Padd image");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Perform Sobel Prewitt edge detection");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Perform gradient edge detection");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Perform iii edge detection");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Perform Laplacian edge detection");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu.addSeparator();
        menuItem = new JMenuItem("Noise");
        menu.add(menuItem);
        menuBar.add(menu);
        menu.addSeparator();

        menuItem = new JMenuItem("Sinusoidal Noise");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Gaussian Noise");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menuItem = new JMenuItem("Salt and Pepper Noise");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu.addSeparator();
        menuItem = new JMenuItem("Filter");
        menu.add(menuItem);
        menuBar.add(menu);
        menu.addSeparator();

        menuItem = new JMenuItem("Mean Filter");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        setSize(1280, 800);
        setJMenuBar(menuBar);
        menuBar.setVisible(true);
        setTitle("CS559 Montezoftware : Edsel Malasig");
        setVisible(true);
        framecasg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {

        if("New Image".equals(e.getActionCommand())){

            IOImage mainio = new IOImage();

            mainio.createImage( mainio.getBio(), 1024, 1024, BufferedImage.TYPE_INT_ARGB);
            frame = new JInternalFrame("Image", true, true, true, true);
            jPanel = new JPanel();
            jLabel = new  JLabel(new ImageIcon(mainio.getBio()));
            jPanel.add(jLabel);
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());

            vjlbl.add(jLabel);
            frame.add(BorderLayout.CENTER, jPanel);
            frame.addInternalFrameListener(new MyInternalFrameListener());
            frame.setSize(mainio.getBio().getWidth(),mainio.getBio().getHeight());
            frame.pack();

            frame.setVisible(true);
            try{
                frame.setSelected(true);
            }catch(PropertyVetoException pve){
                System.err.println("Failed to select the opened frame.");
            }
            vio.add(mainio);
            vjif.add(frame);
            vpnl.add(jPanel);
            desktop.add(frame);
            desktop.repaint();
            statusLabel.setText("Image");
        }

        if("Open Image".equals(e.getActionCommand())){
            File dir = new File(".");
            JFileChooser chooser = new JFileChooser(dir);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                         "Image files", "JPG", "png", "jpg", "gif", "tiff");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showDialog(MainUI.this, "Open file");
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                filename = chooser.getSelectedFile().getAbsolutePath();
                file = chooser.getSelectedFile();
                //Set up the file chooser.
                //Add a custom file filter and disable the default
                //(Accept All) file filter.
                chooser.addChoosableFileFilter(new ImageFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                //Add custom icons for file types.
                chooser.setFileView(new ImageFileView());

                //Add the preview pane.
                chooser.setAccessory(new ImagePreview(chooser));


            }else {
                System.out.println("Open file operation cancelled by user.");
                return;
            }
            IOImage mainio = new IOImage();
            mainio.readImage(filename);
            System.out.println("Opening file: " + filename);

            frame = new JInternalFrame(file.getName(), true, true, true, true);
            jPanel = new JPanel();
            jLabel = new  JLabel(new ImageIcon(mainio.getBio()));
            jPanel.add(jLabel);
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            vjlbl.add(jLabel);
            frame.add(BorderLayout.CENTER, jPanel);
            frame.addInternalFrameListener(new MyInternalFrameListener());
            frame.setSize(mainio.getBio().getWidth(),mainio.getBio().getHeight());
            frame.pack();
            frame.setVisible(true);
            try{
                frame.setSelected(true);
            }catch(PropertyVetoException pve){
                System.err.println("Failed to select the opened frame.");
            }
            vpnl.add(jPanel);
            vio.add(mainio);
            vjif.add(frame);
            desktop.add(frame);
            desktop.repaint();
            statusLabel.setText(filename + " has been opened.");
            frame.toFront();
        }

        if("Save Image".equals(e.getActionCommand())){
            File dir = new File(".");
            JFileChooser chooser = new JFileChooser(dir);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                         "Image files", "JPG", "png", "jpg", "gif", "tiff");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                filename = chooser.getSelectedFile().getAbsolutePath();
                file = chooser.getSelectedFile();
                //Set up the file chooser.
                //Add a custom file filter and disable the default
                //(Accept All) file filter.
                chooser.addChoosableFileFilter(new ImageFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                //Add custom icons for file types.
                chooser.setFileView(new ImageFileView());

                //Add the preview pane.
                chooser.setAccessory(new ImagePreview(chooser));


            }else {
                System.out.println("Save file operation cancelled by user.");
                return;
            }
            IOImage tempioi = vio.elementAt(handler);
            System.out.println("Saving file: " + filename);
            tempioi.writeImage(filename, tempioi.getBio());
            statusLabel.setText(filename + " has been saved.");

        }

        if("Reset Workspace".equals(e.getActionCommand())){
            handler = -1;
            count = 0;
            vio.clear();
            for(int ii = vjif.size()-1; ii > -1; ii--){
                vjif.remove(ii).dispose();
            }
            vjif.clear();
            vjlbl.clear();
            vpnl.clear();
            statusLabel.setText("Workspace has been reset.");
            xycoordFG.clear();
            xycoordBG.clear();
        }

        if("Quit Application".equals(e.getActionCommand())){
            System.exit(1);
        }

        if("Quantize Image".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh image.");
                return;
            }
            */
            //tempjif = new JInternalFrame(tempioi.iofile.getName(), true, true, true, true);
            tempioi.bio = opi.quantiseImage(2, tempioi.getBio(), tempioi.getBii());

            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            //vio.insertElementAt(tempioi, handler);
            //vjif.insertElementAt(tempjif, handler);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " has been Quantized.");
        }

        if("Enlarge Image 2x".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */


            tempioi.bio = opi.scaleImage(tempioi.getBio(), 2);

            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            //vio.insertElementAt(tempioi, handler);
            //vjif.insertElementAt(tempjif, handler);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Enlarged 2x.");

        }

        if("Enlarge Image 3x".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.scaleImage(tempioi.getBio(), 3);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            //vio.insertElementAt(tempioi, handler);
            //vjif.insertElementAt(tempjif, handler);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Enlarged 3x.");

        }

        if("Shrink Image 2x".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.shrinkImage(tempioi.getBio(), 2);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            //vio.insertElementAt(tempioi, handler);
            //vjif.insertElementAt(tempjif, handler);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " shrinked 2x.");
        }

        if("Shrink Image 3x".equals(e.getActionCommand())){
            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */


            tempioi.bio = opi.shrinkImage(tempioi.getBio(), 3);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            //vio.insertElementAt(tempioi, handler);
            //vjif.insertElementAt(tempjif, handler);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " shrinked 3x.");

        }

        if("Convert To Grayscale".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.getGrayLevelImg(tempioi.getBio());
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " converted to grayscale.");
        }

        if("Convert To Equalized Grayscale".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.getEqlBuffImg(tempioi.getBio(),opi.getGrayLevel(tempioi.getBio()));
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Converted to equalized grayscale.");

        }

        if("Get Histogram for this grayscale image".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl;
            /*
             try{
             tempjif.setClosed(true);
             vjif.remove(tempjif);
             vio.remove(tempioi);
             vjlbl.remove(tmplbl);
             }catch (PropertyVetoException pve){
             System.err.println("Failed to refresh frame.");
             return;
             }
             */
            tempjif = new JInternalFrame(tempioi.iofile.getName(), true, true, true, true);
            tempioi.bio = opi.histogram(opi.getGrayLevel(tempioi.getBio()));
            jPanel = new JPanel();
            jLabel = new  JLabel(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.insertElementAt(jLabel,handler);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight()+60);
            tempjif.setVisible(true);
            vio.insertElementAt(tempioi, handler);
            vjif.insertElementAt(tempjif, handler);
            desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " histogram generated.");

        }

        if("Set selected source image".equals(e.getActionCommand())){
            if(!vio.isEmpty()){
                imgtgtHandle = handler;
                msg = vio.elementAt(imgtgtHandle).iofile.getName();
                statusLabel.setText(msg + " selected. Select background image.");
            }
        }

        if("Set selected background image".equals(e.getActionCommand())){
            if(!vio.isEmpty()){
                imgbgHandle = handler;
                msg = vio.elementAt(imgbgHandle).iofile.getName();
                statusLabel.setText(msg+ " selected." );
            }
        }

        if("Subtract first source image".equals(e.getActionCommand())){
            if((imgtgtHandle > -1) && (imgbgHandle > -1)){
                IOImage tempioi1 = vio.elementAt(imgtgtHandle);
                IOImage tempioi2 = vio.elementAt(imgbgHandle);
                OpImage opi = new OpImage(tempioi1.getBio());
                tempioi1.bio = opi.getSubtractedBuffImg(tempioi1.getBio(), tempioi2.getBio(), xycoordFG);
                JInternalFrame tempjif = new JInternalFrame(tempioi1.iofile.getName()+"_subtracted_Image", true, true, true, true);
                jPanel = new JPanel();
                jLabel = new  JLabel(new ImageIcon(tempioi1.getBio()));
                jLabel.addMouseListener(new MyMouseAdapter());
                jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
                jPanel.add(jLabel);
                vjlbl.insertElementAt(jLabel,handler);
                tempjif.add(BorderLayout.CENTER, jPanel);
                tempjif.setResizable(true);
                tempjif.addInternalFrameListener(new MyInternalFrameListener());
                tempjif.setSize(tempioi1.getBio().getWidth(),tempioi1.getBio().getHeight());
                tempjif.setVisible(true);
                vio.add(tempioi1);
                vjif.add(tempjif);
                desktop.add(tempjif);
                desktop.repaint();
                imgtgtHandle = -2;
                imgbgHandle = -3;
                statusLabel.setText(tempioi1.iofile.getName() + " has been subtracted to " + tempioi2.iofile.getName());
            }

        }

        if("Subtract second source image 2".equals(e.getActionCommand())){
            if((imgtgtHandle > -1) && (imgbgHandle > -1)){
                IOImage tempioi1 = vio.elementAt(imgtgtHandle);
                IOImage tempioi2 = vio.elementAt(imgbgHandle);
                OpImage opi = new OpImage(tempioi1.getBio());
                tempioi1.bio = opi.getSubtractedBuffImg2(tempioi1.getBio(), tempioi2.getBio(), xycoordBG);
                JInternalFrame tempjif = new JInternalFrame(tempioi1.iofile.getName()+"_subtracted_Image", true, true, true, true);
                jPanel = new JPanel();
                jLabel = new  JLabel(new ImageIcon(tempioi1.getBio()));
                vjlbl.add(jLabel);
                jLabel.addMouseListener(new MyMouseAdapter());
                jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
                jPanel.add(jLabel);
                vjlbl.insertElementAt(jLabel,handler);

                tempjif.add(BorderLayout.CENTER, jPanel);
                tempjif.setResizable(true);
                tempjif.addInternalFrameListener(new MyInternalFrameListener());
                tempjif.setSize(tempioi1.getBio().getWidth(),tempioi1.getBio().getHeight());
                tempjif.setVisible(true);
                vio.add(tempioi1);
                vjif.add(tempjif);
                desktop.add(tempjif);
                desktop.repaint();
                imgtgtHandle = -2;
                imgbgHandle = -3;
                statusLabel.setText(tempioi1.iofile.getName() + " has been subtracted to " + tempioi2.iofile.getName());
            }

        }

        if("Combine source images".equals(e.getActionCommand())){
            if((imgtgtHandle > -1) && (imgbgHandle > -1)){
                IOImage tempioi1 = vio.elementAt(imgtgtHandle);
                IOImage tempioi2 = vio.elementAt(imgbgHandle);
                OpImage opi = new OpImage(tempioi1.getBio());
                tempioi1.bio = opi.combineImage(tempioi1.getBio(), tempioi2.getBio(), midpoint1, midpoint2);
                JInternalFrame tempjif = new JInternalFrame(tempioi1.iofile.getName()+"_combined_image", true, true, true, true);
                jPanel = new JPanel();
                jLabel = new  JLabel(new ImageIcon(tempioi1.getBio()));
                jLabel.addMouseListener(new MyMouseAdapter());
                jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
                jPanel.add(jLabel);
                vjlbl.insertElementAt(jLabel,handler);
                tempjif.add(BorderLayout.CENTER, jPanel);
                tempjif.setResizable(true);
                tempjif.addInternalFrameListener(new MyInternalFrameListener());
                tempjif.setSize(tempioi1.getBio().getWidth(),tempioi1.getBio().getHeight());
                tempjif.setVisible(true);
                vio.add(tempioi1);
                vjif.add(tempjif);
                desktop.add(tempjif);
                desktop.repaint();
                imgtgtHandle = -2;
                imgbgHandle = -3;
                statusLabel.setText(tempioi1.iofile.getName() + " has been combined to " + tempioi2.iofile.getName());
            }

        }
        if("Calculate XY-Bounds of starting point image".equals(e.getActionCommand())){
            if(!vio.isEmpty()){
                imgtgtHandle = handler;
                msg = vio.elementAt(imgtgtHandle).iofile.getName();
                statusLabel.setText(msg + " selected. Performing computations.");
                IOImage tempioi = vio.elementAt(imgtgtHandle);
                OpImage opi = new OpImage(tempioi.getBio());
                opi.calcxcoord1(tempioi.getBio(), xycoordFG);
            }
        }
        if("Calculate XY-Bounds of ending point image".equals(e.getActionCommand())){
            if(!vio.isEmpty()){
                imgbgHandle = handler;
                msg = vio.elementAt(imgbgHandle).iofile.getName();
                statusLabel.setText(msg + " selected. Performing computations.");
                IOImage tempioi = vio.elementAt(imgtgtHandle);
                OpImage opi = new OpImage(tempioi.getBio());
                opi.calcxcoord2(tempioi.getBio(), xycoordBG);
            }
        }

        if("Compute starting midpoint".equals(e.getActionCommand())){
            if(!xycoordFG.isEmpty() && !xycoordBG.isEmpty()){
                imgtgtHandle = handler;
                msg = vio.elementAt(imgtgtHandle).iofile.getName();
                statusLabel.setText(msg + " selected. Performing computations.");
                IOImage tempioi = vio.elementAt(imgtgtHandle);
                OpImage opi = new OpImage(tempioi.getBio());
                opi.calcxcoord1(tempioi.getBio(), xycoordFG);
                midpoint1 = findBounds(xycoordFG);
            }
        }

        if("Compute ending midpoint".equals(e.getActionCommand())){
            if(!xycoordFG.isEmpty() && !xycoordBG.isEmpty()){
                imgbgHandle = handler;
                msg = vio.elementAt(imgbgHandle).iofile.getName();
                statusLabel.setText(msg + " selected. Performing computations.");
                IOImage tempioi = vio.elementAt(imgbgHandle);
                OpImage opi = new OpImage(tempioi.getBio());
                opi.calcxcoord2(tempioi.getBio(), xycoordBG);
                midpoint2 = findBounds(xycoordBG);
            }
        }

        if("test WritableRaster".equals(e.getActionCommand())){
            imgbgHandle = handler;
            msg = vio.elementAt(imgbgHandle).iofile.getName();
            statusLabel.setText(msg + " selected. Performing computations.");
            IOImage tempioi = vio.elementAt(imgbgHandle);
            OpImage opi = new OpImage(tempioi.getBio());
            opi.testWritableRaster(tempioi.getBio());
        }

        if("Linear mapping".equals(e.getActionCommand())){

            int lmhandler = handler;

            lmJIF = new JInternalFrame("Linear mapping", true, true, true, true);

            textField1 = new JTextField(4);
            textField2 = new JTextField(4);

            btnstr1 = "Apply";
            btnstr2 = "Cancel";

            lmPanel = new JPanel();
            //lmPanel.setBackground(Color.BLUE);
            lmPanel.setMinimumSize(new Dimension(200,150));
            lmPanel.add(new JLabel("Slope: "));
            lmPanel.add(textField1);
            lmPanel.add(new JLabel("Bias: "));
            lmPanel.add(textField2);
            jbtn1 = new JButton(btnstr1);
            jbtn2 = new JButton(btnstr2);
            jbtn1.addActionListener(new LMBAL(lmhandler));

            jbtn2.addActionListener(new LMBAL(lmhandler));

            lmPanel.add(jbtn1);
            lmPanel.add(jbtn2);

            lmJIF.add(BorderLayout.CENTER, lmPanel);
            lmJIF.setResizable(true);
            lmJIF.addInternalFrameListener(new MyInternalFrameListener());
            lmJIF.setSize(new Dimension(200, 150));
            lmJIF.setVisible(true);
            lmJIF.toFront();
            vio.insertElementAt(null, handler);
            vjlbl.insertElementAt(null, handler);
            vjif.insertElementAt(lmJIF, handler);
            desktop.add(lmJIF);
            desktop.repaint();
        }

        if("Non-Linear mapping".equals(e.getActionCommand())){

            int lmhandler = handler;

            lmJIF = new JInternalFrame("Linear mapping", true, true, true, true);

            textField1 = new JTextField(4);
            textField2 = null;
            btnstr1 = "Apply";
            btnstr2 = "Cancel";

            lmPanel = new JPanel();
            //lmPanel.setBackground(Color.BLUE);
            lmPanel.setMinimumSize(new Dimension(200,150));
            lmPanel.add(new JLabel("Gain: "));
            lmPanel.add(textField1);

            jbtn1 = new JButton(btnstr1);
            jbtn2 = new JButton(btnstr2);
            jbtn1.addActionListener(new NLMBAL(lmhandler));

            jbtn2.addActionListener(new NLMBAL(lmhandler));

            lmPanel.add(jbtn1);
            lmPanel.add(jbtn2);

            lmJIF.add(BorderLayout.CENTER, lmPanel);
            lmJIF.setResizable(true);
            lmJIF.addInternalFrameListener(new MyInternalFrameListener());
            lmJIF.setSize(new Dimension(200, 150));
            lmJIF.setVisible(true);
            lmJIF.toFront();
            vio.insertElementAt(null, handler);
            vjlbl.insertElementAt(null, handler);
            vjif.insertElementAt(lmJIF, handler);
            desktop.add(lmJIF);
            desktop.repaint();
        }

        if("Invert pixels".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */
            //tempjif.getContentPane().removeAll();
            //tempjif = new JInternalFrame(tempioi.iofile.getName(), true, true, true, true);
            tempioi.bio = opi.invertPixels(tempioi.getBio());
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Inverted the pixel of the image.");

        }

        if("Non quantise invert pixels".equals(e.getActionCommand())){

           JInternalFrame tempjif = vjif.elementAt(handler);
           IOImage tempioi = vio.elementAt(handler);
           OpImage opi = new OpImage(tempioi.getBio());
           JLabel tmplbl = vjlbl.elementAt(handler);
           JPanel tmpjpnl = vpnl.elementAt(handler);
           /*
           try{
               tempjif.setClosed(true);
               vjif.remove(tempjif);
               vio.remove(tempioi);
               vjlbl.remove(tmplbl);
           }catch (PropertyVetoException pve){
               System.err.println("Failed to refresh frame.");
               return;
           }
           */
           //tempjif.getContentPane().removeAll();
           //tempjif = new JInternalFrame(tempioi.iofile.getName(), true, true, true, true);
           tempioi.bio = opi.invertPixelsNQ(tempioi.getBio());
           tmplbl.removeAll();
           tmpjpnl.removeAll();

           jLabel = tmplbl;
           jPanel = tmpjpnl;

           jPanel.remove(tmplbl);
           jLabel.setIcon(new ImageIcon(tempioi.getBio()));
           jLabel.addMouseListener(new MyMouseAdapter());
           jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
           jPanel.add(jLabel);
           vjlbl.set(handler,jLabel);
           vpnl.set(handler, jPanel);
           tempjif.add(BorderLayout.CENTER, jPanel);
           tempjif.setResizable(true);
           tempjif.addInternalFrameListener(new MyInternalFrameListener());
           tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
           tempjif.setVisible(true);
           jPanel.setVisible(true);
           jLabel.setVisible(true);
           tempjif.pack();
           tempjif.revalidate();
           tempjif.repaint();
           vio.set(handler, tempioi);
           vjif.set(handler, tempjif);
           //desktop.add(tempjif);
           desktop.repaint();
           statusLabel.setText(tempioi.iofile.getName() + " Inverted the pixel of the image.");

       }

        if("Set matrix".equals(e.getActionCommand())){

            int lmhandler = handler;

            lmJIF = new JInternalFrame("Set matrix", true, true, true, true);
            MATSIZE = 3;

            matTF = new JTextField[MATSIZE*MATSIZE];

            btnstr1 = "Apply";
            btnstr2 = "Cancel";
            //textField1 = new JTextField(4);
            lmPanel = new JPanel(new SpringLayout());
            //jbtn1 = new JButton(btnstr1);

            //lmPanel.add(new JLabel("Set matrix size: "));

            //lmPanel.add(textField1);
            //smsbtn = new JButton("Set matrix size");
            //lmPanel.add(smsbtn);
            //smsbtn.addActionListener(new SetMatrixSize());


            for (int i = 0; i < MATSIZE*MATSIZE; i++) {
                lmPanel.add(matTF[i] = new JTextField(4));
            }

            SpringUtilities.makeGrid(lmPanel,
                                     MATSIZE, MATSIZE, //rows, cols
                                     5, 5, //initialX, initialY
                                     5, 5);//xPad, yPad


            jbtn1 = new JButton(btnstr1);
            jbtn2 = new JButton(btnstr2);
            //jbtn1.addActionListener(new SetMatCoeff(lmhandler));

            //jbtn2.addActionListener(new SetMatCoeff(lmhandler));

            lmPanel.add(jbtn1);
            lmPanel.add(jbtn2);

            lmJIF.add(BorderLayout.CENTER, lmPanel);
            lmJIF.setResizable(true);
            lmJIF.addInternalFrameListener(new MyInternalFrameListener());
            lmJIF.pack();
            lmJIF.setVisible(true);
            lmJIF.toFront();
            vio.insertElementAt(null, handler);
            vjlbl.insertElementAt(null, handler);
            vjif.insertElementAt(lmJIF, handler);
            desktop.add(lmJIF);
            desktop.repaint();
        }

        if("Padd image".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.paddImage(tempioi.getBio());
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Image padded.");

        }

        if("Perform Sobel Prewitt edge detection".equals(e.getActionCommand())){
            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);

            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
             */

            tempioi.bio = opi.edgeDetection(tempioi.getBio(), 3, 1, 1, Mx, My, 0);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Image padded.");
        }

        if("Perform gradient edge detection".equals(e.getActionCommand())){
            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.edgeDetection(tempioi.getBio(), 3, 1, 1, Mx, My, 1);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Image padded.");
        }

        if("Perform iii edge detection".equals(e.getActionCommand())){
            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);
            /*
            try{
                tempjif.setClosed(true);
                vjif.remove(tempjif);
                vio.remove(tempioi);
                vjlbl.remove(tmplbl);
            }catch (PropertyVetoException pve){
                System.err.println("Failed to refresh frame.");
                return;
            }
            */

            tempioi.bio = opi.edgeDetection(tempioi.getBio(), 3, 1, 1, Mx, My, 2);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Image padded.");
        }

        if("Perform Laplacian edge detection".equals(e.getActionCommand())){
            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);

            tempioi.bio = opi.edgeDetection(tempioi.getBio(), 3, 1, 1, Mx, My, 2);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Image padded.");
        }

        if("Sinusoidal Noise".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);

            tempioi.bio = opi.sinusoidalNoise(tempioi.getBio(),2, 3, 4);
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Sinusoidal noise applied to the image.");

        }

        if("Gaussian Noise".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);


            tempioi.bio = opi.gaussianNoise(tempioi.getBio(),tempioi.getBio());
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " Sinusoidal noise applied to the image.");

        }

        if("Salt and Pepper Noise".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);

            tempioi.bio = opi.saltandpepperNoise(tempioi.getBio());
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " salt and pepper noise added.");

        }

        if("Mean Filter".equals(e.getActionCommand())){

            JInternalFrame tempjif = vjif.elementAt(handler);
            IOImage tempioi = vio.elementAt(handler);
            OpImage opi = new OpImage(tempioi.getBio());
            JLabel tmplbl = vjlbl.elementAt(handler);
            JPanel tmpjpnl = vpnl.elementAt(handler);

            tempioi.bio = opi.meanFilter(tempioi.getBio());
            tmplbl.removeAll();
            tmpjpnl.removeAll();

            jLabel = tmplbl;
            jPanel = tmpjpnl;

            jPanel.remove(tmplbl);
            jLabel.setIcon(new ImageIcon(tempioi.getBio()));
            jLabel.addMouseListener(new MyMouseAdapter());
            jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
            jPanel.add(jLabel);
            vjlbl.set(handler,jLabel);
            vpnl.set(handler, jPanel);
            tempjif.add(BorderLayout.CENTER, jPanel);
            tempjif.setResizable(true);
            tempjif.addInternalFrameListener(new MyInternalFrameListener());
            tempjif.setSize(tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
            tempjif.setVisible(true);
            jPanel.setVisible(true);
            jLabel.setVisible(true);
            tempjif.pack();
            tempjif.revalidate();
            tempjif.repaint();
            vio.set(handler, tempioi);
            vjif.set(handler, tempjif);
            //desktop.add(tempjif);
            desktop.repaint();
            statusLabel.setText(tempioi.iofile.getName() + " mean filter applied.");

        }

    }


    class LMBAL implements ActionListener{

        JInternalFrame tempjif;
        IOImage tempioi;
        OpImage opi;
        JLabel tmplbl;
        int myhandler;
        JPanel tmpjpnl;

        LMBAL(int myhandler){
            this.myhandler = imgtgtHandle;
            this.tempjif = vjif.elementAt(myhandler);
            this.tempioi = vio.elementAt(myhandler);
            this.opi = new OpImage(this.tempioi.getBio());
            this.tmplbl = vjlbl.elementAt(myhandler);
            this.tmpjpnl = vpnl.elementAt(myhandler);
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == jbtn1){
                processClick();

                try{
                    lmJIF.setClosed(true);
                    vio.remove(null);
                    vjlbl.remove(null);
                    vjif.remove(lmJIF);
                    tempjif.setClosed(true);
                    vjif.remove(this.tempjif);
                    vio.remove(this.tempioi);
                    vjlbl.remove(this.tmplbl);
                    vpnl.remove(this.tmpjpnl);
                }catch (PropertyVetoException pve){
                    System.err.println("Failed to refresh frame.");
                    return;
                }

               this.tempjif = new JInternalFrame(this.tempioi.iofile.getName(), true, true, true, true);
               this.tempioi.bio = this.opi.linearMapping(this.tempioi.getBio(),txtFint1, txtFint2);
               jPanel = new JPanel();
               jLabel = new  JLabel(new ImageIcon(tempioi.getBio()));
               jLabel.addMouseListener(new MyMouseAdapter());
               jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
               jPanel.add(jLabel);
               vjlbl.insertElementAt(jLabel, this.myhandler);
               this.tempjif.add(BorderLayout.CENTER, jPanel);
               this.tempjif.setResizable(true);
               tempjif.addInternalFrameListener(new MyInternalFrameListener());
               this.tempjif.setSize(this.tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
               this.tempjif.setVisible(true);
               vio.insertElementAt(this.tempioi, this.myhandler);
               vjif.insertElementAt(this.tempjif, this.myhandler);
               vpnl.insertElementAt(jPanel, this.myhandler);
               desktop.add(this.tempjif);
               desktop.repaint();
               statusLabel.setText(tempioi.iofile.getName() + " Image non-linearly mapped.");
               textField1.setText(null);
               textField2.setText(null);
           }else if(e.getSource() == jbtn2){
               textField1.setText(null);
               textField2.setText(null);
               lmPanel.setVisible(false);

           }

        }
   }

   class NLMBAL implements ActionListener{

       JInternalFrame tempjif;
       IOImage tempioi;
       OpImage opi;
       JLabel tmplbl;
       int myhandler;
       JPanel tmpjpnl;
       NLMBAL(int myhandler){
           this.myhandler = imgtgtHandle;
           this.tempjif = vjif.elementAt(myhandler);
           this.tempioi = vio.elementAt(myhandler);
           this.opi = new OpImage(this.tempioi.getBio());
           this.tmplbl = vjlbl.elementAt(myhandler);
           this.tmpjpnl = vpnl.elementAt(myhandler);
       }

       public void actionPerformed(ActionEvent e) {
           if(e.getSource() == jbtn1){
               processClick();

               try{
                   lmJIF.setClosed(true);
                   vio.remove(null);
                   vjlbl.remove(null);
                   vjif.remove(lmJIF);
                   tempjif.setClosed(true);
                   vjif.remove(this.tempjif);
                   vio.remove(this.tempioi);
                   vjlbl.remove(this.tmplbl);
                   vpnl.remove(this.tmpjpnl);
               }catch (PropertyVetoException pve){
                   System.err.println("Failed to refresh frame.");
                   return;
               }

               this.tempjif = new JInternalFrame(this.tempioi.iofile.getName(), true, true, true, true);
               this.tempioi.bio = this.opi.nonLinearMapping(this.tempioi.getBio(),txtFint1);
               jPanel = new JPanel();
               jLabel = new  JLabel(new ImageIcon(tempioi.getBio()));
               jLabel.addMouseListener(new MyMouseAdapter());
               jLabel.addMouseMotionListener(new MyMouseMotionAdapter());
               jPanel.add(jLabel);
               vjlbl.insertElementAt(jLabel, this.myhandler);
               this.tempjif.add(BorderLayout.CENTER, jPanel);
               this.tempjif.setResizable(true);
               tempjif.addInternalFrameListener(new MyInternalFrameListener());
               this.tempjif.setSize(this.tempioi.getBio().getWidth(),tempioi.getBio().getHeight());
               this.tempjif.setVisible(true);
               vio.insertElementAt(this.tempioi, this.myhandler);
               vjif.insertElementAt(this.tempjif, this.myhandler);
               vpnl.insertElementAt(jPanel, this.myhandler);
               desktop.add(this.tempjif);
               desktop.repaint();
               statusLabel.setText(tempioi.iofile.getName() + " Image non-linearly mapped.");
               textField1.setText(null);
               textField2.setText(null);
           }else if(e.getSource() == jbtn2){
               textField1.setText(null);
               textField2.setText(null);
               lmPanel.setVisible(false);

           }

        }
    }

    public void processClick(){
        System.out.println("processing mouse click." );
        txtFint1 = Integer.parseInt(textField1.getText());
        if(textField2 != null)
            txtFint2 = Integer.parseInt(textField2.getText());
        lmPanel.setVisible(false);
    }

    public static int[] findBounds(Vector<int[]> vec){
        int[] bounds = {0, 0, 0, 0, 0, 0};
        //xmin/xmax/xmid
        bounds[0] = vec.elementAt(0)[0];
        bounds[2] = vec.elementAt(0)[0];
        for(int i = 0; i < vec.size(); i++){
            if(bounds[0] > vec.elementAt(i)[0])
                bounds[0] = vec.elementAt(i)[0];
            if(bounds[2] < vec.elementAt(i)[0])
                bounds[2] = vec.elementAt(i)[0];
        }
        bounds[4] = (bounds[2] - bounds[1])/2;
        //ymin/ymax/ymid
        bounds[1] = vec.elementAt(0)[1];
        bounds[3] = vec.elementAt(0)[1];
        for(int i = 0; i < vec.size(); i++){
            if(bounds[1] > vec.elementAt(i)[1])
                bounds[1] = vec.elementAt(i)[1];
            if(bounds[3] < vec.elementAt(i)[1])
                bounds[3] = vec.elementAt(i)[1];
        }
        bounds[5] = (bounds[3] - bounds[1])/2;

        return bounds;

    }

    //class for JInternalFrame
    class MyInternalFrameListener implements InternalFrameListener{

        MyInternalFrameListener(){

        }

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            displayMessage("Internal frame opened", e);
            try{
                e.getInternalFrame().setSelected(true);
                e.getInternalFrame().moveToFront();
            }catch(PropertyVetoException pve){
                System.err.println("Failed to focus on frame.");
            }
            count++;
        }
        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            displayMessage("Internal frame iconified", e);
        }
        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
            displayMessage("Internal frame deiconified", e);
        }
        @Override
        public void internalFrameActivated(InternalFrameEvent e) {

            displayMessage("Internal frame activated", e);
            handler = vjif.indexOf(e.getInternalFrame());
            System.out.println(handler + " : " + e.getSource() );
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            displayMessage("Internal frame deactivated", e);
        }
        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            displayMessage("Internal frame closing", e);
        }
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            displayMessage("Internal frame closed", e);

            if(!vjif.isEmpty()){
                int idx = vjif.indexOf(e.getInternalFrame());
                vjif.remove(e.getInternalFrame());
                if(idx > -1){
                    vio.remove(vio.elementAt(idx));
                    vjlbl.remove(vjlbl.elementAt(idx));
                }
            }
            count--;
        }
        //Add some text to the text area.
        void displayMessage(String prefix, InternalFrameEvent e) {
            //String s = prefix + ": " + e.getSource();
            System.out.println(prefix);
        }
    }
    boolean mousefocus = false;
    class MyMouseAdapter extends MouseInputAdapter{
        boolean drag = false;
        public MyMouseAdapter(){

        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            System.out.println("mouseClicked");

        }
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            System.out.println("mouseEntered");
            mousefocus = true;
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            System.out.println("mouseExited");
            mousefocus = false;
        }
        public void mousePressed(java.awt.event.MouseEvent evt) {
            System.out.println("mousePressed");
        }
        @Override
        public void mouseDragged(MouseEvent evt) {
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            System.out.println("mouseReleased");
        }

    }
    //class for Mouse painting.
    class MyMouseMotionAdapter extends MouseMotionAdapter{
        BufferedImage oimage;

        public void mouseDragged(MouseEvent e){
            if(mousefocus){
                vio.elementAt(handler).getBio().setRGB(e.getX(), e.getY(), (new Color(255,255,255,255)).getRGB());
                vjlbl.elementAt(handler).repaint();
            }
        }

    }
}
