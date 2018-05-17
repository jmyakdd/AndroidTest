package lcq.com.androidtest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by CRTE-CD-13 on 2018/5/17.
 */

public class MyScollorView extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {
    private Scroller scroller;
    private int mearsureHeight;
    private int height;
    private VelocityTracker velocityTracker;

    public MyScollorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int count = getChildCount();
        int preHeight = 0;
        for (int m = 0; m < count; m++) {
            View children = getChildAt(m);
            int cHeight = children.getMeasuredHeight();
            if (children.getVisibility() != View.GONE) {
                children.layout(i, preHeight, i2, preHeight += cHeight);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mearsureHeight = 0;
        height = getMeasuredHeight();
        int count = getChildCount();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
                mearsureHeight += view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            }
            setMeasuredDimension(getMeasuredWidth(), mearsureHeight);
        } else {
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                mearsureHeight += view.getMeasuredHeight();
            }
        }
    }

    private float downY;
    private final int MAX = 200;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                }
                scroller.forceFinished(true);
                velocityTracker.clear();
                velocityTracker.addMovement(event);
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                int move = (int) (downY - y);
                if (move < 0) {
                    if (getScaleY() > -MAX) {
                        if (getScaleY() < 0) {
                            scrollBy(0, move * 1 / 4);
                            downY = y;
                        } else {
                            scrollBy(0, move);
                            downY = y;
                        }
                    }
                } else if (move > 0) {
                    if (getScaleY() < mearsureHeight - height + MAX) {
                        if (getScaleY() > mearsureHeight - height) {
                            scrollBy(0, move * 1 / 4);
                            downY = y;
                        } else {
                            scrollBy(0, move);
                            downY = y;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getScaleY() > mearsureHeight - height || getScaleY() < 0) {
                    scrollReset();
                } else {
                    scrollFling();
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }


    /**
     * 滚动复位
     */
    private void scrollReset() {
        int scrollY = getScrollY();
        if (scrollY < 0) {
            scroller.startScroll(0, scrollY, 0, -scrollY);
            invalidate();
        } else {
            scroller.startScroll(0, scrollY, 0, -(scrollY - (mearsureHeight - height)));
            invalidate();
        }
    }

    /**
     * 惯性滚动
     */
    private void scrollFling() {
        velocityTracker.computeCurrentVelocity(1000);
        float yVelocity = velocityTracker.getYVelocity();
        /**
         * fling 方法参数注解
         *
         * startX 滚动起始点X坐标
         * startY 滚动起始点Y坐标
         * velocityX   当滑动屏幕时X方向初速度，以每秒像素数计算
         * velocityY   当滑动屏幕时Y方向初速度，以每秒像素数计算
         * minX    X方向的最小值，scroller不会滚过此点。
         *　maxX    X方向的最大值，scroller不会滚过此点。
         *　minY    Y方向的最小值，scroller不会滚过此点。
         *　maxY    Y方向的最大值，scroller不会滚过此点。
         */
        scroller.fling(0, getScrollY(), 0, (int) -yVelocity * 2, 0, 0, 0, mearsureHeight - height);
        invalidate();
    }
}
