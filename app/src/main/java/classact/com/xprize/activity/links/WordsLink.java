package classact.com.xprize.activity.links;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.template.LinkTemplate;

public class WordsLink extends LinkTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_words_link);

        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            super.mBackground.setBackgroundResource(R.drawable.sw_words_link_bg);
            super.mResourceId = R.raw.sw_words_link;
        } else {
            // Fallback to English
            super.mBackground.setBackgroundResource(R.drawable.en_words_link_bg);
            super.mResourceId = R.raw.en_words_link;
        }
        super.mActivityName = "WordsLink";
        super.mNextActivityClassName = null;
    }

    @Override
    public void finishIntent() {
        Intent intent = new Intent();
        setResult(Code.WORD01, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
