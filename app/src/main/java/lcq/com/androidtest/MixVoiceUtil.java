package lcq.com.androidtest;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class MixVoiceUtil {

    public static byte[] mixVoice(byte[] data1, byte[] data2) {
        int length1 = data1.length;
        int length2 = data2.length;
        int count = length1 <= length2 ? length1 : length2;
        int size = length1 >= length2 ? length1 : length2;
        byte[] data = new byte[size];
        int i = 0;
        double f = 1;
        for (; i < count; i++) {
//            data[i] = (byte) (data1[i]+data2[i]-(data1[i]*data2[i]>>0x10));
            /*if (data1[i] < 0 && data2[i] < 0) {
                data[i] = (byte) (data1[i] + data2[i] - (data1[i] * data2[i] / -(Math.pow(2, 16 - 1) - 1)));
            } else {
                data[i] = (byte) (data1[i] + data2[i] - (data1[i] * data2[i] / (Math.pow(2, 16 - 1) - 1)));
            }*/
            int d = data1[i] + data2[i] + count * 2;
            d = (int) (d * f);
            if (d > 32767) {
                f = 32767d / d;
                d = 32767;
            }
            if (d < -32768) {
                f = -32767d / d;
                d = -32768;
            }
            if (f < 1) {
                f += ((double) 1 - f) / (double) 32;
            }
            data[i] = (byte) d;
//            data[i]= (byte) ((data1[i]+data2[i])/2);
        }
        if (i == length1) {
            for (int j = i; j < length2; j++) {
                data[j] = data2[j];
            }
        } else if (i == length2) {
            for (int j = i; j < length1; j++) {
                data[j] = data1[j];
            }
        }
        return data;
    }

    public static void mixAudios(File[] rawAudioFiles) {

        final int fileSize = rawAudioFiles.length;

        FileInputStream[] audioFileStreams = new FileInputStream[fileSize];
        File audioFile = null;

        FileInputStream inputStream;
        byte[][] allAudioBytes = new byte[fileSize][];
        boolean[] streamDoneArray = new boolean[fileSize];
        byte[] buffer = new byte[512];
        int offset;

        try {

            for (int fileIndex = 0; fileIndex < fileSize; ++fileIndex) {
                audioFile = rawAudioFiles[fileIndex];
                audioFileStreams[fileIndex] = new FileInputStream(audioFile);
            }

            while (true) {

                for (int streamIndex = 0; streamIndex < fileSize; ++streamIndex) {

                    inputStream = audioFileStreams[streamIndex];
                    if (!streamDoneArray[streamIndex] && (offset = inputStream.read(buffer)) != -1) {
                        allAudioBytes[streamIndex] = Arrays.copyOf(buffer, buffer.length);
                    } else {
                        streamDoneArray[streamIndex] = true;
                        allAudioBytes[streamIndex] = new byte[512];
                    }
                }

                byte[] mixBytes = averageMix(allAudioBytes);

                //mixBytes 就是混合后的数据

                boolean done = true;
                for (boolean streamEnd : streamDoneArray) {
                    if (!streamEnd) {
                        done = false;
                    }
                }

                if (done) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                for (FileInputStream in : audioFileStreams) {
                    if (in != null)
                        in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每一行是一个音频的数据
     */
    public static byte[] averageMix(byte[][] bMulRoadAudioes) {

        if (bMulRoadAudioes == null || bMulRoadAudioes.length == 0)
            return null;

        byte[] realMixAudio = bMulRoadAudioes[0];

        if (bMulRoadAudioes.length == 1)
            return realMixAudio;

        for (int rw = 0; rw < bMulRoadAudioes.length; ++rw) {
            if (bMulRoadAudioes[rw].length != realMixAudio.length) {
                Log.e("app", "column of the road of audio + " + rw + " is diffrent.");
                return null;
            }
        }

        int row = bMulRoadAudioes.length;
        int coloum = realMixAudio.length / 2;
        short[][] sMulRoadAudioes = new short[row][coloum];

        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < coloum; ++c) {
                sMulRoadAudioes[r][c] = (short) ((bMulRoadAudioes[r][c * 2] & 0xff) | (bMulRoadAudioes[r][c * 2 + 1] & 0xff) << 8);
            }
        }

        short[] sMixAudio = new short[coloum];
        int mixVal;
        int sr = 0;
        for (int sc = 0; sc < coloum; ++sc) {
            mixVal = 0;
            sr = 0;
            for (; sr < row; ++sr) {
                mixVal += sMulRoadAudioes[sr][sc];
            }
            sMixAudio[sc] = (short) (mixVal / row);
        }

        for (sr = 0; sr < coloum; ++sr) {
            realMixAudio[sr * 2] = (byte) (sMixAudio[sr] & 0x00FF);
            realMixAudio[sr * 2 + 1] = (byte) ((sMixAudio[sr] & 0xFF00) >> 8);
        }

        return realMixAudio;
    }

    /**
     * 归一化混音
     */
    public static byte[] normalizationMix(byte[][] allAudioBytes) {
        if (allAudioBytes == null || allAudioBytes.length == 0)
            return null;

        byte[] realMixAudio = allAudioBytes[0];

        //如果只有一个音频的话，就返回这个音频数据
        if (allAudioBytes.length == 1)
            return realMixAudio;

        //row 有几个音频要混音
        int row = realMixAudio.length / 2;
        //
        short[][] sourecs = new short[allAudioBytes.length][row];
        for (int r = 0; r < 2; ++r) {
            for (int c = 0; c < row; ++c) {
                sourecs[r][c] = (short) ((allAudioBytes[r][c * 2] & 0xff) | (allAudioBytes[r][c * 2 + 1] & 0xff) << 8);
            }
        }

        //coloum第一个音频长度 / 2
        short[] result = new short[row];
        //转成short再计算的原因是，提供精确度，高端的混音软件据说都是这样做的，可以测试一下不转short直接计算的混音结果
        for (int i = 0; i < row; i++) {
            int a = sourecs[0][i];
            int b = sourecs[1][i];
            if (a < 0 && b < 0) {
                int i1 = a + b - a * b / (-32768);
                if (i1 > 32767) {
                    result[i] = 32767;
                } else if (i1 < -32768) {
                    result[i] = -32768;
                } else {
                    result[i] = (short) i1;
                }
            } else if (a > 0 && b > 0) {
                int i1 = a + b - a * b / 32767;
                if (i1 > 32767) {
                    result[i] = 32767;
                } else if (i1 < -32768) {
                    result[i] = -32768;
                } else {
                    result[i] = (short) i1;
                }
            } else {
                int i1 = a + b;
                if (i1 > 32767) {
                    result[i] = 32767;
                } else if (i1 < -32768) {
                    result[i] = -32768;
                } else {
                    result[i] = (short) i1;
                }
            }
        }
        return toByteArray(result);
    }

    public static byte[] toByteArray(short[] src) {
        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2 + 1] = (byte) ((src[i] & 0xFF00) >> 8);
            dest[i * 2] = (byte) ((src[i] & 0x00FF));
        }
        return dest;
    }

    public static byte[] mixRawAudioBytes(byte[][] bMulRoadAudioes) {

        if (bMulRoadAudioes == null || bMulRoadAudioes.length == 0)
            return null;

        byte[] realMixAudio = bMulRoadAudioes[0];

        if (bMulRoadAudioes.length == 1)
            return realMixAudio;

        for (int rw = 0; rw < bMulRoadAudioes.length; ++rw) {
            if (bMulRoadAudioes[rw].length != realMixAudio.length) {
                Log.e("app", "column of the road of audio + " + rw + " is diffrent.");
                return null;
            }
        }

        int row = bMulRoadAudioes.length;
        int coloum = realMixAudio.length / 2;
        short[][] sMulRoadAudioes = new short[row][coloum];

        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < coloum; ++c) {
                sMulRoadAudioes[r][c] = (short) ((bMulRoadAudioes[r][c * 2] & 0xff) | (bMulRoadAudioes[r][c * 2 + 1] & 0xff) << 8);
            }
        }

        short[] sMixAudio = new short[coloum];
        int mixVal;
        int sr = 0;

        for (int sc = 0; sc < coloum; ++sc) {
            mixVal = 0;
            sr = 0;
            for (; sr < row; ++sr) {
                mixVal += sMulRoadAudioes[sr][sc];
            }
            sMixAudio[sc] = (short) (mixVal / row);
        }

        for (sr = 0; sr < coloum; ++sr) {
            realMixAudio[sr * 2] = (byte) (sMixAudio[sr] & 0x00FF);
            realMixAudio[sr * 2 + 1] = (byte) ((sMixAudio[sr] & 0xFF00) >> 8);
        }

        return realMixAudio;
    }
}
