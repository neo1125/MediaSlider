package com.neo1125.sample;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neo1125.mediaslider.Media;
import com.neo1125.mediaslider.MediaSliderAdapter;
import com.neo1125.mediaslider.MediaSliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Media> medias = new ArrayList<>();
    MediaSliderView mediaSliderView;
    boolean isSlide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medias.add(new Media("8", "https://static.pexels.com/photos/235621/pexels-photo-235621.jpeg"));
        medias.add(new Media(Media.Type.video,"10", "https://pmdvod.nationalgeographic.com/NG_Video/886/551/0000015a-8b94-d395-afdb-af9f1d3b0000-sunset-beach-olympic-national-park__698886.mp4"));
        medias.add(new Media("7", "https://erinewart.com/wp-content/uploads/2016/06/summer-01.jpg"));
        medias.add(new Media(Media.Type.video,"10", "https://www.smalley.com/sites/default/files/u49/SpringsAngleYellowSmall.mp4"));
        medias.add(new Media("1", "https://devimages-cdn.apple.com/assets/elements/icons/swift/swift-64x64.png"));
        medias.add(new Media("2", "https://yt3.ggpht.com/-tUnSh4hL1b0/AAAAAAAAAAI/AAAAAAAAAAA/AIy5-05CyFk/s900-c-k-no-mo-rj-c0xffffff/photo.jpg"));
        medias.add(new Media("3", "https://www.apple.com/kr/ios/images/og.png?201709110842"));
        medias.add(new Media("4", "https://kotlinlang.org/assets/images/twitter-card/kotlin_800x320.png"));
        medias.add(new Media("5", "http://diversecity.dk/wp-content/uploads/2016/03/spring-daffodils_2845661b.jpg"));
        medias.add(new Media("9", "https://az616578.vo.msecnd.net/files/2016/09/10/636090695124499947223829352_fall-012.jpg"));

        // drawable image set
        Resources res = getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + res.getResourcePackageName(R.drawable.daily_image) + "/"
                + res.getResourceTypeName(R.drawable.daily_image) + "/"
                + res.getResourceEntryName(R.drawable.daily_image));
        medias.add(new Media("6", uri.toString()));

        // sdcard mp4 file set
        //medias.add(new Media(Media.Type.video, "1", Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/test.mp4"));

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
//        mediaSliderView.setEventListner(new MediaSliderEventListner() {
//            @Override
//            public void changedState(Media.Type type, MediaSliderView.State state) {
//                if (type == Media.Type.video && state == MediaSliderView.State.end) {
//                    mediaSliderView.next();
//                }
//            }
//        });

        mediaSliderView.setDuration(3000);
//        mediaSliderView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("debug", "########## touch");
//                isSlide = !isSlide;
//                if (isSlide)
//                    mediaSliderView.resume();
//                else
//                    mediaSliderView.pause();
//                return false;
//            }
//        });
        mediaSliderView.start();
    }
}
