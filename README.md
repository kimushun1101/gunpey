# Gunpey

Java のSwing でGUI アプリケーションを作りました。

![Playing Image](imgs/playingImg.png)

## プレイ方法

### Java のインストール

[こちら](https://www.java.com/ja/download/help/download_options.html)をご参照ください。
端的にいえば[ここ](https://www.java.com/ja/download/manual.jsp)からダウンロードできます。

Ubuntu などapt を使用できる場合にはターミナルでインストールすると良いでしょう。
```
$ sudo apt update
$ sudo apt install default-jdk -y
```

### 実行

Gunpey.jar をダブルクリック、もしくはターミナルで
```
$ java -jar Gunpey.jar
```
とすることで実行できます。

## プログラムの改変とコンパイル

### コンパイル方法

コンパイルするためにはJava の種類をjre ではなくjdk でインストールしてください。
お好きなエディタで編集してしてから以下で実行。
```
$ javac -encoding UTF-8 *.java
$ java Gunpey
```
jar ファイルを作成したい場合には
```
$ jar cfe Gunpey.jar Gunpey *.class ./sounds/*.wav
```
### 音源の追加

sounds ディレクトリを作成して以下のファイルを追加してください。
ゲームBGM はマルチプラットフォームでテストできておりません。
wav に関しては[魔王魂様の効果音](https://maou.audio/category/se/) を使わせていただきテストしました。
.gitignore で設定してGithub 上にはアップデートを控えさせていただいております。二次配布可能な音源などありましたら教えていただけますと幸いです。

- bgm.mid : ゲームBGM
- swap.wav : パネル入れ替え時の音
- connect.wav : ライン結合時の音
- vanish.wav : ライン消去時の音
- panelup.wav : パネルせり上がり時の音


## プログラムの中身について

### パネル集合の種類と表示の仕方 

基本的なパネル normalPanel、消滅中のパネル vanishPanel、消滅中のパネルの上にあるパネル floatingPanel の３種類をパネルの集合として用意しました。
normalPanel を緑色で表示、vanishPanel を黄色で表示、floatingPanel は緑色で位置をずらして表示します。


### 時間の管理 

タイマを管理するクラス(TimeFlag.java)を作って大量生産しました。
設定時間が経過するとTimeFlag のprivate変数であるflagがtrueになります。
- ゲーム自体の制限時間のタイマgame Timer (tGame) 
- パネルがつながれてから消えるまでのタイマ vanish Timer (vTimer) 
- パネルの入れ替え時間のタイマ swap Timer (sTimer) 
- 新しい列が出るまでの時間のタイマ panel up Timer (panelupTimer) 


### 消える判定のためのフラグ 

ラインがつながっているかどうかを判定するために、パネルの状態panelFlag とパネルの頂点の状態gridFlag を用意して再帰関数で処理しています。
左端から右へ伝搬させる処理と右端から左へ伝搬させる処理を両方行い、折り返して接続されているようなラインに対しても正確に判定します。

### 既知の課題

プログラムが複雑になりそうでしたので以下を放置してます。

- Help のURL リンクの対応。
- 音出力がApplet で出力されている（現在は非推奨）。

## 参考

- [グンペイホームページ](http://www.bandaigames.channel.or.jp/list/gunpey/ )
：おそらくここが本家ページです。
- [ウィキペディア](http://ja.wikipedia.org/wiki/GUNPEY)
：噛み砕いたルール説明なども記載してあります。
- [gunpey maniax](http://www.asahi-net.or.jp/~VG5M-OBT/gunpey_maniax.html )
：得点計算に利用させていただきました。

### ライセンス等

ソースコードはMIT License としました。
画像は[Pixabay](https://pixabay.com/ja/service/license/) で検索したものを使用させていただいております。
アイコンは公式を模倣した私の自作です。