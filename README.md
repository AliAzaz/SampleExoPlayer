# How to deal with ExoPlayer 📹
 - ExoPlayer is an application level media player for Android. 


> Get the description and implementation of ExoPlayer from **[HERE](https://medium.com/@ali.azaz.alam/how-to-deal-with-exoplayer-67e528e2cbcc)**

## Getting Started
Firstly, work in project.gradle file:

```sh
allprojects {
     repositories {
         // Add these lines
         google()
         jcenter()
     }
 }
 ```
 
Secondly, enable JAVA_8 support in app.gradle file:

```sh
android {

     /*Add Compile options in following block*/
     compileOptions {
         sourceCompatibility JavaVersion.VERSION_1_8
         targetCompatibility JavaVersion.VERSION_1_8
     }
 
 }
 ```
 
Thirdly, implement dependency in app.gradle:

```sh
implementation 'com.google.android.exoplayer:exoplayer:2.9.6'
```

Congoo!!👌 we done the basis settings of ExoPlayer.

You can find the implementation from:
  - Medium article: **[How to deal with ExoPlayer](https://medium.com/@ali.azaz.alam/how-to-deal-with-exoplayer-67e528e2cbcc)**
