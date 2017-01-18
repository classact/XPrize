package classact.com.xprize.activity.language_select;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;

public class LanguageSelectActivity extends AppCompatActivity {

    Activity activity;
    RelativeLayout layoutContainer;
    int nextBGCode;

    Button selectEnglishButton;
    Button selectKiswahiliButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        activity = this;

        Intent intent = getIntent();
        nextBGCode = intent.getIntExtra("NEXT_BG_CODE", -1);

        // get buttons
        selectEnglishButton = (Button) findViewById(R.id.language_select_english_button);
        selectKiswahiliButton = (Button) findViewById(R.id.language_select_kiswahili_button);

        // get my layout
        layoutContainer = (RelativeLayout) findViewById(R.id.language_select_container);

        // add listeners
        selectEnglishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(Languages.ENGLISH);
                fadeOutCurrentUI();
                selectEnglishButton.setOnClickListener(null);
                selectKiswahiliButton.setOnClickListener(null);
            }
        });

        selectKiswahiliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(Languages.SWAHILI);
                fadeOutCurrentUI();
                selectKiswahiliButton.setOnClickListener(null);
                selectEnglishButton.setOnClickListener(null);
            }
        });
    }

    public void setSelectedLanguage(int language) {
        if (language == Languages.SWAHILI) {
            Globals.SELECTED_LANGUAGE = Languages.SWAHILI;
        } else {
            Globals.SELECTED_LANGUAGE = Languages.ENGLISH;
        }
        updateNextBG(language);
    }

    public void updateNextBG(int language) {

        RelativeLayout bg = (RelativeLayout) findViewById(R.id.activity_language_select);

        if (language == Languages.SWAHILI) {
            switch (nextBGCode) {
                case Code.INTRO:
                    bg.setBackgroundResource(R.drawable.tutorial_intro_bg_swahili);
                    break;
                case Code.TUT:
                    bg.setBackgroundResource(R.drawable.tutorial_bg_empty);
                    break;
                case Code.MV01:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
                case Code.PHON01:
                    bg.setBackgroundResource(R.drawable.phonics_link_bg);
                    break;
                default:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
            }
        } else {
            switch (nextBGCode) {
                case Code.INTRO:
                    bg.setBackgroundResource(R.drawable.tutorial_intro_bg_english);
                    break;
                case Code.TUT:
                    bg.setBackgroundResource(R.drawable.tutorial_bg_empty);
                    break;
                case Code.MV01:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
                case Code.PHON01:
                    bg.setBackgroundResource(R.drawable.phonics_link_bg);
                    break;
                default:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
            }
        }
    }

    public void fadeOutCurrentUI() {
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                startIntro();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        fadeOut.setFillAfter(true);
        layoutContainer.startAnimation(fadeOut);
    }

    public void startIntro() {
        Intent intent = new Intent();
        setResult(Code.LANG, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}