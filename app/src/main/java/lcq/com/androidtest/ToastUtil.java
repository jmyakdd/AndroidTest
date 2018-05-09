package lcq.com.androidtest;

import android.widget.Toast;

/**
 * Created by CRTE-CD-13 on 2018/5/9.
 */

public class ToastUtil {
    private static ToastUtil instance = null;
    private Toast toast;
    private ToastUtil(){

    }

    public static ToastUtil getInstance(){
        if(instance == null){
            instance = new ToastUtil();
        }
        return instance;
    }

    public void showToastShort(String msg){

    }
}
