package lcq.com.androidtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by CRTE-CD-13 on 2018/5/11.
 */

public class agcTest {

    public native int initAgcMod();

    public native int agcFrame(byte[] in, int length, byte[] out);
}
