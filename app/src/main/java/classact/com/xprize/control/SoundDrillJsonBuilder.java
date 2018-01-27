package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import classact.com.xprize.utils.ResourceDecoder;

/**
 * Created by Tseliso on 12/2/2016.
 */

public class SoundDrillJsonBuilder {

    public static String getSoundDrillSixJson(Context context,
                                              String smallLetterImage,
                                              String upperLetterImage,
                                              String letterSound,
                                              String weCanWriteTheLetterSound,
                                              String inTwoWaysSound,
                                              String thisIsLowerCaseSound,
                                              String thisisUppderCaseSound,
                                              String dragTheLettersSound){
        String drillData = "{\"small_letter\":" + ResourceDecoder.getIdentifier(context,smallLetterImage,"drawable") + "," +
                "\"letter_sound\":\"" + letterSound + "\"," +
                "\"big_letter\":" + ResourceDecoder.getIdentifier(context,upperLetterImage,"drawable") + "," +
                "\"we_can_write_the_letter\":\"" + weCanWriteTheLetterSound + "\"," +
                "\"in_two_ways\":\"" + inTwoWaysSound + "\"," +
                "\"this_is_the_lower_case\":\"" + thisIsLowerCaseSound + "\"," +
                "\"this_is_the_upper_case\":\"" + thisisUppderCaseSound + "\"," +
                "\"drag_the_letters\":\"" + dragTheLettersSound + "\"" +
                "}";

        return drillData;
    }

    public static String getSoundDrillEightJson(Context context,
                                                 String smallLetterPath,
                                                 String letsLearnToWriteUpperSound,
                                                 String letsLearnToWriteLowerSound,
                                                 String watchSound,
                                                 String nowYouWriteSound,
                                                 String smallLetterDotsImage,
                                                 String letterSound,
                                                 String bigLetterPath,
                                                 String bigLetterDotsImage){
        System.out.println("small letter path: " + smallLetterPath);
        System.out.println("big letter path: " + bigLetterPath);
        System.out.println();
        String drillData = "{\"small_letter_path\":" + ResourceDecoder.getIdentifier(context,smallLetterPath,"raw") + "," +
                "\"lets_learn_how_to_write_upper\":\"" + letsLearnToWriteUpperSound + "\"," +
                "\"lets_learn_how_to_write_lower\":\"" + letsLearnToWriteLowerSound + "\"," +
                "\"now_you_write\":\"" + nowYouWriteSound + "\"," +
                "\"watch\":\"" + watchSound + "\"," +
                "\"small_letter\":" + ResourceDecoder.getIdentifier(context,smallLetterDotsImage,"drawable") + "," +
                "\"letter_sound\":\"" + letterSound + "\"," +
                "\"big_letter_path\":" + ResourceDecoder.getIdentifier(context,bigLetterPath,"raw") + "," +
                "\"big_letter\":" + ResourceDecoder.getIdentifier(context,bigLetterDotsImage,"drawable") +
                "}";
        return drillData;
    }

    public static String getSoundDrillNineJson(Context context,
                                                String letterSound,
                                                String letsDrawSound,
                                                String drawSomethingStartsWith,
                                                String whatDidYouDrawSound) {

        String drillData = "{\"sound\":\"" + letterSound + "\"," +
                "\"lets_draw\":\"" + letsDrawSound + "\"," +
                "\"draw_something_that_starts_with\":\"" + drawSomethingStartsWith + "\"," +
                "\"what_did_you_draw\":\"" + whatDidYouDrawSound + "\"" +
                "}";
        return drillData;
    }

