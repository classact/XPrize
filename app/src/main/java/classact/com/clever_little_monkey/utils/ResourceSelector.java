package classact.com.clever_little_monkey.utils;

import android.content.Context;

import java.util.Random;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.locale.Languages;

/**
 * Created by Tseliso on 11/3/2016.
 */

public class ResourceSelector {

    public static int getPositiveAffirmationSound(Context context){
        Random rand = new Random();
        int resourceId = 0;

        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            switch (rand.nextInt(5)) {
                case 0:
                    resourceId = R.raw.s_amazing;
                    break;
                case 1:
                    resourceId = R.raw.s_congrats1;
                    break;
                case 2:
                    resourceId = R.raw.s_congrats2;
                    break;
                case 3:
                    resourceId = R.raw.s_cool;
                    break;
                case 4:
                    resourceId = R.raw.s_goodjob;
                    break;
                default:
                    System.err.println("ResourceSelector.getPositiveAffirmationSound > Error(SWAHILI): " +
                            "random number does not correspond to Resource Id options");
                    break;
            }
        } else {
            switch (rand.nextInt(11)) {
                case 0:
                    resourceId = R.raw.amazing;
                    break;
                case 1:
                    resourceId = R.raw.awesome;
                    break;
                case 2:
                    resourceId = R.raw.good_job;
                    break;
                case 3:
                    resourceId = R.raw.i_like_it;
                    break;
                case 4:
                    resourceId = R.raw.nice;
                    break;
                case 5:
                    resourceId = R.raw.number_1;
                    break;
                case 6:
                    resourceId = R.raw.number_1_2;
                    break;
                case 7:
                    resourceId = R.raw.shine;
                    break;
                case 8:
                    resourceId = R.raw.whoohoo;
                    break;
                case 9:
                    resourceId = R.raw.yeah;
                    break;
                case 10:
                    resourceId = R.raw.yippeee;
                    break;
                default:
                    System.err.println("ResourceSelector.getPositiveAffirmationSound > Error(ENGLISH): " +
                            "random number does not correspond to Resource Id options");
                    break;
            }
        }
        return resourceId;
    }

    public static int getNegativeAffirmationSound(Context context) {
        Random rand = new Random();
        int resourceId = 0;

        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            switch (rand.nextInt(5)) {
                case 0:
                    resourceId = R.raw.s_sorry;
                    break;
                case 1:
                    resourceId = R.raw.s_tryagain;
                    break;
                case 2:
                    resourceId = R.raw.s_keepgoing;
                    break;
                case 3:
                    resourceId = R.raw.s_goodtry2;
                    break;
                case 4:
                    resourceId = R.raw.s_goodtry1;
                    break;
                default:
                    System.err.println("ResourceSelector.getNegativeAffirmationSound > Error(SWAHILI): " +
                            "random number does not correspond to Resource Id options");
                    break;
            }
        }
        else{
            switch (rand.nextInt(4)) {
                case 0:
                    resourceId = R.raw.sorry;
                    break;
                case 1:
                    resourceId = R.raw.try_again;
                    break;
                case 2:
                    resourceId = R.raw.try_again_2;
                    break;
                case 3:
                    resourceId = R.raw.uh_oh;
                    break;
                default:
                    System.err.println("ResourceSelector.getNegativeAffirmationSound > Error(ENGLISH): " +
                            "random number does not correspond to Resource Id options");
                    break;
            }
        }
        return resourceId;
    }
}
