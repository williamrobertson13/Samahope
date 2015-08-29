package com.samahop.samahope.doctors;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * DoctorProfile is a representation of a doctor profile found on
 * See <a href="http://www.samahope.com">http://www.samahope.com</a>
 *
 * These objects are stored in Parse and scraped continuously for updated
 * information, such as treatments needed and money earned.
 */
@ParseClassName("Doctor")
public class DoctorProfile extends ParseObject {

    /**
     * Default constructor for DoctorProfile
     */
    public DoctorProfile() {
        // do nothing here
    }

    /**
     * Gets the name of the doctor object from Parse.
     * @return the name of the doctor
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Gets the biography for the doctor object from Parse.
     * @return the biography for the doctor's profile
     */
    public String getBiography() { return getString("biography"); }

    /**
     * Gets the location of the doctor object from Parse.
     * @return the location of the doctor
     */
    public String getLocation() {
        return getString("location");
    }

    /**
     * Gets the treatment focus from the doctor object from Parse.
     * @return the treatment focus for the doctor
     */
    public String getTreatmentName() { return getString("treatment_name"); }

    /**
     * Gets the amount of money needed for the doctor object from Parse.
     * @return the amount of money needed for the doctor's next treatment
     */
    public int getMoneyNeeded() {
        return getInt("money_needed");
    }

    /**
     * Gets the total cost of the doctor object from Parse.
     * @return the total cost of the doctor's next treatment
     */
    public int getTotalCost() {
        return getInt("total_cost");
    }

    /**
     * Gets the amount of money raised for the doctor object from Parse.
     * @return the amount of money raised for the doctor's next treatment
     */
    public int getMoneyRaised() { return getInt("total_cost") - getInt("money_needed"); }

    /**
     * Gets the percentage amount of money raised for the doctor's next treatment.
     * @return the percentage of the treatment funded.
     */
    public int getCostPercentage() {
        int moneyRaised = getInt("total_cost") - getInt("money_needed");

        // fix divide by 0 if parse value is scraped correctly
        int totalCost = getInt("total_cost");
        if (totalCost == 0)
            return 0;

        return (moneyRaised * 100) / totalCost;
    }

    /**
     * Gets the number of treatments funded so far.
     * @return the number of treatments funded.
     */
    public int getTreatmentsFunded() { return getInt("treatment_count"); }

    /**
     * Gets the number of treatments needed.
     * @return the number of treatments needed.
     */
    public int getTreatmentsNeeded() { return getInt("treatments_needed"); }

    /**
     * Gets the appropriate treatment image for the doctor object from Parse.
     * @return the appropriate image for the doctor object's treatment
     */
    public String getTreatmentImage() { return getString("treatment_image"); }

    /**
     * Gets the profile image for the doctor object from Parse.
     * @return the image for the doctor's profile
     */
    public String getBannerImage() { return getString("image"); }

    /**
     * Gets the parse query for this class.
     * @return the parse query for a DoctorProfile object
     */
    public static ParseQuery<DoctorProfile> getQuery() {
        return ParseQuery.getQuery(DoctorProfile.class);
    }
}
