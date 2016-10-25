package fruitbasket.com.audioprocessor.modulate;

import android.text.method.NumberKeyListener;
import android.util.Log;

import fruitbasket.com.audioprocessor.AppCondition;

/**
 * 将一段声音信号解码成文本
 * Created by Study on 20/10/2016.
 */

public class Decoder {
    private static final String TAG="modulate.Decoder";

    public void Decoder(){}

    /**
     * 将一段声音信号解码成单个字符。注意，此声音信号只能包含单个字符
     * @reurn
     */
    public char decode(short[] audioData){
        if(audioData==null){
            Log.e(TAG,"audioData==null");
            return '\u0000';
        }
        else {
            final int frequency = FrequencyDetector.getFrequence(
                    FFT.fft(audioData, true),
                    AppCondition.DEFAULE_SIMPLE_RATE
            );
            Log.i(TAG,"frequency=="+frequency);
            return charOfFrequency(frequency);
        }
    }

    /**
     *
     * @param frequency
     * @return
     */
    private char charOfFrequency(int frequency){

        final int bookLength=ModulateCondition.WAVE_RATE_BOOK.length-2;//记录WAVE_RATE_BOOK包含声波频率的实际个数
        final int errorRange=5;//定义一个误差范围
        int standard;
        int index;
        int i;
        //之所以这样控制循环范围，是因为不能使用WAVE_RATE_BOOK开始和结束的元素
        for(i=1;i<=bookLength;i++){
            standard=ModulateCondition.WAVE_RATE_BOOK[i];

            if(frequency-errorRange>=standard
                    &&frequency+errorRange<=standard){
                index=i-1;
                return ModulateCondition.CHAR_BOOK.charAt(index);
            }
        }

        //如果frequency无效
        Log.w(TAG,"i>=bookLength : frequency is invalid");
        return '\u0000';
    }
}
