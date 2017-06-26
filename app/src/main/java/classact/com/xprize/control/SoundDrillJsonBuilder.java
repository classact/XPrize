package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import classact.com.xprize.utils.ResourceDecoder;

/**
 * Created by Tseliso on 12/2/2016.
 */

public class SoundDrillJsonBuilder {

    //Builds JSON to pass to Sound Drill Activity One
    public static String getSoundDrillOneJson(Context context,
            ObjectAndSound<String> letter,
            String thisIsTheLetterSound,
            String itMakesTheSound,
            String nowTrySound,
            ArrayList<ObjectAndSound<String>> illustrativeObjects){
        String drillData = "";
        drillData += "{\"letter\":" + ResourceDecoder.getIdentifier(context,letter.getObjectImage(),"drawable") + ",";
        drillData += "\"intro\":\"" + thisIsTheLetterSound + "\",";
        drillData += "\"it_makes_sound\":\"" + itMakesTheSound+ "\",";
        drillData += "\"now_you_try\":\"" + nowTrySound+ "\",";
        drillData += "\"letter_sound\":\"" + letter.getObjectSound()+ "\",";
        drillData += "\"letter_phonic_sound\":\"" + letter.getObjectPhonicSound()+ "\",";
        drillData += "\"objects\": [";
        drillData += "{\"object\": " + ResourceDecoder.getIdentifier(context,illustrativeObjects.get(0).getObjectImage(),"drawable")+ ",";
        drillData += "\"object_sound\":\"" + illustrativeObjects.get(0).getObjectSound()+ "\"},";
        drillData += "{\"object\": " + ResourceDecoder.getIdentifier(context,illustrativeObjects.get(1).getObjectImage(),"drawable") + ",";
        drillData += "\"object_sound\":\"" + illustrativeObjects.get(1).getObjectSound()+ "\"},";
        drillData += "{\"object\": " + ResourceDecoder.getIdentifier(context,illustrativeObjects.get(2).getObjectImage(),"drawable") + ",";
        drillData += "\"object_sound\":\"" + illustrativeObjects.get(2).getObjectSound()+ "\"}";
        drillData += "]}";
        return drillData;
    }

    //Builds JSON to pass to SoundDrill Activity Two
    public static String getSoundDrillTwoJson(Context context,
                                     String drillSound,
                                     String thisIsASound,
                                     String touchPictureStartWithSound,
                                     ArrayList<RightWrongPair> pairs){

        for (int i = 0; i < pairs.size(); i++) {
            System.out.println("Right: " + pairs.get(i).getRightObject().getObjectImage() +
                    ", Wrong: " + pairs.get(i).getWrongObject().getObjectImage());
        }

        String drillData = "{\"paircount\": 4," +
                "\"drillsound\":\"" + drillSound + "\"," +
                "\"this_is_a\":\"" + thisIsASound + "\"," +
                "\"touch_picture_starts_with\":\"" + touchPictureStartWithSound + "\"," +
                "\"pairs\":[" +
                "{\"correctimage\": " + ResourceDecoder.getIdentifier(context,pairs.get(0).getRightObject().getObjectImage(),"drawable") + "," +
                "\"correctsound\":\"" + pairs.get(0).getRightObject().getObjectSound() + "\"," +
                "\"wrongimage\":" + ResourceDecoder.getIdentifier(context,pairs.get(0).getWrongObject().getObjectImage(),"drawable") + "," +
                "\"wrongsound\":\"" + pairs.get(0).getWrongObject().getObjectSound() + "\"}," +
                "{\"correctimage\":" + ResourceDecoder.getIdentifier(context,pairs.get(1).getRightObject().getObjectImage(),"drawable") + "," +
                "\"correctsound\":\"" + pairs.get(1).getRightObject().getObjectSound() + "\"," +
                "\"wrongsound\":\"" + pairs.get(1).getWrongObject().getObjectSound() + "\"," +
                "\"wrongimage\":" + ResourceDecoder.getIdentifier(context,pairs.get(1).getWrongObject().getObjectImage(),"drawable") +"}," +
                "{\"correctimage\": " + ResourceDecoder.getIdentifier(context,pairs.get(2).getRightObject().getObjectImage(),"drawable") + "," +
                "\"correctsound\":\"" + pairs.get(2).getRightObject().getObjectSound() + "\"," +
                "\"wrongsound\":\"" + pairs.get(2).getWrongObject().getObjectSound() + "\"," +
                "\"wrongimage\":" + ResourceDecoder.getIdentifier(context,pairs.get(2).getWrongObject().getObjectImage(),"drawable") +"}," +
                "{\"correctimage\":" + ResourceDecoder.getIdentifier(context,pairs.get(3).getRightObject().getObjectImage(),"drawable") + "," +
                "\"correctsound\":\"" + pairs.get(3).getRightObject().getObjectSound() + "\"," +
                "\"wrongsound\":\"" + pairs.get(3).getWrongObject().getObjectSound() + "\"," +
                "\"wrongimage\":" + ResourceDecoder.getIdentifier(context,pairs.get(3).getWrongObject().getObjectImage(),"drawable") +"}" +
                "]}";
        return drillData;
    }

