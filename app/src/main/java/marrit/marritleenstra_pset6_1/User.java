package marrit.marritleenstra_pset6_1;


import java.io.Serializable;

/**
 * Created by Marrit on 4-12-2017.
 */

public class User implements Serializable {

    private String mUID;
    private String mEmail;
    private String mDisplayName;
    private int mRunStreak;
    private int mDaysVegetarian;
    private double mAnimalsSaved;

    // default constructor for FireBase
    public User() {}

    // constructor
    public User(String UID, String email, String displayName) {
        mUID = UID;
        mEmail = email;
        mDisplayName = displayName;
        mRunStreak = 0;
        mDaysVegetarian = 0;
        mAnimalsSaved = 0;
    }

    // getters and setters
    public String getUID() {
        return mUID;
    }

    public void setUID(String UID) {
        mUID = UID;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public int getRunStreak() {
        return mRunStreak;
    }

    public void setRunStreak(int runStreak) {
        mRunStreak = runStreak;
    }

    public int getDaysVegetarian() {
        return mDaysVegetarian;
    }

    public void setDaysVegetarian(int daysVegetarian) {
        mDaysVegetarian = daysVegetarian;
    }

    public double getAnimalsSaved() {
        return mAnimalsSaved;
    }

    public void setAnimalsSaved(double animalsSaved) {
        mAnimalsSaved = animalsSaved;
    }

}