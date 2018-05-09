package lcq.com.androidtest;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by CRTE-CD-13 on 2018/5/8.
 */

public class App extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
