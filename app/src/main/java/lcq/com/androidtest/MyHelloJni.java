package lcq.com.androidtest;

/**
 * Created by CRTE-CD-13 on 2018/5/10.
 */

public class MyHelloJni {
    public String getHello(){
        return hello();
    }

    public String getHello1(String str){
        return hello(str);
    }

    public native String hello();

    public native String hello(String string);

    static {
        System.loadLibrary("MyHelloJni");
    }
}
