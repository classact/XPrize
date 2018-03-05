package classact.com.clever_little_monkey.activity.link;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.template.LinkTemplate;
import classact.com.clever_little_monkey.common.Code;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.locale.Languages;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.ResourceDecoder;

public class LevelCompleteLink extends LinkTemplate {

    private final String SWAHILI_PREFIX = "s_";

    private String mPrevLevelBackground;
    private String mNextLevelBackground;

    private RelativeLayout mNewBackground;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_complete_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_level_complete_link);

        // Get audio file to play
        // Requires data from invoker intent
        Intent intent = getIntent();

        mPrevLevelBackground = intent.getStringExtra(Code.PREV_BG_RES);
        mNextLevelBackground = intent.getStringExtra(Code.NEXT_BG_RES);

        int nextLevelBackgroundResourceId = ResourceDecoder.getIdentifier(THIS,
                mNextLevelBackground, "drawable");

        if (mPrevLevelBackground == null) {
            mBackground.setBackgroundResource(nextLevelBackgroundResourceId);
        } else {
            int prevLevelBackgroundResourceId = ResourceDecoder.getIdentifier(THIS,
                    mPrevLevelBackground, "drawable");
            mBackground.setBackgroundResource(prevLevelBackgroundResourceId);

            mNewBackground = new RelativeLayout(THIS);
            RelativeLayout.LayoutParams newBackgroundLP = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            mNewBackground.setLayoutParams(newBackgroundLP);
            mNewBackground.setBackgroundResource(nextLevelBackgroundResourceId);
            mNewBackground.setAlpha(0f);
            mBackground.addView(mNewBackground);
            mNewBackground.animate()
                    .setStartDelay(1325L)
                    .alpha(1f)
                    .setDuration(1250)
                    .setInterpolator(new LinearInterpolator());
        }

        // Append appropriate language identifier if required
        // (Default is English)
        // * Note that no need to localize Bg
        String resourceName = intent.getStringExtra(Code.RES_NAME);
        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            resourceName = SWAHILI_PREFIX + resourceName;
        }

        // Get resource id using resource name
        super.mNarrator = FetchResource.sound(getApplicationContext(), resourceName);

        // Set activity name and next-activity class name
        super.mActivityName = "LevelCompleteLink";
        super.mNextActivityClassName = null;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mNewBackground != null) {
            mNewBackground.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(new LinearInterpolator());
        }
    }

    @Override
    public void finishIntent() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
