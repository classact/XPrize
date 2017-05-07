package classact.com.xprize.activity.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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