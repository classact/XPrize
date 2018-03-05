package classact.com.clever_little_monkey.activity.menu;

/**
 * Created by hyunchanjeong on 2017/05/06.
 */

public class Chapter extends ItemCollection implements ItemChild {

    private Book book;

    public Chapter(Book book, String name) {
        super(name);
        this.book = book;
    }

    @Override
    public Item getParent() {
        return book;
    }
}