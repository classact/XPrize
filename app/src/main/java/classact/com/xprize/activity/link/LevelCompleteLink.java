package classact.com.xprize.activity.link;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.activity.template.LinkTemplate;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.FetchResource;

public class LevelCompleteLink extends LinkTemplate {

    private final String SWAHILI_PREFIX = "s_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_complete_link);

        super.mContext = getApplicationContext();
        super.mBackground = (RelativeLayout) findViewById(R.id.activity_level_complete_link);

        // Get audio file to play
        // Requires data from invoker intent
        Intent intent = getIntent();

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
    public void finishIntent() {
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
