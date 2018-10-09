package hm.hm_slideshow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author:ljuns
 * Origin：https://ljuns.github.io/2016/11/21/CycleRotationView-%E8%87%AA%E5%AE%9A%E4%B9%89%E6%8E%A7%E4%BB%B6%E4%B9%8B%E8%BD%AE%E6%92%AD%E5%9B%BE
 * Mender: hemingyang
 * Whats different: easier to use
 * <p>
 * Step:
 * 1. compile 'com.squareup.picasso:picasso:2.3.2'
 * 2. Copy this file to your project and 'Rebuild' your project.
 * 3. Add this code to your layout xml :"<yourPackageName.Hm_slideShow></yourPackageName.Hm_slideShow>"
 * 4. Use Hm_slideShow.setUrls() or Hm_slideShow.setImages() method show images.
 */

public class Hm_slideShow extends FrameLayout {
    private int color_indicator_normal = Color.parseColor("#66000000");//小圆点
    private int color_indicator_selected = Color.parseColor("#ffffff");//小圆点
    private Drawable drawable_indicator_normal;//小圆点
    private Drawable drawable_indicator_selected;//小圆点
    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mPointGroup;//小圆点的父布局
    private List<ImageView> mList; // 资源集合
    private Handler mHandler;
    private int pointSize = 20; // 小圆点的大小，默认为20dp
    private int pointMargin = 20; // 与前面一个小圆点的距离，默认为20dp
    private long lastPageChangeTime = 0;
    private OnItemClickListener mListener;
    private int pageDuration = 3000;

    public Hm_slideShow(Context context) {
        super(context);
    }

    public Hm_slideShow(Context context, AttributeSet attrs) {
        super(context, attrs);

        initIndicatorColor();
        this.mContext = context;
        mHandler = new Handler();
        mList = new ArrayList();
        initView(mContext);
    }

    public int getColor_indicator_normal() {
        return color_indicator_normal;
    }

    public void setColor_indicator_normal(int color_indicator_normal) {
        this.color_indicator_normal = color_indicator_normal;
        initIndicatorColor();
    }

    public int getColor_indicator_selected() {
        return color_indicator_selected;
    }

    public void setColor_indicator_selected(int color_indicator_selected) {
        this.color_indicator_selected = color_indicator_selected;
        initIndicatorColor();
    }

    private void initIndicatorColor() {
        drawable_indicator_normal = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint p = new Paint();
                p.setColor(color_indicator_normal);
                canvas.drawCircle(pointSize / 2, pointSize / 2, pointSize / 2, p);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
        drawable_indicator_selected = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint p = new Paint();
                p.setColor(color_indicator_selected);
                canvas.drawCircle(pointSize / 2, pointSize / 2, pointSize / 2, p);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
    }

    /**
     * 初始化布局
     *
     * @param mContext
     */
    private void initView(Context mContext) {
        RelativeLayout relative = new RelativeLayout(mContext);
        ViewPager pager = new ViewPager(mContext);
        LinearLayout linear = new LinearLayout(mContext);

        //初始化父布局
        RelativeLayout.LayoutParams params_relatvie = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relative.setLayoutParams(params_relatvie);


        //初始化pager
        ViewGroup.LayoutParams params_pager = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pager.setLayoutParams(params_pager);

        //初始化游标linear
        LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linear.setLayoutParams(params_linear);
        linear.setOrientation(LinearLayout.HORIZONTAL);

        relative.addView(pager);

        RelativeLayout.LayoutParams params_toParent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_toParent.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//在底部
        params_toParent.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//在右边
        params_toParent.rightMargin = pointMargin;
        params_toParent.bottomMargin = pointMargin;
        relative.addView(linear, params_toParent);

        mViewPager = pager;
        mPointGroup = linear;

        addView(relative);
    }

