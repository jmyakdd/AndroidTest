package lcq.com.androidtest

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main1.*
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Main1Activity : AppCompatActivity() {
    private var audioTrack: AudioTrack? = null
    internal val frequency = 8000

    lateinit var storage: FileStorage


    private val permission = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        storage = FileStorage(this)

        val playBufSize = AudioTrack.getMinBufferSize(frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT)
        audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, playBufSize, AudioTrack.MODE_STREAM)
        audioTrack!!.play()

        play1.setOnClickListener {
            Thread(MyThread("test11.pcm")).start()
        }

        play2.setOnClickListener {
            Thread(MyThread("test1.pcm")).start()
        }

        play3.setOnClickListener {
            Thread(MyThread1("test11.pcm", "test1.pcm", 0)).start()
        }

        play4.setOnClickListener {
            Thread(MyThread1("test11.pcm", "test1.pcm", 1)).start()
        }

        play5.setOnClickListener {
            Thread(MyThread1("test11.pcm", "test1.pcm", 2)).start()
        }

        play6.setOnClickListener {
            Thread(MyThread1("test11.pcm", "test1.pcm", 3)).start()
        }

        /*Thread(object :Runnable{
            override fun run() {
                val inputStream = this@Main1Activity.assets.open("test.pcm")
                MixVoiceUtil.pcm2amr(inputStream)
            }
        }).start()*/

        /*var mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this.assets.openFd("test.amr"))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener(object :MediaPlayer.OnPreparedListener{
            */
        /**
         * Called when the media file is ready for playback.
         *
         * @param mp the MediaPlayer that is ready for playback
         *//*
            override fun onPrepared(mp: MediaPlayer?) {
                mediaPlayer.start()
            }
        })*/
        for (p in permission) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(permission, 111)
                return
            }
        }
    }

    val a = agcTest()
    var bOut = ShortArray(480)
    var bIn = ShortArray(480)

    internal inner class MyThread1(val file: String, val file1: String, val type: Int) : Runnable {

        override fun run() {
            try {
                a.initAgcMod(3)
                val inputStream = this@Main1Activity.assets.open(file)
                val inputStream1 = this@Main1Activity.assets.open(file1)
                var b = ByteArray(960)
                var b1 = ByteArray(960)
                storage.startStorage()
                while (inputStream.read(b) != -1) {
                    if (inputStream1.read(b1) != -1) {
                        var byte = arrayOf(b1, b)
                        var data = byteArrayOf()
                        when (type) {
                            0 -> {
                                data = MixVoiceUtil.averageMix(byte)
                            }
                            1 -> {
                                data = MixVoiceUtil.normalizationMix(byte)
                            }
                            2 -> {
                                data = MixVoiceUtil.mixRawAudioBytes(byte)
                            }
                            3 -> {
                                data = MixVoiceUtil.mixVoice(b, b1)
                            }
                        }

                        ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(bIn)
                        val n = a.agcFrame(bIn, data.size, bOut)
                        if (n == 1) {
                            ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(bOut)
                        }
                        audioTrack!!.write(data, 0, data.size)
                        storage.writeData(data)
                    } else {
                        audioTrack!!.write(b, 0, b.size)
                        storage.writeData(b)
                    }
                }
                while (inputStream1.read(b1) != -1) {
                    audioTrack!!.write(b1, 0, b1.size)
                    storage.writeData(b1)
                }
                storage.closeStorage()
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
                var b = ByteArray(960)
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