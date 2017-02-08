package com.linsaya.parallaxlistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/2/9.
 */

public class ParallaxListView extends ListView {

    private int imageHeight;
    private ImageView imageView;
    private int maxHeight;
    private int originHeight;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setImageView(final ImageView imageView) {
        this.imageView = imageView;
        //获取设置给imageview的图片实际高度
        imageHeight = imageView.getDrawable().getIntrinsicHeight();
        //此处需要在layout后获取imageview的高度
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                originHeight = imageView.getHeight();
                //当图片高度比imageview的高度小时，最大的高度为imageview控件高度的2倍，否则为图片的高度
                maxHeight = (originHeight > imageHeight ? originHeight * 2 : imageHeight);
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    /**
     * 此方法在ListView滑动到顶部或底部时调用
     *
     * @param deltaX         超出X轴边界后的滑动距离，正值为右边到头，负值为左边到头
     * @param deltaY         超出Y轴边界后的滑动距离，正值为底边到头，负值为顶部到头
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX 超出X轴边界后的最大滑动距离
     * @param maxOverScrollY 超出Y轴边界后的最大滑动距离
     * @param isTouchEvent   ture表示用手指滑动，flase表示fling滑动
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        if (imageView != null) {

            //新的高度为，控件高度加上位移长度（此处除以3表示加大滑动难度）
            int newHeight = imageView.getHeight() - deltaY / 3;
            //进行逻辑判断，当新高度大于允许的最大高度时，不能继续增大，负值为最大高度
            if (newHeight > maxHeight) newHeight = maxHeight;
            //设置图片的高度属性
            imageView.getLayoutParams().height = newHeight;
            imageView.requestLayout();
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //当手指抬起时，执行属性动画，将imageview的高度恢复为原始高度
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            com.nineoldandroids.animation.ValueAnimator animator = com.nineoldandroids.animation.ValueAnimator.ofInt(imageView.getHeight(), originHeight);
            animator.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
                    int value = (int) valueAnimator.getAnimatedValue();
                    imageView.getLayoutParams().height = value;
                    imageView.requestLayout();
                }
            });
            animator.setInterpolator(new OvershootInterpolator());
            animator.setDuration(350);
            animator.start();
        }
        return super.onTouchEvent(ev);
    }
}
