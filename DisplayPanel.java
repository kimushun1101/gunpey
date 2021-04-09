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

	private JLabel dispScore = new JLabel("123456");
	private JLabel dispHighScore = new JLabel(RankingData.getHiScore()+"");
	private JLabel dispTimer = new JLabel("00:00");

	public DisplayPanel() {
        // pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        setLayout(null);
        
		JLabel textScore = new JLabel(" SCORE");
		textScore.setBackground(Color.darkGray);
		textScore.setForeground(Color.ORANGE);
		textScore.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		textScore.setFont(f);
		textScore.setBounds(360, 5, 130, 15);
		
		dispScore.setHorizontalAlignment(JLabel.RIGHT);
		dispScore.setBackground(Color.darkGray);
		dispScore.setForeground(Color.WHITE);
		dispScore.setOpaque(true);
		f = new Font("TimesRoman",Font.BOLD, 30);
		dispScore.setFont(f);
		dispScore.setBounds(360, 20, 130, 30);
		
		JLabel textHighScore = new JLabel(" HIGH SCORE");
		textHighScore.setBackground(Color.darkGray);
		textHighScore.setForeground(Color.ORANGE);
		textHighScore.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		textHighScore.setFont(f);
		textHighScore.setBounds(380, 60, 110, 15);
		
		dispHighScore.setHorizontalAlignment(JLabel.RIGHT);
		dispHighScore.setBackground(Color.darkGray);
		dispHighScore.setForeground(Color.WHITE);
		dispHighScore.setOpaque(true);
		f = new Font("TimesRoman",Font.BOLD, 30);
		dispHighScore.setFont(f);
		dispHighScore.setBounds(380, 75, 110, 30);

		JLabel textTime = new JLabel(" TIME");
		textTime.setSize(50,50);
		textTime.setBackground(Color.darkGray);
		textTime.setForeground(Color.ORANGE);
		textTime.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		textTime.setFont(f);
		textTime.setBounds(10, 5, 50, 15);
		
		dispTimer.setHorizontalAlignment(JLabel.RIGHT);
		dispTimer.setBackground(Color.darkGray);
		dispTimer.setForeground(Color.WHITE);
		dispTimer.setOpaque(true);
		f = new Font("TimesRoman",Font.BOLD, 30);
		dispTimer.setFont(f);
		dispTimer.setBounds(10, 20, 130, 30);
		
		JLabel textInstruction = new JLabel("<html>操作方法<br>ゲームの開始：<br>「Alt+G」→Enter<br>カーソルの移動：<br>　方向キー<br>パネル入れ替え：<br>　スペースキー<br>新しい列を出す：<br>　Bキー");
		textInstruction.setSize(120,110);
		textInstruction.setBackground(Color.darkGray);
		textInstruction.setForeground(Color.ORANGE);
		textInstruction.setOpaque(true);
		f = new Font("TimesRoman",Font.PLAIN,15);
		textInstruction.setFont(f);
		textInstruction.setBounds(10, 110, 130, 190);

		add(textScore);
		add(dispScore);
		add(textHighScore);
		add(dispHighScore);
		add(textTime);
		add(dispTimer);
		add(textInstruction);

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
			// 5ミリ秒だけ休止
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(game.flag){
				game.tickTime();
				pPanel.tick();
				long Remaining = (long)(timeLimit - game.getTime());
		        long sec = TimeUnit.MILLISECONDS.toSeconds(Remaining);
		        long ms  = (long)Remaining - TimeUnit.SECONDS.toMillis(sec);
		        
		        if(sec >= 30){
					dispTimer.setForeground(Color.WHITE);
				}else if(sec >= 10){
					dispTimer.setForeground(Color.ORANGE);
				}else{
					dispTimer.setForeground(Color.RED);
				}
		        dispTimer.setText(String.format("%02d",sec)+":"+String.format("%02d",ms/10));
		        if(pPanel.gameSROW != true){
					game.flag = false;
				}
			}else if (readHiScore){
				pPanel.end();
				dispHighScore.setText(RankingData.getHiScore()+"");
				readHiScore = false;
			}
			dispScore.setText(pPanel.score+"");
			
			if(pPanel.f2){
				pPanel.end();
				pPanel.f2 = false;
			}
		}
	}
}
