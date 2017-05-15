package classact.com.xprize.control;

import android.content.Context;

import java.util.ArrayList;

import classact.com.xprize.common.Code;
import classact.com.xprize.database.model.MathImages;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceDecoder;

/**
 * Created by Tseliso on 12/22/2016.
 */

public class MathDrillJsonBuilder {
    public static String getDrillOneJson(Context context,
                                         String itsTimeToCount,
                                         String sayNumbersWithMe,
                                         ArrayList<Numeral> numerals) {
        String drillData = "{\"its_time_to_count\":\"" + itsTimeToCount + "\"," +
                "\"say_numbers_with_me\":\"" + sayNumbersWithMe + "\"," +
                "\"numerals\":[";
        for (int i = 0; i < numerals.size(); i++) {
            if (i > 0) { // add comma in front logic
                drillData += ",";
            }
            Numeral numeral = numerals.get(i);
            drillData +=  "{\"sound\":\"" + numeral.getSound() + "\"," +
                    "\"numeral\":\"" + numeral.getBlackImage() + "\"," +
                    "\"numeral_sparkling\":\"" + numeral.getSparklingImage() + "\"" +
                    "}";
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
                                         ArrayList<ObjectAndSound<String>> numbers) {

        // Debug
        System.out.println("MathDrillJsonBuilder.getDrillTwoJson > Debug: monkeyHasSound = " + monkeyHasSound);
        System.out.println("MathDrillJsonBuilder.getDrillTwoJson > Debug: numberOfObjectsSound = " + numberOfObjectsSound);
        System.out.println("MathDrillJsonBuilder.getDrillTwoJson > Debug: numberOfObjects = " + numberOfObjects);
        System.out.println("MathDrillJsonBuilder.getDrillTwoJson > Debug: objectImage = " + objectImage);
        System.out.println("MathDrillJsonBuilder.getDrillTwoJson > Debug: touchSound = " + touchSound);
        System.out.println("MathDrillJsonBuilder.getDrillTwoJson > Debug: numeralSound = " + numeralSound);

        String drillData = "{\"monkey_has\":\"" + monkeyHasSound + "\"," +
                "\"object\":\"" + objectImage + "\"," +
                "\"number_of_objects\":" + numberOfObjects + "," + // int
                "\"number_of_objects_sound\":\"" + numberOfObjectsSound + "\"," +
                "\"numeral_sound\":\"" + numeralSound + "\"," +
                "\"can_you_find_and_touch\":\"" + touchSound + "\"," +
                "\"numerals\":[";
        for (int i = 0; i < numbers.size(); i++) {
            ObjectAndSound<String> number = numbers.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"sound\":\"" + number.getObjectSound() + "\"," +
                    "\"image\":\"" + number.getObjectImage() + "\"," +
                    "\"right\":" + number.getCustomData() + // int
                    "}";
        }
        drillData += "]}";
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
                                           ArrayList<String> numberSounds) {

        String drillData =  "{\"monkey_wants_to_eat\":\"" + monkeyWantsToEatSound + "\"," +
                "\"number_of_items_sound\":\"" + numberOfItemsSound + "\"," +
                "\"drag_sound\":\"" + dragSound + "\"," +
                "\"to_the_table_sound\":\"" + toTheTableSound + "\"," +
                "\"number_of_items\":" + numberOfItems + "," +
                "\"total_items\":" + totalItems + "," +
                "\"item\":\"" + item + "\"," +
                "\"one_sound\":\"" + numberSounds.get(0) + "\"," +
                "\"two_sound\":\"" + numberSounds.get(1) + "\"," +
                "\"three_sound\":\"" + numberSounds.get(2) + "\"," +
                "\"four_sound\":\"" + numberSounds.get(3) + "\"," +
                "\"five_sound\":\"" + numberSounds.get(4) + "\"," +
                "\"six_sound\":\"" + numberSounds.get(5) + "\"," +
                "\"seven_sound\":\"" + numberSounds.get(6) + "\"," +
                "\"eight_sound\":\"" + numberSounds.get(7) + "\"," +
                "\"nine_sound\":\"" + numberSounds.get(8) + "\"," +
                "\"ten_sound\":\"" + numberSounds.get(9) + "\"" +
                "}";
        return drillData;
    }

    public static String getDrillFourJson(Context context,
                                          String checkBigger,
                                          String monkeyHasSound,
                                          String andSound,
                                          String moreOfQuestion,
                                          String touchTheBiggerSound,
                                          ArrayList<MathImages> mathImages,
                                          ArrayList<Numerals> numerals) {

        int n = mathImages.size();

        // Validate number of math images
        if (n < 2) {
            return null; // Need at least two
        }

        // Get scrambled indexes
        int[] s = FisherYates.shuffle(n);

        // Get left and right image
        MathImages leftObject = mathImages.get(s[0]);
        MathImages rightObject = mathImages.get(s[1]);

        // Get sizes
        int leftSize = leftObject.getNumberOfImages();
        int rightSize = rightObject.getNumberOfImages();
        int maxSize = Math.max(leftSize, rightSize);

        // Get left and right's respective numeral
        Numerals leftNumber = null;
        Numerals rightNumber = null;
        // Assignment numerals
        for (int i = 0; i < numerals.size(); i++) {
            Numerals number = numerals.get(i);
            int numberValue = number.getNumber();
            // Check if left
            if (numberValue == leftSize) {
                leftNumber = number;
                // Check if right is not null
                // If it isn't all happy - can break!
                if (rightNumber != null) {
                    break;
                }
            }
            // Check if right
            // (Check both, not mutually exclusive)
            if (numberValue == rightSize) {
                rightNumber = number;
                // Check if left is not null
                // If it isn't all happy - can break!
                if (leftNumber != null) {
                    break;
                }
            }
        }

        String drillData = "{\"monkey_has\":\"" + monkeyHasSound + "\"," +
                "\"check_bigger\":\"" + checkBigger + "\"," +
                "\"number_of_left_items_sound\":\"" + leftObject.getNumberOfImagesSound() + "\"," +
                "\"number_of_right_items_sound\":\"" + rightObject.getNumberOfImagesSound() + "\"," +
                "\"and_sound\":\"" + andSound + "\"," +
                "\"more_of_question\":\"" + moreOfQuestion + "\"," +
                "\"touch_the_number\":\"" + touchTheBiggerSound + "\"," +
                "\"number_of_left_items\":" + leftSize + "," +
                "\"left_items_item\":\"" + leftObject.getImageName() + "\"," +
                "\"left_items_sound\":\"" + leftObject.getImageSound() + "\"," +
                "\"left_number_image\":\"" + leftNumber.getBlackImage() + "\"," +
                "\"number_of_right_items\":" + rightSize + "," +
                "\"right_items_item\":\"" + rightObject.getImageName() + "\"," +
                "\"right_items_sound\":\"" + rightObject.getImageSound() + "\"," +
                "\"right_number_image\":\"" + rightNumber.getBlackImage() + "\"" +
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
        String drillData = "{\"help_monkey_pack\":\"" + helpMonkeyPackSound + "\"," +
                "\"drag_items_onto_shelf\":\"" + dragItemsOntoShelfSound + "\"," +
                "\"can_you_find_and_touch\":\"" + touchSound + "\"," +
                "\"items\":[";
        for (int i = 0; i < items.size(); i++) {
            ObjectAndSound<String> item = items.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"sound\":\"" +  item.getObjectSound() + "\"," +
                    "\"image\":\"" +  item.getObjectImage() + "\"," +
                    "\"number\":" + item.getCustomData() +
                    "}";
        }
        drillData += "],\"numerals\":[";
        for (int i = 0; i < numerals.size(); i++) {
            DraggableImage<String> item = numerals.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"right\":" + item.isRight() +
                    "}";
        }
        drillData += "]," +
                "\"one_sound\":\"" + oneSound + "\"," +
                "\"two_sound\":\"" + twoSound + "\"," +
                "\"three_sound\":\"" + threeSound + "\"," +
                "\"four_sound\":\"" + fourSound + "\"," +
                "\"five_sound\":\"" + fiveSound      + "\"," +
                "\"six_sound\":\"" + sixSound + "\"," +
                "\"seven_sound\":\"" + sevenSound + "\"," +
                "\"eight_sound\":\"" + eightSound + "\"," +
                "\"nine_sound\":\"" + nineSound + "\"," +
                "\"ten_sound\":\"" + tenSound + "\"" +
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
        String drillData = "{\"help_dama_with_maths\":\"" + helpDamaWithMathsSound + "\"," +
                "\"drag_items_onto_table\":\"" + dragItemsOntoTableSound + "\"," +
                "\"can_you_find_and_touch\":\"" + touchTheNumber + "\"," +
                "\"equation_sound\":\"" + equationSound + "\"," +
                "\"answer_image\":\"" + answerImage + "\"," +
                "\"items\":[";
        for (int i = 0; i < items.size(); i++) {
            ObjectAndSound<String> item = items.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"sound\":\"" +  item.getObjectSound() + "\"," +
                    "\"image\":\"" +  item.getObjectImage() + "\"," +
                    "\"numeral\":\"" +  item.getObjectPhonicSound() + "\"," +
                    "\"number\":\"" + item.getCustomData() + "\"" +
                    "}";

        }
        drillData += "],\"numerals\":[";
        for (int i = 0; i < numerals.size(); i++) {
            DraggableImage<String> item = numerals.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"right\":\"" + item.isRight() + "\"" +
                    "}";
        }
        drillData += "]," +
                "\"one_sound\":\"" + oneSound + "\"," +
                "\"two_sound\":\"" + twoSound + "\"," +
                "\"three_sound\":\"" + threeSound + "\"," +
                "\"four_sound\":\"" + fourSound + "\"," +
                "\"five_sound\":\"" + fiveSound      + "\"," +
                "\"six_sound\":\"" + sixSound + "\"," +
                "\"seven_sound\":\"" + sevenSound + "\"," +
                "\"eight_sound\":\"" + eightSound + "\"," +
                "\"nine_sound\":\"" + nineSound + "\"," +
                "\"ten_sound\":\"" + tenSound + "\"," +
                "\"eleven_sound\":\"" + elevenSound + "\"," +
                "\"twelve_sound\":\"" + twelveSound + "\"," +
                "\"thirteen_sound\":\"" + thirteenSound + "\"," +
                "\"fourteen_sound\":\"" + fourteenSound + "\"," +
                "\"fifteen_sound\":\"" + fifteenSound + "\"," +
                "\"sixteen_sound\":\"" + sixteenSound + "\"," +
                "\"seventeen_sound\":\"" + seventeenSound + "\"," +
                "\"eighteen_sound\":\"" + eighteenSound + "\"," +
                "\"nineteen_sound\":\"" + nineteenSound + "\"," +
                "\"twenty_sound\":\"" + twentySound + "\"" +
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

        // Debug
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: letsLookAtShapesSound = " + letsLookAtShapesSound);
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: thisIsASound = " + thisIsASound);
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: objectSound = " + objectSound);
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: repeatAfterMeSound = " + repeatAfterMeSound);
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: touchSound = " + touchSound);
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: demoObject = " + demoObject);
        System.out.println("MathDrillJsonBuilder.getDrillSixJson > Debug: objectToTouch = " + objectToTouch);

        String drillData = "{\"lets_look_at_shapes\":\"" + letsLookAtShapesSound + "\"," +
                "\"this_is_sound\":\"" + thisIsASound + "\"," +
                "\"object_sound\":\"" + objectSound + "\"," +
                "\"repeat_afterme_sound\":\"" + repeatAfterMeSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"demo_object\":\"" + demoObject + "\"," +
                "\"object_to_touch\":\"" + objectToTouch + "\"}";
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
        String drillData = "{\"lets_look_at_shapes\":\"" + letsLookAtShapesSound + "\"," +
                "\"these_are_sound\":\"" + theseAreSound + "\"," +
                "\"objects_sound\":\"" + objectSound + "\"," +
                "\"repeat_afterme_sound\":\"" + repeatAfterMeSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"object_to_touch_sound\":\"" + objectToTouchSound + "\"," +
                "\"demo_object\":\"" + demoObject + "\","+
                "\"object_to_touch\":\"" + objectToTouch + "\"}";
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
        String drillData = "{\"lets_look_at_shapes\":\"" + letsLookAtShapesSound + "\"," +
                "\"these_are_sound\":\"" + theseAreSound + "\"," +
                "\"objects_one_sound\":\"" + objectsOneSound + "\"," +
                "\"objects_two_sound\":\"" + objectsTwoSound + "\"," +
                "\"and_sound\":\"" + andSound + "\"," +
                "\"repeat_afterme_sound\":\"" + repeatAfterMeSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"object_to_touch_sound\":\"" + objectToTouchSound + "\"," +
                "\"object_one\":\"" + objectOne + "\","+
                "\"object_two\":\"" + objectTwo + "\","+
                "\"object_to_touch\":\"" + objectToTouch + "\"}";
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
        String drillData = "{\"monkey_is_hungry\":\"" + monkeyIsHungryAndEatsSound + "\"," +
                "\"he_eats_sound\":\"" + heEatsSound + "\"," +
                "\"objects_eaten\":\"" + objectsEatenSound + "\"," +
                "\"objects_sound\":\"" + objectsSound + "\"," +
                "\"drag_sound\":\"" + dragSound + "\"," +
                "\"to_the_monkey_sound\":\"" + toTheMonkeysMouthSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"objects_image\":\"" + objectsImage + "\","+
                "\"number_of_eaten_objects\":\"" + numberOfEatenObjects + "\"," +
                "\"number_of_objects\":\"" + numberOfObjects + "\",";
        drillData += "\"numerals\":[";
        for (int i = 0; i < numerals.size(); i++) {
            DraggableImage<String> item = numerals.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"right\":\"" + item.isRight() + "\"" +
                    "}";
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
        String drillData = "{\"dama_has_sound\":\"" + damaHasSound + "\"," +
                "\"number_of_objects_sound\":\"" + numberOfObjectsSound + "\"," +
                "\"number_of_given_object_sound\":\"" + numberOfGivenObjectsSound + "\"," +
                "\"he_gives_sound\":\"" + heGivesSound + "\"," +
                "\"to_monkey_sound\":\"" + toMonkeySound + "\"," +
                "\"drag_sound\":\"" + dragSound + "\"," +
                "\"to_the_monkey_sound\":\"" + toTheMonkeySpaceSound + "\"," +
                "\"equation_sound\":\"" + equationSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"objects_image\":\"" + objectsImage + "\","+
                "\"number_of_given_objects\":\"" + numberOfGivenObjects + "\"," +
                "\"number_of_given_objects_image\":\"" + numberOfGivenObjectsImage + "\","+
                "\"number_of_objects_image\":\"" + numberOfObjectsImage + "\","+
                "\"answer_image\":\"" + answerImage + "\","+
                "\"number_of_objects\":\"" + numberOfObjects + "\",";
        drillData += "\"numerals\":[";
        for (int i = 0; i < numerals.size(); i++) {
            DraggableImage<String> item = numerals.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += ",{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"right\":\"" + item.isRight() + "\"" +
                    "}";
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

        // Debug
        System.out.println("MathDrillJsonBuilder.getDrillSevenJson > Debug: patternIntrodution = " + patternIntrodution);
        System.out.println("MathDrillJsonBuilder.getDrillSevenJson > Debug: demoPattern = " + demoPattern);
        System.out.println("MathDrillJsonBuilder.getDrillSevenJson > Debug: patternSound = " + patternSound);
        System.out.println("MathDrillJsonBuilder.getDrillSevenJson > Debug: dragSound = " + dragSound);
        System.out.println("MathDrillJsonBuilder.getDrillSevenJson > Debug: objectToDragSound = " + objectToDragSound);
        System.out.println("MathDrillJsonBuilder.getDrillSevenJson > Debug: intoTheSpaceSound = " + intoTheSpaceSound);

        String drillData = "{\"pattern_introduction_sound\":\"" + patternIntrodution + "\"," +
                "\"drag_sound\":\"" + dragSound + "\"," +
                "\"pattern_sound\":\"" + patternSound + "\"," +
                "\"object_sound\":\"" + objectToDragSound + "\"," +
                "\"into_the_space_sound\":\"" + intoTheSpaceSound + "\"," +
                "\"demo_pattern\":\"" +  demoPattern + "\",";
        drillData += "\"completion_pieces\":[";
        for (int i = 0; i < completionPieces.size(); i++) {
            DraggableImage<String> item = completionPieces.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"isRight\":\"" + item.isRight() + "\"" +
                    "}";
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
        String drillData = "{\"pattern_introduction_sound\":\"" + monkeyIsMissingSound + "\"," +
                "\"help_monkey_sound\":\"" + canYouHelpMonkey + "\"," +
                "\"drag_sound\":\"" + dragMissingSound + "\",";
        drillData += "\"pattern_to_complete\":[";
        for (int i = 0; i < patternToComplete.size(); i++) {
            String item = patternToComplete.get(i);
            if (i > 0) {
                drillData += ",";
            }
            if (item.equalsIgnoreCase("blank")) {
                drillData = "\"" + Code.BLANK_IMAGE + "\"";
            } else {
                drillData += "\"" + item + "\"";
            }

        }
        drillData += "],\"completion_pieces\":[";
        for (int i = 0; i < patternToComplete.size(); i++) {
            DraggableImage<String> item = completionPieces.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"isRight\":\"" + item.isRight() + "\"" +
                    "}";

        }
        drillData += "]}";
        return drillData;
    }
}
