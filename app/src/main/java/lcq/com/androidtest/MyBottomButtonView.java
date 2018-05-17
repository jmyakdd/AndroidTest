package lcq.com.androidtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by CRTE-CD-13 on 2018/5/17.
 */

public class MyBottomButtonView extends LinearLayout {
    private Fragment fragment;
    private ImageView imageView;
    private TextView textView;
    private int iv_height;
    private int iv_width;
    private int tv_select_color;
    private int tv_unselect_color;
    private int tv_size;
    private boolean isSelect = false;

    public MyBottomButtonView(Context context) {
        super(context, null);
    }

    public MyBottomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.NavigationView);
        iv_height = array.getDimensionPixelSize(R.styleable.NavigationView_image_height,100);
        iv_width = array.getDimensionPixelSize(R.styleable.NavigationView_image_width,100);
        tv_select_color = array.getColor(R.styleable.NavigationView_text_select_color,Color.BLACK);
        tv_unselect_color = array.getColor(R.styleable.NavigationView_text_unselect_color,Color.GRAY);
        tv_size = array.getDimensionPixelSize(R.styleable.NavigationView_text_size,14);
        array.recycle();
    }

}
