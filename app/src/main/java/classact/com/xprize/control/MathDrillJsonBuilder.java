package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;

import classact.com.xprize.utils.ResourceDecoder;

/**
 * Created by Tseliso on 12/22/2016.
 */

public class MathDrillJsonBuilder {
    public static String getDrillOneJson(Context context,
                                         String itsTimeToCount,
                                         String sayNumbersWithMe,
                                         ArrayList<Numeral> numerals){
        String drillData = "{\"its_time_to_count\":" + ResourceDecoder.getIdentifier(context,itsTimeToCount,"raw") + "," +
                "\"say_numbers_with_me\":" + ResourceDecoder.getIdentifier(context,sayNumbersWithMe,"raw") + "," +
                "\"numerals\":[";
        int i = 0;
        for (Numeral numeral: numerals) {
            if (i == 0){
                drillData +=  "{\"sound\":" + ResourceDecoder.getIdentifier(context,numeral.getSound(),"raw") + "," +
                        "\"numeral\":" + ResourceDecoder.getIdentifier(context,numeral.getBlackImage(),"drawable") + "," +
                        "\"numeral_sparkling\":" +  ResourceDecoder.getIdentifier(context,numeral.getSparklingImage(),"drawable") +
                        "}";
            }
            else{
                drillData +=  ",{\"sound\":" + ResourceDecoder.getIdentifier(context,numeral.getSound(),"raw") + "," +
                        "\"numeral\":" + ResourceDecoder.getIdentifier(context,numeral.getBlackImage(),"drawable") + "," +
                        "\"numeral_sparkling\":" +  ResourceDecoder.getIdentifier(context,numeral.getSparklingImage(),"drawable") +
                        "}";
            }
            i++;
        }
        drillData += "]}";

        return drillData;
    }

