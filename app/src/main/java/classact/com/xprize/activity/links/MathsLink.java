package classact.com.xprize.activity.links;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.template.LinkTemplate;

public class MathsLink extends LinkTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_maths_link);

        // Note that no need to localize Bg
        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            super.mResourceId = R.raw.sw_maths_link;
        } else {
            // Fallback to English
            super.mResourceId = R.raw.en_maths_link;
        }

        super.mActivityName = "MathsLink";
        super.mNextActivityClassName = null; // PhonicsLink.class.getName();
    }

    @Override
    public void finishIntent() {
        Intent intent = new Intent();
        setResult(Code.MATH01, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}