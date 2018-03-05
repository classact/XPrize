package classact.com.clever_little_monkey.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import classact.com.clever_little_monkey.common.Globals;

/**
 * Created by hcdjeong on 2017/06/16.
 */

public class WordLetterLayout {
    public static List<ImageView> level(
            Context context,
            List<ImageView> letterViews,
            List<Integer> letterResources,
            String word,
            DisplayMetrics displayMetrics,
            float letterWidth,
            float letterScale,
            boolean evenWidths) {

        // Get letters
        System.out.println(word);
        List<String> letters = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            letters.add("" + word.charAt(i));
        }
        System.out.println(letters.size());

        // Setup list of top and bot y coords
        LinkedList<Integer> yTopList = new LinkedList<>();
        LinkedList<Integer> yMidTopList = new LinkedList<>();
        LinkedList<Integer> yMidBotList = new LinkedList<>();
        LinkedList<Integer> yBotList = new LinkedList<>();

        // Get screen density and screen width
        float density = displayMetrics.density;
        int screenWidth = displayMetrics.widthPixels;

        // Letter count
        int letterCount = 0;
        // Get top and boy y coords where black starts / ends
        for (int i = 0; i < letterViews.size(); i++) {
            ImageView iv = letterViews.get(i);
            MarginLayoutParams ivLayout = (MarginLayoutParams) iv.getLayoutParams();
            iv.setScaleX(letterScale);
            iv.setScaleY(letterScale);
            ivLayout.width = (int) letterWidth;
            ivLayout.height = (int) letterWidth;
            int leftMargin = (int) ((float) ivLayout.leftMargin * letterScale * density);
            ivLayout.leftMargin = leftMargin;

            Drawable d = iv.getDrawable();
            if (d != null) {
                // Get bitmap
                Bitmap bitmapOriginal = BitmapFactory.decodeResource(
                        context.getResources(), letterResources.get(letterCount)
                );
                // Convert bitmap to ARGB_8888
                Bitmap bitmap = bitmapOriginal.copy(Bitmap.Config.ARGB_8888, true);
                // Letter
                String letter = letters.get(letterCount++);
                // Init bitmap coordinates
                int bYTop = 0;
                int bYBot = 0;
                int bW = bitmap.getWidth();
                int bH = bitmap.getHeight();

                // Sort out even widths
                if (!evenWidths) {
                    float bitMapToLetterScale = letterWidth / bH;
                    float uniqueWidth = bW * bitMapToLetterScale;
                    ivLayout.width = (int) uniqueWidth;
                }

                // Get bitmap pixels
                int[] bp = new int[bH * bW];
                bitmap.getPixels(bp, 0, bW, 0, 0, bW, bH);
                // Find position where black / white starts
                for (int j = 0; j < bp.length; j++) {
                    float r = (float) (bp[j] & 0xff);
                    float g = (float) ((bp[j] >> 8) & 0xff);
                    float b = (float) ((bp[j] >> 16) & 0xff);
                    float a = (float) ((bp[j] >> 32) & 0xff);
                    if (a > 0) {
                        bYTop = (j+1) / bW;
                        break;
                    }
                }
                for (int j = bp.length - 1; j >= 0; j--) {
                    float r = (float) (bp[j] & 0xff);
                    float g = (float) ((bp[j] >> 8) & 0xff);
                    float b = (float) ((bp[j] >> 16) & 0xff);
                    float a = (float) ((bp[j] >> 32) & 0xff);

                    if (a > 0) {
                        bYBot = (j+1) / bW;
                        break;
                    }
                }
                float bRatio = letterWidth / bH;
                int yTop = (int) ((float) bYTop * letterScale * bRatio);
                int yBot = (int) ((float) bYBot * letterScale * bRatio);
                yTopList.add(yTop);
                yBotList.add(yBot);

                int bYMidBot = 0;
                if (Globals.checkAlphaBase(letter) == Globals.ALPHA_BASE_MID_BOT) {
                    // Detect mid y
                    boolean blackFoundOnce = false;
                    boolean whiteAgain = false;

                    int first = -1;
                    int next = -1;

                    for (int j = 0; j < bp.length; j++) {
                        float r = (float) (bp[j] & 0xff);
                        float g = (float) ((bp[j] >> 8) & 0xff);
                        float b = (float) ((bp[j] >> 16) & 0xff);
                        float a = (float) ((bp[j] >> 32) & 0xff);

                        if (a > 0 && !blackFoundOnce) {
                            blackFoundOnce = true;
                        }

                        if (a <= 0 && blackFoundOnce && !whiteAgain) {
                            whiteAgain = true;
                        }

                        if (a > 0 && whiteAgain) {
                            if (first == -1) {
                                first = (j+1) / bW;
                                blackFoundOnce = false;
                                whiteAgain = false;
                            } else {
                                next = (j+1) / bW;
                                if ((next - first) > 1) {
                                    bYMidBot = (j+1) / bW;
                                    break;
                                } else {
                                    first = next;
                                    blackFoundOnce = false;
                                    whiteAgain = false;
                                }
                            }
                        }
                    }
                    int yMidBot = (int) ((float) bYMidBot * letterScale * bRatio);
                    yMidBotList.add(yMidBot);
                } else {
                    yMidBotList.add(0);
                }

                int bYMidTop = 0;
                if (Globals.checkAlphaBase(letter) == Globals.ALPHA_BASE_MID_TOP) {
                    // Detect mid y
                    boolean blackFoundOnce = false;
                    boolean whiteAgain = false;

                    int first = -1;
                    int next = -1;

                    for (int j = 0; j < bp.length; j++) {
                        float r = (float) (bp[j] & 0xff);
                        float g = (float) ((bp[j] >> 8) & 0xff);
                        float b = (float) ((bp[j] >> 16) & 0xff);
                        float a = (float) ((bp[j] >> 32) & 0xff);

                        if (a > 0 && !blackFoundOnce) {
                            blackFoundOnce = true;
                        }

                        if (a <= 0 && blackFoundOnce && !whiteAgain) {
                            whiteAgain = true;
                        }

                        if (a > 0 && whiteAgain) {
                            if (first == -1) {
                                first = (j+1) / bW;
                                blackFoundOnce = false;
                                whiteAgain = false;
                            } else {
                                next = (j+1) / bW;
                                if ((next - first) > 1) {
                                    bYMidTop = (j+1) / bW;
                                    break;
                                } else {
                                    first = next;
                                    blackFoundOnce = false;
                                    whiteAgain = false;
                                }
                            }
                        }
                    }
                    int yMidTop = (int) ((float) bYMidTop * letterScale * bRatio);
                    yMidTopList.add(yMidTop);
                } else {
                    yMidTopList.add(0);
                }
            }
            iv.setLayoutParams(ivLayout);
        }

