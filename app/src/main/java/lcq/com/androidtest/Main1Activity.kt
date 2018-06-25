package lcq.com.androidtest

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main1.*
import java.io.IOException

class Main1Activity : AppCompatActivity() {
    private var audioTrack: AudioTrack? = null
    internal val frequency = 8000
    private val b = ByteArray(1024)
    private val b1 = ByteArray(1024)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        val playBufSize = AudioTrack.getMinBufferSize(frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT)
        audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, playBufSize, AudioTrack.MODE_STREAM)
        audioTrack!!.play()

        play1.setOnClickListener {
            Thread(MyThread("test.pcm")).start()
        }

        play2.setOnClickListener {
            Thread(MyThread("test11.pcm")).start()
        }

        play3.setOnClickListener {
            Thread(MyThread1("test11.pcm", "test1.pcm")).start()
        }

        /*var data = byteArrayOf(0x01, 0x02)
        var voiceMixUtil = VoiceMixUtil()
        var data1 = voiceMixUtil.mixData(data, data)
        for (d in data1) {
            Log.e("test", d.toString())
        }*/
    }

    internal inner class MyThread1(val file: String, val file1: String) : Runnable {

        override fun run() {
            try {
                val inputStream = this@Main1Activity.assets.open(file)
                val inputStream1 = this@Main1Activity.assets.open(file1)
                while (inputStream.read(b) != -1) {
                    if (inputStream1.read(b1) != -1) {
                        Log.e("test", "hun")
                        var byte = arrayOf(b1,b)
//                        var data = MixVoiceUtil.normalizationMix(byte)
                        var data = MixVoiceUtil.mixVoice(b1, b)
                        audioTrack!!.write(data, 0, data.size)
                    } else {
                        audioTrack!!.write(b, 0, b.size)
                    }
                }
                while (inputStream1.read(b1) != -1) {
                    audioTrack!!.write(b1, 0, b1.size)
                }
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {

            }
        }
    }

    internal inner class MyThread(val file: String) : Runnable {

        override fun run() {
            try {
                val inputStream = this@Main1Activity.assets.open(file)
                inputStream.read(b)
                while (inputStream.read(b) != -1) {
                    audioTrack!!.write(b, 0, b.size)
                }
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {

            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    /*external fun stringFromJNI(): String

    external fun stringFromJNI1(msg:String):String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }*/
}