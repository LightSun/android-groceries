package com.example.administrator.myapplication;

//http://blog.csdn.net/qiuxiaolong007/article/details/7860610
public class RegisterNativesTest {

    static {
        System.loadLibrary("test_register_native");
        //System.load("/home/zmh/workspace/RegisterNativesTest/lib/libCallClass.so");  //like this ok too.
    }

    public static void main(String[] args) {
        RegisterNativesTest app = new RegisterNativesTest();
        MyJavaClass obj = new MyJavaClass();
        obj.iValue = 10;
        System.out.println("Before callCustomClass: " + obj.iValue);
        app.callCustomClass(obj);
        System.out.println("After callCustomClass: " + obj.iValue);
    }

    private native void callCustomClass(MyJavaClass obj);
}  