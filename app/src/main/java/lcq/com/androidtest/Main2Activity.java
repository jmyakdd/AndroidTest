package lcq.com.androidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private List<String> strings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        for(int i=0;i<10;i++){
            strings.add(i+"");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                    for (int i=0;i<strings.size();i++) {
                        try {
                            Thread.sleep(1000);
                            Log.e("test", "Thread=" + strings.get(i));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("test", e.toString());
                        }
                    }
            }
        }).start();
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    strings.remove(5);
                    for (String s : strings) {
                        Log.e("test", "Main=" + s);
                    }
            }
        });
    }
}
