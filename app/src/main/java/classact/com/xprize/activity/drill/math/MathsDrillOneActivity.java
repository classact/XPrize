package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.utils.FisherYates;

public class MathsDrillOneActivity extends DrillActivity {

    @BindView(R.id.activity_maths_unit_one) RelativeLayout rootView;

    private int numCurrent;
    private int numMax;
    private int[] positions;
    boolean isRepeat;

    private MathDrill01ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_one);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill01ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        rootView.setPadding(0, 50, 75, 50);

        init();
        itsTimeToCount();
    }

    private void init(){
        try {
            isRepeat = false;
            numCurrent = 0;
            numMax = Math.min(vm.getNumberCount(), rootView.getChildCount());
            positions = FisherYates.shuffle(numMax);
            for (int i = 0; i < numMax; i++) {
                ImageView iv = (ImageView) rootView.getChildAt(i);
                iv.setImageResource(fetch.imageId(vm.getNumber(positions[i]).getBlackImage()));
                iv.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void itsTimeToCount() {
        playSound(vm.getInstruction(0), this::countNumber);
    }

    private void countNumber() {
        String sound = "";
        Numerals number = vm.getNumber(positions[numCurrent]);
        ImageView iv = (ImageView) rootView.getChildAt(numCurrent);
        iv.setImageResource(fetch.imageId(number.getSparklingImage()));
        try {
            sound = number.getSound();
            String soundPath = fetch.raw(sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                handler.delayed(() -> {
                    iv.setImageResource(fetch.imageId(number.getBlackImage()));
                    if (++numCurrent < numMax) {
                        countNumber();
                    } else {
                        if (isRepeat) {
                            end();
                        } else {
                            sayTheNumbersWithMeAsTheySparkle();
                        }
                    }
                }, Math.max(0, 1000 - mp.getDuration())); // Wait for duration difference
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Error for ‘" + sound + "’", Toast.LENGTH_LONG).show();
        }
    }

    private void sayTheNumbersWithMeAsTheySparkle() {
        isRepeat = true;
        numCurrent = 0;
        playSound(vm.getInstruction(1), this::countNumber);
    }

    private void end() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}