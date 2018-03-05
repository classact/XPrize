package classact.com.clever_little_monkey.fragment.drill.book;

import java.util.List;

import classact.com.clever_little_monkey.database.model.Story;

/**
 * Created by hcdjeong on 2017/12/06.
 */

public class StoryBook {

    private Story story;
    private List<StoryBookParagraph> paragraphs;

    public void setStory(Story story) {
        this.story = story;
    }

    public void setParagraphs(List<StoryBookParagraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public Story getStory() {
        return story;
    }

    public List<StoryBookParagraph> getParagraphs() {
        return paragraphs;
    }
}