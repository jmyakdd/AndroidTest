package lcq.com.androidtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;

import lcq.com.androidtest.bean.User;

public class MainActivity extends AppCompatActivity {
    private EditText name,age;
    private RadioGroup sexGroup;
    private RadioButton male,female;
    private Button submit,query,start,stop,record;
    private String sex = "male";
    private ImageView callingAnimation,callingAnimationBg;
    private AnimationDrawable animationDrawable;

    private AudioTrack audioTrack;
    static final int frequency = 8000;
    private InputStream inputStream = null;
    private byte[] b = new byte[1024];

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeAnimation();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        sexGroup = findViewById(R.id.sex_group);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        submit = findViewById(R.id.submit);
        query = findViewById(R.id.query);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        callingAnimation = findViewById(R.id.calling_animation);
        callingAnimationBg = findViewById(R.id.calling_animation_bg);
        animationDrawable = (AnimationDrawable) callingAnimation.getDrawable();
        animationDrawable.start();
        closeAnimation();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setName(name.getText().toString());
                user.setAge(Integer.parseInt(age.getText().toString()));
                user.setSex(sex);
                user.save();
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = DataSupport.findFirst(User.class);
                Log.e("test",user.toString());
            }
        });

        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.male:
                        sex = "male";
                        break;
                    case R.id.female:
                        sex = "female";
                        break;
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    return;
                }
                openAnimation();
                new Thread(new MyThread()).start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAnimation();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,VoiceActivity.class));
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    111);
        }
    }

    public void openAnimation() {
        callingAnimation.setVisibility(View.VISIBLE);
        isPlaying = true;
    }

    public void closeAnimation() {
        callingAnimation.setVisibility(View.GONE);
        if(audioTrack==null){
            return;
        }
        isPlaying = false;
        audioTrack.stop();
        audioTrack.release();
        audioTrack = null;
    }
    private boolean isPlaying = false;

    class MyThread implements Runnable{

        @Override
        public void run() {
            int playBufSize = AudioTrack.getMinBufferSize(frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, playBufSize, AudioTrack.MODE_STREAM);
            audioTrack.play();
            try {
                inputStream = MainActivity.this.getAssets().open("test.pcm");
                int count = 0;
                while (inputStream.read(b)!=-1&&isPlaying){
                    audioTrack.write(b,0,b.length);
                    count++;
                    Log.e("test",count+"");
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                handler.sendEmptyMessage(0);
            }
        }
    }
}
