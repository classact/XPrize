package classact.com.xprize.database.model;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class DrillType {

    private int drillTypeId;
    private String name;
    private int languageId;

    public int getDrillTypeId() {
        return drillTypeId;
    }

    public void setDrillTypeId(int drillTypeId) {
        this.drillTypeId = drillTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
}
