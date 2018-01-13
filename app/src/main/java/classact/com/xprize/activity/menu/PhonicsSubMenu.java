package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import classact.com.xprize.R;
import classact.com.xprize.activity.MenuActivity;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.model.UnitSection;
import dagger.android.support.DaggerAppCompatActivity;

public class PhonicsSubMenu extends MenuActivity {

    private TextView mChapterTitle;
    private TextView mChapterNumber;
    private TextView mChapterSection;
    private ImageView mChapterHeader;

    private LinkedHashMap<Integer, Integer> mChapterHeaderImageResources;

    private LinkedHashMap<String, Integer> mSelectedLetterResource;
    private LinkedHashMap<String, Integer> mNonSelectedLetterResource;
    private LinkedHashMap<String, Integer> mSelectedSoundResource;
    private LinkedHashMap<String, Integer> mNonSelectedSoundResource;
    private TextView mSelectionInstruction;
    private ImageButton mButtonA;
    private ImageButton mButtonB;
    private ImageButton mMonkeyA;
    private ImageButton mMonkeyB;

    private Intent mIntent;
    private int mSelectedChapter;
    private int mSelectedSection;
    private int mSelectedSubId;
    private ConstraintLayout mRootView;
    private boolean mFinishActivity;

    @Inject DatabaseController mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonics_sub_menu);
        mIntent = getIntent();
        mSelectedChapter = mIntent.getIntExtra("selected_chapter", 0);
        mSelectedSection = mIntent.getIntExtra("selected_section", 1);
        mRootView = (ConstraintLayout) findViewById(R.id.activity_phonics_sub_menu);
        // mRootView.setBackgroundResource(R.drawable.bg_yellow);
        mFinishActivity = false;

        mChapterHeaderImageResources = new LinkedHashMap<>();
        mChapterTitle = (TextView) findViewById(R.id.chapter_title);
        mChapterNumber = (TextView) findViewById(R.id.chapter_number);
        mChapterSection = (TextView) findViewById(R.id.chapter_section);
        mChapterHeader = (ImageView) findViewById(R.id.chapter_header);

        mSelectedLetterResource = new LinkedHashMap<>();
        mNonSelectedLetterResource = new LinkedHashMap<>();
        mSelectedSoundResource = new LinkedHashMap<>();
        mNonSelectedSoundResource = new LinkedHashMap<>();
        mSelectionInstruction = (TextView) findViewById(R.id.selection_instruction);

        mButtonA = (ImageButton) findViewById(R.id.button_A);
        mButtonB = (ImageButton) findViewById(R.id.button_B);
        mMonkeyA = (ImageButton) findViewById(R.id.monkey_A);
        mMonkeyB = (ImageButton) findViewById(R.id.monkey_B);

        // Setup selected letter resources
        mSelectedLetterResource.put("a", R.drawable.letter_selected_a_button);
        mSelectedLetterResource.put("b", R.drawable.letter_selected_b_button);
        mSelectedLetterResource.put("c", R.drawable.letter_selected_c_button);
        mSelectedLetterResource.put("d", R.drawable.letter_selected_d_button);
        mSelectedLetterResource.put("e", R.drawable.letter_selected_e_button);
        mSelectedLetterResource.put("f", R.drawable.letter_selected_f_button);
        mSelectedLetterResource.put("g", R.drawable.letter_selected_g_button);
        mSelectedLetterResource.put("h", R.drawable.letter_selected_h_button);
        mSelectedLetterResource.put("i", R.drawable.letter_selected_i_button);
        mSelectedLetterResource.put("j", R.drawable.letter_selected_j_button);
        mSelectedLetterResource.put("k", R.drawable.letter_selected_k_button);
        mSelectedLetterResource.put("l", R.drawable.letter_selected_l_button);
        mSelectedLetterResource.put("m", R.drawable.letter_selected_m_button);
        mSelectedLetterResource.put("n", R.drawable.letter_selected_n_button);
        mSelectedLetterResource.put("o", R.drawable.letter_selected_o_button);
        mSelectedLetterResource.put("p", R.drawable.letter_selected_p_button);
        mSelectedLetterResource.put("q", R.drawable.letter_selected_q_button);
        mSelectedLetterResource.put("r", R.drawable.letter_selected_r_button);
        mSelectedLetterResource.put("s", R.drawable.letter_selected_s_button);
        mSelectedLetterResource.put("t", R.drawable.letter_selected_t_button);
        mSelectedLetterResource.put("u", R.drawable.letter_selected_u_button);
        mSelectedLetterResource.put("v", R.drawable.letter_selected_v_button);
        mSelectedLetterResource.put("w", R.drawable.letter_selected_w_button);
        mSelectedLetterResource.put("x", R.drawable.letter_selected_x_button);
        mSelectedLetterResource.put("y", R.drawable.letter_selected_y_button);
        mSelectedLetterResource.put("z", R.drawable.letter_selected_z_button);

        // Setup non-selected letter resources
        mNonSelectedLetterResource.put("a", R.drawable.letter_a_button);
        mNonSelectedLetterResource.put("b", R.drawable.letter_b_button);
        mNonSelectedLetterResource.put("c", R.drawable.letter_c_button);
        mNonSelectedLetterResource.put("d", R.drawable.letter_d_button);
        mNonSelectedLetterResource.put("e", R.drawable.letter_e_button);
        mNonSelectedLetterResource.put("f", R.drawable.letter_f_button);
        mNonSelectedLetterResource.put("g", R.drawable.letter_g_button);
        mNonSelectedLetterResource.put("h", R.drawable.letter_h_button);
        mNonSelectedLetterResource.put("i", R.drawable.letter_i_button);
        mNonSelectedLetterResource.put("j", R.drawable.letter_j_button);
        mNonSelectedLetterResource.put("k", R.drawable.letter_k_button);
        mNonSelectedLetterResource.put("l", R.drawable.letter_l_button);
        mNonSelectedLetterResource.put("m", R.drawable.letter_m_button);
        mNonSelectedLetterResource.put("n", R.drawable.letter_n_button);
        mNonSelectedLetterResource.put("o", R.drawable.letter_o_button);
        mNonSelectedLetterResource.put("p", R.drawable.letter_p_button);
        mNonSelectedLetterResource.put("q", R.drawable.letter_q_button);
        mNonSelectedLetterResource.put("r", R.drawable.letter_r_button);
        mNonSelectedLetterResource.put("s", R.drawable.letter_s_button);
        mNonSelectedLetterResource.put("t", R.drawable.letter_t_button);
        mNonSelectedLetterResource.put("u", R.drawable.letter_u_button);
        mNonSelectedLetterResource.put("v", R.drawable.letter_v_button);
        mNonSelectedLetterResource.put("w", R.drawable.letter_w_button);
        mNonSelectedLetterResource.put("x", R.drawable.letter_x_button);
        mNonSelectedLetterResource.put("y", R.drawable.letter_y_button);
        mNonSelectedLetterResource.put("z", R.drawable.letter_z_button);

        // Setup selected sound resources
        mSelectedSoundResource.put("a-e", R.drawable.sound_selected_ae_button);
        mSelectedSoundResource.put("ai", R.drawable.sound_selected_ai_button);
        mSelectedSoundResource.put("ay", R.drawable.sound_selected_ay_button);
        mSelectedSoundResource.put("ch", R.drawable.sound_selected_ch_button);
        mSelectedSoundResource.put("ck", R.drawable.sound_selected_ck_button);
        mSelectedSoundResource.put("ea", R.drawable.sound_selected_ea_button);
        mSelectedSoundResource.put("ee", R.drawable.sound_selected_ee_button);
        mSelectedSoundResource.put("i-e", R.drawable.sound_selected_ie_button);
        mSelectedSoundResource.put("o-e", R.drawable.sound_selected_oe_button);
        mSelectedSoundResource.put("oo", R.drawable.sound_selected_oo_button);
        mSelectedSoundResource.put("sh", R.drawable.sound_selected_sh_button);
        mSelectedSoundResource.put("th", R.drawable.sound_selected_th_button);
        mSelectedSoundResource.put("u-e", R.drawable.sound_selected_ue_button);
        mSelectedSoundResource.put("wh", R.drawable.sound_selected_wh_button);

        // Setup non-selected sound resources
        mNonSelectedSoundResource.put("a-e", R.drawable.sound_ae_button);
        mNonSelectedSoundResource.put("ai", R.drawable.sound_ai_button);
        mNonSelectedSoundResource.put("ay", R.drawable.sound_ay_button);
        mNonSelectedSoundResource.put("ch", R.drawable.sound_ch_button);
        mNonSelectedSoundResource.put("ck", R.drawable.sound_ck_button);
        mNonSelectedSoundResource.put("ea", R.drawable.sound_ea_button);
        mNonSelectedSoundResource.put("ee", R.drawable.sound_ee_button);
        mNonSelectedSoundResource.put("i-e", R.drawable.sound_ie_button);
        mNonSelectedSoundResource.put("o-e", R.drawable.sound_oe_button);
        mNonSelectedSoundResource.put("oo", R.drawable.sound_oo_button);
        mNonSelectedSoundResource.put("sh", R.drawable.sound_sh_button);
        mNonSelectedSoundResource.put("th", R.drawable.sound_th_button);
        mNonSelectedSoundResource.put("u-e", R.drawable.sound_ue_button);
        mNonSelectedSoundResource.put("wh", R.drawable.sound_wh_button);

        // Setup chapter header image resources
        mChapterHeaderImageResources.put(1, R.drawable.chapter_01_header);
        mChapterHeaderImageResources.put(2, R.drawable.chapter_02_header);
        mChapterHeaderImageResources.put(3, R.drawable.chapter_03_header);
        mChapterHeaderImageResources.put(4, R.drawable.chapter_04_header);
        mChapterHeaderImageResources.put(5, R.drawable.chapter_05_header);
        mChapterHeaderImageResources.put(6, R.drawable.chapter_06_header);
        mChapterHeaderImageResources.put(7, R.drawable.chapter_07_header);
        mChapterHeaderImageResources.put(8, R.drawable.chapter_08_header);
        mChapterHeaderImageResources.put(9, R.drawable.chapter_09_header);
        mChapterHeaderImageResources.put(10, R.drawable.chapter_10_header);
        mChapterHeaderImageResources.put(11, R.drawable.chapter_11_header);
        mChapterHeaderImageResources.put(12, R.drawable.chapter_12_header);
        mChapterHeaderImageResources.put(13, R.drawable.chapter_13_header);
        mChapterHeaderImageResources.put(14, R.drawable.chapter_14_header);
        mChapterHeaderImageResources.put(15, R.drawable.chapter_15_header);
        mChapterHeaderImageResources.put(16, R.drawable.chapter_16_header);
        mChapterHeaderImageResources.put(17, R.drawable.chapter_17_header);
        mChapterHeaderImageResources.put(18, R.drawable.chapter_18_header);
        mChapterHeaderImageResources.put(19, R.drawable.chapter_19_header);
        mChapterHeaderImageResources.put(20, R.drawable.chapter_20_header);

        // Set type face
        AssetManager assets = getAssets();
        mChapterTitle.setTypeface(Globals.TYPEFACE_EDU_AID(assets));
        mChapterNumber.setTypeface(Globals.TYPEFACE_EDU_AID(assets));
        mChapterSection.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mSelectionInstruction.setTypeface(Globals.TYPEFACE_EDU_AID(assets));

        // Setup sub id button map
        LinkedHashMap<Integer, ImageButton> subIdButtonMap = new LinkedHashMap<>();
        subIdButtonMap.put(1, mButtonA);
        subIdButtonMap.put(2, mButtonB);

        // Get letter map and selected sub id
        LinkedHashMap<Integer, String> subIdLetterMap = new LinkedHashMap<>();
        mSelectedSubId = 1;
        SparseArray<UnitSection> unitSectionMap = mDb.getUnitSections(mSelectedChapter, mSelectedSection);
        for (int i = 0; i < unitSectionMap.size(); i++) {
            int key = unitSectionMap.keyAt(i);
            UnitSection unitSection = unitSectionMap.get(key);
            int subId = unitSection.getSectionSubId();
            String subject = unitSection.getSectionSubject();
            subIdLetterMap.put(subId, subject);
            if (unitSection.getUnlocked() == 1) {
                ImageButton button = subIdButtonMap.get(subId);
                button.setEnabled(true);
                if (unitSection.getInProgress() == 1) {
                    mSelectedSubId = subId;
                }
            } else {
                ImageButton button = subIdButtonMap.get(subId);
                button.setEnabled(false);
            }
        }

        final String A = subIdLetterMap.get(1);
        final String B = subIdLetterMap.get(2);
        String c = subIdLetterMap.get(mSelectedSubId);
        setSelected(A, B, c);

        // Button click listeners
        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Button touch listeners
        mButtonA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        String c = String.valueOf(A);
                        setSelected(A, B, c);
                        if (mMonkeyA.getVisibility() == View.VISIBLE) {
                            mMonkeyA.setPressed(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mMonkeyA.getVisibility() == View.VISIBLE) {
                            mMonkeyA.setPressed(false);
                        }

                        UnitSection selectedUnitSection = mDb.getUnitSection(mSelectedChapter, mSelectedSection, 1);

                        Intent intent = new Intent(context, DrillsMenu.class);
                        intent.putExtra("selected_chapter", mSelectedChapter);
                        intent.putExtra("selected_section", mSelectedSection);
                        intent.putExtra("selected_unit_section", selectedUnitSection.getUnitSectionId());
                        intent.putExtra("selected_sub_id", 1);
                        intent.putExtra("selected_letter", A);
                        mFinishActivity = true;
                        startActivityForResult(intent, 0);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mButtonB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        String c = String.valueOf(B);
                        setSelected(A, B, c);
                        if (mMonkeyB.getVisibility() == View.VISIBLE) {
                            mMonkeyB.setPressed(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mMonkeyB.getVisibility() == View.VISIBLE) {
                            mMonkeyB.setPressed(false);
                        }

                        UnitSection selectedUnitSection = mDb.getUnitSection(mSelectedChapter, mSelectedSection, 2);

                        Intent intent = new Intent(context, DrillsMenu.class);
                        intent.putExtra("selected_chapter", mSelectedChapter);
                        intent.putExtra("selected_section", mSelectedSection);
                        intent.putExtra("selected_unit_section", selectedUnitSection.getUnitSectionId());
                        intent.putExtra("selected_sub_id", 2);
                        intent.putExtra("selected_letter", B);
                        mFinishActivity = true;
                        startActivityForResult(intent, 0);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        // Set chapter header
        mChapterTitle.setText(R.string.Chapter);
        mChapterNumber.setText(String.valueOf(mSelectedChapter));
        mChapterSection.setText(mDb.getSections().get(mSelectedSection).getName());
        mChapterHeader.setImageResource(mChapterHeaderImageResources.get(mSelectedChapter));
    }

    public void setSelected(String a, String b, String c) {
        // Init resources
        int resourceA = 0;
        int resourceB = 0;
        boolean soundAFound = false;
        boolean soundBFound = false;

        System.out.println("A B C: " + a + ", " + b + ", " + c);

        // Check if "a" is selected
        if (a.equalsIgnoreCase(c)) {
            // Check if "a" is a sound
            if (mSelectedSoundResource.containsKey(a)) {
                soundAFound = true;
                resourceA = mSelectedSoundResource.get(a);
            } else {
                resourceA = mSelectedLetterResource.get(a);
            }
            // Check if "b" is a sound
            if (mSelectedSoundResource.containsKey(b)) {
                soundBFound = true;
                resourceB = mNonSelectedSoundResource.get(b);
            } else {
                resourceB = mNonSelectedLetterResource.get(b);
            }
            mMonkeyA.setVisibility(View.VISIBLE);
            mMonkeyB.setVisibility(View.INVISIBLE);
        } else if (b.equalsIgnoreCase(c)) {
            // Check if "a" is a sound
            if (mSelectedSoundResource.containsKey(a)) {
                soundAFound = true;
                resourceA = mNonSelectedSoundResource.get(a);
            } else {
                resourceA = mNonSelectedLetterResource.get(a);
            }
            // Check if "b" is a sound
            if (mSelectedSoundResource.containsKey(b)) {
                soundBFound = true;
                resourceB = mSelectedSoundResource.get(b);
            } else {
                resourceB = mSelectedLetterResource.get(b);
            }
            mMonkeyA.setVisibility(View.INVISIBLE);
            mMonkeyB.setVisibility(View.VISIBLE);
        }
        mButtonA.setBackgroundResource(resourceA);
        mButtonB.setBackgroundResource(resourceB);

        if (soundAFound && soundBFound) {
            mSelectionInstruction.setText("Choose a sound !");
        } else if (!(soundAFound || soundBFound)) {
            mSelectionInstruction.setText("Choose a letter !");
        } else {
            mSelectionInstruction.setText("Choose a letter or sound !");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("PhonicsSubMenu: " + requestCode + "," + resultCode);
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case Code.DRILL_SPLASH:
            case Code.INTRO:
            case Code.TUTORIAL:
            case Code.MOVIE:
            case Code.RUN_DRILL:
            case Code.CHAPTER_END:
            case Code.FINALE:
                setResult(resultCode);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFinishActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(context);
        } else {
            mFinishActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mFinishActivity) {
            Globals.PAUSE_BACKGROUND_MUSIC(context);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();
        if (action == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mFinishActivity = true;
        // setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}