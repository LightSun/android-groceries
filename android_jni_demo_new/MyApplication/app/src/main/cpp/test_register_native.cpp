#include <jni.h>
#include <string>

extern "C"
void callCustomClass(JNIEnv* env, jobject, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "iValue", "I");
    jmethodID mid = env->GetMethodID(cls, "squa", "()V");
    int value = env->GetIntField(obj, fid);
    printf("Native: %d\n", value);
    env->SetIntField(obj, fid, 5);
    env->CallVoidMethod(obj, mid);
    value = env->GetIntField(obj, fid);
    printf("Native:%d\n", value);
}
//'com.example.administrator.myapplication.RegisterNativesTest' must be 'com/example/administrator/myapplication/RegisterNativesTest'
static JNINativeMethod s_methods[] = {
        {"callCustomClass", "(Lcom/example/administrator/myapplication/MyJavaClass;)V", (void*)callCustomClass},
};

int JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK)
    {
        return JNI_ERR;
    }

    //jclass cls = env->FindClass("LRegisterNativesTest;");
    jclass cls = env->FindClass("com/example/administrator/myapplication/RegisterNativesTest");
    if (cls == NULL)
    {
        return JNI_ERR;
    }

    int len = sizeof(s_methods) / sizeof(s_methods[0]);
    if (env->RegisterNatives(cls, s_methods, len) < 0)
    {
        return JNI_ERR;
    }

    return JNI_VERSION_1_4;
}
