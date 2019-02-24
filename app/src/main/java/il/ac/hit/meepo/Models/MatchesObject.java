package il.ac.hit.meepo.Models;

public class MatchesObject {
    private String userId;
    private String userFirsName;
    private String userProfileImgUrl;

    public MatchesObject(String userId, String userFirsName, String userProfileImgUrl) {
        this.userId = userId;
        this.userFirsName = userFirsName;
        this.userProfileImgUrl = userProfileImgUrl;
    }

    public String getUserFirsName() {
        return userFirsName;
    }

    public void setUserFirsName(String userFirsName) {
        this.userFirsName = userFirsName;
    }

    public String getUserProfileImgUrl() {
        return userProfileImgUrl;
    }

    public void setUserProfileImgUrl(String userProfileImgUrl) {
        this.userProfileImgUrl = userProfileImgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