        letterCount = 0;
        int baseDiff = 0;
        int botBase = 0;
        for (int i = 0; i < yBotList.size(); i++) {
            String letter = letters.get(i);
            int alphaBase = Globals.checkAlphaBase(letter);
            if (alphaBase == Globals.ALPHA_BASE_BOT ||
                alphaBase == Globals.ALPHA_BASE_MID_BOT) {
                if (botBase == 0) {
                    botBase = yBotList.get(i);
                } else {
                    botBase = Math.max(botBase, yBotList.get(i));
                }
                int bDiffOffset = 0;
                if (alphaBase == Globals.ALPHA_BASE_MID_BOT) {
                    bDiffOffset = yMidBotList.get(i);
                    System.out.println("XXXXXXXXXX: " + bDiffOffset);
                }
                int bDiff = (yBotList.get(i) - yTopList.get(i)) + bDiffOffset;
                if (baseDiff == 0) {
                    baseDiff = bDiff;
                } else {
                    baseDiff = Math.min(baseDiff, bDiff);
                }
            }
        }

        letterCount = 0;
        for (int i = 0; i < letterViews.size(); i++) {
            ImageView iv = letterViews.get(i);
            MarginLayoutParams ivLayout = (MarginLayoutParams) iv.getLayoutParams();
            Drawable d = iv.getDrawable();
            if (d != null) {
                String letter = letters.get(letterCount);
                int alphaBase = Globals.checkAlphaBase(letter);

                if (alphaBase == Globals.ALPHA_BASE_BOT ||
                    alphaBase == Globals.ALPHA_BASE_MID_BOT) {
                    int letterBotBase = yBotList.get(letterCount);
                    int botBaseDiff = botBase - letterBotBase;
                    ivLayout.topMargin += botBaseDiff;
                } else if (alphaBase == Globals.ALPHA_BASE_MID_TOP) {
                    int letterTopBase = yTopList.get(letterCount);
                    int letterMidBase = yMidTopList.get(letterCount);
                    System.out.println(letterTopBase + "," + letterMidBase);
                    int midBaseDiff = letterMidBase - letterTopBase;
                    int topBase = botBase - (baseDiff + midBaseDiff);
                    int topBaseDiff = topBase - letterTopBase;
                    ivLayout.topMargin += topBaseDiff;
                } else if (alphaBase == Globals.ALPHA_BASE_TOP) {
                    int letterTopBase = yTopList.get(letterCount);
                    int topBase = botBase - baseDiff;
                    int topBaseDiff = topBase - letterTopBase;
                    ivLayout.topMargin += topBaseDiff;
                }
                letterCount++;
            }
            iv.setLayoutParams(ivLayout);
        }
        return letterViews;
    }
}