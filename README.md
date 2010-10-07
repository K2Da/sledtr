#sledtr
rssのurlタグとか、2chの特定板のキーワードがタイトルに入っているスレッドをまとめてkindle用のフォーマット(.mobi)に変換するものです。

つくりかけです。

##使い方
    java -jar sledtr_2.8.0-0.1.min.jar c:/work/WorkSpace/config.yaml
とか。

yamlファイルは、
    workdir   : c:/work/tdwork
    kindlegen : c:/Program Files (x86)/kindlegen/kindlegen.exe
の二つは変えないと動かないと思います。

###RSSフィード
###2ch

##コンパイル
sbt使います。

##ライセンス
GPLv3です。
Copyright (C) 2010 津田K2Da