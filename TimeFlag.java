class TimeFlag{
	private boolean flag;
	private int time;
	private int MaxTime;
	
	public TimeFlag(boolean f, int t, int m){
		flag = f;	time = t;	MaxTime = m;
	}
	
	// 時間でフラグを下すためのメソッド
	// trueで時間加算，Maxまで行ったらfalse;
	public void tickTime(){
		if(flag) {
			if(time < MaxTime){
				time += 5;
			}else{
				flag = false;
			}
		}else{
			resetTime();
		}
	}

	public void resetTime(){
		time = 0;
	}

	public int getTime(){
		return time;
	}

	public void setFlag(boolean TorF){
		flag = TorF;
		if(TorF){
			time = 0;
		}
	}

	public boolean getFlag(){
		return flag;
	}


	public double getRate(){
		return (double)time / (double)MaxTime;
	}

}

