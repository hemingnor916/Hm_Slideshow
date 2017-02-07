# hm_slideshow
简单几个步骤，轻松实现Android轮播图。
说明：本控件原作者为ljuns，本人基于ljuns的作品进行简单的修改，从而达到‘更容易使用’的目的，仅此而已。关于此控件的不足之处以及优化方案，欢迎大家献言献策。

# 使用
        1.添加引用 compile 'com.squareup.picasso:picasso:2.3.2' ，用于显示网络图片
        2.将Hm_slideShow.java复制项目中。
        3.在xml中引用：
        <hm.hm_slideshow.Hm_slideShow
        android:id="@+id/slideShow"
        android:layout_width="match_parent"
        android:layout_height="180dp"></hm.hm_slideshow.Hm_slideShow>
        4.在Java文件中设置图片以及处理点击事件：
        Hm_slideShow slideShow = (Hm_slideShow) findViewById(R.id.slideShow);
        String[] urls = new String[]{"http://pic.z4bbs.com/forum/201411/18/002300pkmi8ym2rjyerhqm.jpg", "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1306/27/c3/22647769_1372328143774.jpg", "http://b.zol-img.com.cn/soft/5_800x600/500/ceuvpp29238CY.jpg"};
        slideShow.setUrls(urls);
        //slideShow.setImages();
        slideShow.setOnItemClickListener();


# Steps:
 1. compile 'com.squareup.picasso:picasso:2.3.2'
 2. Copy this file to your project and 'Rebuild' your project.
 3. Add this code to your layout xml ： 
       <hm.hm_slideshow.Hm_slideShow
        android:id="@+id/slideShow"
        android:layout_width="match_parent"
        android:layout_height="180dp"></hm.hm_slideshow.Hm_slideShow>
 4. Use Hm_slideShow.setUrls() or Hm_slideShow.setImages() method show images.
 
 ps:求英文版readme



# 声明 declaration ：
 * 原作者Author:ljuns
 * 出处Origin：https://ljuns.github.io/2016/11/21/CycleRotationView-%E8%87%AA%E5%AE%9A%E4%B9%89%E6%8E%A7%E4%BB%B6%E4%B9%8B%E8%BD%AE%E6%92%AD%E5%9B%BE
 * 修改者Mender: hemingyang
 
#Contact me
Email: he.mingyang@qq.com
