import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;

class GameMenu extends JMenuBar {

	public final JMenuItem iStart = new JMenuItem("ゲームの開始");
	public final JMenuItem iExit = new JMenuItem("終了");

	public final JMenuItem iHelp = new JMenuItem("ヘルプの表示");
	
	GameMenu() {
		JMenu mGame = new JMenu("Game");
	    mGame.setMnemonic(KeyEvent.VK_G);

		mGame.add(iStart);
		mGame.addSeparator();
		mGame.add(iExit);
		add(mGame);

		JMenu mHelp = new JMenu("Help");
		mHelp.setMnemonic(KeyEvent.VK_H);
		mHelp.add(iHelp);
		add(mHelp);
		
		add(Box.createRigidArea(new Dimension(5, 1)));

	}
}
