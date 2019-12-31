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

public class jPhotoEdit{
    public static void main(final String[] args) {
        MainUI app = new MainUI();
        app.createAndShowGUI();
    }
}
