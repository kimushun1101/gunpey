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
	public int[][] bPanel = new int[COL][ROW];		// panelの種類
	public int[][] kPanel = new int[COL][ROW];		// 消えるpanelのコピー
	public int[][] fPanel = new int[COL][ROW];		// 浮いているpanelのコピー
	public int[][] cFlag = new int[COL][ROW];		// mass flag
	public int[][] gFlag = new int[COL+1][ROW+1];	// grid flag
	public int cF;		// connect flag
	// パネルの種類
	private static final int bs = 1;
	private static final int sl = 2;
	private static final int vi = 3;
	private static final int ha = 4;
	// 得点計算用
	public int score = 0;
	public int scoreTemp = 0;
	public Font f;
	// タイムのクラス生成
	private TimeFlag swap;
	private TimeFlag kill;
	private TimeFlag nline;
	public  boolean gameSROW = false;
	// パネルの位置
    private int curX = 2, curY = 4;	    // カーソルの位置
    private int s1X = 0, s1Y = 0;	    // 移動パネルの位置
    private int s2X = 0, s2Y = 0;	    // 移動パネルの位置
    // 背景のイメージ
	private String bgPlay = "imgs" + File.separator + "bg_silver.png";
	private String bgOver = "imgs" + File.separator + "bg_gold.png";
	// 効果音やBGM
	private AudioClip acBGM    = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "bgm.mid"));
	private AudioClip acSwap   = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "swap.wav"));
	private AudioClip acConnect  = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "connect.wav"));
	private AudioClip acVanish   = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "vanish.wav"));
	private AudioClip acLineUp = Applet.newAudioClip(getClass().getResource("sounds" + File.separator + "lineup.wav"));

	// F2の判定
	public boolean f2 = false;

	public PuzzlePanel() {
        // pack()するときに必要
    	// setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
		addKeyListener(this);
		
		swap = new TimeFlag(false, 0, 80);
		kill = new TimeFlag(false, 0, 6000);
		nline = new TimeFlag(false, 0, 5000);
	}
	
	public void start(){
		// パズルの初期化
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
				bPanel[x][y] = 0;
			}
        }
    	curX = 2; curY = 4;	    // カーソルの位置
		score = 0;
		nline.setFlag(true);
		gameSROW = true;
		acBGM.loop();
	}
	public void end(){
	    score += scoreTemp;
	    scoreTemp = 0;
	    RankingData.setScore(score);
	    swap.setFlag(false);
	    kill.setFlag(false);
		gameSROW = false;
		repaint();
		acBGM.stop();
	}
	
    public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D)g;
	    // フィールドを描く
	    // 背景を描く
		Image img = getToolkit().getImage(bgPlay);
		if(gameSROW == false){
			img = getToolkit().getImage(bgOver);
		}
		g.drawImage(img,-100,-100,this);
		// マス枠を描画する
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
				// bPanelの表示
				g2.setColor(Color.GREEN);
				drawPanel(g2, bPanel[x][y], x*GSW+1, y*GSH);
				// kPanelの表示
				g2.setColor(Color.ORANGE);
				drawPanel(g2, kPanel[x][y], x*GSW+1, y*GSH);
				// fPanelの表示(ずらして表示)
				if(fPanel[x][y] != 0){
					g2.setColor(Color.GREEN);
					drawPanel(g2, fPanel[x][y], x*GSW+1-5, y*GSH-5);
					g2.setStroke(wideStroke1);
	                g2.setColor(Color.BLACK);
	                g2.drawRect(x*GSW+1-5, y*GSH-5, GSW, GSH);
					g2.setStroke(wideStroke2);
				}
	        }
	    }
		// 入れ替え中のパネルを描く
        if(swap.flag){
			s1X = curX*GSW+1; s1Y = curY*GSH; 	    // 移動パネルの位置
			s2X = curX*GSW+1; s2Y = curY*GSH+GSH;	// 移動パネルの位置

			g.setColor(Color.PINK);
			g.fillRect(s1X+1, s1Y, GSW, GSH);
			g.fillRect(s2X+1, s2Y, GSW, GSH);

	        g2.setColor(Color.GREEN);
			drawPanel(g2, bPanel[curX][curY+1], s1X+1, s1Y+(int)(GSH*swap.getRate()));
			drawPanel(g2, bPanel[curX][curY], s2X+1, s2Y-(int)(GSH*swap.getRate()));
		}

        // カーソルを描く
        g2.setColor(Color.RED);
		g2.setStroke(wideStroke2);
        g2.drawRect(curX*GSW+1, curY*GSH, GSW, 2*GSH);
        
        // 得点を表示
        if(scoreTemp != 0){
			g2.setColor(Color.RED);
			f=new Font("TimesRoman",Font.PLAIN,20);
			g2.setFont(f);
			int dispX = 0, dispY = 0;
		    for (int y = 0; y < ROW; y++) {
		        for (int x = 0; x < COL; x++) {
			    	if(cFlag[x][y] == 1){
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
   	    // killPanelをもとに戻す．
	    for (int x = 0; x < COL; x++) {
		    for (int y = 0; y < ROW; y++) {
		    	if(kPanel[x][y] != 0){
		    		bPanel[x][y] = kPanel[x][y];
		    	}
			}
	    }
		// flagの初期化
        for (int x = 0; x < COL+1; x++) {
		    for (int y = 0; y < ROW+1; y++) {
		    	if((x<COL) & (y<ROW))
					cFlag[x][y] = 0;	// 定義外に数字を入れない
				if(x < COL){
					gFlag[x][y] = 0;
				}else{
					gFlag[x][y] = 1;	// 右端の格子のみフラグ立て
				}
			}
	    }
	    // 再帰関数の呼び出し
	    for (int y = 0; y < ROW+1; y++) {
	    	cF = 0;
	    	g_check(0,y,0,y);
	    }
		// 右から探索
		// flagの初期化
	    for (int y = 0; y < ROW+1; y++) {
			gFlag[COL][y] = 0;	// 右端の格子のみフラグおろし
	    }
	    // 再帰関数の呼び出し
	    for (int y = 0; y < ROW+1; y++) {
	    	cF = 0;
	    	g_check(COL,y,COL,y);
	    }
	    
	    // 消えるパネルをkillPanelへ移し，点数を数える
    	scoreTemp = 0;
	    for (int x = 0; x < COL; x++) {
		    for (int y = 0; y < ROW; y++) {
		    	if(cFlag[x][y] == 1){
		    		kPanel[x][y] = bPanel[x][y];
		    		bPanel[x][y] = 0;
		    		if(kill.flag != true){
			    		kill.flag = true;
			    		acConnect.play();
			    	}
		    		scoreTemp++;
		    	}
			}
	    }
    	scoreTemp = 100 * scoreTemp * (scoreTemp - 4);
	}

    
    // 再帰関数
    // 0:失敗, 未探索	1:成功		2:探索中
	public int g_check(int x, int y, int px, int py) {
		// now grid x, now grid y, previous grid x, and previous grid y
		int final_gF = 0;	// 現在の格子の最終的なフラグ
		// 未探索だったら判定を開始，そうでなければそれぞれリターン
		switch(gFlag[x][y]){
		case 0:
			gFlag[x][y] = 2;
			break;
		case 1:
			return 1;
		case 2:
			return cF;
		}
    	
		if(x != COL){	// 右方向チェック
			if(y != 0){		// 右上方向チェック
				if(bPanel[x][y-1] == sl & (px != x+1 | py != y-1))
					final_gF += cFlag[x][y-1] = g_check(x+1,y-1,x,y);
				if(bPanel[x][y-1] == ha & (px != x+1 | py != y))
					final_gF += cFlag[x][y-1] = g_check(x+1,y,x,y);
			}
			if(y != ROW){// 右下方向チェック
				if(bPanel[x][y] == vi & (px != x+1 | py != y))
					final_gF += cFlag[x][y] = g_check(x+1,y,x,y);
				if(bPanel[x][y] == bs & (px != x+1 | py != y+1))
					final_gF += cFlag[x][y] = g_check(x+1,y+1,x,y);
			}
		}
		if(x != 0){			// 左方向チェック
			if(y != 0){		// 左上方向チェック
				if(bPanel[x-1][y-1] == bs & (px != x-1 | py != y-1))
					final_gF += cFlag[x-1][y-1] = g_check(x-1,y-1,x,y);
				if(bPanel[x-1][y-1] == ha & (px != x-1 | py != y))
					final_gF += cFlag[x-1][y-1] = g_check(x-1,y,x,y);
			}
			if(y != ROW){// 左下方向チェック
				if(bPanel[x-1][y] == vi & (px != x-1 | py != y))
					final_gF += cFlag[x-1][y] = g_check(x-1,y,x,y);
				if(bPanel[x-1][y] == sl & (px != x-1 | py != y+1))
					final_gF += cFlag[x-1][y] = g_check(x-1,y+1,x,y);
			}
		}
		if(final_gF == 0){
			gFlag[x][y] = 0;
		}else{
			gFlag[x][y] = 1;
			cF = 1;
		}
		return gFlag[x][y];
	}
    
	public void keyPressed(KeyEvent e) {
		if(swap.flag != true && gameSROW){
			switch(e.getKeyCode()){
			case KeyEvent.VK_F2:	// F2キー
				f2 = true;
				break;
			case KeyEvent.VK_LEFT:	// 左キー
				if(curX > 0)	curX--;	break;
			case KeyEvent.VK_UP:	// 上キー
				if(curY > 0)	curY--;	break;
			case KeyEvent.VK_RIGHT:	// 右キー
				if(curX < COL-1)curX++;break;
			case KeyEvent.VK_DOWN:	// 下キー
				if(curY < ROW-2)curY++;break;
			case KeyEvent.VK_SPACE:	// スペースキー(入れ替え)
				swap.flag = true;
				acSwap.play();
				int buf1, buf2;
				
				// 入れ替え
				// パネルを取る
				if(kPanel[curX][curY] == 0){
					buf1 = bPanel[curX][curY];
				}else{
					buf1 = fPanel[curX][curY];
				}
				if(kPanel[curX][curY+1] == 0){
					buf2 = bPanel[curX][curY+1];
				}else{
					buf2 = fPanel[curX][curY+1];
				}
				// パネルを埋める
				if(kPanel[curX][curY] == 0){
					bPanel[curX][curY] = buf2;
				}else{
					fPanel[curX][curY] = buf2;
				}
				if(kPanel[curX][curY+1] == 0){
					bPanel[curX][curY+1] = buf1;
				}else{
					fPanel[curX][curY+1] = buf1;
				}
				// フラグチェック
			    checkFlags();
				break;
			case KeyEvent.VK_B:	// bキー(新しい列の追加)ちなみにvキーは86
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
		if(kill.flag != true){
			acLineUp.play();
			// 一番上にパネルが存在していればゲーム終了
	        for (int x = 0; x < COL; x++) {
				if(bPanel[x][0] != 0){
					gameSROW = false;
				}
	        }
	        for (int y = 0; y < ROW-1; y++) {// 一番下は除く
	            for (int x = 0; x < COL; x++) {
					bPanel[x][y] = bPanel[x][y+1];
	            }
	        }
	        // 一番下に新しい行を入れる．
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
					bPanel[x][ROW-1] = rnd.nextInt(4)+1;
				}else{
					bPanel[x][ROW-1] = 0;
				}
	        }
	        if(curY > 0){
	        	curY--;		// カーソルを一つ上げる．
			}
			
		    checkFlags();	// フラグチェック
	    }
		nline.setFlag(true);
	}
	
	// パズルパネルの時間を進める．
    public void tick() {
		swap.tickTime();
		kill.tickTime();
		nline.tickTime();
		
		if(kill.flag != true){
			for (int x = 0; x < COL; x++) {
			    for (int y = 0; y < ROW; y++) {
		    		kPanel[x][y] = 0;
		    		if(fPanel[x][y] != 0){
			    		bPanel[x][y] = fPanel[x][y];
			    		fPanel[x][y] = 0;
			    	}
				}
		    }
		    if(scoreTemp != 0){
			    score += scoreTemp;
			    scoreTemp = 0;
		    }
		    if(kill.getTime() > 5){
		    	acVanish.play();
		    }
		}
		if(nline.flag != true){
			panelUp();
		}
		repaint();
    }
}
