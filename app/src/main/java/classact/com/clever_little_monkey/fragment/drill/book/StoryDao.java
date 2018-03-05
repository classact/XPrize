package classact.com.clever_little_monkey.fragment.drill.book;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import classact.com.clever_little_monkey.database.model.Story;
import classact.com.clever_little_monkey.database.model.StoryParagraph;
import classact.com.clever_little_monkey.database.model.StorySentence;
import classact.com.clever_little_monkey.database.model.StoryWord;

/**
 * Created by hcdjeong on 2017/12/04.
 */

public class StoryDao {

    public static List<StoryWord> getWords(SQLiteDatabase db,
                                                   int storyId,
                                                   int paragraphNo,
                                                   int sentenceNo) {
        List<StoryWord> words = null;

        Cursor cursor = db.rawQuery(
                "SELECT sw.* " +
                        "FROM tbl_Story s " +
                        "INNER JOIN tbl_StoryParagraph sp " +
                        "ON sp.story_id = s.id " +
                        "INNER JOIN tbl_StorySentence ss " +
                        "ON ss.story_paragraph_id = sp.id " +
                        "INNER JOIN tbl_StoryWord sw " +
                        "ON sw.story_sentence_id = ss.id " +
                        "WHERE s.id = " + storyId + " " +
                        "AND sp.paragraph_no = " + paragraphNo + " " +
                        "AND ss.sentence_no = " + sentenceNo + " " +
                        "ORDER BY word_no ASC", null);

        if (cursor.getCount() > 0) {
            words = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                StoryWord word = new StoryWord(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("story_sentence_id")),
                        cursor.getInt(cursor.getColumnIndex("word_no")),
                        cursor.getString(cursor.getColumnIndex("word")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_image"))),
                        cursor.getString(cursor.getColumnIndex("image_file")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_punctuation"))),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("combine_right"))),
                        cursor.getString(cursor.getColumnIndex("sound_file")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("use")))
                );
                words.add(word);
            }
            cursor.close();
        }
        return words;
    }

    public static List<StoryWord> getWords(SQLiteDatabase db,
                                                   int storySentenceId) {
        List<StoryWord> words = null;

        Cursor cursor = db.rawQuery(
                "SELECT sw.* " +
                        "FROM tbl_StoryWord sw " +
                        "WHERE sw.story_sentence_id = " + storySentenceId + " " +
                        "ORDER BY sw.word_no ASC", null);

        if (cursor.getCount() > 0) {
            words = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                StoryWord word = new StoryWord(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("story_sentence_id")),
                        cursor.getInt(cursor.getColumnIndex("word_no")),
                        cursor.getString(cursor.getColumnIndex("word")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_image"))),
                        cursor.getString(cursor.getColumnIndex("image_file")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_punctuation"))),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("combine_right"))),
                        cursor.getString(cursor.getColumnIndex("sound_file")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("use")))
                );
                words.add(word);
            }
            cursor.close();
        }
        return words;
    }

    public static List<StorySentence> getSentences(SQLiteDatabase db,
                                                       int storyParagraphId) {
        List<StorySentence> sentences = null;

        Cursor cursor = db.rawQuery(
                "SELECT ss.* " +
                        "FROM tbl_StorySentence ss " +
                        "WHERE ss.story_paragraph_id = " + storyParagraphId + " " +
                        "ORDER BY ss.sentence_no ASC", null);

        if (cursor.getCount() > 0) {
            sentences = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                StorySentence sentence = new StorySentence(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("story_paragraph_id")),
                        cursor.getInt(cursor.getColumnIndex("sentence_no")),
                        cursor.getString(cursor.getColumnIndex("sentence")),
                        cursor.getString(cursor.getColumnIndex("sound_file"))
                );
                sentences.add(sentence);
            }
            cursor.close();
        }
        return sentences;
    }

    public static List<StoryParagraph> getParagraphs(SQLiteDatabase db,
                                                     int storyId) {
        List<StoryParagraph> paragraphs = null;

        Cursor cursor = db.rawQuery(
                "SELECT sp.* " +
                        "FROM tbl_StoryParagraph sp " +
                        "WHERE sp.story_id = " + storyId + " " +
                        "ORDER BY sp.paragraph_no ASC", null);

        if (cursor.getCount() > 0) {
            paragraphs = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                StoryParagraph sentence = new StoryParagraph(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("story_id")),
                        cursor.getInt(cursor.getColumnIndex("paragraph_no")),
                        cursor.getString(cursor.getColumnIndex("paragraph")),
                        cursor.getString(cursor.getColumnIndex("sound_file"))
                );
                paragraphs.add(sentence);
            }
            cursor.close();
        }
        return paragraphs;
    }

    public static List<Story> getStories(SQLiteDatabase db,
                                         int languageId) {

        List<Story> stories = null;

        Cursor cursor = db.rawQuery(
                "SELECT s.* " +
                        "FROM tbl_Story s " +
                        "WHERE s.language = " + languageId + " " +
                        "ORDER BY s.id ASC", null);

        if (cursor.getCount() > 0) {
            stories = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Story story = new Story(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("language")),
                        cursor.getString(cursor.getColumnIndex("story")),
                        cursor.getString(cursor.getColumnIndex("sound_file")),
                        cursor.getString(cursor.getColumnIndex("splash_image_file"))
                );
                stories.add(story);
            }
            cursor.close();
        }
        return stories;
    }

    public static Story getStory(SQLiteDatabase db,
                                      int storyId) {

        Story story = null;

        Cursor cursor = db.rawQuery(
                "SELECT s.* " +
                        "FROM tbl_Story s " +
                        "WHERE s.id = " + storyId + " " +
                        "ORDER BY s.id ASC", null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                story = new Story(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("language")),
                        cursor.getString(cursor.getColumnIndex("story")),
                        cursor.getString(cursor.getColumnIndex("sound_file")),
                        cursor.getString(cursor.getColumnIndex("splash_image_file")));
            }
            cursor.close();
        }
        return story;
    }

    public static int getStoryId(SQLiteDatabase db,
                                   int unitSectionDrillId) {

        int storyId = -1;

        Cursor cursor = db.rawQuery(
                "SELECT story_id " +
                        "FROM tbl_UnitSectionDrillStory usds " +
                        "WHERE usds.unit_section_drill_id = " + unitSectionDrillId + " " +
                        "ORDER BY usds.id ASC", null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                storyId = cursor.getInt(cursor.getColumnIndex("story_id"));
            }
            cursor.close();
        }
        return storyId;
    }
}