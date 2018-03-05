package classact.com.clever_little_monkey.activity.menu;

/**
 * Created by hyunchanjeong on 2017/05/06.
 */

public class ChapterActivity extends ItemCollection implements ItemChild {

    private Chapter chapter;

    public ChapterActivity(Chapter chapter, String name) {
        super(name);
        this.chapter = chapter;
    }

    @Override
    public Item getParent() {
        return chapter;
    }
}