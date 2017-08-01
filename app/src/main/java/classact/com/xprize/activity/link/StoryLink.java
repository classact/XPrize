package classact.com.xprize.activity.link;

import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.activity.template.LinkTemplate;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.FetchResource;

public class StoryLink extends LinkTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_story_link);

        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            super.mBackground.setBackgroundResource(R.drawable.sw_story_link_bg);
            super.mNarrator = FetchResource.sound(getApplicationContext(), "sw_story_link");
        } else {
            // Fallback to English
            super.mBackground.setBackgroundResource(R.drawable.en_story_link_bg);
            super.mNarrator = FetchResource.sound(getApplicationContext(), "en_story_link");
        }
        super.mActivityName = "StoryLink";
        super.mNextActivityClassName = null;
    }

    @Override
    public void finishIntent() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}