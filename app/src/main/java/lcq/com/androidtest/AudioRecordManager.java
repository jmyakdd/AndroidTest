package lcq.com.androidtest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by CRTE-CD-13 on 2018/5/9.
 */

public class AudioRecordManager {
    public static final int WHAT_START_RECORD = 0;
    public static final int WHAT_COMPLETE_RECORD = 1;
    private final int m_buff_size = 1024;
    private AudioRecord audioRecord;
    private byte[] buff = new byte[m_buff_size];
    private List<byte[]>data = new ArrayList<>();

    private Handler handler;

    private ScheduledExecutorService scheduledExecutorService;
    private boolean isRecord = false;

    public AudioRecordManager(Handler handler) {
        this.handler = handler;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        init();
    }

    public List<byte[]> getData() {
        return data;
    }

    private void init() {
        if (audioRecord != null) {
            if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                try {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                } catch (Exception e) {
                }
            }
        } else {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, m_buff_size);
        }
    }

    public void startRecord() {
        scheduledExecutorService.execute(new RecordThread());
    }

    public void startVoice() {
        new ReadData().start();
    }

    public void stopRecord() {
        if (audioRecord == null) {
            return;
        }
        isRecord = false;
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
    }

    private class RecordThread implements Runnable {

        @Override
        public void run() {
            if (audioRecord == null) {
                log("audioRecord is null");
                return;
            }
            if (audioRecord.getRecordingState() == AudioRecord.STATE_UNINITIALIZED) {
                log("audioRecord init wrong");
                return;
            }
            try {
                audioRecord.startRecording();
            } catch (Exception e) {
                log("audioRecord start fail");
            }
            if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                log("麦克风被占用,请关掉其他录音软件");
                return;
            }
            isRecord = true;
        }
    }

    private class ReadData extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (isRecord) {
                count++;
                audioRecord.read(buff, 0, m_buff_size);
                data.add(buff);
                log(count + " " + buff.length);
            }
        }
    }

    private void log(String msg) {
        Log.e("test", msg);
    }
}
