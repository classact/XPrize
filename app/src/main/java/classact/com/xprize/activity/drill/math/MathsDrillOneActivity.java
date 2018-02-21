package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;

public class MathsDrillOneActivity extends DrillActivity {

    @BindView(R.id.activity_maths_unit_one) RelativeLayout rootView;

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

        init();
    }

    private void init(){
        try {
            Log.d("Number of numbers", String.valueOf(vm.getNumberCount()));
            Log.d("Number of instructions", String.valueOf(vm.getInstructionCount()));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}