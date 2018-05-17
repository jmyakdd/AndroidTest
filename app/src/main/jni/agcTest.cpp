//  
//

 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
 

#include "signal_processing_library.h"
#include "noise_suppression_x.h"
#include "noise_suppression.h"
#include "gain_control.h"
#include "lcq_com_androidtest_agcTest.h"
/*int agcFrame(unsigned char *pIn, int len, unsigned char *pOut);
int initAgcMod();*/

void *AgcHandle = NULL;
/*
void agcTest(char *filename, char *outfilename, int fs)
{
	FILE *infp = NULL;
	FILE *outfp = NULL;

	short *pData = NULL;
	short *pOutData = NULL;
	void *agcHandle = NULL;

	do
	{
		initAgcMod();
		infp = fopen(filename, "rb");
		int frameSize = 80;
		pData = (short*)malloc(frameSize * sizeof(short)*6);
		pOutData = (short*)malloc(frameSize * sizeof(short)*6);

		outfp = fopen(outfilename, "wb");
		int len = frameSize * sizeof(short)  *6;
		int micLevelIn = 0;
		int micLevelOut = 0;
		while (true)
		{
			memset(pData, 0, len);
			len = fread(pData, 1, len, infp);
			if (len > 0)
			{
-				agcFrame((unsigned char*)pData, len, (unsigned char*)pOutData);
				fwrite(pOutData, 1, len, outfp);
			}
			else
			{
				break;
			}
		}
	} while (0);

	fclose(infp);
	fclose(outfp);
	free(pData);
	free(pOutData);
	WebRtcAgc_Free(agcHandle);
}
*/
JNIEXPORT jint JNICALL Java_lcq_com_androidtest_agcTest_initAgcMod
  (JNIEnv *, jobject, jint level)
//int initAgcMod()
{
	WebRtcAgc_Create(&AgcHandle);

	int minLevel = 0;
	int maxLevel = 255;
	int agcMode = kAgcModeFixedDigital;
	WebRtcAgc_Init(AgcHandle, minLevel, maxLevel, agcMode, 8000);

	WebRtcAgc_config_t agcConfig;
	agcConfig.compressionGaindB = 20;
	agcConfig.limiterEnable = 1;
	agcConfig.targetLevelDbfs = level;
	WebRtcAgc_set_config(AgcHandle, agcConfig);
	return 0;
}

JNIEXPORT jint JNICALL Java_lcq_com_androidtest_agcTest_agcFrame
  (JNIEnv *env, jobject job, jshortArray in, jint l, jshortArray out)
//int agcFrame(unsigned char *pIn, int len, unsigned char *pOut)
{
    jshort* bBuffer = env->GetShortArrayElements(in,0);
    jshort* bBuffer1 = env->GetShortArrayElements(out,0);
    unsigned char *pIn = (unsigned char*)bBuffer;
    int len = l;
    unsigned char *pOut = (unsigned char*)bBuffer1;
	short *pData = NULL;
	short *pOutData = NULL;
	short inBuf[80 * 6];
	short outBuf[80*6];
	int frameSize = 80;
	if (len > 960 || len <=0)
		return -1;
	memcpy((char*)inBuf, pIn, len);
	pData = inBuf; // malloc(frameSize * sizeof(short));
	pOutData = outBuf;// (short*)malloc(frameSize * sizeof(short));

	int micLevelIn = 0;
	int micLevelOut = 0;
	int inMicLevel = micLevelOut;
	int outMicLevel = 0;
	uint8_t saturationWarning;
	int cnt = 0;
	while (1)
	{
		int nAgcRet = WebRtcAgc_Process(AgcHandle, pData+cnt, NULL, frameSize, pOutData+cnt, NULL, inMicLevel, &outMicLevel, 0, &saturationWarning);
		if (nAgcRet != 0)
		{
			printf("failed in WebRtcAgc_Process\n");
			return 0;
		}
		micLevelIn = outMicLevel;
		cnt += frameSize;
		if (cnt >= len/2)
			break;
	}
	memcpy(pOut,(char*)outBuf, len);
	env->ReleaseShortArrayElements(in,bBuffer,0);
	env->ReleaseShortArrayElements(out,bBuffer1,0);
	return 1;
}


/*
int main(int argc, char* argv[])
{
	
	if(argc<3)
	{
		printf("usage: agcText fileIn fileOut\n");
		exit(-1);
	}
	agcTest(argv[1], argv[2], 8000);

	return 0;
}
*/

