package classact.com.clever_little_monkey.activity.link;

import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.template.LinkTemplate;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.locale.Languages;
import classact.com.clever_little_monkey.utils.FetchResource;

public class WordsLink extends LinkTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_words_link);

        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            super.mBackground.setBackgroundResource(R.drawable.sw_words_link_bg);
            super.mNarrator = FetchResource.sound(getApplicationContext(), "sw_words_link");
        } else {
            // Fallback to English
            super.mBackground.setBackgroundResource(R.drawable.en_words_link_bg);
            super.mNarrator = FetchResource.sound(getApplicationContext(), "en_words_link");
        }
        super.mActivityName = "WordsLink";
        super.mNextActivityClassName = null;
    }

    @Override
    public void finishIntent() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
