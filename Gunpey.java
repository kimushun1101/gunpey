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
