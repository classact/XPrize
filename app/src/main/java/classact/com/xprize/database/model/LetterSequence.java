package classact.com.xprize.database.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class LetterSequence {
        private int sequenceID;
        private int languageID;
        private int unitID;
        private int subUnitID;
        private int letterID;

        public int getSequenceID() {
            return sequenceID;
        }

        public void setSequenceID(int sequenceID) {
            this.sequenceID = sequenceID;
        }

        public int getLanguageID() {
        return languageID;
    }

        public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }


        public int getSubUnitID() {
            return subUnitID;
        }

        public void setSubUnitID(int subUnitID) {
            this.subUnitID = subUnitID;
        }

        public int getUnitID() {
            return unitID;
        }

        public void setUnitID(int unitID) {
            this.unitID = unitID;
        }

        public int getLetterID() {
            return letterID;
        }

        public void setLetterID(int letterID) {
            this.letterID = letterID;
        }


}

