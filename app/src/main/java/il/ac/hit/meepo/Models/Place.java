package il.ac.hit.meepo.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Place implements Serializable {
    private String mPlaceName;
    private String mPlaceId;
    private double mLat;
    private double mLng;
    private String PhotoRefference;
    private List<User> mInPlaceUsers;

    public Place(String mPlaceName, String mPlaceId, double mLat, double mLng, String photoRefference, List<User> mInPlaceUsers) {
        this.mPlaceName = mPlaceName;
        this.mPlaceId = mPlaceId;
        this.mLat = mLat;
        this.mLng = mLng;
        this.PhotoRefference = photoRefference;
        this.mInPlaceUsers = new ArrayList<>();
    }

    public List<User> getmInPlaceUsers() {
        return mInPlaceUsers;
    }

    public void setmInPlaceUsers(List<User> mInPlaceUsers) {
        this.mInPlaceUsers = mInPlaceUsers;
    }

    public Place() {
    }

    public String getmPlaceName() {
        return mPlaceName;
    }

    public void setmPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }

    public String getmPlaceId() {
        return mPlaceId;
    }

    public void setmPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLng() {
        return mLng;
    }

    public void setmLng(double mLng) {
        this.mLng = mLng;
    }

    public String getPhotoRefference() {
        return PhotoRefference;
    }

    public void setPhotoRefference(String photoRefference) {
        PhotoRefference = photoRefference;
    }

    @Override
    public String toString() {
        return "Place{" +
                "mPlaceName='" + mPlaceName + '\'' +
                ", mPlaceId='" + mPlaceId + '\'' +
                ", mLat=" + mLat +
                ", mLng=" + mLng +
                ", PhotoRefference='" + PhotoRefference + '\'' +
                ", mInPlaceUsers=" + mInPlaceUsers +
                '}';
    }
}
