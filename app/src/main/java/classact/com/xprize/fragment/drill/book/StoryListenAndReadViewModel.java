package classact.com.xprize.fragment.drill.book;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.model.Story;
import classact.com.xprize.database.model.StoryParagraph;
import classact.com.xprize.database.model.StorySentence;
import classact.com.xprize.database.model.StoryWord;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/16.
 */

public class StoryListenAndReadViewModel extends DrillViewModel {

    private StoryBuilder storyBuilder;
    private List<StoryBook> storyBooks;
    int iStory, iParagraph, iSentence, iWord;
    int iColoredSentence, iColoredWord;
    private boolean isFirstPage, isLastPage;
    private boolean narrationPhase, selfReadPhase;

    boolean drillStarted = false;

    int currentState; // Current drill state
    int currentSlide; // Current slide to show

    private UnitSectionDrillHelper unitSectionDrillHelper;

    @Inject
    public StoryListenAndReadViewModel(Bus bus, UnitSectionDrillHelper unitSectionDrillHelper) {
        super(bus);
        iColoredSentence = -1;
        iColoredWord = -1;
        isFirstPage = true;
        isLastPage = false;
        narrationPhase = true;
        selfReadPhase = false;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
    }

    public StoryListenAndReadViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public StoryListenAndReadViewModel prepare(Context context) {
        // Setup story builder
        if (storyBuilder == null) {
            storyBuilder = new StoryBuilder(context);
        }

        // Setup story books
        if (storyBooks == null) {

            // Initialize DB Helper
            DbHelper dbHelper = DbHelper.getDbHelper(context);

            // Open database
            dbHelper.openDatabase();

            // Get unit section drill
            UnitSectionDrill unitSectionDrill =
                    unitSectionDrillHelper.getUnitSectionDrillInProgress(
                            dbHelper.getReadableDatabase(), 1);

            // Get Story Id
            int storyId = StoryDao.getStoryId(dbHelper.getReadableDatabase(), unitSectionDrill.getUnitSectionDrillId());

            // Get Story
            Story drillStory = StoryDao.getStory(dbHelper.getReadableDatabase(), storyId);

            // Create stories array
            List<Story> stories = new ArrayList<>();

            // Add story to stories array
            stories.add(drillStory);

//            // Find stories from db
//            List<Story> stories = StoryDao.getStories(
//                    dbHelper.getReadableDatabase(), 1);

            // Create new story books array
            storyBooks = new ArrayList<>();

            // For all stories from db
            for (Story story : stories) {

                // Create new story book
                StoryBook storyBook = new StoryBook();
                // Set story of story book
                storyBook.setStory(story);

                // Get paragraphs

                // Find story paragraphs from db
                List<StoryParagraph> storyParagraphs = StoryDao.getParagraphs(
                        dbHelper.getReadableDatabase(), story.id);
                // Create new story book paragraphs array
                List<StoryBookParagraph> storyBookParagraphs = new ArrayList<>();

                // For all story paragraphs from db
                for (StoryParagraph storyParagraph : storyParagraphs) {

                    // Create new story book paragraph
                    StoryBookParagraph storyBookParagraph = new StoryBookParagraph();
                    // Set story paragraph of story book paragraph
                    storyBookParagraph.setStoryParagraph(storyParagraph);

                    // Find story sentences from db
                    List<StorySentence> storySentences = StoryDao.getSentences(
                            dbHelper.getReadableDatabase(), storyParagraph.id);
                    // Create new story book sentences array
                    List<StoryBookSentence> storyBookSentences = new ArrayList<>();

                    // For all story sentences from db
                    for (StorySentence storySentence : storySentences) {

                        // Create new story book sentence
                        StoryBookSentence storyBookSentence = new StoryBookSentence();
                        // Set story sentence of story book sentence
                        storyBookSentence.setSentence(storySentence);

                        // Find story sentence words from db
                        List<StoryWord> storyWords = StoryDao.getWords(
                                dbHelper.getReadableDatabase(), storySentence.id);

                        // Set story sentence words of story book sentence
                        storyBookSentence.setWords(storyWords);

                        // Add story book sentence to story book sentences array
                        storyBookSentences.add(storyBookSentence);
                    }
                    // Set sentences for story book paragraph
                    storyBookParagraph.setSentences(storyBookSentences);

                    // Add story book paragraph to story book paragraphs array
                    storyBookParagraphs.add(storyBookParagraph);
                }
                // Set paragraphs for story book
                storyBook.setParagraphs(storyBookParagraphs);

                // Add story book to story books array
                storyBooks.add(storyBook);
            }

            // Close database
            dbHelper.close();
        }

        return this;
    }

    public void setFirstPage() {
        isFirstPage = true;
        isLastPage = false;
    }

    public void setInBetweenPage() {
        isFirstPage = false;
        isLastPage = false;
    }

    public void setLastPage() {
        isFirstPage = false;
        isLastPage = true;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setNarrationPhase() {
        narrationPhase = true;
        selfReadPhase = false;
    }

    public void setSelfReadPhase() {
        narrationPhase = false;
        selfReadPhase = true;
    }

    public boolean isNarrationPhase() {
        return narrationPhase;
    }

    public boolean isSelfReadPhase() {
        return selfReadPhase;
    }

    public StoryBuilder getStoryBuilder() {
        return storyBuilder;
    }

    public List<StoryBook> getStoryBooks() {
        return storyBooks;
    }
}
