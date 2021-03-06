package fruitbasket.com.audioprocessor.process;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.text.TextUtils;
import android.util.Log;

import fruitbasket.com.audioprocessor.AppCondition;

/**
 * 用于播放音频信息
 */
final public class MessageAudioPlayer {
    private static final String TAG="MessageAudioPlayer";

    private AudioTrack audioTrack;
    private boolean isRepeat;

    public MessageAudioPlayer(){}

    /**
     * 将一段文本调制成音频信息，然后播放出来
     * @param text 文本
     * @param isRepeat 是否重复播放
     * @param muteInterval 重复播放时的时间间隔，以毫秒为单位
     * @return true 如果播放成功
    */
    public boolean play( final String text, final boolean isRepeat, final int muteInterval){
        Log.i(TAG,"play()");

        if(TextUtils.isEmpty(text)){
            Log.e(TAG,"play(): TextUtils.isEmpty(text)==ture");
            return false;
        }
        else if(muteInterval<0||muteInterval>1000){
            Log.e(TAG,"play(): muteInterval<0||muteInterval>1000");
            return false;
        }
       new Thread(new Runnable() {
           @Override
           public void run() {
               final int bufferSize = AudioTrack.getMinBufferSize(
                       AppCondition.DEFAULE_SIMPLE_RATE,
                       AudioFormat.CHANNEL_OUT_MONO,
                       AudioFormat.ENCODING_PCM_16BIT);

               if(audioTrack==null){
                   audioTrack=new AudioTrack(
                           AudioManager.STREAM_MUSIC,
                           AppCondition.DEFAULE_SIMPLE_RATE,
                           AudioFormat.CHANNEL_OUT_MONO,
                           AudioFormat.ENCODING_PCM_16BIT,
                           bufferSize,
                           AudioTrack.MODE_STREAM);
               }
               audioTrack.play();

               short [][]data=(new Encoder(text)).getAudioData();
               if(data!=null){
                   int i;
                   do{
                       for(i=0;i<data.length;++i){
                           audioTrack.write(data[i],0,data[i].length);
                       }
                       try {
                           Thread.sleep(muteInterval);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }while(isRepeat&&
                           audioTrack.getPlayState()==AudioTrack.PLAYSTATE_PLAYING);
                   if(isRepeat==false){
                       audioTrack.stop();
                   }
                   audioTrack.flush();
               }
           }
       }).start();
        return true;
    }

    public void stopPlaying(){
        if(audioTrack!=null){
            audioTrack.stop();
        }
    }

    /**
     * 释放资源。当MessageAudioPlayer不再使用时，应当调用此方法以释放资源
     */
    public void releaseResource(){
        if(audioTrack !=null){
            audioTrack.release();
        }
    }
}
