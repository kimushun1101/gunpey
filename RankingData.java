import java.io.*;
import java.util.*;

class RankingData{
	public static int getHighScore(){
		ArrayList<Integer> al = new ArrayList<Integer>();
        try{
            File file = new File("ranking" + File.separator + "score.txt");
    		// ファイルの読み込み
            if (checkBeforeReadfile(file)){
                BufferedReader br 
                    = new BufferedReader(new FileReader(file));

                String str;
                while((str = br.readLine()) != null){
					al.add(Integer.valueOf(str));
                }
                br.close();
            }else{
                System.out.println("ファイルが見つからないか開けません");
            }
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        // Score を降順にする
        Collections.sort(al);
		Collections.reverse(al);
        // 一番上のScore を取り出す
		return al.get(0);
	}

	// Score を入れてHigh Score を返す．
	public static int setScore(int score){
		ArrayList<Integer> al = new ArrayList<Integer>();
        try{
            File file = new File("ranking" + File.separator + "score.txt");
            if(score != 0){
            	al.add(score);
            }
		// ファイルの読み込み
            if (checkBeforeReadfile(file)){
                BufferedReader br 
                    = new BufferedReader(new FileReader(file));

                String str;
                while((str = br.readLine()) != null){
					al.add(Integer.valueOf(str));
                }
                br.close();
            }else{
                System.out.println("ファイルが見つからないか開けません");
            }
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        Collections.sort(al);
		Collections.reverse(al);
		
		// ファイルの書き込み
        try{
            File file1 = new File("ranking" + File.separator + "score.txt");

            if (checkBeforeWritefile(file1)){
                PrintWriter pw 
                  = new PrintWriter(new BufferedWriter(new FileWriter(file1)));
				for ( int i = 0; i < al.size(); ++i ) {
					pw.println( al.get( i ) );
				}
                pw.close();
            }else{
                System.out.println("ファイルに書き込めません");
            }
        }catch(IOException e){
            System.out.println(e);
        }
		return al.get(0);
	}

    private static boolean checkBeforeReadfile(File file){
        if (file.exists()){
            if (file.isFile() && file.canRead()){
                return true;
            }
        }
        return false;
    }
    
    private static boolean checkBeforeWritefile(File file){
        if (file.exists()){
            if (file.isFile() && file.canWrite()){
                return true;
            }
        }
        return false;
    }
}
