package classact.com.xprize.controller.catalogue;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import classact.com.xprize.activity.drill.math.MathsDrillFiveActivity;
import classact.com.xprize.activity.drill.math.MathsDrillFiveAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillFourActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSevenActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSevenAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndFourActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndThreeActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndTwoActivity;
import classact.com.xprize.activity.drill.math.MathsDrillThreeActivity;
import classact.com.xprize.activity.drill.math.MathsDrillTwoActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.MathDrillJsonBuilder;
import classact.com.xprize.control.ObjectAndSound;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.MathDrillFlowWordsHelper;
import classact.com.xprize.database.helper.MathImageHelper;
import classact.com.xprize.database.helper.NumeralHelper;
import classact.com.xprize.database.model.MathDrillFlowWords;
import classact.com.xprize.database.model.MathImages;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.locale.Languages;

public class MathDrills {

    @Inject
    public MathDrills() {}

    public Intent D2(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D2 > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);

            ArrayList<Integer>  mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);
            MathImages mathImages = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)); // drill 2 only has one record per unit. So we can select one at a time.

            int numberOfImages = mathImages.getNumberOfImages();
            int chosenNumberIndex = 0;

            ArrayList<Integer> numeralsFromDB;
            numeralsFromDB = NumeralHelper.getNumeralsBelowLimit(dbHelper.getReadableDatabase(), languageId,
                    numberOfImages + 2, boyGirl); // add 2 extra images (learning help?)

            ArrayList<ObjectAndSound<String>> numbers = new ArrayList<>();
            for (int i=0; i < numeralsFromDB.size(); i++ ) {
                Numerals numeralFromDB = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(i));
                ObjectAndSound<String> number = new ObjectAndSound<>(numeralFromDB.getBlackImage(), numeralFromDB.getSound(), "");
                if (numeralFromDB.getNumber() == numberOfImages){
                    number.setCustomData("1");
                    chosenNumberIndex = i;
                } else {
                    number.setCustomData("0");
                }
                numbers.add(number);
            }

            System.out.println("Chosen number sound: " + numbers.get(chosenNumberIndex).getObjectSound());

            String drillData = MathDrillJsonBuilder.getDrillTwoJson(
                    context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathImages.getNumberOfImagesSound(),
                    mathImages.getNumberOfImages(),
                    mathImages.getImageName(),
                    mathDrillFlowWord.getDrillSound2(),
                    numbers.get(chosenNumberIndex).getObjectSound(),
                    numbers
            );
            intent = new Intent(context, MathsDrillTwoActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D2: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D2: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D3(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D3 > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);
            ArrayList<Integer> numerals;
            numerals = NumeralHelper.getNumeralsBelowLimit(dbHelper.getReadableDatabase(), languageId, limit, boyGirl);

            ArrayList<Integer>  mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            ArrayList<String> numberSounds = new ArrayList<>();
            for (int i = 0; i < numerals.size(); i++) {
                String number = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numerals.get(i)).getSound();
                numberSounds.add(number);
            }

            String drillData = MathDrillJsonBuilder.getDrillThreeJson(
                    context,
                    mathDrillFlowWord.getDrillSound1(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(),
                    mathImageList.get(0)).getTestNumber(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(),
                    mathImageList.get(0)).getNumberOfImagesSound(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(),
                    mathImageList.get(0)).getNumberOfImages(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(),
                    mathImageList.get(0)).getImageName(),
                    numberSounds
            );
            intent = new Intent(context, MathsDrillThreeActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D3: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D3: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D4(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D4 > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId,languageId);

            ArrayList<Integer> mathImageIds = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);
            ArrayList<MathImages> mathImages = new ArrayList<>();

            int numeralLimit = 0;
            for (int i = 0; i < mathImageIds.size(); i++) {
                int mathImageId = mathImageIds.get(i);
                MathImages mathImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageId);
                mathImages.add(mathImage);
                numeralLimit = Math.max(numeralLimit, mathImage.getNumberOfImages());
            }

            ArrayList<Integer> numeralIds = NumeralHelper.getNumeralsBelowLimitFromZero(dbHelper.getReadableDatabase(), languageId, numeralLimit, boyGirl);
            ArrayList<Numerals> numerals = new ArrayList<>();
            for (int i = 0; i < numeralIds.size(); i++) {
                numerals.add(NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralIds.get(i)));
            }

            String drillData = MathDrillJsonBuilder.getDrillFourJson(
                    context,
                    "yes",
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    mathDrillFlowWord.getDrillSound4(),
                    mathImages,
                    numerals
            );
            intent = new Intent(context, MathsDrillFourActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D4: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D4: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D5A(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D5A > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);

            ArrayList<Integer> mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            ArrayList<ObjectAndSound<String>> items = new ArrayList<>();
            int correctTestNumber=0;
            for (int i=0; i < mathImageList.size(); i++ ) {
                MathImages mathImages = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(i));
                correctTestNumber = mathImages.getTestNumber();
                ObjectAndSound<String> item = new ObjectAndSound<>(mathImages.getImageName(), mathImages.getImageSound(), "");
                item.setCustomData(String.valueOf(mathImages.getNumberOfImages()));
                items.add(item);
            }

            ArrayList<DraggableImage<String>> numerals = new ArrayList<>();

            ArrayList<Integer> numeralsFromDB_2_Only;
            numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(dbHelper.getReadableDatabase(), languageId,10, 2, correctTestNumber, boyGirl);
            for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
                Numerals numeralFromDB = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
                numerals.add(new DraggableImage<>(0, 0, numeralFromDB.getBlackImage()));
            }
            Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, correctTestNumber);
            numerals.add(new DraggableImage<>(0, 1, numeralCorrectAnswer.getBlackImage()));

            ArrayList<Integer> numeralsFromDB;
            numeralsFromDB = NumeralHelper.getNumeralsBelowLimit(dbHelper.getReadableDatabase(), languageId, limit, boyGirl);
            String drillData = MathDrillJsonBuilder.getDrillFiveJson(
                    context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    items,
                    numerals,
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(0)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(1)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(2)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(3)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(4)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(5)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(6)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(7)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(8)).getSound(),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB.get(9)).getSound()
            );
            intent = new Intent(context, MathsDrillFiveActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D5A: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D5A: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D5B(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D5B > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);
            ArrayList<Integer> mathImageIds = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            List<MathImages> mathImages = new ArrayList<>();

            /* HACK LOGIC: DB is not reflecting this correctly 2017-05-16 10:42AM */
            // PENCIL
            MathImages mathImageA = new MathImages();
            mathImageA.setImageName("orange");
            if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
                mathImageA.setImageSound("orange_sound");
            } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
                mathImageA.setImageSound("orange_sound");
            }
            // Add pencil to math images
            mathImages.add(mathImageA);
            // PEN
            MathImages mathImageB = new MathImages();
            mathImageB.setImageName("banana");
            if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
                mathImageB.setImageSound("banana_sound");
            } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
                mathImageB.setImageSound("ndizi_sound");
            }
            // Add pen to math images
            mathImages.add(mathImageB);

            int sumTotal = 0;
            int mathImagesEdited = 0;
            int maxMathImages = mathImages.size();

            if (unitId < 18) {
                for (int i = 0; i < mathImageIds.size(); i++) {
                    MathImages mathDBImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageIds.get(i));
                    if (!mathDBImage.getImageName().contains("plus")) {
                        if (mathImagesEdited < maxMathImages) {
                            int value = mathDBImage.getNumberOfImages();
                            mathImages.get(mathImagesEdited++).setNumberOfImages(value);
                            sumTotal += value;
                        }
                    }
                }
            } else {

                if (unitId == 18) {
                    // 10 pencils + 8 pens (books) = 18
                    mathImageA.setNumberOfImages(10);
                    mathImageB.setNumberOfImages(8);
                    sumTotal = 18;
                } else if (unitId == 19) {
                    // 12 pencils + 7 pens (books) = 19
                    mathImageA.setNumberOfImages(12);
                    mathImageB.setNumberOfImages(7);
                    sumTotal = 19;
                } else if (unitId == 20) {
                    // 14 pencils + 6 pens (books) = 20
                    mathImageA.setNumberOfImages(14);
                    mathImageB.setNumberOfImages(6);
                    sumTotal = 20;
                }
            }

            // Get numerals
            Numerals sumTotalNumber = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, sumTotal);
            SparseArray<Numerals> numbers = new SparseArray<>();

            for (int i = 0; i <= 20; i++) {
                Numerals number = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, i);
                numbers.put(number.getNumber(), number);
            }

            // Get operators
            String[] mathOperators = {"w_plus", "w_equals"};
            String equationSound = String.format(Locale.ENGLISH, "maths_%dplus%d",
                    mathImages.get(0).getNumberOfImages(),
                    mathImages.get(1).getNumberOfImages());

            // Setup drill data
            String drillData = MathDrillJsonBuilder.getDrillFiveAndOneJson(
                    context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    equationSound,
                    mathOperators,
                    mathImages,
                    sumTotalNumber,
                    numbers);
            intent = new Intent(context, MathsDrillFiveAndOneActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            sqlex.printStackTrace();
            throw new SQLiteException("D5B: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D5B: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D6A(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D6A > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId,languageId);

            ArrayList<Integer> mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            String objectSound = "";
            objectSound = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getImageSound();
            if (objectSound.equalsIgnoreCase("none")) {
                objectSound = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound();
            }

            String drillData = MathDrillJsonBuilder.getDrillSixJson(context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    objectSound,
                    mathDrillFlowWord.getDrillSound3(),
                    mathDrillFlowWord.getDrillSound4(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(),
                    objectSound
            );

            intent = new Intent(context, MathsDrillSixActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D5A: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D5A: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D6B(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D6B > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId,languageId);

            ArrayList<Integer> mathImageIds = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            String shapeImageName ="";
            String shapeSingularSound = "";
            String shapesPluralSound = "";
            String shapesComparativeSound = "";

            final String CIRCLE = "circle";
            final String RECTANGLE = "rectangle";
            final String SQUARE = "square";
            final String TRIANGLE = "triangle";

            final String CIRCLES = "circles";
            final String RECTANGLES = "rectangles";
            final String SQUARES = "squares";
            final String TRIANGLES = "triangles";

            for (int i = 0; i < mathImageIds.size(); i++) {
                MathImages mathImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageIds.get(i));

                // Get shape image
                String imageName = mathImage.getImageName();
                if (imageName.equalsIgnoreCase(CIRCLE) ||
                        imageName.equalsIgnoreCase(RECTANGLE) ||
                        imageName.equalsIgnoreCase(SQUARE) ||
                        imageName.equalsIgnoreCase(TRIANGLE)) {
                    shapeImageName = imageName;
                }

                // Get shape singular sound
                String imageSound = mathImage.getImageSound();
                String strippedImageSound = imageSound.replace("s_", ""); // language measure
                if (strippedImageSound.equalsIgnoreCase(CIRCLE) ||
                        strippedImageSound.equalsIgnoreCase(RECTANGLE) ||
                        strippedImageSound.equalsIgnoreCase(SQUARE) ||
                        strippedImageSound.equalsIgnoreCase(TRIANGLE)) {
                    shapeSingularSound = imageSound;
                }

                // Get shapes plural sound
                String numberOfImagesSound = mathImage.getNumberOfImagesSound();
                String strippedNumberOfImagesSound = numberOfImagesSound.replace("s_", ""); // language measure
                if (strippedNumberOfImagesSound.equalsIgnoreCase(CIRCLES) ||
                        strippedNumberOfImagesSound.equalsIgnoreCase(RECTANGLES) ||
                        strippedNumberOfImagesSound.equalsIgnoreCase(SQUARES) ||
                        strippedNumberOfImagesSound.equalsIgnoreCase(TRIANGLES)) {
                    shapesPluralSound = numberOfImagesSound;
                } else {
                    shapesComparativeSound = numberOfImagesSound;
                }
            }

            String drillData = MathDrillJsonBuilder.getDrillSixAndOneJson(context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    mathDrillFlowWord.getDrillSound4(),
                    shapeImageName,
                    shapeSingularSound,
                    shapesPluralSound,
                    shapesComparativeSound);

            intent = new Intent(context, MathsDrillSixAndOneActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D6B: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D6B: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D6C(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D6C > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId,languageId);

            ArrayList<Integer> mathImageIds = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            String shapeOneImage;
            String shapeOneSingularSound;
            String shapeOnePluralSound;
            String shapeTwoImage;
            String shapeTwoSingularSound;
            String shapeTwoPluralSound;
            String shapeOneAndTwoPluralSound = "";
            String shapeComparativeSound = "";

            List<String> shapeImageNames = new ArrayList<>();

            final String NONE = "none";

            final String CIRCLE = "circle";
            final String RECTANGLE = "rectangle";
            final String SQUARE = "square";
            final String TRIANGLE = "triangle";

            final String CIRCLES = "circles";
            final String RECTANGLES = "rectangles";
            final String SQUARES = "squares";
            final String TRIANGLES = "triangles";

            LinkedHashMap<String, String> shapeSingularSounds = new LinkedHashMap<>();
            shapeSingularSounds.put(CIRCLE, CIRCLE);
            shapeSingularSounds.put(RECTANGLE, RECTANGLE);
            shapeSingularSounds.put(SQUARE, SQUARE);
            shapeSingularSounds.put(TRIANGLE, TRIANGLE);

            LinkedHashMap<String, String> shapePluralSounds = new LinkedHashMap<>();
            shapePluralSounds.put(CIRCLE, CIRCLES);
            shapePluralSounds.put(RECTANGLE, RECTANGLES);
            shapePluralSounds.put(SQUARE, SQUARES);
            shapePluralSounds.put(TRIANGLE, TRIANGLES);

            for (int i = 0; i < mathImageIds.size(); i++) {
                // Get math image
                MathImages mathImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageIds.get(i));

                // Get shape image
                String imageName = mathImage.getImageName().replace("big", "").replace("small", "");
                if (imageName.equalsIgnoreCase(CIRCLE) ||
                        imageName.equalsIgnoreCase(RECTANGLE) ||
                        imageName.equalsIgnoreCase(SQUARE) ||
                        imageName.equalsIgnoreCase(TRIANGLE)) {
                    if (!shapeImageNames.contains(imageName) && shapeImageNames.size() < 2) {
                        shapeImageNames.add(imageName);
                    }
                }

                // Get shapes one and two plural sound
                String imageSound = mathImage.getImageSound();
                if (imageSound.equalsIgnoreCase(NONE)) {
                    shapeOneAndTwoPluralSound = mathImage.getNumberOfImagesSound();
                }

                // Get shapes comparative sound
                String numberOfImagesSound = mathImage.getNumberOfImagesSound();
                String strippedNumberOfImagesSound = numberOfImagesSound.replace("s_", ""); // internationalized validation
                String strippedName = numberOfImagesSound.replace("small","").replace("big","");
                if (shapeImageNames.contains(strippedName) &&
                        (strippedNumberOfImagesSound.contains("small") ||
                                strippedNumberOfImagesSound.contains("big"))) {
                    shapeComparativeSound = numberOfImagesSound;
                }
            }

            int numOfShapeImageNames = shapeImageNames.size();
            if (numOfShapeImageNames < 2) {
                String errorMessage = "Insufficient Math 6C data (only " + numOfShapeImageNames + " shapes)";
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                throw new Exception(errorMessage);
            } else {
                // Shape one
                shapeOneImage = shapeImageNames.get(0);
                shapeOneSingularSound = shapeSingularSounds.get(shapeOneImage);
                shapeOnePluralSound = shapePluralSounds.get(shapeOneImage);

                // Shape two
                shapeTwoImage = shapeImageNames.get(1);
                shapeTwoSingularSound = shapeSingularSounds.get(shapeTwoImage);
                shapeTwoPluralSound = shapePluralSounds.get(shapeTwoImage);
            }

            String drillData = MathDrillJsonBuilder.getDrillSixAndTwoJson(context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    "and",
                    mathDrillFlowWord.getDrillSound3(),
                    mathDrillFlowWord.getDrillSound4(),
                    shapeOneImage,
                    shapeOneSingularSound,
                    shapeOnePluralSound,
                    shapeTwoImage,
                    shapeTwoSingularSound,
                    shapeTwoPluralSound,
                    shapeOneAndTwoPluralSound,
                    shapeComparativeSound);

            intent = new Intent(context, MathsDrillSixAndTwoActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D6C: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D6C: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D6D(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D6D > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);

            ArrayList<Integer> mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            MathImages mathImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0));
            int correctTestNumber = mathImage.getTestNumber();

            List<Numerals> numerals = new ArrayList<>();
            List<Integer> numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(dbHelper.getReadableDatabase(), languageId,20, 2, correctTestNumber, boyGirl);

            for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
                Numerals numeralFromDB = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
                numerals.add(numeralFromDB);
            }
            Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, correctTestNumber);
            numerals.add(numeralCorrectAnswer);

            List<Numerals> countNumbers = new ArrayList<>();
            List<Integer> countNumberIds = NumeralHelper.getNumeralsBelowLimitFromZero(dbHelper.getReadableDatabase(), languageId, 20, boyGirl);
            for (int i = 0; i < countNumberIds.size(); i++) {
                Numerals countNumber = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), countNumberIds.get(i));
                countNumbers.add(countNumber);
            }

            String drillData = MathDrillJsonBuilder.getDrillSixAndThreeJson(context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathImage.getNumberOfImagesSound(), // sound of 'plural' objects to be eaten
                    mathImage.getImageSound(), // sound of 'singular' object to be eaten
                    mathDrillFlowWord.getDrillSound3(),
                    mathDrillFlowWord.getDrillSound4(),
                    mathDrillFlowWord.getDrillSound5(),
                    mathImage.getImageName(), // image of object (ie. banana / mango)
                    String.valueOf(mathImage.getNumberOfImages()), // number of objects
                    String.valueOf(mathImage.getTestNumber()), // number of objects to be eaten
                    numerals,
                    countNumbers);
            intent = new Intent(context, MathsDrillSixAndThreeActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            sqlex.printStackTrace();
            throw new SQLiteException("D6D: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D6D: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D6E(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {

            // Debug
            System.out.println("MathDrills.D6E > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);

            ArrayList<Integer> mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            MathImages mathImages = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0));
            int correctTestNumber = mathImages.getTestNumber();

            ArrayList<Numerals> numerals = new ArrayList<>();

            ArrayList<Integer> numeralsFromDB_2_Only;
            numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(dbHelper.getReadableDatabase(), languageId, 20, 2, correctTestNumber, boyGirl);
            for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
                Numerals number = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
                numerals.add(number);
            }
            Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, correctTestNumber);
            numerals.add(numeralCorrectAnswer);

            List<Numerals> countNumbers = new ArrayList<>();
            List<Integer> countNumberIds = NumeralHelper.getNumeralsBelowLimitFromZero(dbHelper.getReadableDatabase(), languageId, 20, boyGirl);
            for (int i = 0; i < countNumberIds.size(); i++) {
                Numerals countNumber = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), countNumberIds.get(i));
                countNumbers.add(countNumber);
            }

            String drillData = MathDrillJsonBuilder.getDrillSixAndFourJson(context,
                    mathDrillFlowWord.getDrillSound1(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImagesSound(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    mathDrillFlowWord.getDrillSound4(),
                    mathDrillFlowWord.getDrillSound5(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(2)).getNumberOfImagesSound(),
                    mathDrillFlowWord.getDrillSound6(),
                    MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(),
                    String.valueOf(MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages()),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages()).getBlackImage(),
                    String.valueOf(MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImages()),
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImages()).getBlackImage(),
                    numeralCorrectAnswer.getBlackImage(),
                    numerals,
                    countNumbers);
            intent = new Intent(context, MathsDrillSixAndFourActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D6E: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D6E: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D7A(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D7A > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);
            ArrayList<Integer> mathImageList;
            mathImageList = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);

            MathImages masterPattern = new MathImages();
            MathImages targetObject = new MathImages();

            ArrayList<DraggableImage<String>> itemsToCompletePattern = new ArrayList<>();
            for (int i=0; i < mathImageList.size(); i++) {
                MathImages mathImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageList.get(i));
                DraggableImage<String> item = new DraggableImage<>(0, mathImage.getTestNumber(), mathImage.getImageName());

                // Get master pattern
                if (mathImage.getNumberOfImages() == 0) {
                    masterPattern = mathImage;
                } else {
                    itemsToCompletePattern.add(item);
                }

                // Get target object
                if (mathImage.getTestNumber() == 1) {
                    targetObject = mathImage;
                }
            }

            String drillData = MathDrillJsonBuilder.getDrillSevenJson(
                    context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    masterPattern.getImageName(),
                    masterPattern.getNumberOfImagesSound(),
                    targetObject.getImageSound(),
                    itemsToCompletePattern);
            intent = new Intent(context, MathsDrillSevenActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D7A: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D7A: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D7B(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int mathDrillId, int subId, int limit, int boyGirl) throws SQLiteException, Exception  {
        Intent intent;

        try {
            // Debug
            System.out.println("MathDrills.D7B > Debug: PREPARING");

            MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), mathDrillId, subId, languageId);

            List<Integer> mathImageIds = MathImageHelper.getMathImageList(dbHelper.getReadableDatabase(), unitId, mathDrillId, languageId);
            List<MathImages> mathImages = new ArrayList<>();

            SortedMap<Integer, MathImages> sortedMathImages = new TreeMap<>();

            for (int i=0; i < mathImageIds.size(); i++ ) {
                MathImages mathImage = MathImageHelper.getMathImage(dbHelper.getReadableDatabase(), mathImageIds.get(i));
                mathImages.add(mathImage);

                int numberValue = Integer.parseInt(mathImage.getImageName().replace("numeral_", ""));
                sortedMathImages.put(numberValue, mathImage);
            }

            List<DraggableImage<String>> itemsToCompletePattern = new ArrayList<>();

            for (Map.Entry entry : sortedMathImages.entrySet()) {
                MathImages mathImage = (MathImages) entry.getValue();
                DraggableImage<String> item = new DraggableImage<>(0, mathImage.getTestNumber(), mathImage.getImageName());
                item.setExtraData(mathImage.getImageName().replace("numeral_", ""));
                itemsToCompletePattern.add(item);
            }

            List<Numerals> countNumbers = new ArrayList<>();
            List<Integer> countNumberIds = NumeralHelper.getNumeralsBelowLimitFromZero(dbHelper.getReadableDatabase(), languageId, 20, boyGirl);
            for (int i = 0; i < countNumberIds.size(); i++) {
                Numerals countNumber = NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), countNumberIds.get(i));
                countNumbers.add(countNumber);
            }

            String drillData = MathDrillJsonBuilder.getDrillSevenAndOneJson(
                    context,
                    mathDrillFlowWord.getDrillSound1(),
                    mathDrillFlowWord.getDrillSound2(),
                    mathDrillFlowWord.getDrillSound3(),
                    itemsToCompletePattern,
                    countNumbers);
            intent = new Intent(context, MathsDrillSevenAndOneActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            sqlex.printStackTrace();
            throw new SQLiteException("D7B: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D7B: " + ex.getMessage());
        }
        return intent;
    }
}