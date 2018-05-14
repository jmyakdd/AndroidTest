package lcq.com.androidtest;

/**
 * Created by CRTE-CD-13 on 2018/5/11.
 */

public class agcTest {

    public native int initAgcMod();

    public native int agcFrame(short[] in, int length, short[] out);

    static {
        System.loadLibrary("MyHelloJni");
    }
}
