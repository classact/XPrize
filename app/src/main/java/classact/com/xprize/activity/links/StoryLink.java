package classact.com.xprize.activity.links;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.template.LinkTemplate;

public class StoryLink extends LinkTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_story_link);

        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            super.mBackground.setBackgroundResource(R.drawable.sw_story_link_bg);
            super.mResourceId = R.raw.sw_story_link;
        } else {
            // Fallback to English
            super.mBackground.setBackgroundResource(R.drawable.en_story_link_bg);
            super.mResourceId = R.raw.en_story_link;
        }
        super.mActivityName = "StoryLink";
        super.mNextActivityClassName = null;
    }

    @Override
    public void finishIntent() {
        Intent intent = new Intent();
        setResult(Code.STOR01, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}