package classact.com.xprize.activity.menu;

/**
 * Created by hyunchanjeong on 2017/05/06.
 */

public class ChapterActivityDrill implements Item, ItemChild {

    private ChapterActivity chapterActivity;
    private String name;

    public ChapterActivityDrill(ChapterActivity chapterActivity, String name) {
        this.chapterActivity = chapterActivity;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Item getParent() {
        return chapterActivity;
    }
}
