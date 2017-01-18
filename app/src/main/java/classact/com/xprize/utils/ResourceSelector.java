package classact.com.xprize.utils;

import android.content.Context;

import java.util.Random;

import classact.com.xprize.R;

/**
 * Created by Tseliso on 11/3/2016.
 */

public class ResourceSelector {
    private static int languageId = 1;

    public static int getPositiveAffirmationSound(Context context){
        Random rand = new Random();
        if (languageId == 1) {
            switch (rand.nextInt(12)) {
                case 1:
                    return R.raw.amazing;
                case 2:
                    return R.raw.awesome;
                case 3:
                    return R.raw.good_job;
                case 4:
                    return R.raw.i_like_it;
                case 5:
                    return R.raw.nice;
                case 6:
                    return R.raw.number_1;
                case 7:
                    return R.raw.number_1_2;
                case 8:
                    return R.raw.shine;
                case 9:
                    return R.raw.whoohoo;
                case 10:
                    return R.raw.yeah;
                default:
                    return R.raw.yippeee;
            }
        }
        else{
            switch (rand.nextInt(5)) {
                case 1:
                    return R.raw.s_amazing;
                case 2:
                    return R.raw.s_congrats1;
                case 3:
                    return R.raw.s_congrats2;
                case 4:
                    return R.raw.s_cool;
                default:
                    return R.raw.s_goodjob;
            }
        }
    }

    public static int getNegativeAffirmationSound(Context context) {
        Random rand = new Random();
        if (languageId == 1) {
            switch (rand.nextInt(5)) {
                case 1:
                    return R.raw.sorry;
                case 2:
                    return R.raw.try_again;
                case 3:
                    return R.raw.try_again_2;
                default:
                    return R.raw.uh_oh;
            }
        }
        else{
            switch (rand.nextInt(5)) {
                case 1:
                    return R.raw.s_sorry;
                case 2:
                    return R.raw.s_tryagain;
                case 3:
                    return R.raw.s_keepgoing;
                case 4:
                    return R.raw.s_goodtry2;
                default:
                    return R.raw.s_goodtry1;
            }
        }
    }
}
