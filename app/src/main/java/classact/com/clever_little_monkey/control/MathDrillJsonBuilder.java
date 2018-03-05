package classact.com.clever_little_monkey.control;

import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import classact.com.clever_little_monkey.database.model.MathImages;
import classact.com.clever_little_monkey.database.model.Numerals;
import classact.com.clever_little_monkey.utils.FisherYates;

/**
 * Created by Tseliso on 12/22/2016.
 */

public class MathDrillJsonBuilder {

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
                                                String touchTheNumber,
                                                String equationSound,
                                                String[] mathOperators,
                                                List<MathImages> mathImages,
                                                Numerals sumTotalNumber,
                                                SparseArray<Numerals> numbers) {

        String drillData = "{\"help_dama_with_maths\":\"" + helpDamaWithMathsSound + "\"," +
                "\"drag_items_onto_table\":\"" + dragItemsOntoTableSound + "\"," +
                "\"can_you_find_and_touch\":\"" + touchTheNumber + "\"," +
                "\"equation_sound\":\"" + equationSound + "\"," +
                "\"plus_sign\":\"" + mathOperators[0] + "\"," +
                "\"equals_sign\":\"" + mathOperators[1] + "\"," +
                "\"answer\":{" +
                    "\"image\":\"" + sumTotalNumber.getBlackImage() + "\"," +
                    "\"sound\":\"" + sumTotalNumber.getSound() + "\"," +
                    "\"value\":" + sumTotalNumber.getNumber() +
                "}," +
                "\"things\":[";

        for (int i = 0; i < mathImages.size(); i++) {
            MathImages mathImage = mathImages.get(i);
            if (i > 0) {
                drillData += ",";
            }
            int numberOfImages = mathImage.getNumberOfImages();
            drillData += "{\"sound\":\"" + mathImage.getImageSound() + "\"," +
                    "\"image\":\"" + mathImage.getImageName() + "\"," +
                    "\"number_image\":\"" + numbers.get(numberOfImages).getBlackImage() + "\"," +
                    "\"number_sound\":\"" + numbers.get(numberOfImages).getSound() + "\"," +
                    "\"count\":" + numberOfImages + "" +
                    "}";
        }
        drillData += "],\"numbers\":[";
        for (int i = 0; i < numbers.size(); i++) {
            Numerals number = numbers.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" + number.getBlackImage() + "\"," +
                    "\"sound\":\"" + number.getSound() + "\"," +
                    "\"value\":" + number.getNumber() +
                    "}";
        }
        drillData += "]}";
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
                                                String repeatAfterMeSound,
                                                String touchSound,
                                                String shapeImageName,
                                                String shapeSingularSound,
                                                String shapePluralSound,
                                                String shapeComparativeSound){
        String drillData = "{\"lets_look_at_shapes\":\"" + letsLookAtShapesSound + "\"," +
                "\"these_are_sound\":\"" + theseAreSound + "\"," +
                "\"repeat_afterme_sound\":\"" + repeatAfterMeSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"object_name\":\"" + shapeImageName + "\"," +
                "\"object_singular_sound\":\"" + shapeSingularSound + "\"," +
                "\"object_plural_sound\":\"" + shapePluralSound + "\","+
                "\"object_comparative_sound\":\"" + shapeComparativeSound + "\"}";
        return drillData;
    }

    //objectToTouch = small_object_one, big_object_one etc..
    public static String getDrillSixAndTwoJson(Context context,
                                               String letsLookAtShapesSound,
                                               String theseAreSound,
                                               String andSound,
                                               String repeatAfterMeSound,
                                               String touchSound,
                                               String shapeOneImage,
                                               String shapeOneSingularSound,
                                               String shapeOnePluralSound,
                                               String shapeTwoImage,
                                               String shapeTwoSingularSound,
                                               String shapeTwoPluralSound,
                                               String shapeOneAndTwoPluralSound,
                                               String shapeComparativeSound){
        String drillData = "{\"lets_look_at_shapes\":\"" + letsLookAtShapesSound + "\"," +
                "\"these_are_sound\":\"" + theseAreSound + "\"," +
                "\"and_sound\":\"" + andSound + "\"," +
                "\"repeat_afterme_sound\":\"" + repeatAfterMeSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"object_one\":{" +
                    "\"image_name\":\"" + shapeOneImage + "\","+
                    "\"singular_sound\":\"" + shapeOneSingularSound + "\","+
                    "\"plural_sound\":\"" + shapeOnePluralSound + "\"},"+
                "\"object_two\":{" +
                    "\"image_name\":\"" + shapeTwoImage + "\"," +
                    "\"singular_sound\":\"" + shapeTwoSingularSound + "\"," +
                    "\"plural_sound\":\"" + shapeTwoPluralSound + "\"}," +
                "\"objects_plural_sound\":\"" + shapeOneAndTwoPluralSound + "\"," +
                "\"object_comparative_sound\":\"" + shapeComparativeSound + "\"}";
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
                                                 List<Numerals> numerals,
                                                 List<Numerals> countNumbers){
        String drillData = "{\"monkey_is_hungry\":\"" + monkeyIsHungryAndEatsSound + "\"," +
                "\"he_eats_sound\":\"" + heEatsSound + "\"," +
                "\"objects_eaten\":\"" + objectsEatenSound + "\"," +
                "\"objects_sound\":\"" + objectsSound + "\"," +
                "\"drag_sound\":\"" + dragSound + "\"," +
                "\"to_the_monkey_sound\":\"" + toTheMonkeysMouthSound + "\"," +
                "\"touch_sound\":\"" + touchSound + "\"," +
                "\"objects_image\":\"" + objectsImage + "\","+
                "\"number_of_eaten_objects\":" + numberOfEatenObjects + "," +
                "\"number_of_objects\":" + numberOfObjects + ",";
        drillData += "\"drill_specific_numbers\":[";
        for (int i = 0; i < numerals.size(); i++) {
            Numerals number = numerals.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  number.getBlackImage() + "\"," +
                    "\"value\":" + number.getNumber() +
                    "}";
        }
        drillData += "],";
        drillData += "\"count_numbers\":[";
        for (int i = 0; i < countNumbers.size(); i++) {
            Numerals countNumber = countNumbers.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  countNumber.getBlackImage() + "\"," +
                    "\"sound\":\"" +  countNumber.getSound() + "\"," +
                    "\"value\":" + countNumber.getNumber() +
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
                                                 List<Numerals> numerals,
                                                 List<Numerals> countNumbers){
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
                "\"number_of_given_objects\":" + numberOfGivenObjects + "," +
                "\"number_of_given_objects_image\":\"" + numberOfGivenObjectsImage + "\","+
                "\"number_of_objects_image\":\"" + numberOfObjectsImage + "\","+
                "\"answer_image\":\"" + answerImage + "\","+
                "\"number_of_objects\":" + numberOfObjects + ",";
        drillData += "\"numerals\":[";
        for (int i = 0; i < numerals.size(); i++) {
            Numerals number = numerals.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" + number.getBlackImage() + "\"," +
                    "\"sound\":\"" + number.getSound() + "\"," +
                    "\"value\":" + number.getNumber() +
                    "}";
        }
        drillData += "],";
        drillData += "\"count_numbers\":[";
        for (int i = 0; i < countNumbers.size(); i++) {
            Numerals countNumber = countNumbers.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  countNumber.getBlackImage() + "\"," +
                    "\"sound\":\"" +  countNumber.getSound() + "\"," +
                    "\"value\":" + countNumber.getNumber() +
                    "}";
        }
        drillData += "]}";
        return drillData;
    }

    public static String getDrillSevenJson(Context context,
                                           String patternIntrodution,
                                           String dragSound,
                                           String intoTheSpaceSound,
                                           String demoPattern,
                                           String patternSound,
                                           String objectToDragSound,
                                           ArrayList<DraggableImage<String>> completionPieces) {

        String drillData = "{\"pattern_introduction_sound\":\"" + patternIntrodution + "\"," +
                "\"drag_sound\":\"" + dragSound + "\"," +
                "\"into_the_space_sound\":\"" + intoTheSpaceSound + "\"," +
                "\"demo_pattern\":\"" +  demoPattern + "\"," +
                "\"pattern_sound\":\"" + patternSound + "\"," +
                "\"object_sound\":\"" + objectToDragSound + "\",";
        drillData += "\"completion_pieces\":[";
        for (int i = 0; i < completionPieces.size(); i++) {
            DraggableImage<String> item = completionPieces.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"isRight\":" + item.isRight() +
                    "}";
        }
        drillData += "]}";
        return drillData;
    }

    public static String getDrillSevenAndOneJson(Context context,
                                                 String monkeyIsMissingSound,
                                                 String canYouHelpMonkey,
                                                 String dragMissingSound,
                                                 List<DraggableImage<String>> completionPieces,
                                                 List<Numerals> countNumbers){
        String drillData = "{\"pattern_introduction_sound\":\"" + monkeyIsMissingSound + "\"," +
                "\"help_monkey_sound\":\"" + canYouHelpMonkey + "\"," +
                "\"drag_sound\":\"" + dragMissingSound + "\",";
        drillData += "\"completion_pieces\":[";
        for (int i = 0; i < completionPieces.size(); i++) {
            DraggableImage<String> item = completionPieces.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  item.getcontent() + "\"," +
                    "\"value\":" + Integer.parseInt(item.getExtraData()) + "," +
                    "\"isRight\":" + item.isRight() +
                    "}";

        }
        drillData += "],";
        drillData += "\"count_numbers\":[";
        for (int i = 0; i < countNumbers.size(); i++) {
            Numerals countNumber = countNumbers.get(i);
            if (i > 0) {
                drillData += ",";
            }
            drillData += "{\"image\":\"" +  countNumber.getBlackImage() + "\"," +
                    "\"sound\":\"" +  countNumber.getSound() + "\"," +
                    "\"value\":" + countNumber.getNumber() +
                    "}";
        }
        drillData += "]}";
        return drillData;
    }
}