    /**
     * 设置图片的URL
     *
     * @param urls：图片的URL
     */
    public void setUrls(String[] urls) {
        if (mPointGroup != null) {
            mPointGroup.removeAllViews();
        }
        mList = new ArrayList<>();
        // 数据集合为空时隐藏当前布局
        if (urls == null || urls.length == 0) {
            this.setVisibility(GONE);
            return;
        }
        for (int i = 0; i < urls.length; i++) {
            // 创建 ImageView，并设置图片
            ImageView img = new ImageView(mContext);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(mContext).load(urls[i]).into(img);
            mList.add(img);
            makePoints(i);
        }

        setUpWithAdapter(); // 设置 Adapter
        timerTask(); // 定时任务
    }

    /**
     * 设置图片资源
     *
     * @param images：图片的集合
     */
    public void setImages(int[] images) {
        if (mPointGroup != null) {
            mPointGroup.removeAllViews();
        }
        mList = new ArrayList<>();
        // 数据集合为空时隐藏当前布局
        if (images == null || images.length == 0) {
            this.setVisibility(GONE);
            return;
        }
        for (int i = 0; i < images.length; i++) {
            // 创建 ImageView，并设置图片
            ImageView img = new ImageView(mContext);
            img.setImageResource(images[i]);
            mList.add(img);

            makePoints(i); // 创建小圆点
        }

        setUpWithAdapter(); // 设置 Adapter
        timerTask(); // 定时任务
    }

    /**
     * 创建小圆点
     *
     * @param i
     */
    private void makePoints(int i) {


        // 创建小圆点，实质也是 ImageView
        ImageView point = new ImageView(mContext);

        // 小圆点布局参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pointSize, pointSize);

        // 第2个起才设置左边距
        if (i > 0) {
            params.leftMargin = pointMargin;
            point.setImageDrawable(drawable_indicator_normal);
        } else {
            point.setImageDrawable(drawable_indicator_selected);
        }

        point.setLayoutParams(params); // 给小圆点设置参数
        mPointGroup.addView(point); // 把小圆点添加到容器
    }

    /**
     * 与 Adapter 关联
     */
    private void setUpWithAdapter() {
        mViewPager.setAdapter(new CycleAdapter());

        // ViewPager 的监听事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                lastPageChangeTime = new Date().getTime();
            }

            @Override
            public void onPageSelected(int position) {
                position = position % mList.size();
                // 设置当前圆点选中
                ImageView iv_xuanzhong = (ImageView) mPointGroup.getChildAt(position);
                iv_xuanzhong.setImageDrawable(drawable_indicator_selected);
                // 设置前一个圆点不选中
                ImageView iv_qianyige = (ImageView) mPointGroup.getChildAt(lastPosition);
                iv_qianyige.setImageDrawable(drawable_indicator_normal);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public int getPageDuration() {
        return pageDuration;
    }

    public void setPageDuration(int pageDuration) {
        this.pageDuration = pageDuration;
    }

    /**
     * 定时任务
     */
    private void timerTask() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long pageStayeddTime = new Date().getTime() - lastPageChangeTime;
                if (pageStayeddTime > pageDuration * 0.9) {
                    // 当前选中的 item
                    int currentItem = mViewPager.getCurrentItem();
                    // 判断是否是最后一个 item
                    if (currentItem == mViewPager.getAdapter().getCount() - 1) {
                        mViewPager.setCurrentItem(1);
                    } else {
                        mViewPager.setCurrentItem(currentItem + 1);
                    }
                }
                // 不断给自己发消息
                mHandler.postDelayed(this, pageDuration);
            }
        }, pageDuration);
    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置小圆点的大小
     * 默认为20dp，如果需要修改，则在 setImages() 方法前调用
     *
     * @param size：小圆点大小
     */
    public void setPointSize(int size) {
        this.pointSize = dp2px(size);
    }

    /**
     * 设置小圆点的左边距
     * 默认为20dp，如果需要修改，则在 setImages() 方法前调用
     *
     * @param margin：距离
     */
    public void setPointMargin(int margin) {
        this.pointMargin = dp2px(margin);
    }

    /**
     * 将 dp 转换成 px
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 适配器
     */
    class CycleAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mList.size();
            final View child = mList.get(position);
            if (child.getParent() != null) {
                container.removeView(child);
            }

            // 点击事件
            if (mListener != null) {
                final int finalPosition = position;
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClick(child, finalPosition);
                    }
                });
            }
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            position = position % mList.size();
//            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}

