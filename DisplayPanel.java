import java.awt.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;
import java.io.File;

class DisplayPanel extends JPanel implements Runnable{
	private PuzzlePanel pPanel;

    // 盤面の大きさ＝メインパネルの大きさと同じ
    private static final int WIDTH = 500;
    private static final int HEIGHT = 310;

    private static int score = 1000;
	public Font f = new Font("TimesRoman",Font.PLAIN,15);

	private Thread thread;	// タイマ用スレッド
	private TimeFlag game;	// タイムのクラス生成
	private int timeLimit = 1000 * 90;
	
	private boolean readHiScore = false;

	private JLabel nSCORE = new JLabel("123456");
	private JLabel nHS = new JLabel(RankingData.getHiScore()+"");
	private JLabel nTime = new JLabel("00:00");

	public DisplayPanel() {
        // pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        setLayout(null);
        
		JLabel tSCORE = new JLabel(" SCORE");
		tSCORE.setBackground(Color.darkGray);
		tSCORE.setForeground(Color.ORANGE);
		tSCORE.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		tSCORE.setFont(f);
		tSCORE.setBounds(360, 5, 130, 15);
		
		nSCORE.setHorizontalAlignment(JLabel.RIGHT);
		nSCORE.setBackground(Color.darkGray);
		nSCORE.setForeground(Color.WHITE);
		nSCORE.setOpaque(true);
		f = new Font("TimesRoman",Font.BOLD, 30);
		nSCORE.setFont(f);
		nSCORE.setBounds(360, 20, 130, 30);
		
		JLabel tHS = new JLabel(" HIGH SCORE");
		tHS.setBackground(Color.darkGray);
		tHS.setForeground(Color.ORANGE);
		tHS.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		tHS.setFont(f);
		tHS.setBounds(380, 60, 110, 15);
		
		nHS.setHorizontalAlignment(JLabel.RIGHT);
		nHS.setBackground(Color.darkGray);
		nHS.setForeground(Color.WHITE);
		nHS.setOpaque(true);
		f = new Font("TimesRoman",Font.BOLD, 30);
		nHS.setFont(f);
		nHS.setBounds(380, 75, 110, 30);

		JLabel tTime = new JLabel(" TIME");
		tTime.setSize(50,50);
		tTime.setBackground(Color.darkGray);
		tTime.setForeground(Color.ORANGE);
		tTime.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		tTime.setFont(f);
		tTime.setBounds(10, 5, 50, 15);
		
		nTime.setHorizontalAlignment(JLabel.RIGHT);
		nTime.setBackground(Color.darkGray);
		nTime.setForeground(Color.WHITE);
		nTime.setOpaque(true);
		f = new Font("TimesRoman",Font.BOLD, 30);
		nTime.setFont(f);
		nTime.setBounds(10, 20, 130, 30);
		
		JLabel OpeInst = new JLabel("<html>操作方法<br>ゲームの開始：<br>「Alt+G」→Enter<br>カーソルの移動：<br>　方向キー<br>パネル入れ替え：<br>　スペースキー<br>新しい列を出す：<br>　Bキー");
		OpeInst.setSize(120,110);
		OpeInst.setBackground(Color.darkGray);
		OpeInst.setForeground(Color.ORANGE);
		OpeInst.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		OpeInst.setFont(f);
		OpeInst.setBounds(10, 110, 130, 190);

		add(tSCORE);
		add(nSCORE);
		add(tHS);
		add(nHS);
		add(tTime);
		add(nTime);
		add(OpeInst);

		pPanel = new PuzzlePanel();
		pPanel.setBounds(149, 5, 202, 302);
		add(pPanel, BorderLayout.SOUTH);
		
		game = new TimeFlag(false, 0, timeLimit);

		// スレッドを起動
		thread = new Thread(this);
		thread.start();
	}
	
	public void gameStart(){
		game.setFlag(true);
		readHiScore = true;
		pPanel.start();
	}
	
    public void paintComponent(Graphics g) {
	    // 背景を描く
		Image img = getToolkit().getImage("imgs" + File.separator + "bg_blue.png");
		g.drawImage(img,-100,-100,this);
	}
	
	public void run() {
		while (true) {
			// 10ミリ秒だけ休止
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(game.flag){
				game.tickTime();
				pPanel.tick();
				long Remaining = (long)(timeLimit - game.time);
		        long sec = TimeUnit.MILLISECONDS.toSeconds(Remaining);
		        long ms  = (long)Remaining - TimeUnit.SECONDS.toMillis(sec);
		        
		        if(sec >= 30){
					nTime.setForeground(Color.WHITE);
				}else if(sec >= 10){
					nTime.setForeground(Color.ORANGE);
				}else{
					nTime.setForeground(Color.RED);
				}
		        nTime.setText(String.format("%02d",sec)+":"+String.format("%02d",ms/10));
		        if(pPanel.gameState != true){
					game.flag = false;
				}
			}else if (readHiScore){
				pPanel.end();
				nHS.setText(RankingData.getHiScore()+"");
				readHiScore = false;
			}
			nSCORE.setText(pPanel.score+"");
			
			if(pPanel.f2){
				pPanel.end();
				pPanel.f2 = false;
			}
		}
	}
}
