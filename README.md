# MediaSlider

[![Language](https://img.shields.io/badge/language-Java-orange.svg?style=flat)]()

## Example
![](/images/example.gif)

Gradle dependency  ```build.gradle```

```
dependencies {
    compile 'com.android.support:support-v4:26.1.0'
    compile 'com.neo1125:mediaslider:0.1.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.google.android.exoplayer:exoplayer:r2.4.0'
}
```

## Example Usage:

activity_layout.xml :
```
<com.neo1125.mediaslider.MediaSliderView
        android:id="@+id/mediaSliderView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
```

set image :
```
List<Media> medias = new ArrayList<>();
medias.add(new Media("ios", "https://www.apple.com/kr/ios/images/og.png?201709110842"));
medias.add(new Media("kotlin", "https://kotlinlang.org/assets/images/twitter-card/kotlin_800x320.png"));

```

set video : 
```
medias.add(new Media(Media.Type.video, "1", Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/test.mp4"));
```

```
mediaSliderView = findViewById(R.id.mediaSliderView);
mediaSliderView.setAdapter(new MediaSliderAdapter() {
    @Override
    public int getCount() {
        return medias.size();
    }

    @Override
    public Media instantiateMedia(int position) {
        return medias.get(position);
    }
});
mediaSliderView.setDuration(3000);
mediaSliderView.start();

```