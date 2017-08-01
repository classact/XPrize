package classact.com.xprize.common;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Dimension;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import classact.com.xprize.R;
import classact.com.xprize.locale.Languages;

public class Globals {
    // General constants
    public static final String RESOURCE_PREFIX = "android.resource://";

    // Unit/Drill-based constants
    public static final int INTRO_ID = 0;
    public static final int FINALE_ID = 21;

    public static final int PHONICS_STARTING_ID = 1;
    public static final int WORDS_STARTING_ID = 10;
    public static final int STORY_STARTING_ID = 16;
    public static final int MATHS_STARTING_ID = 17;

    public static final int PHONICS_MAX_SUB_ID = 2;
    public static final int WORDS_MAX_SUB_ID = 0;
    public static final int STORY_MAX_SUB_ID = 0;
    public static final int MATHS_MAX_SUB_ID = 0;

    public static final int ALPHA_BASE_BOT = 0;
    public static final int ALPHA_BASE_MID_BOT = 1;
    public static final int ALPHA_BASE_MID_TOP = 1;
    public static final int ALPHA_BASE_TOP = 2;

    public static int checkAlphaBase(String letter) {
        int alphaBase = 0;
        if (letter.matches("[abcdefhiklmnorstuvwxz]")) {
            alphaBase = ALPHA_BASE_BOT;
        } else if (letter.matches("[i]")) {
            alphaBase = ALPHA_BASE_MID_BOT;
        } else if (letter.matches("[j]")) {
            alphaBase = ALPHA_BASE_MID_TOP;
        } else if (letter.matches("[gpqy]")) {
            alphaBase = ALPHA_BASE_TOP;
        }
        return alphaBase;
    }

