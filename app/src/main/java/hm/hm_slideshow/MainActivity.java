package hm.hm_slideshow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hm_slideShow slideShow = (Hm_slideShow) findViewById(R.id.slideShow);
        String[] urls = new String[]{"http://pic.z4bbs.com/forum/201411/18/002300pkmi8ym2rjyerhqm.jpg", "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1306/27/c3/22647769_1372328143774.jpg", "http://b.zol-img.com.cn/soft/5_800x600/500/ceuvpp29238CY.jpg"};
        slideShow.setUrls(urls);
        //slideShow.setImages();
    }
}
