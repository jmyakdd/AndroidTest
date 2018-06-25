package lcq.com.androidtest;

public class VoiceMixUtil {
    public native byte[] mixData(byte[] buff1, byte[] buff2);

    public native String getString();

    public native int getSize(byte[] data);

    static {
        System.loadLibrary("MyHelloJni");
    }
}
