package classact.com.xprize.database.model;

import android.support.annotation.Nullable;

/**
 * Created by hcdjeong on 2017/12/06.
 */

public class StoryParagraph {

    public StoryParagraph(int id,
                         int storyId,
                         int paragraphNo,
                         @Nullable String paragraph,
                         @Nullable String soundFile) {
        this.id = id;
        this.storyId = storyId;
        this.paragraphNo = paragraphNo;
        this.paragraph = paragraph;
        this.soundFile = soundFile;
    }

    public final int id;
    public final int storyId;
    public final int paragraphNo;
    public final String paragraph;
    public final String soundFile;
}