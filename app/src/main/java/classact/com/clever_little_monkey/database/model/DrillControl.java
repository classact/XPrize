package classact.com.clever_little_monkey.database.model;

/**
 * Created by Tseliso on 4/24/2016.
 */
public class DrillControl {
    private int drillID;
    private int wordAndLetter;
    private int letterOnly;
    private int drawDrill;
    private int sentenceOnly;
    private int wordOnly;
    private int NoOfPhrases;

    public int getDrillID() {
        return drillID;
    }

    public void setDrillID(int drillID) {
        this.drillID = drillID;
    }

    public int getWordAndLetter() {
        return wordAndLetter;
    }

    public void setWordAndLetter(int wordAndLetter) {
        this.wordAndLetter = wordAndLetter;
    }

    public int getLetterOnly() {
        return letterOnly;
    }

    public void setLetterOnly(int letterOnly) {
        this.letterOnly = letterOnly;
    }

    public int getDrawDrill() {
        return drawDrill;
    }

    public void setDrawDrill(int drawDrill) {
        this.drawDrill = drawDrill;
    }

    public int getSentenceOnly() {
        return sentenceOnly;
    }

    public void setSentenceOnly(int sentenceOnly) {
        this.sentenceOnly = sentenceOnly;
    }

    public int getWordOnly() {
        return wordOnly;
    }

    public void setWordOnly(int wordOnly) {
        this.wordOnly = wordOnly;
    }

    public int getNoOfPhrases() {
        return NoOfPhrases;
    }

    public void setNoOfPhrases(int NoOfPhrases) {
        this.NoOfPhrases = NoOfPhrases;
    }
}
