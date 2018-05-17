package lcq.com.androidtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by CRTE-CD-13 on 2018/5/16.
 */

public class MyBannerAdapter extends PagerAdapter {

    private Context context;
    private List<String> data;

    public MyBannerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
