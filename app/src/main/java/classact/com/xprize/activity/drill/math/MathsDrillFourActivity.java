package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.SquarePacker;

public class MathsDrillFourActivity extends DrillActivity {
    private RelativeLayout rightContainer;
    private RelativeLayout leftContainer;
    private ImageView leftNumber;
    private ImageView rightNumber;
    private JSONObject allData;
    private int segment = 1;
    private boolean touchEnabled;
    private boolean drillComplete;

    private final int CONTAINER_WIDTH = 750;
    private final int CONTAINER_HEIGHT = 730;

    private MathDrill04ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_four);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill04ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        leftContainer = (RelativeLayout)findViewById(R.id.left_container);
        // leftContainer.setBackgroundColor(Color.argb(150, 255, 0, 0));
        RelativeLayout.LayoutParams lcParams = (RelativeLayout.LayoutParams) leftContainer.getLayoutParams();
        lcParams.topMargin = 100;
        lcParams.leftMargin = 330;
        lcParams.width = CONTAINER_WIDTH;
        lcParams.height = CONTAINER_HEIGHT;
        leftContainer.setLayoutParams(lcParams);

        leftNumber = (ImageView)findViewById(R.id.leftNumber);
        // leftNumber.setBackgroundColor(Color.argb(150, 255, 0, 0));
        RelativeLayout.LayoutParams lnParams = (RelativeLayout.LayoutParams) leftNumber.getLayoutParams();
        lnParams.topMargin = 30;
        lnParams.leftMargin = 555;
        leftNumber.setLayoutParams(lnParams);
        leftNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(true);
            }
        });

        rightContainer = (RelativeLayout)findViewById(R.id.right_container);
        // rightContainer.setBackgroundColor(Color.argb(150, 0, 0, 255));
        RelativeLayout.LayoutParams rcParams = (RelativeLayout.LayoutParams) rightContainer.getLayoutParams();
        rcParams.removeRule(RelativeLayout.ALIGN_TOP);
        rcParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        rcParams.topMargin = 100;
        rcParams.leftMargin = 1570;
        rcParams.width = CONTAINER_WIDTH;
        rcParams.height = CONTAINER_HEIGHT;
        rightContainer.setLayoutParams(rcParams);

        rightNumber = (ImageView)findViewById(R.id.rightNumber);
        // rightNumber.setBackgroundColor(Color.argb(150, 0, 0, 255));
        RelativeLayout.LayoutParams rnParams = (RelativeLayout.LayoutParams) rightNumber.getLayoutParams();
        rnParams.topMargin = 30;
        rnParams.leftMargin = 1795;
        rightNumber.setLayoutParams(rnParams);
        rightNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(false);
            }
        });

        touchEnabled = false;
        drillComplete = false;
        initialise();
    }

    private void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupItems();
            String sound = allData.getString("monkey_has");
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

    private void clicked(boolean left){
        if (touchEnabled && !drillComplete) {
            try {
                ImageView iv = null;

                String checkBigger = allData.getString("check_bigger");
                int leftItems = allData.getInt("number_of_left_items");
                int rightItems = allData.getInt("number_of_right_items");

                boolean isRight = false;

                if (checkBigger.equalsIgnoreCase("yes")) {
                    if (left && leftItems >= rightItems) {
                        isRight = true;
                        iv = leftNumber;
                    } else if (!left && rightItems >= leftItems) {
                        isRight = true;
                        iv = rightNumber;
                    }
                } else {
                    if (left && leftItems <= rightItems) {
                        isRight = true;
                        iv = leftNumber;
                    } else if (!left && rightItems <= leftItems) {
                        isRight = true;
                        iv = rightNumber;
                    }
                }

                if (isRight) {
                    touchEnabled = false;
                    drillComplete = true;

                    if (iv != null) {
                        starWorks.play(this, iv);
                    }

                    playSound(FetchResource.positiveAffirmation(context), () -> {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    });
                } else {
                    playSound(FetchResource.negativeAffirmation(context), null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setupItems(){
        try{
            // Wipe left container images (in xml)
            for(int i = leftContainer.getChildCount() - 1; i >= 0; i--){
                ((ImageView) leftContainer.getChildAt(i)).setImageResource(0);
            };
            // Wipe left container images (in xml)
            for(int i = rightContainer.getChildCount() - 1 ; i >= 0; i--){
                ((ImageView) rightContainer.getChildAt(i)).setImageResource(0);
            }

            // Redo this logic
            // Init variables
            int n;
            int w;
            int h;
            int imageId;
            int numberId;
            // ImageView[] imageViews = new ImageView[0];

            // Left objects
            // Set number
            numberId = FetchResource.imageId(context, allData, "left_number_image");
            loadImage(leftNumber, numberId);

            // Set data
            n = allData.getInt("number_of_left_items");
            w = CONTAINER_WIDTH;
            h = CONTAINER_HEIGHT;
            imageId = FetchResource.imageId(context, allData, "left_items_item");

            // Pack squares
            SquarePacker.generate(this, leftContainer, n, w, h, imageId);

            /*
            // Get left items sound
            final String leftItemsSound = allData.getString("left_items_sound");
            // Add listeners to image views
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (touchEnabled) {
                            playSound(leftItemsSound, null);
                        }
                    }
                });
            }
            */

            // Right objects
            // Set number
            numberId = FetchResource.imageId(context, allData, "right_number_image");
            loadImage(rightNumber, numberId);

            // Set data
            n = allData.getInt("number_of_right_items");
            w = CONTAINER_WIDTH;
            h = CONTAINER_HEIGHT;
            imageId = FetchResource.imageId(context, allData, "right_items_item");

            // Pack squares
            SquarePacker.generate(this, rightContainer, n, w, h, imageId);

            /*
            // Get right items sound
            final String rightItemsSound = allData.getString("right_items_sound");
            // Add listeners to image views
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (touchEnabled) {
                            playSound(rightItemsSound, null);
                        }
                    }
                });
            }
            */
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayNumber(){
        try{
            String sound = allData.getString("number_of_left_items_sound");
            if (segment == 2) {
                sound = allData.getString("number_of_right_items_sound");
            }
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    if (segment == 1) {
                        sayAnd();
                    } else {
                        sayWhich();
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void sayAnd() {
        try {
            segment = 2;
            String sound = allData.getString("and_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumber();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sayWhich() {
        try {
            segment = 2;
            String sound = allData.getString("more_of_question");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayTouch();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sayTouch() {
        try {
            segment = 2;
            String sound = allData.getString("touch_the_number");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    touchEnabled = true;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
