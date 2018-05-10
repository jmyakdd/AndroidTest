package lcq.com.androidtest;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by CRTE-CD-13 on 2018/5/10.
 */

public class FileStorage {
    public static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/my/voice/";

    private DataOutputStream dataOutputStream;
    private static ExecutorService executorService;
    private boolean isStart = false;
    private Context context;

    public FileStorage(Context context) {
        this.context = context;
        executorService = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(1, true), new ThreadPoolExecutor.DiscardPolicy());
    }

    public void startStorage() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                File file;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    file = new File(filePath,"test1.pcm");
                } else {
                    file = new File(filePath,"test1.pcm");
                }
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log("文件创建失败"+e.toString());
                        return;
                    }
                }
                try {
                    dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    log("打开输出流失败"+e.toString());
                    return;
                }
            }
        });
    }

    public void writeData(byte[] data) {
        try {
            dataOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            log("输出流打开失败");
        }
    }

    public void closeStorage() {
        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void log(String msg) {
        Log.e("test", msg);
    }
}
