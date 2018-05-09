package lcq.com.androidtest;

import android.media.AudioRecord;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VoiceActivity extends AppCompatActivity {
    private AudioRecord audioRecord;
    private byte[] buff = new byte[1024];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        init
    }
}
