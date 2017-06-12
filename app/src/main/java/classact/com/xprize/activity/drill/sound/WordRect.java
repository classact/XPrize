package classact.com.xprize.activity.drill.sound;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyunchanjeong on 2017/05/29.
 */

public class WordRect {

    public int x;
    public int y;
    public int w;
    public int h;
    private String name;
    private static int phraseCursorY;
    private static SparseIntArray phraseWidths;

    public WordRect(int w, int h, String name) {
        x = 0;
        y = 0;
        this.w = w;
        this.h = h;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static List<List<WordRect>> pack(List<List<WordRect>> pageWordRects,
                                             float density, int w, int h,
                                             int wordHeight, double wordSpaceToHeightRatio) {

        int numOfPhrases = pageWordRects.size();
        phraseWidths = new SparseIntArray();

        int phraseHeight = Math.min(wordHeight, h/numOfPhrases);

        int multipliedHeight = phraseHeight;
        int multipliedSpace = (int) ((double) phraseHeight * wordSpaceToHeightRatio);

        phraseCursorY = 0;

        for (int i = 0; i < numOfPhrases; i++) {

            List<WordRect> phraseRects = pageWordRects.get(i);
            int numOfWords = phraseRects.size();

            int wordCursorX = 0;
            int wordCursorY = phraseCursorY;

            for (int j = 0; j < numOfWords; j++) {

                WordRect wordRect = phraseRects.get(j);
                resize(wordRect, multipliedHeight);

                int wordWidth = wordRect.w;
                int wordCursorWidthSum = wordCursorX + wordWidth;

                if (wordCursorWidthSum > w) {
                    phraseWidths.put(wordCursorY, wordCursorX); // add phrase width
                    wordCursorX = 0;
                    wordCursorY += multipliedHeight;
                }

                // Add a space after if not last word
                if (wordCursorX > 0) {
                    String wordRectName = wordRect.getName();
                    if (!(wordRectName == null || (
                            wordRectName.matches("^question.*")))) {
                        wordCursorX += multipliedSpace;
                        // System.out.println(nextWordRectName.matches("^question.*") + " " + nextWordRectName);
                    } /* else {
                        // System.out.println("Don't add space!!!!!! (" + nextWordRectName + ")");
                    } */
                }

                wordRect.x = wordCursorX;
                wordRect.y = wordCursorY;

                wordCursorX += wordWidth;
                if (j == numOfWords - 1) {
                    phraseWidths.put(wordCursorY, wordCursorX);
                }
            }
            phraseCursorY = wordCursorY + multipliedHeight;
        }
        return pageWordRects;
    }

    public static List<List<WordRect>> pack(
            List<List<WordRect>> pageWordRects,
            float density, int w, int h,
            int wordHeight, int wordSpacing,
            boolean centerX, boolean centerY) {

        double wordSpaceToHeightRatio = (double) wordSpacing / (double) wordHeight;

        pageWordRects = pack(pageWordRects, density, w, h, wordHeight, wordSpaceToHeightRatio);

        System.out.println("p: " + phraseCursorY + ", h: " + h);

        int resizeCount = 0;
        while(phraseCursorY > h && resizeCount < 5) {
            int diff = phraseCursorY - h;
            double dividedDiff = (double) diff / (double) phraseWidths.size();
            System.out.println("divided diff is: " + dividedDiff);
            int newWordHeight = (int) Math.floor((double) wordHeight - dividedDiff);
            System.out.println("!!!! New height: " + newWordHeight);
            pageWordRects = pack(pageWordRects, density, w, h, newWordHeight, wordSpaceToHeightRatio);
            resizeCount++;
        }
        System.out.println("Wohohoho");

        if (centerX || centerY) {

            int offsetY = Math.max((h-phraseCursorY)/2, 0);
            int numOfPhrases = pageWordRects.size();

            for (int i = 0; i < numOfPhrases; i++) {
                List<WordRect> phraseRects = pageWordRects.get(i);
                int numOfWords = phraseRects.size();
                for (int j = 0; j < numOfWords; j++) {
                    WordRect wordRect = phraseRects.get(j);
                    if (centerX) {
                        int currY = wordRect.y;
                        int phraseWidth = phraseWidths.get(currY);
                        int offsetX = (w-phraseWidth)/2;
                        System.out.println("y: " + currY + ", w: " + phraseWidth + ", o: " + offsetX);
                        wordRect.x += offsetX;
                    }
                    if (centerY) {
                        wordRect.y += offsetY;
                    }
                }
            }
        }

        return pageWordRects;
    }

    public static WordRect resize(WordRect rect, int h) {

        double oldH = (double) rect.h;

        if (oldH > 0) {
            double newH = (double) h;
            double ratio = newH / oldH;
            double oldW = (double) rect.w;
            double newW = oldW * ratio;

            rect.w = (int) newW;
            rect.h = (int) newH;
        }

        return rect;
    }
}