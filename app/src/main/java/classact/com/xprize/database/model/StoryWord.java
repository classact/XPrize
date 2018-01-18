package classact.com.xprize.database.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hcdjeong on 2017/12/04.
 */

public class StoryWord {

    public StoryWord(int id,
                     int storySentenceId,
                     int wordNo,
                     @NonNull String word,
                     boolean isImage,
                     @Nullable String imageFile,
                     boolean isPunctuation,
                     boolean combineRight,
                     @Nullable String soundFile,
                     boolean use) {
        this.id = id;
        this.storySentenceId = storySentenceId;
        this.wordNo = wordNo;
        this.word = word;
        this.isImage = isImage;
        this.imageFile = imageFile;
        this.isPunctuation = isPunctuation;
        this.combineRight = combineRight;
        this.soundFile = soundFile;
        this.use = use;
    }

    public final int id;
    public final int storySentenceId;
    public final int wordNo;
    public final String word;
    public final boolean isImage;
    public final String imageFile;
    public final boolean isPunctuation;
    public final boolean combineRight;
    public final String soundFile;
    public final boolean use;
}