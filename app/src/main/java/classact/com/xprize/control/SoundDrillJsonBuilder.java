package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;

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
            ArrayList<ObjectAndSound<String>> illustrativeObjeccts){
        String drillData = "";
        System.out.println("5A");
        drillData += "{\"letter\":" + ResourceDecoder.getIdentifier(context,letter.getObjectImage(),"drawable") + ",";
        System.out.println("5B");
        drillData += "\"intro\":" + ResourceDecoder.getIdentifier(context,thisIsTheLetterSound,"raw") + ",";
        System.out.println("5C");
        drillData += "\"it_makes_sound\":" +  ResourceDecoder.getIdentifier(context,itMakesTheSound,"raw") + ",";
        System.out.println("5D");
        drillData += "\"now_you_try\":" +  ResourceDecoder.getIdentifier(context,nowTrySound,"raw") + ",";
        System.out.println("5E");
        drillData += "\"letter_sound\":" + ResourceDecoder.getIdentifier(context,letter.getObjectSound(),"raw") + ",";
        System.out.println("5F");
        drillData += "\"letter_phonic_sound\":" + ResourceDecoder.getIdentifier(context,letter.getObjectPhonicSound(),"raw") + ",";
        System.out.println("5G");
        drillData += "\"objects\": [";
        drillData += "{\"object\": " + ResourceDecoder.getIdentifier(context,illustrativeObjeccts.get(0).getObjectImage(),"drawable")+ ",";
        System.out.println("5H");
        drillData += "\"object_sound\":" + ResourceDecoder.getIdentifier(context,illustrativeObjeccts.get(0).getObjectSound(),"raw") + "},";
        System.out.println("5I");
        drillData += "{\"object\": " + ResourceDecoder.getIdentifier(context,illustrativeObjeccts.get(1).getObjectImage(),"drawable") + ",";
        System.out.println("5J");
        drillData += "\"object_sound\":" + ResourceDecoder.getIdentifier(context,illustrativeObjeccts.get(1).getObjectSound(),"raw") + "},";
        System.out.println("5I");
        drillData += "{\"object\": " + ResourceDecoder.getIdentifier(context,illustrativeObjeccts.get(2).getObjectImage(),"drawable") + ",";
        System.out.println("5J");
        drillData += "\"object_sound\":" + ResourceDecoder.getIdentifier(context,illustrativeObjeccts.get(2).getObjectSound(),"raw") + "}";
        drillData += "]}";
        System.out.println("5K");
        return drillData;
    }

    //Builds JSON to pass to SoundDrill Activity Two
    public static String getSoundDrillTwoJson(Context context,
                                     String drillSound,
                                     String thisIsASound,
                                     String touchPictureStartWithSound,
                                     ArrayList<RightWrongPair> pairs){
        String drillData = "{\"paircount\": 4," +
                "\"drillsound\":" +  ResourceDecoder.getIdentifier(context,drillSound,"raw") + "," +
                "\"this_is_a\":" +  ResourceDecoder.getIdentifier(context,thisIsASound,"raw") + "," +
                "\"touch_picture_starts_with\":" + ResourceDecoder.getIdentifier(context,touchPictureStartWithSound,"raw") + "," +
                "\"pairs\": [" +
                "{\"correctimage\": " +  ResourceDecoder.getIdentifier(context,pairs.get(0).getRightObject().getObjectImage(),"drawable")  + "," +
                "\"correctsound\":" + ResourceDecoder.getIdentifier(context,pairs.get(0).getRightObject().getObjectSound(),"raw")  + "," +
                "\"wrongimage\":" +  ResourceDecoder.getIdentifier(context,pairs.get(0).getWrongObject().getObjectImage(),"drawable")  + "," +
                "\"wrongsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(0).getWrongObject().getObjectSound(),"raw")  +"}," +
                "{\"correctimage\": " +  ResourceDecoder.getIdentifier(context,pairs.get(1).getRightObject().getObjectImage(),"drawable")  + "," +
                "\"correctsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(1).getRightObject().getObjectSound(),"raw")  + "," +
                "\"wrongsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(1).getWrongObject().getObjectSound(),"raw")  + "," +
                "\"wrongimage\":" +  ResourceDecoder.getIdentifier(context,pairs.get(1).getWrongObject().getObjectImage(),"drawable")  +"}," +
                "{\"correctimage\": " +  ResourceDecoder.getIdentifier(context,pairs.get(2).getRightObject().getObjectImage(),"drawable")  + "," +
                "\"correctsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(2).getRightObject().getObjectSound(),"raw")  + "," +
                "\"wrongsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(2).getWrongObject().getObjectSound(),"raw")  + "," +
                "\"wrongimage\":" +  ResourceDecoder.getIdentifier(context,pairs.get(2).getWrongObject().getObjectImage(),"drawable")  +"}," +
                "{\"correctimage\": " +  ResourceDecoder.getIdentifier(context,pairs.get(3).getRightObject().getObjectImage(),"drawable")  + "," +
                "\"correctsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(3).getRightObject().getObjectSound(),"raw")  + "," +
                "\"wrongsound\":" +  ResourceDecoder.getIdentifier(context,pairs.get(3).getWrongObject().getObjectSound(),"raw")  + "," +
                "\"wrongimage\":" +  ResourceDecoder.getIdentifier(context,pairs.get(3).getWrongObject().getObjectImage(),"drawable")  +"}" +
                "]}";
        return drillData;
    }

    //Builds JSON to pass to Sound Drill Activity three
    public static String getSoundDrillThreeJson(Context context,
                                             String thisIsTheLetterSound,
                                             String itMakesTheSound,
                                             String touchSound,
                                             ArrayList<SoundDrillThreeObject> list){
        String drillData = "{\"sets\": [" +
                "{\"image\": " +  ResourceDecoder.getIdentifier(context,list.get(0).getObject().getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,list.get(0).getObject().getObjectSound(),"raw") + "," +
                "\"phonic_sound\":" + ResourceDecoder.getIdentifier(context,list.get(0).getObject().getObjectPhonicSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" +  ResourceDecoder.getIdentifier(context,list.get(0).getPair().getRightObject().getObjectImage(),"drawable") +",\"correct\":1}," +
                "{\"image\":" +  ResourceDecoder.getIdentifier(context,list.get(0).getPair().getWrongObject().getObjectImage(),"drawable") +",\"correct\":0}" +
                "]}," +
                "{\"image\": " + ResourceDecoder.getIdentifier(context,list.get(1).getObject().getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,list.get(1).getObject().getObjectSound(),"raw") + "," +
                "\"phonic_sound\":" + ResourceDecoder.getIdentifier(context,list.get(1).getObject().getObjectPhonicSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,list.get(1).getPair().getRightObject().getObjectImage(),"drawable") +",\"correct\":0}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,list.get(1).getPair().getWrongObject().getObjectImage(),"drawable") +",\"correct\":1}" +
                "]}," +
                "{\"image\": " + ResourceDecoder.getIdentifier(context,list.get(2).getObject().getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,list.get(2).getObject().getObjectSound(),"raw") + "," +
                "\"phonic_sound\":" + ResourceDecoder.getIdentifier(context,list.get(2).getObject().getObjectPhonicSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" +ResourceDecoder.getIdentifier(context,list.get(2).getPair().getRightObject().getObjectImage(),"drawable") +",\"correct\":0}," +
                "{\"image\":" +  ResourceDecoder.getIdentifier(context,list.get(2).getPair().getWrongObject().getObjectImage(),"drawable") +",\"correct\":1}" +
                "]}," +
                "{\"image\": " +ResourceDecoder.getIdentifier(context,list.get(3).getObject().getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,list.get(3).getObject().getObjectSound(),"raw") + "," +
                "\"phonic_sound\":" + ResourceDecoder.getIdentifier(context,list.get(3).getObject().getObjectPhonicSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" +ResourceDecoder.getIdentifier(context,list.get(3).getPair().getRightObject().getObjectImage(),"drawable") +",\"correct\":1}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,list.get(3).getPair().getWrongObject().getObjectImage(),"drawable") +",\"correct\":0}" +
                "]}" +
                "]," +
                "\"this_is_the_letter\":" +  ResourceDecoder.getIdentifier(context,thisIsTheLetterSound,"raw") + "," +
                "\"it_makes_sound\":" +  ResourceDecoder.getIdentifier(context,itMakesTheSound,"raw") + "," +
                "\"touch\":" +  ResourceDecoder.getIdentifier(context,touchSound,"raw") + "}";

        return drillData;
    }

    //Builds JSON to pass to Sound Drill activity Four
    public static String getSoundDrillFourActivity(Context context,
                                                   String drillSound,
                                                   String intoTheBoxSound,
                                                   String damaNeedsToCleanSound,
                                                   String dragPicturesThatStartWithSound,
                                                   ArrayList<DraggableImage<ObjectAndSound>> images){

        String drillData = "{\"images\": [" +
                "{\"position\":" +  images.get(0).getPosition() +  "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(0).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(0).getcontent()).getObjectSound(),"raw") +
                ",\"right\":" + images.get(0).isRight() + "}," +
                "{\"position\":" +  images.get(1).getPosition() +  "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(1).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(1).getcontent()).getObjectSound(),"raw") +
                ",\"right\":" + images.get(1).isRight() + "}," +
                "{\"position\":" +  images.get(2).getPosition() +  "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(2).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(2).getcontent()).getObjectSound(),"raw") +
                ",\"right\":" + images.get(2).isRight() + "}," +
                "{\"position\":" +  images.get(3).getPosition() +  "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(3).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(3).getcontent()).getObjectSound(),"raw") +
                ",\"right\":" + images.get(3).isRight() + "}," +
                "{\"position\":" +  images.get(4).getPosition() +  "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(4).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(4).getcontent()).getObjectSound(),"raw") +
                ",\"right\":" + images.get(4).isRight() + "}," +
                "{\"position\":" +  images.get(5).getPosition() +  "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(5).getcontent()).getObjectImage(),"drawable") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,((ObjectAndSound)images.get(5).getcontent()).getObjectSound(),"raw") +
                ",\"right\":" + images.get(5).isRight() + "}" +
                "],\"drillsound\": " + ResourceDecoder.getIdentifier(context,drillSound,"raw") + "," +
                "\"into_the_box\": " +ResourceDecoder.getIdentifier(context,intoTheBoxSound,"raw") + "," +
                "\"dama_needs_to_clean\": " + ResourceDecoder.getIdentifier(context,damaNeedsToCleanSound,"raw") + "," +
                "\"drag_the_pictures_that_start\": " + ResourceDecoder.getIdentifier(context,dragPicturesThatStartWithSound,"raw") +
                "}";
        return drillData;
    }

    //BuildJSON String for Drill 5
    public static String getSoundDrillFiveJson(Context context,
                                               String bahatiHasASound,
                                               String sheNeedsSomethingElseSound,
                                               ArrayList<SoundDrillFiveObject> data){
        String drillData = "{\"sets\": [" +
                "{\"demoimage\": " + ResourceDecoder.getIdentifier(context,data.get(0).getDrillObject().getObjectImage(),"drawable") + "," +
                "\"demosound\":" + ResourceDecoder.getIdentifier(context,data.get(0).getDrillObject().getObjectSound(),"raw") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(0).getDrillObject().getBeginningLetterSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(0).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(0).getImages().get(0).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(0).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(1).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(0).getImages().get(1).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(1).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(2).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(0).getImages().get(2).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(2).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(3).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(0).getImages().get(3).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(0).getImages().get(3).getObjectSound(),"raw") +
                "}" +
                "]}," +
                "{\"demoimage\": " + ResourceDecoder.getIdentifier(context,data.get(1).getDrillObject().getObjectImage(),"drawable") + "," +
                "\"demosound\":" + ResourceDecoder.getIdentifier(context,data.get(1).getDrillObject().getObjectSound(),"raw") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(1).getDrillObject().getBeginningLetterSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(0).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(1).getImages().get(0).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(0).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(1).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(1).getImages().get(1).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(1).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(2).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(1).getImages().get(2).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(2).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(3).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(1).getImages().get(3).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(1).getImages().get(3).getObjectSound(),"raw") +
                "}" +
                "]}," +
                "{\"demoimage\": " + ResourceDecoder.getIdentifier(context,data.get(2).getDrillObject().getObjectImage(),"drawable") + "," +
                "\"demosound\":" + ResourceDecoder.getIdentifier(context,data.get(2).getDrillObject().getObjectSound(),"raw") + "," +
                "\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(2).getDrillObject().getBeginningLetterSound(),"raw") + "," +
                "\"images\": [" +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(0).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(2).getImages().get(0).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(0).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(1).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(2).getImages().get(1).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(1).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(2).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(2).getImages().get(2).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(2).getObjectSound(),"raw") +
                "}," +
                "{\"image\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(3).getObjectImage(),"drawable") +
                ",\"correct\":" + data.get(2).getImages().get(3).getCustomData() +
                ",\"sound\":" + ResourceDecoder.getIdentifier(context,data.get(2).getImages().get(3).getObjectSound(),"raw") +
                "}" +
                "]}" +
                "]," +
                "\"bahati_has_a\":" +  ResourceDecoder.getIdentifier(context,bahatiHasASound,"raw") +"," +
                "\"she_needs_something_else\":" +  ResourceDecoder.getIdentifier(context,sheNeedsSomethingElseSound,"raw") +
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
        String drillData = "{\"small_letter\":" + ResourceDecoder.getIdentifier(context,smallLetterImage,"drawable")  + "," +
                "\"letter_sound\":" + ResourceDecoder.getIdentifier(context,letterSound,"raw")+  "," +
                "\"big_letter\":" + ResourceDecoder.getIdentifier(context,upperLetterImage,"drawable") + "," +
                "\"we_can_write_the_letter\":" +ResourceDecoder.getIdentifier(context,weCanWriteTheLetterSound,"raw") +"," +
                "\"in_two_ways\":" + ResourceDecoder.getIdentifier(context,inTwoWaysSound,"raw") +"," +
                "\"this_is_the_lower_case\":" + ResourceDecoder.getIdentifier(context,thisIsLowerCaseSound,"raw") +"," +
                "\"this_is_the_upper_case\":" + ResourceDecoder.getIdentifier(context,thisisUppderCaseSound,"raw") +"," +
                "\"drag_the_letters\":" + ResourceDecoder.getIdentifier(context,dragTheLettersSound,"raw") +
                "}";

        return drillData;
    }

    public static  String getSoundDrillSevenJson(Context context,
                                                 String listenToWordAndTouch,
                                                 ArrayList<SpelledWord> words){
        String drillData = "{\"listen_to_word_and_touch\":" +  ResourceDecoder.getIdentifier(context,listenToWordAndTouch,"raw") + "," + "\"words\":[";
        int i = 1;
        for(SpelledWord obj : words) {
            if (i > 1) {
                drillData += ",";
            }
            drillData += "{\"segmeted_word_spelling\":["  ;
            String[] images = words.get(i-1).getWord().getSpelling().split(",");
            for(int x = 0; x < images.length; x++){
                String[] BlackAndRed = images[x].split("&");
                if (x == 0) {
                    drillData += "{\"black\":" + ResourceDecoder.getIdentifier(context,BlackAndRed[0],"drawable");
                }
                else{
                    drillData += ",{\"black\":" + ResourceDecoder.getIdentifier(context,BlackAndRed[0],"drawable");
                }
                drillData += ",\"red\":" + ResourceDecoder.getIdentifier(context,BlackAndRed[1],"drawable") + "}";
            }
            drillData += "],\"segmeted_word_sound\":" + ResourceDecoder.getIdentifier(context, words.get(i-1).getWord().getObjectSound(), "raw") + "," +
                    "\"segmeted_word_slow_sound\":" + ResourceDecoder.getIdentifier(context, words.get(i-1).getWord().getObjectSlowSound(), "raw") + "," +
                    "\"segmeted_word_sounds\": [";
            int j = 1;
            for (String sound: obj.getLettersSound()) {
                if (j == 1)
                    drillData += "{\"sound\":" + ResourceDecoder.getIdentifier(context,sound, "raw") + "}";
                else
                    drillData += ",{\"sound\":" + ResourceDecoder.getIdentifier(context, sound, "raw") + "}";
                j++;
            }
            drillData += "],";
            drillData +=  "\"pictures\":[" ;
            j = 0;
            for (DraggableImage<String> image: obj.getLettersImages()) {
                if (j == 0)
                    drillData += "{\"picture\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable") + ",\"correct\":" +  image.isRight() + "}";
                else
                    drillData += ",{\"picture\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable") + ",\"correct\":" +  image.isRight() + "}";
                j++;
            }
            drillData += "]}";
            i++;
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
        String drillData = "{\"small_letter_path\":" + ResourceDecoder.getIdentifier(context,smallLetterPath,"raw") + "," +
                "\"lets_learn_how_to_write_upper\":" +ResourceDecoder.getIdentifier(context,letsLearnToWriteUpperSound,"raw")  + "," +
                "\"lets_learn_how_to_write_lower\":" + ResourceDecoder.getIdentifier(context,letsLearnToWriteLowerSound,"raw")  + "," +
                "\"now_you_write\":" + ResourceDecoder.getIdentifier(context,nowYouWriteSound,"raw")  + "," +
                "\"watch\":" + ResourceDecoder.getIdentifier(context,watchSound,"raw")  + "," +
                "\"small_letter\":" + ResourceDecoder.getIdentifier(context,smallLetterDotsImage,"drawable")   + "," +
                "\"letter_sound\":" + ResourceDecoder.getIdentifier(context,letterSound,"raw")  +  "," +
                "\"big_letter_path\":" + ResourceDecoder.getIdentifier(context,bigLetterPath,"raw") + "," +
                "\"big_letter\":" + ResourceDecoder.getIdentifier(context,bigLetterDotsImage,"drawable") +
                "}";
        return drillData;
    }

    public static String getSoundDrillNineJson(Context context,
                                                 String letterSound,
                                                 String drawSomethingStartsWith,
                                                 String yippeSound,
                                                 String letsDrawSound) {
        String drillData = "{\"sound\":" + ResourceDecoder.getIdentifier(context,letterSound,"raw") +
                "\"draw_something_that_starts_with\":" + ResourceDecoder.getIdentifier(context,drawSomethingStartsWith,"raw") + "," +
                "\"what_did_you_draw\":" + ResourceDecoder.getIdentifier(context,yippeSound,"raw") + "," +
                "\"lets_draw\":" + ResourceDecoder.getIdentifier(context,letsDrawSound,"raw") +
                "}";
        return drillData;
    }

    public static String getSoundDrillTenJson(Context context,
                                              String readAfterDamaSound,
                                              String touchSound,
                                              ArrayList<SpelledWord> words){
        String drillData = "{\"instructions\":" + ResourceDecoder.getIdentifier(context,readAfterDamaSound,"raw") + "," +
                "\"touch\":" +  ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"words\": [" ;
        for (int i = 0; i < words.size(); i++) {
            SpelledWord word = words.get(i);

            drillData += "{\"word\":" + ResourceDecoder.getIdentifier(context, word.getWord().getObjectImage(),"drawable");
            drillData += "," + "\"sound\":" + ResourceDecoder.getIdentifier(context,word.getWord().getObjectSound(),"raw");
            drillData += "," + "\"name\":" + word.getWord().getSpelling() + "}";

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
                                                  ArrayList<ObjectAndSound<String>> words) {
        String drillData = "{\"monkey_wants_two\":" + ResourceDecoder.getIdentifier(context, monkeyWantsTwoSound, "raw") + "," +
                "\"can_you_match\":" + ResourceDecoder.getIdentifier(context, canYouMatchSound, "raw") + "," +
                "\"count_1\":" + ResourceDecoder.getIdentifier(context, countOneSound, "raw") + "," +
                "\"words\": [";

        for (int i = 0; i < words.size(); i++) {
            ObjectAndSound<String> word = words.get(i);

            drillData += "{\"word\":" + ResourceDecoder.getIdentifier(context, word.getObjectImage(),"drawable");
            drillData += "," + "\"sound\":" + ResourceDecoder.getIdentifier(context, word.getObjectSound(),"raw");
            drillData += "," + "\"name\":" + word.getSpelling() + "}";

            // Append comma if required
            if (i != words.size() - 1) {
                drillData += ",";
            }

        }
        drillData += "]}" ;
        return drillData;
    }

    public static String getSoundDrilTwelveJson(Context context,
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
        String drillData = "{\"quick_mothers_coming\":" +  ResourceDecoder.getIdentifier(context,quickMotherIsComing,"raw") + "," +
                "\"you_got\":" +ResourceDecoder.getIdentifier(context,youGotSound,"raw") + "," +
                "\"no_sound\":" + ResourceDecoder.getIdentifier(context,noSound,"raw") + "," +
                "\"count_0\":" + ResourceDecoder.getIdentifier(context,countZeroSound,"raw") + "," +
                "\"count_1\":" + ResourceDecoder.getIdentifier(context,countOneSound,"raw") + "," +
                "\"count_2\":" + ResourceDecoder.getIdentifier(context,countTwoSound,"raw") + "," +
                "\"count_3\":" + ResourceDecoder.getIdentifier(context,countThreeSound,"raw") + "," +
                "\"count_4\":" + ResourceDecoder.getIdentifier(context,countFourSound,"raw") + "," +
                "\"count_5\":" +ResourceDecoder.getIdentifier(context,countFiveSound,"raw") + "," +
                "\"count_6\":" + ResourceDecoder.getIdentifier(context,countSixSound,"raw") + "," +
                "\"words_sound\":" + ResourceDecoder.getIdentifier(context,wordsSound,"raw") + "," +
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
            drillData += "{\"sound\": " + ResourceDecoder.getIdentifier(context, set.getRightWord().getWordSoundURI(), "raw")  + "," +

                    // Open words array
                    "\"words\": [" ;

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
                                            ArrayList<SpelledWord> words){
        String drillData = "{\"drag_the_letters_to_write\":" + ResourceDecoder.getIdentifier(context,dragLettersToWriteSound,"raw") + "," +
                "\"you_got\":" + ResourceDecoder.getIdentifier(context,youGotSound,"raw") + "," +
                "\"words\":[";
        int i = 0;
        for(SpelledWord obj: words) {
            if (i == 0)
                drillData += "{\"word\":" + obj.getWord().getSpelling() +"," +
                        "\"sound\":" + ResourceDecoder.getIdentifier(context,obj.getWord().getObjectSound(),"raw") + "," +
                        "\"letters\": [" ;
            else
                drillData += ",{\"word\":" + obj.getWord().getSpelling() +"," +
                        "\"sound\":" + ResourceDecoder.getIdentifier(context,obj.getWord().getObjectSound(),"raw") + "," +
                    "\"letters\": [" ;
            int j = 0;
            for(DraggableImage<String> image: obj.getLettersImages()) {
                if (j == 0)
                    drillData += "{\"letter\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable")  +
                            ",\"positions\":[" + image.getExtraData()+ "]}";
                else
                    drillData += ",{\"letter\":" + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable")  +
                            ",\"positions\":[" + image.getExtraData()+ "]}";
                j++;
            }
            drillData += "]}";
            i++;
        }
        drillData += "]}";
        return drillData;
    }

    public static String getSoundDrillFourteenJson(Context context,
                                        String writeSound,
                                        String thisIsSound,
                                        String wereYouCorrectSound,
                                        ArrayList<SpelledWord> words){
        String drillData = "{\"write\":" + ResourceDecoder.getIdentifier(context,writeSound,"raw") + "," +
                "\"this_is\":" + ResourceDecoder.getIdentifier(context,thisIsSound,"raw") + "," +
                "\"were_you_correct\":" +  ResourceDecoder.getIdentifier(context,wereYouCorrectSound,"raw") + "," +
                "\"words\": [";
        int i = 0;
        for(SpelledWord obj: words) {
            if (i == 0)
                drillData += "{\"word\":" + obj.getWord().getSpelling() +"," +
                        "\"sound\":" + ResourceDecoder.getIdentifier(context,obj.getWord().getObjectSound(),"raw") + "," +
                        "\"letters\": [" ;
            else
                drillData += ",{\"word\":" + obj.getWord().getSpelling() +"," +
                        "\"sound\":" + ResourceDecoder.getIdentifier(context,obj.getWord().getObjectSound(),"raw") + "," +
                        "\"letters\": [" ;
            int j = 0;
            for(DraggableImage<String> image: obj.getLettersImages()) {
                if (j == 0)
                    drillData += ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable");
                else
                    drillData += "," + ResourceDecoder.getIdentifier(context,image.getcontent(),"drawable");
                j++;
            }
            drillData += "]}";
            i++;
        }
        drillData += "]}";
        return drillData;
    }

    public static String getSoundDrillFifteenJson(Context context,
                                           String dragWordToWriteSound,
                                           String thisIsSound,
                                           ArrayList<Sentence> sentences)
    {
        String drillData = "{\"drag_word_to_write\":" +  ResourceDecoder.getIdentifier(context,dragWordToWriteSound,"raw") + "," +
                "\"this_is\":" +  ResourceDecoder.getIdentifier(context,thisIsSound,"raw") + "," +
                "\"sentences\":[" ;
        int i = 0;
        for(Sentence sentence : sentences) {
            if (i == 0)
                drillData += "{\"sentence\":" + sentence.getWCount() + "," +
                    "\"text\": \"" + ResourceDecoder.getIdentifier(context,sentence.getSentenceText(),"raw") + "\"," +
                    "\"words\": [";
            else
                drillData += ",{\"sentence\":" + sentence.getWCount() + "," +
                        "\"text\": \"" + ResourceDecoder.getIdentifier(context,sentence.getSentenceText(),"raw") + "\"," +
                        "\"words\": [";
            i++;
            int j = 0;
            for (DraggableImage<String> word : sentence.getWords()) {
                String[] imageAndSound = word.getcontent().split(",");
                if (j == 0)
                    drillData += "{\"word\":" + ResourceDecoder.getIdentifier(context,imageAndSound[0],"drawable") + ",\"positions\":[" + word.getExtraData() +"],\"sound\":" + ResourceDecoder.getIdentifier(context,imageAndSound[1],"raw") + "}";
                else
                    drillData += ",{\"word\":" + ResourceDecoder.getIdentifier(context,imageAndSound[0],"drawable") + ",\"positions\":[" + word.getExtraData() +"],\"sound\":" + ResourceDecoder.getIdentifier(context,imageAndSound[1],"raw") + "}";
                j++;
            }
            drillData += "]}";
        }
        drillData += "]}";
        return drillData;
    }
}
