package lcq.com.androidtest;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by CRTE-CD-13 on 2018/5/9.
 */

public class CommunicateButton extends AppCompatButton {
    public CommunicateButton(Context context) {
        super(context);
    }

    public CommunicateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
