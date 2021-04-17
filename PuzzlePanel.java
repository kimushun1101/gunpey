/*
The MIT License (MIT)

Copyright (c) 2021 Shunsuke KIMURA.

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
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Random;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

class PuzzlePanel extends JPanel implements KeyListener {

    // マスのサイズ（GRID SIZE）
    private static final int GSW = 40;
    private static final int GSH = 30;
    // マスの数。Gunpeyは5×10マス
    private static final int COL = 5;
    private static final int ROW = 10;
    // 盤面の大きさ＝メインパネルの大きさと同じ
    private static final int WIDTH = GSW * COL + 2;
    private static final int HEIGHT = GSH * ROW + 2;    
    // パネルの配置
	private int[][] normalPanel = new int[COL][ROW];		// 通常のpanel
	private int[][] vanishPanel = new int[COL][ROW];		// 消えるpanel
	private int[][] floatingPanel = new int[COL][ROW];	// 浮いているpanel
	private int[][] panelFlag = new int[COL][ROW];		// 接続判定用
	private int[][] gridFlag = new int[COL+1][ROW+1];	// 接続判定用
	private int connectFlag;								// 接続判定用
	// パネルの種類
	private static final int bs = 1;
	private static final int sl = 2;
	private static final int vi = 3;
	private static final int ha = 4;
	// 得点計算用
	public int score = 0;				// DisplayPanel から読まれる
	private int scoreTemp = 0;
	private int scoreTempPre = 0;
	// タイムのクラス生成
	private TimeFlag swapTimer;
	private TimeFlag vanishTimer;
	private TimeFlag panelupTimer;
	public  boolean isPlaying = false;	// DisplayPanel から読まれる
	// パネルの位置
    private int curX = 2, curY = 4;	    // カーソルの位置
    private int s1X = 0, s1Y = 0;	    // 移動パネルの位置
    private int s2X = 0, s2Y = 0;	    // 移動パネルの位置
    // 背景のイメージ
	private String PlayingImg = "imgs" + File.separator + "playing.jpg";
	private String StanbyImg = "imgs" + File.separator + "standby.jpg";
	// 効果音やBGM
	// private AudioClip acBGM     = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "bgm.mid"));
	private AudioClip acSwap    = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "swap.wav"));
	private AudioClip acConnect = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "connect.wav"));
	private AudioClip acVanish  = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "vanish.wav"));
	private AudioClip acPanelUp = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "panelup.wav"));

	public PuzzlePanel() {
        // pack()するときに必要
    	// setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
		addKeyListener(this);
		
		swapTimer = new TimeFlag(false, 0, 80);
		vanishTimer = new TimeFlag(false, 0, 6000);
		panelupTimer = new TimeFlag(false, 0, 5000);
	}
	
	public void start(){
		// パズルの初期化
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
				normalPanel[x][y] = 0;
			}
        }
    	curX = 2; curY = 4;	    // カーソルの位置
		score = 0;
		panelupTimer.setFlag(true);
		isPlaying = true;
		// acBGM.loop();
	}
	public void end(){
	    score += scoreTemp;
	    scoreTemp = 0;
	    RankingData.setScore(score);
	    swapTimer.setFlag(false);
	    vanishTimer.setFlag(false);
		isPlaying = false;
		repaint();
		// acBGM.stop();
	}
	
    public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D)g;
	    // 背景を描く
		Image img = getToolkit().getImage(PlayingImg);
		if(isPlaying == false){
			img = getToolkit().getImage(StanbyImg);
		}
		g.drawImage(img,-100,-100,this);
		// パネル枠を描画する
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                g.setColor(Color.BLACK);
                g.drawRect(x*GSW+1, y*GSH, GSW, GSH);
            }
        }
	    // パネルを描く
		BasicStroke wideStroke1 = new BasicStroke(1.0f);
		BasicStroke wideStroke2 = new BasicStroke(4.0f);
		g2.setStroke(wideStroke2);
        for (int x = 0; x < COL; x++) {
		    for (int y = 0; y < ROW; y++) {
				// normalPanelの表示
				g2.setColor(Color.GREEN);
				drawPanel(g2, normalPanel[x][y], x*GSW+1, y*GSH);
				// vanishPanelの表示
				g2.setColor(Color.ORANGE);
				drawPanel(g2, vanishPanel[x][y], x*GSW+1, y*GSH);
				// floatingPanelの表示(ずらして表示)
				if(floatingPanel[x][y] != 0){
					g2.setColor(Color.GREEN);
					drawPanel(g2, floatingPanel[x][y], x*GSW+1-5, y*GSH-5);
					g2.setStroke(wideStroke1);
	                g2.setColor(Color.BLACK);
	                g2.drawRect(x*GSW+1-5, y*GSH-5, GSW, GSH);
					g2.setStroke(wideStroke2);
				}
	        }
	    }
		// 入れ替え中のパネルを描く
        if(swapTimer.getFlag()){
			s1X = curX*GSW+1; s1Y = curY*GSH; 	    // 移動パネルの位置
			s2X = curX*GSW+1; s2Y = curY*GSH+GSH;	// 移動パネルの位置

			g.setColor(Color.PINK);
			g.fillRect(s1X+1, s1Y, GSW, GSH);
			g.fillRect(s2X+1, s2Y, GSW, GSH);

	        g2.setColor(Color.GREEN);
			drawPanel(g2, normalPanel[curX][curY+1], s1X+1, s1Y+(int)(GSH*swapTimer.getRate()));
			drawPanel(g2, normalPanel[curX][curY], s2X+1, s2Y-(int)(GSH*swapTimer.getRate()));
		}

        // カーソルを描く
		if(isPlaying){
			g2.setColor(Color.RED);
			g2.setStroke(wideStroke2);
			g2.drawRect(curX*GSW+1, curY*GSH, GSW, 2*GSH);
		}
        
        // 接続中の得点を表示
        if(scoreTemp != 0){
			g2.setColor(Color.YELLOW);
			Font f=new Font("TimesRoman",Font.PLAIN,20);
			g2.setFont(f);
			int dispX = 0, dispY = 0;
		    for (int y = 0; y < ROW; y++) {
		        for (int x = 0; x < COL; x++) {
			    	if(panelFlag[x][y] == 1){
						dispX = x;	dispY = y;
					}
				}
		    }
		    // 表示位置の微調整
		    if(dispX > 3){
			    dispX = 3;
			}
		    if(dispY > 5){
			    dispY -= 1;
			}
	        g2.drawString(String.valueOf(scoreTemp), dispX*GSW, dispY*GSH);
		}
    }
    
    // パネルを描くメソッド
    public void drawPanel(Graphics2D g2, int p, int x, int y) {
		switch(p){
		case bs:	// バックスラッシュ
			g2.draw(new Line2D.Double(x, y, x+GSW+1, y+GSH));
			break;
		case sl:	// スラッシュ
			g2.draw(new Line2D.Double(x, y+GSH+1, x+GSW, y));
			break;
		case vi:	// ブイ
			g2.draw(new Line2D.Double(x, y, x+GSW/2+1, y+GSH/2));
			g2.draw(new Line2D.Double(x+GSW/2, y+GSH/2+1, x+GSW, y));
			break;
		case ha:	// ハット
			g2.draw(new Line2D.Double(x, y+GSH, x+GSW/2+1, y+GSH/2));
			g2.draw(new Line2D.Double(x+GSW/2, y+GSH/2+1, x+GSW, y+GSH));
			break;
		default:
			break;
		}
	}
    
	// パネル消去フラグチェックメソッド
	public void checkFlags(){
   	    // vanishPanelも判定材料に加える
	    for (int x = 0; x < COL; x++) {
		    for (int y = 0; y < ROW; y++) {
		    	if(vanishPanel[x][y] != 0){
		    		normalPanel[x][y] = vanishPanel[x][y];
		    	}
			}
	    }
		// 接続確認用flagの初期化
        for (int x = 0; x < COL+1; x++) {
		    for (int y = 0; y < ROW+1; y++) {
		    	if((x<COL) & (y<ROW))
					panelFlag[x][y] = 0;	// 定義外に数字を入れない
				if(x < COL){
					gridFlag[x][y] = 0;
				}else{
					gridFlag[x][y] = 1;	// 右端の格子のみフラグ立て
				}
			}
	    }
	    // 再帰関数の呼び出し
	    for (int y = 0; y < ROW+1; y++) {
	    	connectFlag = 0;
	    	connectionCheck(0,y,0,y);
	    }
		// 右から探索
	    for (int y = 0; y < ROW+1; y++) {
			gridFlag[COL][y] = 0;	// 右端の格子のみフラグおろし
	    }
	    // 再帰関数の呼び出し
	    for (int y = 0; y < ROW+1; y++) {
	    	connectFlag = 0;
	    	connectionCheck(COL,y,COL,y);
	    }
	    
	    // 消えるパネルをvanishPanelへ移して点数を数える
    	scoreTemp = 0;
	    for (int x = 0; x < COL; x++) {
		    for (int y = 0; y < ROW; y++) {
		    	if(panelFlag[x][y] == 1){
		    		vanishPanel[x][y] = normalPanel[x][y];
		    		normalPanel[x][y] = 0;
					if(!vanishTimer.getFlag()){
						vanishTimer.setFlag(true);
					}
		    		scoreTemp++;
		    	}
			}
	    }
    	scoreTemp = 100 * scoreTemp * (scoreTemp - 4);
		if(scoreTemp > scoreTempPre){	// 前回の判定より得点が増えたときのみ再生する
			acConnect.play();
		}
		scoreTempPre = scoreTemp;
	}

    // 再帰関数
    // 0:失敗, 未探索	1:成功		2:探索中
	public int connectionCheck(int x, int y, int px, int py) {
		// now grid x, now grid y, previous grid x, and previous grid y
		int final_gF = 0;	// 現在の格子の最終的なフラグ
		// 未探索だったら判定を開始，そうでなければそれぞれリターン
		switch(gridFlag[x][y]){
		case 0:
			gridFlag[x][y] = 2;
			break;
		case 1:
			return 1;
		case 2:
			return connectFlag;
		}
    	
		if(x != COL){		// 右方向チェック
			if(y != 0){		// 右上方向チェック
				if(normalPanel[x][y-1] == sl & (px != x+1 | py != y-1))
					final_gF += panelFlag[x][y-1] = connectionCheck(x+1,y-1,x,y);
				if(normalPanel[x][y-1] == ha & (px != x+1 | py != y))
					final_gF += panelFlag[x][y-1] = connectionCheck(x+1,y,x,y);
			}
			if(y != ROW){	// 右下方向チェック
				if(normalPanel[x][y] == vi & (px != x+1 | py != y))
					final_gF += panelFlag[x][y] = connectionCheck(x+1,y,x,y);
				if(normalPanel[x][y] == bs & (px != x+1 | py != y+1))
					final_gF += panelFlag[x][y] = connectionCheck(x+1,y+1,x,y);
			}
		}
		if(x != 0){			// 左方向チェック
			if(y != 0){		// 左上方向チェック
				if(normalPanel[x-1][y-1] == bs & (px != x-1 | py != y-1))
					final_gF += panelFlag[x-1][y-1] = connectionCheck(x-1,y-1,x,y);
				if(normalPanel[x-1][y-1] == ha & (px != x-1 | py != y))
					final_gF += panelFlag[x-1][y-1] = connectionCheck(x-1,y,x,y);
			}
			if(y != ROW){	// 左下方向チェック
				if(normalPanel[x-1][y] == vi & (px != x-1 | py != y))
					final_gF += panelFlag[x-1][y] = connectionCheck(x-1,y,x,y);
				if(normalPanel[x-1][y] == sl & (px != x-1 | py != y+1))
					final_gF += panelFlag[x-1][y] = connectionCheck(x-1,y+1,x,y);
			}
		}
		if(final_gF == 0){
			gridFlag[x][y] = 0;
		}else{
			gridFlag[x][y] = 1;
			connectFlag = 1;
		}
		return gridFlag[x][y];
	}
    
	public void keyPressed(KeyEvent e) {
		if(!swapTimer.getFlag() && isPlaying){
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:	// 左キー
				if(curX > 0)	curX--;	break;
			case KeyEvent.VK_UP:	// 上キー
				if(curY > 0)	curY--;	break;
			case KeyEvent.VK_RIGHT:	// 右キー
				if(curX < COL-1)curX++;break;
			case KeyEvent.VK_DOWN:	// 下キー
				if(curY < ROW-2)curY++;break;
			case KeyEvent.VK_SPACE:	// スペースキー(入れ替え)
				swapTimer.setFlag(true);
				acSwap.play();
				int buf1, buf2;
				
				// 入れ替え
				// パネルを取る
				if(vanishPanel[curX][curY] == 0){
					buf1 = normalPanel[curX][curY];
				}else{
					buf1 = floatingPanel[curX][curY];
				}
				if(vanishPanel[curX][curY+1] == 0){
					buf2 = normalPanel[curX][curY+1];
				}else{
					buf2 = floatingPanel[curX][curY+1];
				}
				// パネルを埋める
				if(vanishPanel[curX][curY] == 0){
					normalPanel[curX][curY] = buf2;
				}else{
					floatingPanel[curX][curY] = buf2;
				}
				if(vanishPanel[curX][curY+1] == 0){
					normalPanel[curX][curY+1] = buf1;
				}else{
					floatingPanel[curX][curY+1] = buf1;
				}
				// フラグチェック
			    checkFlags();
				break;
			case KeyEvent.VK_B:	// bキーを押下したとき新しいパネルの追加
				panelUp();
				break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
	
	public void panelUp(){
		if(!vanishTimer.getFlag()){
			acPanelUp.play();
			// 一番上にパネルが存在していればゲーム終了
	        for (int x = 0; x < COL; x++) {
				if(normalPanel[x][0] != 0){
					isPlaying = false;
				}
	        }
	        for (int y = 0; y < ROW-1; y++) {// 一番下は除く
	            for (int x = 0; x < COL; x++) {
					normalPanel[x][y] = normalPanel[x][y+1];
	            }
	        }
	        // 一番下に新しい行を入れる
			// パネルの個数を決定
			Random rnd = new Random();
			int num = rnd.nextInt(100);
			if(num < 45){
				num = 2;
			}else if(num < 90){
				num = 3;
			}else{
				num = 4;
			}
			// パネルの位置と形を決定
			boolean[] inputP = {false,false,false,false,false};
			while(num > 0){
				rnd = new Random();
				int px = rnd.nextInt(5);
				if(inputP[px] == false){
					inputP[px] = true;
					num--;
				}
			}
	        for (int x = 0; x < COL; x++) {
				if(inputP[x]){
					normalPanel[x][ROW-1] = rnd.nextInt(4)+1;
				}else{
					normalPanel[x][ROW-1] = 0;
				}
	        }
	        if(curY > 0){
	        	curY--;		// カーソルもパネルに合わせて一つ上げる
			}
			
		    checkFlags();	// フラグチェック
	    }
		panelupTimer.setFlag(true);
	}
	
	// パズルパネルの時間を進める．
    public void tick() {
		swapTimer.tickTime();
		vanishTimer.tickTime();
		panelupTimer.tickTime();
		
		if(!vanishTimer.getFlag()){
			for (int x = 0; x < COL; x++) {
			    for (int y = 0; y < ROW; y++) {
		    		vanishPanel[x][y] = 0;
		    		if(floatingPanel[x][y] != 0){
			    		normalPanel[x][y] = floatingPanel[x][y];
			    		floatingPanel[x][y] = 0;
			    	}
				}
		    }
		    if(scoreTemp != 0){
			    score += scoreTemp;
			    scoreTemp = 0;
		    }
		    if(vanishTimer.getTime() > 5){
		    	acVanish.play();
		    }
		}
		if(!panelupTimer.getFlag()){
			panelUp();
		}
		repaint();
    }
}
