package classact.com.clever_little_monkey.fragment.drill.book;

import java.util.List;

import classact.com.clever_little_monkey.database.model.StoryParagraph;

/**
 * Created by hcdjeong on 2017/12/06.
 */

public class StoryBookParagraph {

    private StoryParagraph paragraph;
    private List<StoryBookSentence> sentences;

    public void setStoryParagraph(StoryParagraph paragraph) {
        this.paragraph = paragraph;
    }

    public void setSentences(List<StoryBookSentence> sentences) {
        this.sentences = sentences;
    }

    public StoryParagraph getParagraph() {
        return paragraph;
    }

    public List<StoryBookSentence> getSentences() {
        return sentences;
    }
}
