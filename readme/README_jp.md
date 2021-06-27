# Overview
このアプリケーションは、次のリポジトリにFlexboxLayoutを導入したサンプルプロジェクトです。<br>
https://github.com/codinginflow/ImageSearchApp<br>

# Usage

1. unsplashのAPI KEYを取得する
https://unsplash.com/developers

2. 手順１で取得したAPI Keyをセットする
```groovy
buildConfigField("String", "UNSPLASH_ACCESS_KEY", "APIKEY")
```
3. アプリを実行する

# Screentshot Pixcel 4 OS 10

## 縦向き
<img src="capture.gif" width=320 />

## 横向き
<img src="https://user-images.githubusercontent.com/16476224/123540744-8529f200-d77b-11eb-9e54-168e18dda900.png" width=320 />

# 使用ライブラリ

- Navigation Component
- DI: Dagger Hilt
- Network: Okhttp + Retrofit + GSON
- Glide
- Paging: Paging 3 + Flow
- UI: Material Component
- FlexboxLayout


# 参考
https://github.com/google/flexbox-layout
https://developers-jp.googleblog.com/2017/03/build-flexible-layouts-with.html
https://github.com/codinginflow/ImageSearchApp