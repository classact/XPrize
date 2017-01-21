package classact.com.xprize.database.model;

/**
 * Created by Tseliso on 4/24/2016.
 */
public class Unit {
    private int unitId;
    private int numberOfLanguageDrills;
    private int numberOfMathDrills;
    private int unitUnlocked;
    private String unitDateLastPlayed;
    private int unitInProgress;
    private int unitSubIDInProgress;
    private int unitCompleted;
    private int unitFirstTime;
    private int unitFirstTimeMovie;
    private int unitDrillLastPlayed;
    private String unitFirstTimeMovieFile;

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getNumberOfLanguageDrills() {
        return numberOfLanguageDrills;
    }

    public void setNumberOfLanguageDrills(int numberOfLanguageDrills) {
        this.numberOfLanguageDrills = numberOfLanguageDrills;
    }

    public int getNumberOfMathDrills() {
        return numberOfMathDrills;
    }

    public void setNumberOfMathDrills(int numberOfMathDrills) {
        this.numberOfMathDrills = numberOfMathDrills;
    }

    public int getUnitUnlocked() {
        return unitUnlocked;
    }

    public void setUnitUnlocked(int unitUnlocked) {
        this.unitUnlocked = unitUnlocked;
    }

    public int getUnitDrillLastPlayed() {
        return unitDrillLastPlayed;
    }

    public void setUnitDrillLastPlayed(int unitDrillLastPlayed) {
        this.unitDrillLastPlayed = unitDrillLastPlayed;
    }

    public String getUnitDateLastPlayed() {
        return unitDateLastPlayed;
    }

    public void setUnitDateLastPlayed(
            String unitDateLastPlayed) {
        this.unitDateLastPlayed = unitDateLastPlayed;
    }

    public int getUnitInProgress() {
        return unitInProgress;
    }

    public void setUnitInProgress(int unitInProgress) {
        this.unitInProgress = unitInProgress;
    }

    public int getUnitSubIDInProgress() {
        return unitSubIDInProgress;
    }

    public void setUnitSubIDInProgress(int unitSubIDInProgress) {
        this.unitSubIDInProgress = unitSubIDInProgress;
    }


    public int getUnitCompleted() {
        return unitCompleted;
    }

    public void setUnitCompleted(int unitCompleted) {
        this.unitCompleted = unitCompleted;
    }

    public int getUnitFirstTime() {
        return unitFirstTime;
    }

    public void setUnitFirstTime(int unitFirstTime) {
        this.unitFirstTime = unitFirstTime;
    }

    public String getUnitFirstTimeMovieFile() {
        return unitFirstTimeMovieFile;
    }

    public int getUnitFirstTimeMovie() {
        return unitFirstTimeMovie;
    }

    public void setUnitFirstTimeMovie(int unitFirstTimeMovie) {
        this.unitFirstTimeMovie = unitFirstTimeMovie;
    }

    public void setUnitFirstTimeMovieFile(String unitFirstTimeMovieFile) {
        this.unitFirstTimeMovieFile = unitFirstTimeMovieFile;
    }

}
