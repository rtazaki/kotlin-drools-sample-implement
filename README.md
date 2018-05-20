# kotlin-drools-sample-implement

## 元ネタ
https://qiita.com/keitaro_1020/items/0e052456d738983428ad  

### 追加した内容
- ランクごとの価格
- 季節ごとの係数

### 実施したいこと
- ランクごとの価格 * 係数を、「ルールを用いて」出力させたい。

### 出来たこと
- 季節ごとの係数をデシジョンテーブルから求める。

### 現状うまくいっていないこと
- data class OutputとApple, Seasonのからみがうまくいかない。  
(Apple, Seasonが処理されたら、Outputを発火させたい)  
- 出力のActionで、$o.setSp($1 * $2);として、$1, $2にそれぞれ  
apple.priceと、season.coefficientを入れたいのだが方法がわからない。
- テーブルをブックにして分割統治したかったが、2つ目のルールファイルを読み込むと  
一つ目のルールが適用されなくなった。  
(複雑なルールになった場合、ルールを分けるかも知れないので、できればやりたい。)

#### [参考]JBOSS RULES 5 リファレンスガイド
https://access.redhat.com/documentation/ja-jp/jboss_enterprise_soa_platform/5/html-single/jboss_rules_5_reference_guide/