    //Builds JSON to pass to Sound Drill Activity three
    public static String getSoundDrillThreeJson(Context context,
                                             String thisIsTheLetterSound,
                                             String itMakesTheSound,
                                             String touchSound,
                                             ArrayList<SoundDrillThreeObject> list){

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Image: " + list.get(i).getObject().getObjectImage() +
                    ", Right: " + list.get(i).getPair().getRightObject().getObjectImage() +
                    ", Wrong: " + list.get(i).getPair().getWrongObject().getObjectImage());
        }

        String drillData = "{\"sets\":[" +
                "{\"image\":\"" + list.get(0).getObject().getObjectImage() + "\"," +
                "\"sound\":\"" + list.get(0).getObject().getObjectSound() + "\"," +
                "\"phonic_sound\":\"" + list.get(0).getObject().getObjectPhonicSound() + "\"," +
                "\"images\":[" +
                "{\"image\":\"" + list.get(0).getPair().getRightObject().getObjectImage() + "\",\"correct\":1}," +
                "{\"image\":\"" + list.get(0).getPair().getWrongObject().getObjectImage() + "\",\"correct\":0}" +
                "]}," +
                "{\"image\":\"" + list.get(1).getObject().getObjectImage() + "\"," +
                "\"sound\":\"" + list.get(1).getObject().getObjectSound() + "\"," +
                "\"phonic_sound\":\"" + list.get(1).getObject().getObjectPhonicSound() + "\"," +
                "\"images\":[" +
                "{\"image\":\"" + list.get(1).getPair().getRightObject().getObjectImage() + "\",\"correct\":1}," +
                "{\"image\":\"" + list.get(1).getPair().getWrongObject().getObjectImage() + "\",\"correct\":0}" +
                "]}," +
                "{\"image\":\"" + list.get(2).getObject().getObjectImage() + "\"," +
                "\"sound\":\"" + list.get(2).getObject().getObjectSound() + "\"," +
                "\"phonic_sound\":\"" + list.get(2).getObject().getObjectPhonicSound() + "\"," +
                "\"images\":[" +
                "{\"image\":\"" + list.get(2).getPair().getRightObject().getObjectImage() + "\",\"correct\":1}," +
                "{\"image\":\"" + list.get(2).getPair().getWrongObject().getObjectImage() + "\",\"correct\":0}" +
                "]}," +
                "{\"image\":\"" + list.get(3).getObject().getObjectImage() + "\"," +
                "\"sound\":\"" + list.get(3).getObject().getObjectSound() + "\"," +
                "\"phonic_sound\":\"" + list.get(3).getObject().getObjectPhonicSound() + "\"," +
                "\"images\": [" +
                "{\"image\":\"" + list.get(3).getPair().getRightObject().getObjectImage() + "\",\"correct\":1}," +
                "{\"image\":\"" + list.get(3).getPair().getWrongObject().getObjectImage() + "\",\"correct\":0}" +
                "]}" +
                "]," +
                "\"this_is_the_letter\":\"" + thisIsTheLetterSound + "\"," +
                "\"it_makes_sound\":\"" + itMakesTheSound + "\"," +
                "\"touch\":\"" + touchSound + "\"}";
        return drillData;
    }

    //Builds JSON to pass to Sound Drill activity Four
    public static String getSoundDrillFourActivity(Context context,
                                                   String drillSound,
                                                   String intoTheBoxSound,
                                                   String damaNeedsToCleanSound,
                                                   String dragPicturesThatStartWithSound,
                                                   ArrayList<DraggableImage<ObjectAndSound>> images){

        String drillData = "{\"images\":[" +
                "{\"position\":" + images.get(0).getPosition() + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,(images.get(0).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":\"" + (images.get(0).getcontent()).getObjectSound() + "\"" +
                ",\"right\":" + images.get(0).isRight() + "}," +
                "{\"position\":" + images.get(1).getPosition() + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,(images.get(1).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":\"" + (images.get(1).getcontent()).getObjectSound() + "\"" +
                ",\"right\":" + images.get(1).isRight() + "}," +
                "{\"position\":" + images.get(2).getPosition() + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,(images.get(2).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":\"" + (images.get(2).getcontent()).getObjectSound() + "\"" +
                ",\"right\":" + images.get(2).isRight() + "}," +
                "{\"position\":" + images.get(3).getPosition() + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,(images.get(3).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":\"" + (images.get(3).getcontent()).getObjectSound() + "\"" +
                ",\"right\":" + images.get(3).isRight() + "}," +
                "{\"position\":" + images.get(4).getPosition() + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,(images.get(4).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":\"" + (images.get(4).getcontent()).getObjectSound() + "\"" +
                ",\"right\":" + images.get(4).isRight() + "}," +
                "{\"position\":" + images.get(5).getPosition() + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,(images.get(5).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":\"" + (images.get(5).getcontent()).getObjectSound() + "\"" +
                ",\"right\":" + images.get(5).isRight() + "}" +
                "],\"drillsound\":\"" + drillSound + "\"," +
                "\"into_the_box\":\"" + intoTheBoxSound + "\"," +
                "\"dama_needs_to_clean\":\"" + damaNeedsToCleanSound + "\"," +
                "\"drag_the_pictures_that_start\":\"" + dragPicturesThatStartWithSound + "\"" +
                "}";
        return drillData;
    }

    //BuildJSON String for Drill 5
    public static String getSoundDrillFiveJson(Context context,
                                               String bahatiHasASound,
                                               String sheNeedsSomethingElseSound,
                                               ArrayList<SoundDrillFiveObject> data){
        String drillData = "{\"sets\":[";

        for (int i = 0; i < data.size(); i++) {
            SoundDrillFiveObject item = data.get(i);
            ObjectAndSound drillObject = item.getDrillObject();
            ArrayList<ObjectAndSound> images = item.getImages();

            System.out.println("== SoundDrillJsonBuilder.getSoundDrillFiveJson > Debug: # of images = " + images.size());

            if (i != 0) {
                drillData += ",";
            }

            drillData +=
                    "{\"demoimage\":" + ResourceDecoder.getIdentifier(context,drillObject.getObjectImage(),"drawable") + "," +
                    "\"demosound\":\"" + drillObject.getObjectSound() + "\"," +
                    "\"sound\":\"" + drillObject.getBeginningLetterSound() + "\"," +
                    "\"images\":[";

            for (int j = 0; j < images.size(); j++) {
                ObjectAndSound image = images.get(j);

                if (j != 0) {
                    drillData += ",";
                }
                drillData +=
                        "{\"image\":" + ResourceDecoder.getIdentifier(context, image.getObjectImage(),"drawable") +
                        ",\"correct\":" + image.getCustomData() +
                        ",\"sound\":\"" + image.getObjectSound() + "\"" +
                        "}";
            }
            drillData += "]}";
        }

        drillData +=
                "]," +
                "\"bahati_has_a\":\"" + bahatiHasASound + "\"," +
                "\"she_needs_something_else\":\"" + sheNeedsSomethingElseSound + "\"" +
                "}";
        return drillData;
    }

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

    public static  String getSoundDrillSevenJson(Context context,
                                                 String listenToWordAndTouch,
                                                 ArrayList<SpelledWord> words){

        String drillData = "{\"listen_to_word_and_touch\":\"" + listenToWordAndTouch + "\"," + "\"words\":[";

        for (int i = 0; i < words.size(); i++) {

            SpelledWord word = words.get(i);

            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"word_image\":" + ResourceDecoder.getIdentifier(context,word.getWord().getObjectImage(),"drawable") + ",";
            drillData += "\"word_sound\":\"" + word.getWord().getObjectSound() + "\",";
            drillData += "\"segmeted_word_spelling\":[";

            String[] images = word.getWord().getSpelling().split(",");

            for(int x = 0; x < images.length; x++){

                String[] BlackAndRed = images[x].split("&");

                if (x > 0) {
                    drillData += ",";
                }
                drillData += "{\"black\":" + ResourceDecoder.getIdentifier(context,BlackAndRed[0],"drawable");
                drillData += ",\"red\":" + ResourceDecoder.getIdentifier(context,BlackAndRed[1],"drawable");
                drillData += ",\"letter\":" + "\"" + ("" + word.getWord().getCustomData().charAt(x)) + "\"" + "}";
            }

            drillData += "],\"segmeted_word_sound\":\"" + word.getWord().getObjectSound() + "\"," +
                    "\"segmeted_word_slow_sound\":\"" + word.getWord().getObjectSlowSound() + "\"," +
                    "\"segmeted_word_sounds\":[";

            List<String> sounds = word.getLettersSound();
            for (int j = 0; j < sounds.size(); j++) {

                String sound = sounds.get(j);

                if (j > 0) {
                    drillData += ",";
                }
                drillData += "{\"sound\":\"" + sound + "\"}";
            }

            drillData += "],";
            drillData +=  "\"pictures\":[" ;

            List<DraggableImage<String>> draggableImages = word.getLettersImages();
            for (int k = 0; k < draggableImages.size(); k++) {

                DraggableImage<String> draggableImage = draggableImages.get(k);

                if (k > 0) {
                    drillData += ",";
                }
                drillData += "{\"picture\":" + ResourceDecoder.getIdentifier(context,draggableImage.getcontent(),"drawable") +
                        ",\"correct\":" +  draggableImage.isRight() + "}";
            }
            drillData += "]}";
        }
        drillData += "]}";

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
