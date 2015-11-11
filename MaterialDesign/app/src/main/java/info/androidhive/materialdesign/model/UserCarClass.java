package info.androidhive.materialdesign.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timbarnard on 24/10/2015.
 */
public class UserCarClass implements Parcelable {

    private String carID;
    private String carReg;
    private String carVin;
    private String carMake;
    private String carModel;
    private List<String> carPhoto;
    private String carColor;
    private String carYear;
    private String carDesc;
    private List<Location> carLocation;


    public UserCarClass() {

    }


    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getCarReg() {
        return carReg;
    }

    public void setCarReg(String carReg) {
        this.carReg = carReg;
    }

    public String getCarVin() {
        return carVin;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public List<String> getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(List<String> carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarDesc() {
        return carDesc;
    }

    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    public List<Location> getCarLocation() {
        return carLocation;
    }

    public void setCarLocation(List<Location> carLocation) {
        this.carLocation = carLocation;
    }

    // Inner class
    public static class Location implements Parcelable {
        private String locID;
        private String loc_country_code;
        private String locCountry;
        private String locCounty;
        private String locTown;
        private String locDate;
        private String reportsUUID;

        public Location() {

        }

        public String getLocCountry() {
            return locCountry;
        }

        public void setLocCountry(String locCountry) {
            this.locCountry = locCountry;
        }

        public String getLocCounty() {
            return locCounty;
        }

        public void setLocCounty(String locCounty) {
            this.locCounty = locCounty;
        }

        public String getLocTown() {
            return locTown;
        }

        public void setLocTown(String locTown) {
            this.locTown = locTown;
        }

        public String getLocDate() {
            return locDate;
        }

        public void setLocDate(String locDate) {
            this.locDate = locDate;
        }

        public String getReportsUUID() {
            return reportsUUID;
        }

        public void setReportsUUID(String reportsUUID) {
            this.reportsUUID = reportsUUID;
        }

        public String getLocID() {
            return locID;
        }

        public void setLocID(String locID) {
            this.locID = locID;
        }

        public String getLoc_country_code() {
            return loc_country_code;
        }

        public void setLoc_country_code(String loc_country_code) {
            this.loc_country_code = loc_country_code;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.locID);
            dest.writeString(this.loc_country_code);
            dest.writeString(this.locCountry);
            dest.writeString(this.locCounty);
            dest.writeString(this.locTown);
            dest.writeString(this.locDate);
            dest.writeString(this.reportsUUID);
        }

        protected Location(Parcel in) {
            this.locID = in.readString();
            this.loc_country_code = in.readString();
            this.locCountry = in.readString();
            this.locCounty = in.readString();
            this.locTown = in.readString();
            this.locDate = in.readString();
            this.reportsUUID = in.readString();
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
            public Location createFromParcel(Parcel source) {
                return new Location(source);
            }

            public Location[] newArray(int size) {
                return new Location[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carID);
        dest.writeString(this.carReg);
        dest.writeString(this.carVin);
        dest.writeString(this.carMake);
        dest.writeString(this.carModel);
        dest.writeStringList(this.carPhoto);
        dest.writeString(this.carColor);
        dest.writeString(this.carYear);
        dest.writeString(this.carDesc);
        dest.writeList(this.carLocation);
    }

    protected UserCarClass(Parcel in) {
        this.carID = in.readString();
        this.carReg = in.readString();
        this.carVin = in.readString();
        this.carMake = in.readString();
        this.carModel = in.readString();
        this.carPhoto = in.createStringArrayList();
        this.carColor = in.readString();
        this.carYear = in.readString();
        this.carDesc = in.readString();
        this.carLocation = new ArrayList<>();
        in.readList(this.carLocation, Location.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserCarClass> CREATOR = new Parcelable.Creator<UserCarClass>() {
        public UserCarClass createFromParcel(Parcel source) {
            return new UserCarClass(source);
        }

        public UserCarClass[] newArray(int size) {
            return new UserCarClass[size];
        }
    };
}