    public static final String[] EN_ALPHABET = {"A","B","C","D","E","F","G","H","I",
        "J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    // Determined values
    public static int SELECTED_LANGUAGE = Languages.ENGLISH;
    public static String ACTIVE_SESSION_TIME = "1900-01-01 00:00:00";

    // Methods
    public final static String STANDARD_DATE_TIME_STRING(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH-mm-ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static Snackbar bugBar(View view, String type, String subject) {
        Snackbar bugBar = Snackbar.make(view, "Bugged " + type + ": " + "\"" + subject + "\"", Snackbar.LENGTH_INDEFINITE);
        bugBar.setAction("CLOSE", new BugBarListener(bugBar));
        bugBar.setActionTextColor(view.getResources().getColor(android.R.color.holo_blue_light, null));
        return bugBar;
    }

    private static class BugBarListener implements View.OnClickListener {

        private Snackbar bugBar;

        private BugBarListener(Snackbar bugBar) {
            this.bugBar = bugBar;
        }

        @Override
        public void onClick(View v) {
            bugBar.dismiss();
        }
    }

    public static final int TO_MAIN = 9999;

    private static MediaPlayer mBackgroundMusicPlayer;

    public static Typeface TYPEFACE_EDU_AID(AssetManager assets) {
        return Typeface.createFromAsset(assets, "fonts/edu_aid_gr1_solid.ttf");
    }

    public static int TEXT_MEASURED_WIDTH(TextView textView, String text) {
        textView.setText(text);
        textView.measure(0, 0);
        return textView.getMeasuredWidth();
    }

    public static Point TEXT_MEASURED_SIZE(TextView textView, String text) {
        textView.setText(text);
        textView.measure(0, 0);

        Typeface textViewTypeface = textView.getTypeface();
        float textViewTextSize = textView.getTextSize();
        float textPaddingWidth = textView.getPaddingLeft() + textView.getPaddingRight();
        float textPaddingHeight = textView.getPaddingTop() + textView.getPaddingBottom();

        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTypeface(textViewTypeface);
        p.setTextSize(textViewTextSize);
        p.getTextBounds(text, 0, text.length(), bounds);

        float textWidth = textView.getMeasuredWidth();
        float textRawWidth = textWidth - textPaddingWidth;

        float textBoundsWidth = bounds.width();
        float textBoundsHeight = bounds.height();

        float textBoldDiff = textRawWidth - textBoundsWidth;
        float textRawHeight = textBoundsHeight + textBoldDiff;
        float textHeight = textRawHeight + textPaddingHeight;

        System.out.println(
                "bw:" + textBoundsWidth +
                ",bh:" + textBoundsHeight +
                ",trw:" + textRawWidth +
                ",twh:" + textRawHeight +
                ",tw:" + textWidth +
                ",th:" + textHeight);

        Point textSize = new Point((int) textWidth, (int) textHeight);
        return textSize;
    }

    public static void SET_VOLUME(Context context, int volume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    public static void PLAY_BACKGROUND_MUSIC(Context context) {
        if (mBackgroundMusicPlayer == null) {
            mBackgroundMusicPlayer = mBackgroundMusicPlayer.create(context, R.raw.bg_music_001);
        }
        mBackgroundMusicPlayer.setLooping(true);
        mBackgroundMusicPlayer.start();
    }

    public static void PAUSE_BACKGROUND_MUSIC(Context context) {
        if (mBackgroundMusicPlayer == null) {
            System.err.println("Background Music Player: Cannot pause, is null");
            return;
        }
        mBackgroundMusicPlayer.pause();
    }

    public static void RESUME_BACKGROUND_MUSIC(Context context) {
        if (mBackgroundMusicPlayer == null) {
            PLAY_BACKGROUND_MUSIC(context);
            return;
        }
        if (!mBackgroundMusicPlayer.isPlaying()) {
            mBackgroundMusicPlayer.start();
        }
    }

    public static void STOP_BACKGROUND_MUSIC(Context context) {
        if (mBackgroundMusicPlayer == null) {
            System.err.println("Background Music Player: Cannot stop, is null");
            return;
        }
        mBackgroundMusicPlayer.stop();
        mBackgroundMusicPlayer.reset();
        mBackgroundMusicPlayer.release();
        mBackgroundMusicPlayer = null;
    }

    public static void playStarWorks(Context context, View v) {
        ParticleSystem ps1 = new ParticleSystem((Activity) context, 6, R.drawable.star_particle_001_l, 1000);
        ps1.setScaleRange(0.7f, 1.3f);
        ps1.setSpeedRange(0.1f, 0.25f);
        ps1.setRotationSpeedRange(180, 360);
        ps1.setFadeOut(500, new AccelerateInterpolator());
        ps1.oneShot(v, 6);

        ParticleSystem ps2 = new ParticleSystem((Activity) context, 8, R.drawable.star_particle_001_m, 1000);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.15f, 0.3f);
        ps2.setRotationSpeedRange(90, 270);
        ps2.setFadeOut(375, new AccelerateInterpolator());
        ps2.oneShot(v, 8);

        ParticleSystem ps3 = new ParticleSystem((Activity) context, 12, R.drawable.star_particle_001_s, 1000);
        ps3.setScaleRange(0.7f, 1.3f);
        ps3.setSpeedRange(0.2f, 0.35f);
        ps3.setRotationSpeedRange(45, 180);
        ps3.setFadeOut(250, new AccelerateInterpolator());
        ps3.oneShot(v, 12);
    }

    public static void playStarWorks(Context context, View v, int numOfSmallParticles, int numOfMediumParticles, int numOfLargeParticles) {

        ParticleSystem particlesSmall = new ParticleSystem((Activity) context, numOfSmallParticles, R.drawable.star_particle_001_s, 1000);
        particlesSmall.setScaleRange(0.7f, 1.3f);
        particlesSmall.setSpeedRange(0.2f, 0.35f);
        particlesSmall.setRotationSpeedRange(45, 180);
        particlesSmall.setFadeOut(250, new AccelerateInterpolator());
        particlesSmall.oneShot(v, numOfSmallParticles);

        ParticleSystem particlesMedium = new ParticleSystem((Activity) context, numOfMediumParticles, R.drawable.star_particle_001_m, 1000);
        particlesMedium.setScaleRange(0.7f, 1.3f);
        particlesMedium.setSpeedRange(0.15f, 0.3f);
        particlesMedium.setRotationSpeedRange(90, 270);
        particlesMedium.setFadeOut(375, new AccelerateInterpolator());
        particlesMedium.oneShot(v, numOfMediumParticles);

        ParticleSystem particlesLarge = new ParticleSystem((Activity) context, numOfLargeParticles, R.drawable.star_particle_001_l, 1000);
        particlesLarge.setScaleRange(0.7f, 1.3f);
        particlesLarge.setSpeedRange(0.1f, 0.25f);
        particlesLarge.setRotationSpeedRange(180, 360);
        particlesLarge.setFadeOut(500, new AccelerateInterpolator());
        particlesLarge.oneShot(v, numOfLargeParticles);
    }
}
