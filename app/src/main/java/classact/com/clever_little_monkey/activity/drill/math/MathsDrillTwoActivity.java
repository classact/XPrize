package classact.com.clever_little_monkey.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.FisherYates;
import classact.com.clever_little_monkey.utils.Square;
import classact.com.clever_little_monkey.utils.SquarePacker;

public class MathsDrillTwoActivity extends DrillActivity {


    private JSONObject allData;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private JSONArray numbers;
    private RelativeLayout rootLayout;
    private RelativeLayout objectsContainer;
    private boolean touchEnabled;

    private LinkedHashMap<Integer, ImageView> mNumberNumberMap;

    private final int PICTURES_FRAME_WIDTH = 745;
    private final int PICTURES_FRAME_HEIGHT = 955;
    private final int NUMBERS_FRAME_WIDTH = 745;
    private final int NUMBERS_FRAME_HEIGHT = 955;

    private MathDrill02ViewModel vm;

    private View lastWrongNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_two);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill02ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        rootLayout = (RelativeLayout) findViewById(R.id.activity_math_drill_two);
        objectsContainer = (RelativeLayout) findViewById(R.id.objects_container);

        numberOne = (ImageView) findViewById(R.id.cakedemo_obect);
        numberTwo = (ImageView) findViewById(R.id.numeral_2);
        numberThree = (ImageView) findViewById(R.id.numeral_3);

        numberOne.setImageResource(0);
        numberTwo.setImageResource(0);
        numberThree.setImageResource(0);

        for (int i = 0; i < objectsContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) objectsContainer.getChildAt(i);
            iv.setImageResource(0);
        }

        // Init data blah blah
        touchEnabled = false;
        initialiseData();

        setupObjects();
        setupNumbers();
    }

    private void setupObjects(){
        try{
            if (allData != null) {

                // Relative layout for 'images'
                RelativeLayout rli = new RelativeLayout(getApplicationContext());
                // rli.setBackgroundColor(Color.argb(150, 0, 255, 255));
                rootLayout.addView(rli);
                RelativeLayout.LayoutParams rliParams = (RelativeLayout.LayoutParams) rli.getLayoutParams();
                rliParams.topMargin = 330; // 310 min
                rliParams.leftMargin = 255; // 230 min
                rliParams.width = PICTURES_FRAME_WIDTH; // 795 max
                rliParams.height = PICTURES_FRAME_HEIGHT; // 995 max
                rli.setLayoutParams(rliParams);

                int n = allData.getInt("number_of_objects");
                int w = NUMBERS_FRAME_WIDTH;
                int h = NUMBERS_FRAME_HEIGHT;
                int imageId = FetchResource.imageId(this, allData, "object");

                SquarePacker squarePacker = new SquarePacker(w, h);
                Square[] squares = squarePacker.get(n);

                for (int i = 0; i < squares.length; i++) {
                    // Get square
                    Square square = squares[i];
                    // Get drawable
                    // Drawable d = getResources().getDrawable(imageId, null);
                    // Create image view
                    ImageView iv = new ImageView(getApplicationContext());
                    loadImage(iv, imageId);
                    iv.setScaleX(0.8f);
                    iv.setScaleY(0.8f);
                    // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
                    // Add image view to numbers layout
                    rli.addView(iv);
                    // Edit image view layout params
                    RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    ivParams.leftMargin = 0;
                    ivParams.topMargin = 0;
                    ivParams.width = square.w;
                    ivParams.height = square.w;
                    iv.setLayoutParams(ivParams);
                    // Set coordinates
                    iv.setX((float) square.x);
                    iv.setY((float) square.y);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setupNumbers() {
        try {
            if (numbers != null) {

                // Relative layout for 'numbers'
                RelativeLayout rln = new RelativeLayout(getApplicationContext());
                // rln.setBackgroundColor(Color.argb(150, 255, 0, 0));
                rootLayout.addView(rln);
                RelativeLayout.LayoutParams rlnParams = (RelativeLayout.LayoutParams) rln.getLayoutParams();
                rlnParams.topMargin = 330; // 310 min, 370 max
                rlnParams.leftMargin = 1465; // 1440 min, 1500 max
                rlnParams.width = NUMBERS_FRAME_WIDTH; // 795 max, 675 min
                rlnParams.height = NUMBERS_FRAME_HEIGHT; // 995 max, 875 min
                rln.setLayoutParams(rlnParams);

                int n = numbers.length();
                int w = NUMBERS_FRAME_WIDTH;
                int h = NUMBERS_FRAME_HEIGHT;

                SquarePacker squarePacker = new SquarePacker(w, h);
                Square[] squares = squarePacker.get(n);
                int[] scrambles = FisherYates.shuffle(n);

                mNumberNumberMap = new LinkedHashMap<>();

                for (int i = 0; i < squares.length; i++) {
                    int si = scrambles[i];
                    // Get square
                    Square square = squares[si];
                    // Get drawable
                    // Drawable d = getResources().getDrawable(FetchResource.imageId(this, numbers, i, "image"), null);
                    // Create image view
                    ImageView iv = new ImageView(context);
                    loadImage(iv, FetchResource.imageId(this, numbers, i, "image"));
                    iv.setScaleX(0.8f);
                    iv.setScaleY(0.8f);
                    // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
                    // Add image view to numbers layout
                    rln.addView(iv);
                    // Edit image view layout params
                    RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    ivParams.leftMargin = 0;
                    ivParams.topMargin = 0;
                    ivParams.width = square.w;
                    ivParams.height = square.w;
                    iv.setLayoutParams(ivParams);
                    // Set coordinates
                    iv.setX((float) square.x);
                    iv.setY((float) square.y);

                    // Setup listener
                    final int numberIndex = i;

                    iv.setAlpha(0.2f);

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            numberClicked(v, numberIndex);
                        }
                    });

                    mNumberNumberMap.put(numberIndex, iv);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            numbers = allData.getJSONArray("numerals");
            String sound = allData.getString("monkey_has");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumberOfObjects();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void sayNumberOfObjects(){
        try{
            String sound = allData.getString("number_of_objects_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayCanYouTouch();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void numberClicked(View view, int position){
        if (touchEnabled) {

            // Uncolor last wrong number
            if (lastWrongNumber != null && view != lastWrongNumber) {
                unHighlight(lastWrongNumber);
            }

            try {
                int correct = numbers.getJSONObject(position).getInt("right");
                String sound = numbers.getJSONObject(position).getString("sound");
                if (correct == 0) {

                    lastWrongNumber = view;

                    // Color number
                    highlightWrong(view);

                    playSound(sound, () ->
                        playSound(FetchResource.negativeAffirmation(context), () -> {

                            // Uncolor number
                            unHighlight(view);
                        }));

                } else {
                    touchEnabled = false;
                    ImageView iv = mNumberNumberMap.get(position);
                    Globals.playStarWorks(this, iv);

                    // Color number
                    highlightCorrect(view);

                    playSound(sound, () -> {
                        try {
                            handler.delayed(() -> {
                                playSound(FetchResource.positiveAffirmation(context), () -> {
                                    handler.delayed(() -> {
                                        finish();
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }, 100);
                                });
                            }, 50);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sayCanYouTouch(){
        try{
            String sound = allData.getString("can_you_find_and_touch");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void sayNumber(){
        try{
            String sound = allData.getString("numeral_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    handler.delayed(new Runnable() {
                        @Override
                        public void run() {
                            touchEnabled = true;
                            for (Map.Entry<Integer, ImageView> entrySet : mNumberNumberMap.entrySet()) {
                                ImageView iv = entrySet.getValue();
                                iv.setAlpha(1f);
                            }
                        }
                    }, 200);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
