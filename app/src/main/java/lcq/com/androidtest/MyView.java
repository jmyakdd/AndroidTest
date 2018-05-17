package lcq.com.androidtest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by CRTE-CD-13 on 2018/5/16.
 */

public class MyView extends View implements View.OnTouchListener {
    private static final String TAG = "MyView";
    private Context context;
    private GestureDetector gestureDetector;

    public MyView(Context context) {
        super(context);
        initData(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    private void initData(Context context) {
        this.context = context;
        super.setOnTouchListener(this);
        super.setClickable(true);
        super.setLongClickable(true);
        super.setFocusable(true);
        gestureDetector = new GestureDetector(context, new MyGestureDetector());
    }

    private void log(String msg) {
        Log.e(TAG, msg);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    class MyGestureDetector implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            log("onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            log("onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            log("onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            log("onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            log("onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            log("onFling");
            return false;
        }
    }
}