    public static String getDrillTwoJson(Context context,
                                         String monkeyHasSound,
                                         String numberOfObjectsSound,
                                         int numberOfObjects,
                                         String objectImage,
                                         String touchSound,
                                         String numeralSound,
                                         ArrayList<ObjectAndSound<String>> numbers
                                         ){
        String drillData = "{\"monkey_has\":" + ResourceDecoder.getIdentifier(context,monkeyHasSound,"raw") + "," +
                "\"object\":" + ResourceDecoder.getIdentifier(context,objectImage,"drawable") + "," +
                "\"number_of_objects\":" + numberOfObjects + "," +
                "\"number_of_objects_sound\":" +  ResourceDecoder.getIdentifier(context,numberOfObjectsSound,"raw") + "," +
                "\"numeral_sound\":" +  ResourceDecoder.getIdentifier(context,numeralSound,"raw") + "," +
                "\"can_you_find_and_touch\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"numerals\":[" +
                "{\"sound\":" +  ResourceDecoder.getIdentifier(context,numbers.get(0).getObjectSound(),"raw")  + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,numbers.get(0).getObjectImage(),"drawable") + "," +
                "\"right\":" + numbers.get(0).getCustomData() +
                "}," +
                "{\"sound\":" +  ResourceDecoder.getIdentifier(context,numbers.get(1).getObjectSound(),"raw")  + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,numbers.get(1).getObjectImage(),"drawable") + "," +
                "\"right\":" + numbers.get(1).getCustomData() +
                "}," +
                "{\"sound\":" +  ResourceDecoder.getIdentifier(context,numbers.get(2).getObjectSound(),"raw")  + "," +
                "\"image\":" + ResourceDecoder.getIdentifier(context,numbers.get(2).getObjectImage(),"drawable") + "," +
                "\"right\":" + numbers.get(2).getCustomData() +
                "}" +
                "]}";

        return drillData;
    }

    public static String getDrillThreeJson(Context context,
                                           String monkeyWantsToEatSound,
                                           int numberOfItems,
                                           String numberOfItemsSound,
                                           String dragSound,
                                           String toTheTableSound,
                                           int totalItems,
                                           String item,
                                           String oneSound,
                                           String twoSound,
                                           String threeSound,
                                           String fourSound,
                                           String fiveSound,
                                           String sixSound,
                                           String sevenSound,
                                           String eightSound,
                                           String nineSound,
                                           String tenSound){
        String drillData =  "{\"monkey_wants_to_eat\":" + ResourceDecoder.getIdentifier(context,monkeyWantsToEatSound,"raw") + "," +
                "\"number_of_items_sound\":" + ResourceDecoder.getIdentifier(context,numberOfItemsSound,"raw") + "," +
                "\"drag_sound\":" + ResourceDecoder.getIdentifier(context,dragSound,"raw") + "," +
                "\"to_the_table_sound\":" + ResourceDecoder.getIdentifier(context,toTheTableSound,"raw") + "," +
                "\"number_of_items\":" + numberOfItems + "," +
                "\"total_items\":" + totalItems + "," +
                "\"item\":" + ResourceDecoder.getIdentifier(context,item,"drawable") + "," +
                "\"one_sound\":" + ResourceDecoder.getIdentifier(context,oneSound,"raw") + "," +
                "\"two_sound\":" + ResourceDecoder.getIdentifier(context,twoSound,"raw") + "," +
                "\"three_sound\":" + ResourceDecoder.getIdentifier(context,threeSound,"raw") + "," +
                "\"four_sound\":" + ResourceDecoder.getIdentifier(context,fourSound,"raw") + "," +
                "\"five_sound\":" + ResourceDecoder.getIdentifier(context,fiveSound     ,"raw") + "," +
                "\"six_sound\":" + ResourceDecoder.getIdentifier(context,sixSound,"raw") + "," +
                "\"seven_sound\":" + ResourceDecoder.getIdentifier(context,sevenSound,"raw") + "," +
                "\"eight_sound\":" + ResourceDecoder.getIdentifier(context,eightSound,"raw") + "," +
                "\"nine_sound\":" + ResourceDecoder.getIdentifier(context,nineSound,"raw") + "," +
                "\"ten_sound\":" + ResourceDecoder.getIdentifier(context,tenSound,"raw") +
                "}";
        return drillData;
    }

    public static String getDrillFourJson(Context context,
                                          String checkBigger,
                                          String monkeyHasSound,
                                          String numberOfLeftItemsSound,
                                          String numberOfRightItemsSound,
                                          String andSound,
                                          String moreOfQuestion,
                                          String touchTheBiggerSound,
                                          int numberOfLeftItems,
                                          String leftItemItem,
                                          String leftNumberImage,
                                          int numberOfRightItems,
                                          String rightItemItem,
                                          String rightNumberImage){
        String drillData = "{\"monkey_has\":" + ResourceDecoder.getIdentifier(context,monkeyHasSound,"raw") + "," +
                "\"check_bigger\":" + checkBigger + "," +
                "\"number_of_left_items_sound\":" + ResourceDecoder.getIdentifier(context,numberOfLeftItemsSound,"raw") + "," +
                "\"number_of_right_items_sound\":" + ResourceDecoder.getIdentifier(context,numberOfRightItemsSound,"raw") + "," +
                "\"and_sound\":" + ResourceDecoder.getIdentifier(context,andSound,"raw") + "," +
                "\"more_of_question\":" + ResourceDecoder.getIdentifier(context,moreOfQuestion,"raw") + "," +
                "\"number_of_left_items_sound\":" + ResourceDecoder.getIdentifier(context,numberOfLeftItemsSound,"raw") + "," +
                "\"touch_the_number\":" + ResourceDecoder.getIdentifier(context,touchTheBiggerSound,"raw") + "," +
                "\"number_of_left_items\":" + numberOfLeftItems + "," +
                "\"left_items_item\":" + ResourceDecoder.getIdentifier(context,leftItemItem,"drawable") + "," +
                "\"left_number_image\":" + ResourceDecoder.getIdentifier(context,leftNumberImage,"drawable") + "," +
                "\"number_of_right_items\":" + numberOfRightItems + "," +
                "\"right_items_item\":" + ResourceDecoder.getIdentifier(context,rightItemItem,"drawable") + "," +
                "\"right_number_image\":" + ResourceDecoder.getIdentifier(context,rightNumberImage,"drawable") +
                "}";
        return drillData;
    }

    public static String getDrillFiveJson(Context context,
                                          String helpMonkeyPackSound,
                                          String dragItemsOntoShelfSound,
                                          String touchSound,
                                          ArrayList<ObjectAndSound<String>> items,
                                          ArrayList<DraggableImage<String>> numerals,
                                          String oneSound,
                                          String twoSound,
                                          String threeSound,
                                          String fourSound,
                                          String fiveSound,
                                          String sixSound,
                                          String sevenSound,
                                          String eightSound,
                                          String nineSound,
                                          String tenSound){
        String drillData = "{\"help_monkey_pack\":" + ResourceDecoder.getIdentifier(context,helpMonkeyPackSound,"raw") + "," +
                "\"drag_items_onto_shelf\":" +  ResourceDecoder.getIdentifier(context,dragItemsOntoShelfSound,"raw") + "," +
                "\"can_you_find_and_touch\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"items\":[";
        int i = 0;
        for (ObjectAndSound<String> item: items) {
            if (i == 0)
            drillData += "{\"sound\":" + ResourceDecoder.getIdentifier(context, item.getObjectSound(), "raw") + "," +
                    "\"image\":" + ResourceDecoder.getIdentifier(context, item.getObjectImage(), "drawable") + "," +
                    "\"number\":" + item.getCustomData() +
                    "}";
            else
                drillData += ",{\"sound\":" + ResourceDecoder.getIdentifier(context, item.getObjectSound(), "raw") + "," +
                        "\"image\":" + ResourceDecoder.getIdentifier(context, item.getObjectImage(), "drawable") + "," +
                        "\"number\":" + item.getCustomData() +
                        "}";
            i++;
        }
        drillData += "],\"numerals\":[";
        i = 0;
        for (DraggableImage<String> item: numerals) {
            if (i == 0)
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            else
                drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            i++;
        }
        drillData += "]," +
                "\"one_sound\":" + ResourceDecoder.getIdentifier(context,oneSound,"raw") + "," +
                "\"two_sound\":" + ResourceDecoder.getIdentifier(context,twoSound,"raw") + "," +
                "\"three_sound\":" + ResourceDecoder.getIdentifier(context,threeSound,"raw") + "," +
                "\"four_sound\":" + ResourceDecoder.getIdentifier(context,fourSound,"raw") + "," +
                "\"five_sound\":" + ResourceDecoder.getIdentifier(context,fiveSound     ,"raw") + "," +
                "\"six_sound\":" + ResourceDecoder.getIdentifier(context,sixSound,"raw") + "," +
                "\"seven_sound\":" + ResourceDecoder.getIdentifier(context,sevenSound,"raw") + "," +
                "\"eight_sound\":" + ResourceDecoder.getIdentifier(context,eightSound,"raw") + "," +
                "\"nine_sound\":" + ResourceDecoder.getIdentifier(context,nineSound,"raw") + "," +
                "\"ten_sound\":" + ResourceDecoder.getIdentifier(context,tenSound,"raw") +
                "}";

        return drillData;
    }

    public static String getDrillFiveAndOneJson(Context context,
                                                String helpDamaWithMathsSound,
                                                String dragItemsOntoTableSound,
                                                String equationSound,
                                                String touchTheNumber,
                                                ArrayList<ObjectAndSound<String>> items,
                                                ArrayList<DraggableImage<String>> numerals,
                                                String answerImage,
                                                String oneSound,
                                                String twoSound,
                                                String threeSound,
                                                String fourSound,
                                                String fiveSound,
                                                String sixSound,
                                                String sevenSound,
                                                String eightSound,
                                                String nineSound,
                                                String tenSound,
                                                String elevenSound,
                                                String twelveSound,
                                                String thirteenSound,
                                                String fourteenSound,
                                                String fifteenSound,
                                                String sixteenSound,
                                                String seventeenSound,
                                                String eighteenSound,
                                                String nineteenSound,
                                                String twentySound){
        String drillData = "{\"help_dama_with_maths\":" + ResourceDecoder.getIdentifier(context,helpDamaWithMathsSound,"raw") + "," +
                "\"drag_items_onto_table\":" +  ResourceDecoder.getIdentifier(context,dragItemsOntoTableSound,"raw") + "," +
                "\"can_you_find_and_touch\":" + ResourceDecoder.getIdentifier(context,touchTheNumber,"raw") + "," +
                "\"equation_sound\":" + ResourceDecoder.getIdentifier(context,equationSound,"raw") + "," +
                "\"answer_image\":" + ResourceDecoder.getIdentifier(context,answerImage,"drawable") + "," +
                "\"items\":[";
        int i = 0;
        for (ObjectAndSound<String> item: items) {
            if (i == 0)
                drillData += "{\"sound\":" + ResourceDecoder.getIdentifier(context, item.getObjectSound(), "raw") + "," +
                        "\"image\":" + ResourceDecoder.getIdentifier(context, item.getObjectImage(), "drawable") + "," +
                        "\"numeral\":" + ResourceDecoder.getIdentifier(context, item.getObjectPhonicSound(), "drawable") + "," +
                        "\"number\":" + item.getCustomData() +
                        "}";
            else
                drillData += ",{\"sound\":" + ResourceDecoder.getIdentifier(context, item.getObjectSound(), "raw") + "," +
                        "\"image\":" + ResourceDecoder.getIdentifier(context, item.getObjectImage(), "drawable") + "," +
                        "\"numeral\":" + ResourceDecoder.getIdentifier(context, item.getObjectPhonicSound(), "drawable") + "," +
                        "\"number\":" + item.getCustomData() +
                        "}";
            i++;
        }
        drillData += "],\"numerals\":[";
        i = 0;
        for (DraggableImage<String> item: numerals) {
            if (i == 0)
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            else
                drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            i++;
        }
        drillData += "]," +
                "\"one_sound\":" + ResourceDecoder.getIdentifier(context,oneSound,"raw") + "," +
                "\"two_sound\":" + ResourceDecoder.getIdentifier(context,twoSound,"raw") + "," +
                "\"three_sound\":" + ResourceDecoder.getIdentifier(context,threeSound,"raw") + "," +
                "\"four_sound\":" + ResourceDecoder.getIdentifier(context,fourSound,"raw") + "," +
                "\"five_sound\":" + ResourceDecoder.getIdentifier(context,fiveSound     ,"raw") + "," +
                "\"six_sound\":" + ResourceDecoder.getIdentifier(context,sixSound,"raw") + "," +
                "\"seven_sound\":" + ResourceDecoder.getIdentifier(context,sevenSound,"raw") + "," +
                "\"eight_sound\":" + ResourceDecoder.getIdentifier(context,eightSound,"raw") + "," +
                "\"nine_sound\":" + ResourceDecoder.getIdentifier(context,nineSound,"raw") + "," +
                "\"ten_sound\":" + ResourceDecoder.getIdentifier(context,tenSound,"raw") + "," +
                "\"eleven_sound\":" + ResourceDecoder.getIdentifier(context,elevenSound,"raw") + "," +
                "\"twelve_sound\":" + ResourceDecoder.getIdentifier(context,twelveSound,"raw") + "," +
                "\"thirteen_sound\":" + ResourceDecoder.getIdentifier(context,thirteenSound,"raw") + "," +
                "\"fourteen_sound\":" + ResourceDecoder.getIdentifier(context,fourteenSound,"raw") + "," +
                "\"fifteen_sound\":" + ResourceDecoder.getIdentifier(context,fifteenSound,"raw") + "," +
                "\"sixteen_sound\":" + ResourceDecoder.getIdentifier(context,sixteenSound,"raw") + "," +
                "\"seventeen_sound\":" + ResourceDecoder.getIdentifier(context,seventeenSound,"raw") + "," +
                "\"eighteen_sound\":" + ResourceDecoder.getIdentifier(context,eighteenSound,"raw") + "," +
                "\"nineteen_sound\":" + ResourceDecoder.getIdentifier(context,nineteenSound,"raw") + "," +
                "\"twenty_sound\":" + ResourceDecoder.getIdentifier(context,twentySound,"raw") +
                "}";

        return drillData;
    }

    public static String getDrillSixJson(Context context,
                                         String letsLookAtShapesSound,
                                         String thisIsASound,
                                         String objectSound,
                                         String repeatAfterMeSound,
                                         String touchSound,
                                         String demoObject,
                                         String objectToTouch){
        String drillData = "{\"lets_look_at_shapes\":" + ResourceDecoder.getIdentifier(context,letsLookAtShapesSound,"raw") + "," +
                "\"this_is_sound\":" + ResourceDecoder.getIdentifier(context,thisIsASound,"raw") + "," +
                "\"object_sound\":" + ResourceDecoder.getIdentifier(context,objectSound,"raw") + "," +
                "\"repeat_afterme_sound\":" + ResourceDecoder.getIdentifier(context,repeatAfterMeSound,"raw") + "," +
                "\"touch_sound\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"demo_object\":" + ResourceDecoder.getIdentifier(context,demoObject,"drawable") + "," +
                "\"object_to_touch\":" + objectToTouch + "}";
        return drillData;
    }

    //object_to_touch == big or small
    public static String getDrillSixAndOneJson(Context context,
                                                String letsLookAtShapesSound,
                                                String theseAreSound,
                                                String objectSound,
                                                String repeatAfterMeSound,
                                                String touchSound,
                                                String objectToTouchSound,
                                                String demoObject,
                                                String objectToTouch){
        String drillData = "{\"lets_look_at_shapes\":" + ResourceDecoder.getIdentifier(context,letsLookAtShapesSound,"raw") + "," +
                "\"these_are_sound\":" + ResourceDecoder.getIdentifier(context,theseAreSound,"raw") + "," +
                "\"objects_sound\":" + ResourceDecoder.getIdentifier(context,objectSound,"raw") + "," +
                "\"repeat_afterme_sound\":" + ResourceDecoder.getIdentifier(context,repeatAfterMeSound,"raw") + "," +
                "\"touch_sound\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"object_to_touch_sound\":" + ResourceDecoder.getIdentifier(context,objectToTouchSound,"raw") + "," +
                "\"demo_object\":" + ResourceDecoder.getIdentifier(context,demoObject,"drawable") + "," +
                "\"object_to_touch\":" + objectToTouch + "}";
        return drillData;
    }

    //objectToTouch = small_object_one, big_object_one etc..
    public static String getDrillSixAndTwoJson(Context context,
                                               String letsLookAtShapesSound,
                                               String theseAreSound,
                                               String objectsOneSound,
                                               String objectsTwoSound,
                                               String andSound,
                                               String repeatAfterMeSound,
                                               String touchSound,
                                               String objectToTouchSound,
                                               String objectOne,
                                               String objectTwo,
                                               String objectToTouch){
        String drillData = "{\"lets_look_at_shapes\":" + ResourceDecoder.getIdentifier(context,letsLookAtShapesSound,"raw") + "," +
                "\"these_are_sound\":" + ResourceDecoder.getIdentifier(context,theseAreSound,"raw") + "," +
                "\"objects_one_sound\":" + ResourceDecoder.getIdentifier(context,objectsOneSound,"raw") + "," +
                "\"objects_two_sound\":" + ResourceDecoder.getIdentifier(context,objectsTwoSound,"raw") + "," +
                "\"and_sound\":" + ResourceDecoder.getIdentifier(context,andSound,"raw") + "," +
                "\"repeat_afterme_sound\":" + ResourceDecoder.getIdentifier(context,repeatAfterMeSound,"raw") + "," +
                "\"touch_sound\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"object_to_touch_sound\":" + ResourceDecoder.getIdentifier(context,objectToTouchSound,"raw") + "," +
                "\"object_one\":" + ResourceDecoder.getIdentifier(context,objectOne,"drawable") + "," +
                "\"object_two\":" + ResourceDecoder.getIdentifier(context,objectTwo,"drawable") + "," +
                "\"object_to_touch\":" + objectToTouch + "}";
        return drillData;
    }

    public static String getDrillSixAndThreeJson(Context context,
                                                 String monkeyIsHungryAndEatsSound,
                                                 String heEatsSound,
                                                 String objectsEatenSound,
                                                 String objectsSound,
                                                 String dragSound,
                                                 String toTheMonkeysMouthSound,
                                                 String touchSound,
                                                 String objectsImage,
                                                 String numberOfObjects,
                                                 String numberOfEatenObjects,
                                                 ArrayList<DraggableImage<String>> numerals){
        String drillData = "{\"monkey_is_hungry\":" + ResourceDecoder.getIdentifier(context,monkeyIsHungryAndEatsSound,"raw") + "," +
                "\"he_eats_sound\":" + ResourceDecoder.getIdentifier(context,heEatsSound,"raw") + "," +
                "\"objects_eaten\":" + ResourceDecoder.getIdentifier(context,objectsEatenSound,"raw") + "," +
                "\"objects_sound\":" + ResourceDecoder.getIdentifier(context,objectsSound,"raw") + "," +
                "\"drag_sound\":" + ResourceDecoder.getIdentifier(context,dragSound,"raw") + "," +
                "\"to_the_monkey_sound\":" + ResourceDecoder.getIdentifier(context,toTheMonkeysMouthSound,"raw") + "," +
                "\"touch_sound\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"objects_image\":" + ResourceDecoder.getIdentifier(context,objectsImage,"drawable") + "," +
                "\"number_of_eaten_objects\":" + numberOfEatenObjects + "," +
                "\"number_of_objects\":" + numberOfObjects + ",";
        drillData += "\"numerals\":[";
        int i = 0;
        for (DraggableImage<String> item: numerals) {
            if (i == 0)
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            else
                drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            i++;
        }
        drillData += "]}";
        return drillData;
    }

    public static String getDrillSixAndFourJson(Context context,
                                                 String damaHasSound,
                                                 String numberOfObjectsSound,
                                                 String numberOfGivenObjectsSound,
                                                 String heGivesSound,
                                                 String toMonkeySound,
                                                 String dragSound,
                                                 String toTheMonkeySpaceSound,
                                                 String equationSound,
                                                 String touchSound,
                                                 String objectsImage,
                                                 String numberOfObjects,
                                                 String numberOfObjectsImage,
                                                 String numberOfGivenObjects,
                                                 String numberOfGivenObjectsImage,
                                                 String answerImage,
                                                 ArrayList<DraggableImage<String>> numerals){
        String drillData = "{\"dama_has_sound\":" + ResourceDecoder.getIdentifier(context,damaHasSound,"raw") + "," +
                "\"number_of_objects_sound\":" + ResourceDecoder.getIdentifier(context,numberOfObjectsSound,"raw") + "," +
                "\"number_of_given_object_sound\":" + ResourceDecoder.getIdentifier(context,numberOfGivenObjectsSound,"raw") + "," +
                "\"he_gives_sound\":" + ResourceDecoder.getIdentifier(context,heGivesSound,"raw") + "," +
                "\"to_monkey_sound\":" + ResourceDecoder.getIdentifier(context,toMonkeySound,"raw") + "," +
                "\"drag_sound\":" + ResourceDecoder.getIdentifier(context,dragSound,"raw") + "," +
                "\"to_the_monkey_sound\":" + ResourceDecoder.getIdentifier(context,toTheMonkeySpaceSound,"raw") + "," +
                "\"equation_sound\":" + ResourceDecoder.getIdentifier(context,equationSound,"raw") + "," +
                "\"touch_sound\":" + ResourceDecoder.getIdentifier(context,touchSound,"raw") + "," +
                "\"objects_image\":" + ResourceDecoder.getIdentifier(context,objectsImage,"drawable") + "," +
                "\"number_of_given_objects\":" + numberOfGivenObjects + "," +
                "\"number_of_given_objects_image\":" + ResourceDecoder.getIdentifier(context,numberOfGivenObjectsImage,"drawable") + "," +
                "\"number_of_objects_image\":" + ResourceDecoder.getIdentifier(context,numberOfObjectsImage,"drawable") + "," +
                "\"answer_image\":" + ResourceDecoder.getIdentifier(context,answerImage,"drawable") + "," +
                "\"number_of_objects\":" + numberOfObjects + ",";
        drillData += "\"numerals\":[";
        int i = 0;
        for (DraggableImage<String> item: numerals) {
            if (i == 0)
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            else
                drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"right\":" + item.isRight() +
                        "}";
            i++;
        }
        drillData += "]}";
        return drillData;
    }

    public static String getDrillSevenJson(Context context,
                                           String patternIntrodution,
                                           String demoPattern,
                                           String patternSound,
                                           ArrayList<DraggableImage<String>> completionPieces,
                                           String dragSound,
                                           String objectToDragSound,
                                           String intoTheSpaceSound){
        String drillData = "{\"pattern_introduction_sound\":" + ResourceDecoder.getIdentifier(context,patternIntrodution,"raw") + "," +
                "\"drag_sound\":" + ResourceDecoder.getIdentifier(context,dragSound,"raw") + "," +
                "\"pattern_sound\":" + ResourceDecoder.getIdentifier(context,patternSound,"raw") + "," +
                "\"object_sound\":" + ResourceDecoder.getIdentifier(context,objectToDragSound,"raw") + "," +
                "\"into_the_space_sound\":" + ResourceDecoder.getIdentifier(context,intoTheSpaceSound,"raw") + "," +
                "\"demo_pattern\":" + ResourceDecoder.getIdentifier(context, demoPattern,"drawable") + ",";
        drillData += "\"completion_pieces\":[";
        int i = 0;
        for (DraggableImage<String> item: completionPieces) {
            if (i == 0)
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"isRight\":" + item.isRight() +
                        "}";
            else
                drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"isRight\":" + item.isRight() +
                        "}";
            i++;
        }
        drillData += "]}";
        return drillData;
    }

    public static String getDrillSevenAndOneJson(Context context,
                                                 String monkeyIsMissingSound,
                                                 String canYouHelpMonkey,
                                                 String dragMissingSound,
                                                 ArrayList<String>patternToComplete,
                                                 ArrayList<DraggableImage<String>> completionPieces){
        String drillData = "{\"pattern_introduction_sound\":" + ResourceDecoder.getIdentifier(context,monkeyIsMissingSound,"raw") + "," +
                "\"help_monkey_sound\":" + ResourceDecoder.getIdentifier(context,canYouHelpMonkey,"raw") + "," +
                "\"drag_sound\":" + ResourceDecoder.getIdentifier(context,dragMissingSound,"raw") + ",";
        drillData += "\"pattern_to_complete\":[";
        int i = 0;
        for (String item: patternToComplete) {
            if (i == 0) {
                if (item.equalsIgnoreCase("blank"))
                    drillData += "0";
                else
                    drillData += ResourceDecoder.getIdentifier(context, item, "drawable");
            }
            else {
                if (item.equalsIgnoreCase("blank"))
                    drillData += ",0";
                else
                    drillData += "," + ResourceDecoder.getIdentifier(context, item, "drawable");
            }
            i++;
        }
        drillData += "],\"completion_pieces\":[";
        i = 0;
        for (DraggableImage<String> item: completionPieces) {
            if (i == 0)
                drillData += "{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"isRight\":" + item.isRight() +
                        "}";
            else
                drillData += ",{\"image\":" + ResourceDecoder.getIdentifier(context, item.getcontent(), "drawable") + "," +
                        "\"isRight\":" + item.isRight() +
                        "}";
            i++;
        }
        drillData += "]}";
        return drillData;

    }
}
