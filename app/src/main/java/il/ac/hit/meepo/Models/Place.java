package il.ac.hit.meepo.Models;

public class Place {
    private String mPlaceName;
    private String mPlaceId;
    private double mLat;
    private double mLng;
    private int mVistorCounter = 0;
    private String PhotoRefference;

    public Place(String mPlaceName, String mPlaceId, double mLat, double mLng, String photoRefference) {
        this.mPlaceName = mPlaceName;
        this.mPlaceId = mPlaceId;
        this.mLat = mLat;
        this.mLng = mLng;
        PhotoRefference = photoRefference;
        mVistorCounter++;
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

    public int getmVistorCounter() {
        return mVistorCounter;
    }

    public void setmVistorCounter(int mVistorCounter) {
        this.mVistorCounter = mVistorCounter;
    }

    public String getPhotoRefference() {
        return PhotoRefference;
    }

    public void setPhotoRefference(String photoRefference) {
        PhotoRefference = photoRefference;
    }
}
