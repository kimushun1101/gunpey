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

import java.io.*;
import java.util.*;


class RankingData{
	public static int getHiScore(){
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
