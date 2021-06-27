# Overview
This application is a sample project that introduced Flexbox Layout in the following repository.<br>
https://github.com/codinginflow/ImageSearchApp<br>
simple explanation, `FlexboxLayout` is a `LinearLayout` with wrapping back function.<br>

# readme
[日本語](https://github.com/LeoAndo/ImageSearchApp/blob/main/readme/README_jp.md)

# Usage

1. get API KEY<br>
https://unsplash.com/developers

2. Set API Key
```groovy
buildConfigField("String", "UNSPLASH_ACCESS_KEY", "APIKEY")
```
3. run app

# Screentshot Pixcel 4 OS 10

## Vertical
<img src="capture.gif" width=320 />

## Horizontal
<img src="https://user-images.githubusercontent.com/16476224/123540744-8529f200-d77b-11eb-9e54-168e18dda900.png" width=320 />

# use Libraries

- Navigation Component
- DI: Dagger Hilt
- Network: Okhttp + Retrofit + GSON
- Glide
- Paging: Paging 3 + Flow
- UI: Material Component
- FlexboxLayout


# reference
https://github.com/google/flexbox-layout
https://android-developers.googleblog.com/2017/02/build-flexible-layouts-with.html
https://github.com/codinginflow/ImageSearchApp
