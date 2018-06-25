//
// Created by CRTE-CD-13 on 2018/6/22.
//
#include "lcq_com_androidtest_VoiceMixUtil.h"
JNIEXPORT jbyteArray JNICALL Java_lcq_com_androidtest_VoiceMixUtil_mixData
  (JNIEnv * env, jobject obj, jbyteArray ch0, jbyteArray ch1){
  jint ch0Length = env->GetArrayLength(ch0);
  return ch0;
}

JNIEXPORT jint JNICALL Java_lcq_com_androidtest_VoiceMixUtil_getSize
  (JNIEnv *env, jobject obj, jbyteArray ch0){
  jint ch0Length = env->GetArrayLength(ch0);
  return ch0Length;
  }