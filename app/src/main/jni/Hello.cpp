//
// Created by CRTE-CD-13 on 2018/5/11.
//
#include "lcq_com_androidtest_MyHelloJni.h"
#include <string.h>
#include <stdlib.h>

JNIEXPORT jstring JNICALL Java_lcq_com_androidtest_MyHelloJni_hello__
  (JNIEnv *env, jobject jobj){
       return env->NewStringUTF("hello");
       }
 JNIEXPORT jstring JNICALL Java_lcq_com_androidtest_MyHelloJni_hello__Ljava_lang_String_2
   (JNIEnv *env, jobject jobj, jstring str){
   const char *s = env->GetStringUTFChars(str,0);
   return env->NewStringUTF(s);
 }
