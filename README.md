# Gunpey

## プレイ方法

### Java のインストール

[こちら](https://www.java.com/ja/download/help/download_options.html)をご参照ください。
端的にいえば、[ここ](https://www.java.com/ja/download/manual.jsp)からダウンロードできます。

Ubuntu などapt を使用できる場合にはターミナルで以下でもインストールできます。
```
sudo apt update
sudo apt install default-jdk -y
```
### 実行

Windows であればGunpey.jar をダブルクリック、
Linux であればターミナルで`java -jar Gunpey.jar` と実行できます。

## 変数など

### パネル集合の種類と表示の仕方 

基本的なパネルbasic Panel (bPanel)、消滅中のパネルkill Panel (kPanel)、消滅中のパネルの上にあるパネルfloat Panel(fPanel)の３種類をパネルの集合として用意しました。
bPanelのパネルを緑色で表示、kPanelのパネルを黄色で表示、fPanelのパネルは緑色で位置をずらして表示します。


### 時間の管理 

タイマを管理するクラス(TimeFlag.java)を作って大量生産しました。
設定時間の間のみTimeFlagのprivate変数であるflagがtrueになります。
- ゲーム自体の制限時間のタイマgame Time (gTime) 
- パネルがつながれてから消えるまでのタイマkill Time (kTime) 
- パネルの入れ替え時間のタイマswap Time (sTime) 
- 新しい列が出るまでの時間のタイマnew line Time (nlTime) 


### 消える判定のためのフラグ 

ラインがつながっているかどうかを判定するために、マスと格子のそれぞれにフラグの配列を用意しました。
cell Flag (cFlag)とgrid Flag (gFlag) の座標設計を図に示します。
図では6行5列で示していますが、実際のゲームでは10行5列まであります。
cFlagは10行5列、gFlagはそれより1つずつ多く11行6列用意します。


## 参考

[グンペイホームページ](http://www.bandaigames.channel.or.jp/list/gunpey/ )

[ウィキペディア](http://ja.wikipedia.org/wiki/GUNPEY)
：噛み砕いたルール説明など

[gunpey maniax](http://www.asahi-net.or.jp/~VG5M-OBT/gunpey_maniax.html )
：得点計算に利用させていただきました。