package classact.com.xprize.database.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hcdjeong on 2017/12/04.
 */

public class StorySentence {

    public StorySentence(int id,
                         int storyParagraphId,
                         int sentenceNo,
                         @Nullable String sentence,
                         @NonNull String soundFile) {
        this.id = id;
        this.storyParagraphId = storyParagraphId;
        this.sentenceNo = sentenceNo;
        this.sentence = sentence;
        this.soundFile = soundFile;
    }

    public final int id;
    public final int storyParagraphId;
    public final int sentenceNo;
    public final String sentence;
    public final String soundFile;
}