    public static String getSoundDrillTenJson(Context context,
                                              String readAfterDamaSound,
                                              String touchSound,
                                              ArrayList<classact.com.xprize.database.model.Word> words){
        String drillData = "{\"instructions\":\"" + readAfterDamaSound + "\"," +
                "\"touch\":\"" +  touchSound + "\"," +
                "\"words\":[" ;
        for (int i = 0; i < words.size(); i++) {
            classact.com.xprize.database.model.Word word = words.get(i);

            drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, word.getWordPictureURI(), "drawable");
            drillData += "," + "\"sound\":\"" + word.getWordSoundURI() + "\"";
            drillData += "," + "\"name\":\"" + word.getWordName() + "\"}";

            // Append comma if required
            if (i != words.size() - 1) {
                drillData += ",";
            }
        }
        drillData += "]}" ;
        return drillData;
    }

    public static String getSoundDrillElevenJson(Context context,
                                                  String monkeyWantsTwoSound,
                                                  String canYouMatchSound,
                                                  String countOneSound,
                                                  List<classact.com.xprize.database.model.Word> words) {
        String drillData = "{\"monkey_wants_two\":\"" + monkeyWantsTwoSound + "\"," +
                "\"can_you_match\":\"" + canYouMatchSound + "\"," +
                "\"count_1\":\"" + countOneSound + "\"," +
                "\"words\": [";

        for (int i = 0; i < words.size(); i++) {
            classact.com.xprize.database.model.Word word = words.get(i);

            drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, word.getWordPictureURI(), "drawable") + "";
            drillData += "," + "\"sound\":\"" + word.getWordSoundURI() + "\"";
            drillData += "," + "\"name\":\"" + word.getWordName() + "\"}";

            // Append comma if required
            if (i != words.size() - 1) {
                drillData += ",";
            }

        }
        drillData += "]}" ;
        return drillData;
    }

    public static String getSoundDrillTwelveJson(Context context,
                                                 String quickMotherIsComing,
                                                 String youGotSound,
                                                 String noSound,
                                                 String countZeroSound,
                                                 String countOneSound,
                                                 String countTwoSound,
                                                 String countThreeSound,
                                                 String countFourSound,
                                                 String countFiveSound,
                                                 String countSixSound,
                                                 String wordsSound,
                                                 ArrayList<RightWrongWordSet> sets){
        String drillData = "{\"quick_mothers_coming\":\"" + quickMotherIsComing + "\"," +
                "\"you_got\":\"" + youGotSound + "\"," +
                "\"no_sound\":\"" + noSound + "\"," +
                "\"count_0\":\"" + countZeroSound + "\"," +
                "\"count_1\":\"" + countOneSound + "\"," +
                "\"count_2\":\"" + countTwoSound + "\"," +
                "\"count_3\":\"" + countThreeSound + "\"," +
                "\"count_4\":\"" + countFourSound + "\"," +
                "\"count_5\":\"" + countFiveSound + "\"," +
                "\"count_6\":\"" + countSixSound + "\"," +
                "\"words_sound\":\"" + wordsSound + "\"," +
                "\"sets\": [" ;

        for (int i = 0; i < sets.size(); i++) {
            // Populate Spelled Words (word + sound)
            RightWrongWordSet set = sets.get(i);

            // Populate Sounds in word
            // Comma logic
            if (i > 0) {
                drillData += ",";
            }
            // Append data
            drillData += "{\"sound\":\"" + set.getRightWord().getWordSoundURI() + "\"," +

                    // Open words array
                    "\"words\":[" ;

            // Get Letter Images in word
            ArrayList<DraggableImage<classact.com.xprize.database.model.Word>> rightAndWrongWords = set.getRightAndWrongWords();

            // Populate Draggable Images (a.k.a. 'Letter Images') in word
            for (int j = 0; j < rightAndWrongWords.size(); j++) {

                DraggableImage<classact.com.xprize.database.model.Word> theWord = rightAndWrongWords.get(j);

                //Comma logic
                if (j > 0) {
                    drillData += ",";
                }

                // Append Letter data
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, theWord.getcontent().getWordPictureURI(), "drawable");

                // Append Letter data
                drillData += ",\"name\":" + theWord.getcontent().getWordName();

                // Append Is Right data
                drillData += ",\"correct\":" + theWord.isRight() + "}";
            }

            // Close words array
            drillData += "]}";
        }

        // Close JSON drill data
        drillData += "]}";

        return drillData;
    }

    public static String getSoundDrillThirteenJson(Context context,
                                            String dragLettersToWriteSound,
                                            String youGotSound,
                                            List<SpelledWord> words){
        String drillData = "{\"drag_the_letters_to_write\":\"" + dragLettersToWriteSound + "\"," +
                "\"you_got\":\"" + youGotSound + "\"," +
                "\"words\":[";

        for (int i = 0; i < words.size(); i++) {
            SpelledWord obj = words.get(i);

            if (i != 0) {
                drillData += ",";
            }

            drillData += "{\"word\":" + obj.getWord().getSpelling() +"," +
                    "\"sound\":\"" + obj.getWord().getObjectSound() + "\"," +
                    "\"letters\": [" ;

            List<DraggableImage<String>> letterImages = obj.getLettersImages();

            for (int j = 0; j < letterImages.size(); j++) {
                DraggableImage<String> image = letterImages.get(j);

                if (j != 0) {
                    drillData += ",";
                }

                drillData += "{\"letter\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable") +
                        ",\"letter_string\":\"" + image.getLetter() + "\"" +
                        ",\"sound\":\"" + image.getSound() + "\"" +
                        ",\"positions\":[" + image.getExtraData()+ "]}";
            }
            drillData += "]}";
        }
        drillData += "]}";
        return drillData;
    }

    public static String getSoundDrillFourteenJson(Context context,
                                        String writeSound,
                                        String thisIsSound,
                                        String wereYouCorrectSound,
                                        ArrayList<SpelledWord> words){
        String drillData = "{\"write\":\"" + writeSound + "\"," +
                "\"this_is\":\"" + thisIsSound + "\"," +
                "\"were_you_correct\":\"" + wereYouCorrectSound + "\"," +
                "\"words\": [";

        for (int i = 0; i < words.size(); i++) {

            SpelledWord obj = words.get(i);

            if (i != 0) {
                drillData += ",";
            }
            drillData += "{\"word\":\"" + obj.getWord().getSpelling() +"\"," +
                    "\"sound\":\"" + obj.getWord().getObjectSound() + "\"," +
                    "\"letters\": [" ;

            List<DraggableImage<String>> letterImages = obj.getLettersImages();

            for (int j = 0; j < letterImages.size(); j++) {

                DraggableImage<String> image = letterImages.get(j);

                if (j != 0) {
                    drillData += ",";
                }
                drillData += ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable");
            }
            drillData += "]}";
        }
        drillData += "]}";
        return drillData;
    }

    public static String getSoundDrillFifteenJson(Context context,
                                           String dragWordToWriteSound,
                                           String thisIsSound,
                                           List<Sentence> sentences) {

        String drillData = "{\"drag_word_to_write\":\"" + dragWordToWriteSound + "\"," +
                "\"this_is\":\"" + thisIsSound + "\"," +
                "\"sentences\":[";

        for (int i = 0; i < sentences.size(); i++) {
            Sentence sentence = sentences.get(i);

            if (i != 0) {
                drillData += ",";
            }

            drillData += "{\"sentence\":" + sentence.getWCount() + "," +
                    "\"sentence_sound\":\"" + sentence.getSentenceText() + "\"," +
                    "\"punctuation\":\"" + sentence.getPunctuation() + "\"," +
                    "\"words\":[";

            List<DraggableImage<String>> words = sentence.getWords();

            for (int j = 0; j < words.size(); j++) {
                DraggableImage<String> word = words.get(j);
                String[] imageAndSound = word.getcontent().split(",");

                if (j != 0) {
                    drillData += ",";
                }
                drillData += "{\"word\":" + ResourceDecoder.getIdentifier(context,imageAndSound[0],"drawable") +
                        ",\"positions\":[" + word.getExtraData() +
                        "],\"sound\":\"" + imageAndSound[1] + "\"}";
            }
            drillData += "]}";
        }
        drillData += "]}";
        return drillData;
    }
}
