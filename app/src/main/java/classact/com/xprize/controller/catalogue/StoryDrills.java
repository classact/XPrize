package classact.com.xprize.controller.catalogue;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.activity.drill.sound.SimpleStoryActivity;
import classact.com.xprize.control.ComprehensionQuestion;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.SimpleStoryJsonBuilder;
import classact.com.xprize.control.SimpleStorySentence;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.ComprehensionHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.SimpleStoriesHelper;
import classact.com.xprize.database.helper.SimpleStoryUnitFileHelper;
import classact.com.xprize.database.helper.SimpleStoryWordHelper;
import classact.com.xprize.database.model.Comprehension;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.database.model.SentenceDBWords;
import classact.com.xprize.database.model.SimpleStories;
import classact.com.xprize.database.model.SimpleStoryUnitFiles;
import classact.com.xprize.database.model.SimpleStoryWords;
import classact.com.xprize.database.model.Word;

public class StoryDrills {

    public static Intent D1(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception  {
        Intent intent;

        System.out.println("-- StoryDrills.D1 > Debug: unitId " + unitId + ", drillId " + drillId + ", languageId " + languageId);

        try {
            // Fetch drillFlowWord
            DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

            // Initialize sentence lists
            ArrayList<SimpleStorySentence> sentences = new ArrayList<>();
            ArrayList<Integer> sentenceIds = SimpleStoriesHelper.getSentences(dbHelper.getReadableDatabase(), languageId, unitId);

            // Fetch story data
            for (int i = 0; i < sentenceIds.size(); i++) {
                SimpleStories sentenceFromDB = SimpleStoriesHelper.getSentence(dbHelper.getReadableDatabase(), sentenceIds.get(i));
                SimpleStorySentence sentence = new SimpleStorySentence();

                sentence.setFullSound(sentenceFromDB.getSentenceSoundFile());

                ArrayList<SimpleStoryWords> words = new ArrayList<>();

                ArrayList<Integer> sentenceWordIDs = SimpleStoryWordHelper.getSentenceWords(dbHelper.getReadableDatabase(), sentenceIds.get(i));
                for (int j = 0; j < sentenceWordIDs.size(); j++) {
                    SimpleStoryWords sentenceWord = SimpleStoryWordHelper.getSentenceWord(dbHelper.getReadableDatabase(), sentenceWordIDs.get(j));
                    words.add(sentenceWord);
                }
                sentence.setWords(words);
                sentences.add(sentence);
            }

            ArrayList<ComprehensionQuestion> questions = new ArrayList<>();
            ArrayList<DraggableImage<String>> images = new ArrayList<>();

            ArrayList<Integer> comprehensionIDs = ComprehensionHelper.getComprehensionIDs(dbHelper.getReadableDatabase(), languageId, unitId);
            int rightPicture = 0;
            for (int i = 0; i < comprehensionIDs.size(); i++) {
                Comprehension comprehensionQA = ComprehensionHelper.getComprehensionQA(dbHelper.getReadableDatabase(), comprehensionIDs.get(i));
                if (comprehensionQA.getQuestionHasSoundAnswer() == 0) {
                    ComprehensionQuestion question = new ComprehensionQuestion(comprehensionQA.getQuestionSound(), "", 0);
                    if (comprehensionQA.getCorrectPicture().equalsIgnoreCase(comprehensionQA.getPicture1())) {
                        DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture1());
                        images.add(image);
                    } else {
                        DraggableImage<String> image = new DraggableImage<>(0, 0, comprehensionQA.getPicture1());
                        images.add(image);
                    }
                    if (comprehensionQA.getCorrectPicture().equalsIgnoreCase(comprehensionQA.getPicture2())) {
                        DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture2());
                        images.add(image);
                    } else {
                        DraggableImage<String> image = new DraggableImage<>(0, 0, comprehensionQA.getPicture2());
                        images.add(image);
                    }
                    if (comprehensionQA.getCorrectPicture().equalsIgnoreCase(comprehensionQA.getPicture3())) {
                        DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture3());
                        images.add(image);
                    } else {
                        DraggableImage<String> image = new DraggableImage<>(0, 0, comprehensionQA.getPicture3());
                        images.add(image);
                    }
                    question.setImages(images);
                    questions.add(question);
                } else {
                    ComprehensionQuestion question = new ComprehensionQuestion(comprehensionQA.getQuestionSound(), comprehensionQA.getAnswerSound(), 0);
                    DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture1());
                    images.add(image);
                    image = new DraggableImage<>(0, 1, comprehensionQA.getPicture2());
                    images.add(image);
                    question.setImages(images);
                    questions.add(question);
                }
            }

            SimpleStoryUnitFiles simpleStoryUnitFiles = SimpleStoryUnitFileHelper.getSimpleStoryUnitFiles(dbHelper.getReadableDatabase(), unitId, languageId);

            String drillData = SimpleStoryJsonBuilder.getSimpleStoryJson(
                    context,
                    "story_link",
                    drillFlowWord.getDrillSound1(),
                    drillFlowWord.getDrillSound2(),
                    drillFlowWord.getDrillSound3(),
                    drillFlowWord.getDrillSound4(),
                    drillFlowWord.getDrillSound5(),
                    drillFlowWord.getDrillSound6(),
                    drillFlowWord.getDrillSound7(),
                    simpleStoryUnitFiles.getSimpleStoryUnitSoundFile(),
                    drillFlowWord.getDrillSound8(),
                    simpleStoryUnitFiles.getSimpleStoryUnitImage(),
                    sentences,
                    simpleStoryUnitFiles.getCompInstr1(),
                    simpleStoryUnitFiles.getCompInstr2(),
                    questions);
            intent = new Intent(context, SimpleStoryActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            sqlex.printStackTrace();
            throw new SQLiteException("D1 > SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D1 > Exception: " + ex.getMessage());
        }
        return intent;
    }
}
