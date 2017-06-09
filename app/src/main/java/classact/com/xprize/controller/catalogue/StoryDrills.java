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
import classact.com.xprize.database.model.SimpleStories;
import classact.com.xprize.database.model.SimpleStoryUnitFiles;
import classact.com.xprize.database.model.SimpleStoryWord;

public class StoryDrills {

    public static Intent D1(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception  {
        Intent intent;

        System.out.println("-- StoryDrills.D1 > Debug: unitId " + unitId + ", drillId " + drillId + ", languageId " + languageId);

        try {
            // Fetch drillFlowWord
            DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

            // Initialize sentence lists
            ArrayList<SimpleStorySentence> simpleStorySentences = new ArrayList<>();

            // Get SimpleStories (Sentences) from tbl_SimpleStories
            ArrayList<Integer> simpleStorySentenceIds = SimpleStoriesHelper.getSentences(dbHelper.getReadableDatabase(), languageId, unitId);

            // Fetch story data
            for (int i = 0; i < simpleStorySentenceIds.size(); i++) {
                // Get an individual SimpleStories (a sentence) object
                SimpleStories simpleStories = SimpleStoriesHelper.getSentence(dbHelper.getReadableDatabase(), simpleStorySentenceIds.get(i));

                // Create object used to store the sentence data
                // Note the 1:many relationship where a single sentence can be split into several words
                // ie. This + is + Bahati
                SimpleStorySentence simpleStorySentence = new SimpleStorySentence();

                // Full sentence sound file
                simpleStorySentence.setFullSound(simpleStories.getSentenceSoundFile());

                // List to hold the words in a sentence
                ArrayList<SimpleStoryWord> simpleStoryWords = new ArrayList<>();

                // Get all the SimpleStoryWordIds
                ArrayList<Integer> simpleStoryWordIds = SimpleStoryWordHelper.getSimpleStoryWordIds(dbHelper.getReadableDatabase(), simpleStorySentenceIds.get(i));

                // For each SimpleStoryWordId
                for (int j = 0; j < simpleStoryWordIds.size(); j++) {

                    System.out.println("Simple story word id is: " + simpleStoryWordIds.get(j));

                    // Get the SimpleStoryWord object
                    SimpleStoryWord simpleStoryWord = SimpleStoryWordHelper.getSimpleStoryWord(dbHelper.getReadableDatabase(), simpleStoryWordIds.get(j));

                    // Add the SimpleStoryWord object to list
                    simpleStoryWords.add(simpleStoryWord);
                }
                // Set (add) the list of SimpleStoryWords for the SimpleStorySentence
                simpleStorySentence.setWords(simpleStoryWords);

                // Add the SimpleStorySentence to the list of SimpleStorySentence(s)
                simpleStorySentences.add(simpleStorySentence);
            }

            ArrayList<ComprehensionQuestion> questions = new ArrayList<>();
            final String DEFAULT_PICTURE = "c_";

            ArrayList<Integer> comprehensionIds = ComprehensionHelper.getComprehensionIDs(dbHelper.getReadableDatabase(), languageId, unitId);
            int rightPicture = 0;
            for (int i = 0; i < comprehensionIds.size(); i++) {

                int comprehensionId = comprehensionIds.get(i);
                Comprehension comprehension = ComprehensionHelper.getComprehensionQA(dbHelper.getReadableDatabase(), comprehensionId);

                // Stores all the comprehension pictures
                String[] comprehensionPictures = {comprehension.getPicture1(), comprehension.getPicture2(), comprehension.getPicture3()};

                // Init images that will store all the valid images for the comprehension item
                ArrayList<DraggableImage<String>> images = new ArrayList<>();

                // Declare comprehension question
                ComprehensionQuestion question;

                // Check if the question has a sound answer
                int hasSoundAnswer = comprehension.getQuestionHasSoundAnswer();

                // Create a new question
                // It's actually a 'question' and 'answer'
                question = new ComprehensionQuestion(comprehension.getQuestionSound(), comprehension.getAnswerSound(), hasSoundAnswer);

                // Init the correct picture
                String correctPicture = comprehension.getCorrectPicture();

                // Check if it actually exists
                // For tbl_Comprehension, some images for Picture1,2,3, are defaulted to "c_" (indicating 'not applicable')
                boolean correctPictureExists = true;

                // Validate the correct picture
                if (correctPicture.equalsIgnoreCase(DEFAULT_PICTURE)) {
                    correctPictureExists = false;
                }

                // An index to check if this is the first valid picture or not
                // Is used when no correct picture exists
                int correctPictureIndex = -1;

                for (int j = 0; j < comprehensionPictures.length; j++) {

                    boolean pictureIsValid = false;
                    int isCorrect = 0;
                    String picture = comprehensionPictures[j];

                    // Ensure that picture is not null
                    if (picture != null) {

                        // If a correct picture to compare against exists
                        if (correctPictureExists) {

                            // If a valid picture exists to compare
                            if (!picture.equalsIgnoreCase(DEFAULT_PICTURE)) {

                                // Mark picture as valid
                                pictureIsValid = true;

                                // Compare
                                if (correctPicture.equalsIgnoreCase(picture)) {

                                    // Correct picture index found
                                    correctPictureIndex = j;

                                    // This is the correct picture
                                    isCorrect = 1;
                                }

                                // In case no correct pictures until now
                                if (j == comprehensionPictures.length - 1 && correctPictureIndex == -1) {

                                    // Check if any images have been added so far
                                    if (images.size() > 0) {

                                        // If so, the first image is the correct one
                                        images.get(0).setIsRight(1);
                                    } else {

                                        // Alternatively, the last image (the current one) is the correct one
                                        isCorrect = 1;
                                    }
                                }
                            }
                        } else {
                            // The first picture will be the 'correct' one as fallback
                            if (!picture.equalsIgnoreCase(DEFAULT_PICTURE)) {

                                // Mark picture as valid
                                pictureIsValid = true;

                                // Note that a valid picture has been found
                                if (correctPictureIndex == -1) {
                                    correctPictureIndex = j;

                                    // Let this be the correct picture
                                    isCorrect = 1;
                                }
                            }
                        }
                    } else {
                        System.err.println(":::: StoryDrills.D1 > Error: Picture (" + j + ") for question (" + i + ") is null");
                    }

                    if (pictureIsValid) {
                        DraggableImage<String> image = new DraggableImage<>(0, isCorrect, picture);
                        images.add(image);

                        System.out.println(":::: StoryDrills.D1 > Debug: Picture (" + j + ") added to question (" + i + ")");
                    }
                }

                if (images.size() == 0) {
                    System.err.println(":::: StoryDrills.D1 > Error: No images for comprehensionId (" + i + ")");
                }

                question.setNumberOfPictures(comprehension.getNumberOfPictures());
                question.setImages(images);

                // Add question to questions list
                questions.add(question);
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
                    simpleStorySentences,
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
