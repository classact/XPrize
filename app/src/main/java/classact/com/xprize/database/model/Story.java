package classact.com.xprize.database.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hcdjeong on 2017/12/04.
 */

public class Story {

    public Story(int id,
                 int language,
                 @NonNull String story,
                 @NonNull String soundFile,
                 @Nullable String splashImageFile) {
        this.id = id;
        this.language = language;
        this.story = story;
        this.soundFile = soundFile;
        this.splashImageFile = splashImageFile;
    }

    public final int id;
    public final int language;
    public final String story;
    public final String soundFile;
    public final String splashImageFile;
}
