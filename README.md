# JavaEP

## 概要

JavaEPは大学でのJavaプログラミング講義の演習を支援するためのWebアプリケーションです。

JavaEP v2は、[JavaEP](https://sourceforge.net/projects/javaep/)のバージョン2.0です。

## 環境構築

### Eclipseのインストール

[Eclipse](https://eclipse.org/downloads/)よりEclipse IDE for Java EE Developersを環境に応じてインストール。

### Eclipseへのインポート

Package Explorer右クリック -> [Import]よりjavaep-v2をインポート。

### Tomcatのインストール

[Apache Tomcat 8](https://tomcat.apache.org/download-80.cgi)より環境に応じて
インストール。

Eclipseの[Preferences] -> [Server] -> [Runtime Environments]においてインストールしたTomcat 8を追加。

### MySQLのインストール

[MySQL](https://dev.mysql.com/downloads/mysql/)より環境に応じてインストール。

my.cnf（my.ini）で文字コードをUTF-8に設定する。

```
[mysqld]
character-set-server=utf8

[client]
default-character-set=utf8
```

MySQL Serverを起動し、db/dbsetting.sqlを実行する。

```
mysql -u [user] -p < db/dbsetting.sql
```

### logdir, tmpdirの設定

src/main/resources/javaep.propertiesよりjavaep.logdir, lavaep.tmpdirを設定する。

設定したディレクトリを作成する。

### 実行

プロジェクトを右クリック -> [Run As] -> [Run on Server]でTomcat 8を選択して実行。

###  ログイン

[JavaEP](http://localhost:8080/JavaEP/)にアクセスしてログイン。
- ID: admin
- PW: admin

### 設定ファイルの変更を無視する

git update-index --skip-worktree src/main/resources/javaep.properties
