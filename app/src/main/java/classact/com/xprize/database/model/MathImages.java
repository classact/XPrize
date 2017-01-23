package classact.com.xprize.database.model;

public class MathImages {
    private int mathImageID;
    private int languageID;
    private int unitID;
    private int drillID;
    private String imageName;
    private int numberOfImages;
    private String imageSound;
    private int testNumber;
    private String numberOfImagesSound;

    public int getMathImageID() {
        return mathImageID;
    }

    public void setMathImageID(int mathImageID) {
        this.mathImageID = mathImageID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }
    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    public int getDrillID() {
        return drillID;
    }

    public void setDrillID(int drillID) {
        this.drillID = drillID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public String getImageSound() {
        return imageSound;
    }

    public void setImageSound(String imageSound) {
        this.imageSound = imageSound;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public String getNumberOfImagesSound() {
        return numberOfImagesSound;
    }

    public void setNumberOfImagesSound(String numberOfImagesSound) {
        this.numberOfImagesSound = numberOfImagesSound;
    }



}