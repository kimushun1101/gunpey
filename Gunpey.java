/*
The MIT License (MIT)

Copyright (c) 2021 ShunsukeKimura.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.event.KeyEvent;
import java.io.File;


class Gunpey extends JFrame {
	private GameMenu  gMenu;
	private DisplayPanel dPanel;
	
	// thread of updating value
	private Thread thread;

	Gunpey() {
		getContentPane().setLayout(new FlowLayout());
		setTitle("Gunpey");
		
		gMenu = new GameMenu();
		// Action listener of starting of the click
		gMenu.iStart.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				dPanel.gameStart();
			}
		});

		// Action listener of ending of the click
		gMenu.iExit.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				System.exit(0);
			}
		});

		// Action listener of ending of the click
		gMenu.iHelp.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				JFrame frame = new JFrame("ヘルプ");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				// URL 対応するのが面倒くさくて暫定的に処理してます。
				// JLabel text = new JLabel("<html>このソフトについては <br><a href='Github'>https://github.com/kimushun1101/gunpey</a><br> をご参照ください。");
				JLabel text = new JLabel("<html>このソフトについては <br><a href='https://github.com/kimushun1101/gunpey'>https://github.com/kimushun1101/gunpey</a><br> をご参照ください。");
				frame.getContentPane().add(text);
				frame.setSize(300, 150);
				frame.setVisible(true);
			}
		});

		dPanel = new DisplayPanel();

        setJMenuBar(gMenu);
        Container contentPane = getContentPane();
		contentPane.add(dPanel, BorderLayout.NORTH);
		
        pack();
		setResizable(false);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		ImageIcon icon = new ImageIcon("img" + File.separator + "gunpey.gif");
		setIconImage(icon.getImage());
	}

	public static void main(String [] args) {
		new Gunpey();
	}
}
