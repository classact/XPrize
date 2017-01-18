package classact.com.xprize.activity.sounddrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.view.WriteView;

public class SoundDrillNineActivity extends AppCompatActivity {
    WriteView view;
    private MediaPlayer mp;
    private Handler handler;
    private RelativeLayout writingContainer;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_nine);
        //getWindow().getDecorView().getRootView().setBackgroundResource(ResourceSelector.getBackgroundResource(this));
        view = new WriteView(this, R.drawable.drawapic1);
        view.setAlpha(0.6f);
        writingContainer = (RelativeLayout)findViewById(R.id.activity_sound_drill_nine);
        handler = new Handler(Looper.getMainLooper());
        writingContainer.addView(view);
        String data = getIntent().getExtras().getString("data");
        try {
            params = new JSONObject(data);
            //Todo: Sound
            int soundid = params.getInt("lets_draw");
            mp = MediaPlayer.create(this,soundid);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(sayDrawSomethingRunnable, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable sayDrawSomethingRunnable = new Runnable(){
        @Override
        public void run() {
            sayDrawSomething();
        }
    };

    private void sayDrawSomething(){
        try {
            int soundid = params.getInt("draw_something_that_starts_with");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    //handler.postDelayed(saySomethingRunnable, 200);
                    saySomething();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable saySomethingRunnable = new Runnable(){
        @Override
        public void run() {
            saySomething();
        }
    };

    private void saySomething(){
        int sound = 0;
        try{
            sound = params.getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    handler.postDelayed(writeRunnable,6000);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable writeRunnable = new Runnable(){
        @Override
        public void run() {
            try{
                int sound = params.getInt("what_did_you_draw");
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.reset();
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        finish();
                    }
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
                finish();
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }
}
