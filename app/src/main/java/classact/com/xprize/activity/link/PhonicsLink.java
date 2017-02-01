package classact.com.xprize.activity.link;

import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.activity.template.LinkTemplate;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.FetchResource;

public class PhonicsLink extends LinkTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonics_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_phonics_link);

        // Note that no need to localize Bg
        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            super.mNarrator = FetchResource.sound(getApplicationContext(), "sw_phonics_link");
        } else {
            // Fallback to English
            super.mNarrator = FetchResource.sound(getApplicationContext(), "en_phonics_link");
        }

        super.mActivityName = "PhonicsLink";
        super.mNextActivityClassName = null;
    }

    @Override
    public void finishIntent() {
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
