package classact.com.clever_little_monkey.utils;

import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by Tseliso on 10/19/2016.
 */

public class SoundPrescence {
    private static final String TAG = "Clapper";

    private static final long DEFAULT_CLIP_TIME = 1000;
    private static final long TIMEOUT = 6;
    private long clipTime = DEFAULT_CLIP_TIME;
    private SoundPresenceListener clipListener;

    public interface SoundPresenceListener{
        public void heard (boolean heard);
    }

    private boolean continueRecording;

    /**
     * how much louder is required to hear a clap 10000, 18000, 25000 are good
     * values
     */
    private int amplitudeThreshold;

    /**
     * requires a little of noise by the user to trigger, background noise may
     * trigger it
     */
    public static final int AMPLITUDE_DIFF_LOW = 10000;
    public static final int AMPLITUDE_DIFF_MED = 18000;
    /**
     * requires a lot of noise by the user to trigger. background noise isn't
     * likely to be this loud
     */
    public static final int AMPLITUDE_DIFF_HIGH = 25000;

    private static final int DEFAULT_AMPLITUDE_DIFF = AMPLITUDE_DIFF_MED;

    private MediaRecorder recorder;

     private int currentAttempt = 0;


    public SoundPrescence( SoundPresenceListener clipListener)
    {
        this.clipTime = DEFAULT_CLIP_TIME;
        this.clipListener = clipListener;
        this.amplitudeThreshold = DEFAULT_AMPLITUDE_DIFF;
    }

    public void listen()
    {
        Log.d(TAG, "record clap");
        boolean spoke = false;
        currentAttempt = 0;
        try
        {
            //recorder = AudioUtil.prepareRecorder(tmpAudioFile);
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile("/dev/null");
            recorder.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


        recorder.start();
        int startAmplitude = recorder.getMaxAmplitude();
        Log.d(TAG, "starting amplitude: " + startAmplitude);

        do
        {
            currentAttempt++;
            Log.d(TAG, "waiting while recording...");
            waitSome();
            int finishAmplitude = recorder.getMaxAmplitude();

            int ampDifference = finishAmplitude - startAmplitude;
            if (ampDifference >= amplitudeThreshold)
            {
                Log.d(TAG, "heard a clap!");
                spoke = true;
            }
            Log.d(TAG, "finishing amplitude: " + finishAmplitude + " diff: "
                    + ampDifference);
        } while (continueRecording || !spoke || currentAttempt < TIMEOUT);

        Log.d(TAG, "stopped recording");
        done();

        clipListener.heard(spoke);
    }

    private void waitSome()
    {
        try
        {
            // wait a while
            Thread.sleep(clipTime);
        } catch (InterruptedException e)
        {
            Log.d(TAG, "interrupted");
        }
    }

    /**
     * need to call this when completely done with recording
     */
    private void done()
    {
        Log.d(TAG, "stop recording");
        if (recorder != null)
        {
            if (isRecording())
            {
                stopRecording();
            }
            //now stop the media player
            recorder.stop();
            recorder.release();
        }
    }

    private boolean isRecording()
    {
        return continueRecording;
    }

    private void stopRecording()
    {
        continueRecording = false;
    }
}
