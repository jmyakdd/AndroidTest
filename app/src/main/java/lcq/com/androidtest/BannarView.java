package lcq.com.androidtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by CRTE-CD-13 on 2018/5/16.
 */

public class BannarView extends ViewPager {
    private MyBannerAdapter adapter;
    public BannarView(@NonNull Context context) {
        super(context);
    }

    public BannarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
