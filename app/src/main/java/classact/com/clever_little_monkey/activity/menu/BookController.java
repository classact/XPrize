package classact.com.clever_little_monkey.activity.menu;

import java.util.List;

/**
 * Created by hyunchanjeong on 2017/05/06.
 */

public class BookController {

    private Book book;

    public BookController(Book book) {
        this.book = book;
    }

    /**
     * PUBLIC FUNCTIONS
     */

    public BookController init() {

        // add chapters
        addChapters();

        // add chapter activities
        addChapterActivities();

        // add chapter activity drills
        addChapterActivityDrills();

        // return (finish init)
        return this;
    }

    /**
     * PRIVATE FUNCTIONS
     */

    private void addChapters() {
        book.addItem(new Chapter(book, "Intro"));
        book.addItem(new Chapter(book, "Chapter 1"));
        book.addItem(new Chapter(book, "Chapter 2"));
        book.addItem(new Chapter(book, "Chapter 3"));
        book.addItem(new Chapter(book, "Chapter 4"));
        book.addItem(new Chapter(book, "Chapter 5"));
        book.addItem(new Chapter(book, "Chapter 6"));
        book.addItem(new Chapter(book, "Chapter 7"));
        book.addItem(new Chapter(book, "Chapter 8"));
        book.addItem(new Chapter(book, "Chapter 9"));
        book.addItem(new Chapter(book, "Chapter 10"));
        book.addItem(new Chapter(book, "Chapter 11"));
        book.addItem(new Chapter(book, "Chapter 12"));
        book.addItem(new Chapter(book, "Chapter 13"));
        book.addItem(new Chapter(book, "Chapter 14"));
        book.addItem(new Chapter(book, "Chapter 15"));
        book.addItem(new Chapter(book, "Chapter 16"));
        book.addItem(new Chapter(book, "Chapter 17"));
        book.addItem(new Chapter(book, "Chapter 18"));
        book.addItem(new Chapter(book, "Chapter 19"));
        book.addItem(new Chapter(book, "Chapter 20"));
        book.addItem(new Chapter(book, "Finale"));
    }

    private void addChapterActivities() {
        List<Item> bookItemList = book.getItemList();
        int bookItemListSize = bookItemList.size();

        for (int i = 0; i < bookItemListSize; i++) {
            ItemCollection chapter = (ItemCollection) book.getItem(i);
            if (chapter.getName().equals("Intro")) {
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Movie"));
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Tutorial"));
            } else if (chapter.getName().equals("Finale")) {
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Movie"));
            } else {
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Movie"));
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Phonics"));
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Words"));
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Simple Story"));
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Maths"));
                chapter.addItem(new ChapterActivity((Chapter) chapter, "Chapter End"));
            }
        }
    }

    private void addChapterActivityDrills() {
        List<Item> bookItemList = book.getItemList();
        int bookItemListSize = bookItemList.size();

        for (int i = 0; i < bookItemListSize; i++) {
            ItemCollection chapter = (ItemCollection) book.getItem(i);
            List<Item> chapterItemList = chapter.getItemList();
            int chapterItemListSize = chapterItemList.size();

            for (int j = 0; j < chapterItemListSize; j++) {
                if (!(chapter.getName().equals("Intro") || chapter.getName().equals("Finale"))) {
                    ItemCollection chapterActivity = (ItemCollection) chapter.getItem(j);
                    if (chapterActivity.getName().equals("Phonics")) {
                        // Phonics logic
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D1"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D2"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D3"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D4"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D5"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D6"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D7"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D8"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D9"));
                    } else if (chapterActivity.getName().equals("Words")) {
                        // Words logic
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D1"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D2"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D3"));
                        // Add 2 more drills if unitId > 1
                        if (i > 1) {
                            chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D4"));
                            chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D5"));
                            chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D6"));
                        }
                    } else if (chapterActivity.getName().equals("Simple Story")) {
                        // Simple story logic
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D1"));
                    } else if (chapterActivity.getName().equals("Maths")) {
                        // Maths logic
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D1"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D2"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D3"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D4"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D5"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D6"));
                        chapterActivity.addItem(new ChapterActivityDrill((ChapterActivity) chapterActivity, "D7"));
                    }
                }
            }
        }
    }

    /**
     * PUBLIC GETTERS
     */

    public Book getBook() {
        return book;
    }

    public String[] getChapterNames() {
        return getItemNames(book.getItemList());
    }

    public String[] getChapterActivityNames(String key) {
        return getItemNames(getChapter(key).getItemList());
    }

    public String[] getChapterActivityNames(int index) {
        return getItemNames(getChapter(index).getItemList());
    }

    public String[] getChapterActivityDrillNames(String chapterKey, String chapterActivityKey) {
        return getItemNames(getChapterActivity(getChapter(chapterKey), chapterActivityKey).getItemList());
    }

    public String[] getChapterActivityDrillNames(int chapterIndex, int chapterActivityIndex) {
        return getItemNames(getChapterActivity(getChapter(chapterIndex), chapterActivityIndex).getItemList());
    }

    /**
     * PRIVATE GETTERS
     */
    private ItemCollection getChapter(String key) {
        return (ItemCollection) book.getItem(key);
    }

    private ItemCollection getChapter(int index) {
        return (ItemCollection) book.getItem(index);
    }

    private ItemCollection getChapterActivity(ItemCollection chapter, String key) {
        return (ItemCollection) chapter.getItem(key);
    }

    private ItemCollection getChapterActivity(ItemCollection chapter, int index) {
        return (ItemCollection) chapter.getItem(index);
    }

    private String[] getItemNames(List<Item> itemList) {
        // init variables used
        int itemListSize = itemList.size();
        String[] itemNames = new String[itemListSize];
        // populate
        for (int i = 0; i < itemListSize; i++) {
            itemNames[i] = itemList.get(i).getName();
        }
        // return
        return itemNames;
    }
}
