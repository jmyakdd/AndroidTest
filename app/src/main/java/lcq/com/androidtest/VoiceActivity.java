package lcq.com.androidtest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class VoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn, play, copy;

    private AudioRecordManager audioRecordManager;
    private AudioTrack audioTrack;
    static final int frequency = 8000;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        bindView();
        audioRecordManager = new AudioRecordManager(handler,getApplicationContext());
    }

    private void bindView() {
        btn = findViewById(R.id.btn);
        play = findViewById(R.id.play);
        copy = findViewById(R.id.copy);

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        showToast("down");
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
//                        showToast("up");
                        stopRecord();
                        break;
                }
                return true;
            }
        });
        play.setOnClickListener(this);

        copy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                playVoice();
                break;
            case R.id.copy:
                copy();
                break;
        }
    }

    private FileStorage storage;
    private InputStream inputStream;
    private byte[] b = new byte[960];
    private byte[]bOut = new byte[960];

    private void copy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                storage = new FileStorage(VoiceActivity.this);
                storage.startStorage();
                agcTest a = new agcTest();
                try {
                    inputStream = VoiceActivity.this.getAssets().open("test.pcm");
                    while (inputStream.read(b)!=-1){
                        a.agcFrame(b,b.length,bOut);
                        storage.writeData(bOut);
                    }
                    inputStream.close();
                    storage.closeStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void playVoice() {
        new Thread(new MyThread()).start();
    }

    class MyThread implements Runnable {

        @Override
        public void run() {
            int playBufSize = AudioTrack.getMinBufferSize(frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, playBufSize, AudioTrack.MODE_STREAM);
            audioTrack.play();
            InputStream inputStream;
            File file = new File(FileStorage.filePath+"test1.pcm");
            byte[] b = new byte[1024];
            try {
                inputStream = new FileInputStream(file);
                while (inputStream.read(b) > 0) {
                    audioTrack.write(b, 0, b.length);
                }
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    private void startRecord() {
        audioRecordManager.startRecord();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                audioRecordManager.startVoice();
            }
        }).start();
    }

    private void stopRecord() {
        audioRecordManager.stopRecord();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
