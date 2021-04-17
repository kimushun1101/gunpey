import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.event.KeyEvent;
import java.io.File;

class Gunpey extends JFrame {
	private GameMenu  gMenu;
	private DisplayPanel dPanel;
	
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
		
		ImageIcon icon = new ImageIcon("imgs" + File.separator + "icon.png");
		setIconImage(icon.getImage());
	}

	public static void main(String [] args) {
		new Gunpey();
	}
}
