package classact.com.clever_little_monkey.database.model;

/**
 * Created by Tseliso on 4/24/2016.
 */
public class Profile {
    private int profileId;
    private String name;
    private String surname;
    private int age;
    private int languageId;
    private int currentUnit;

    public int getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(int currentUnit) {
        this.currentUnit = currentUnit;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }


}
