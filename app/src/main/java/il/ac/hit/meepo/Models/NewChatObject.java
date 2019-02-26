package il.ac.hit.meepo.Models;


public class NewChatObject {
    private String message;
    private Boolean currentUser;
    private String profilePic;
    private String time;

    public NewChatObject(String message, Boolean currentUser, String profilePic, String time) {
        this.message = message;
        this.currentUser = currentUser;
        this.profilePic = profilePic;
        this.time = time;
    }

    public NewChatObject(String message, Boolean currentUser, String time) {
        this.message = message;
        this.currentUser = currentUser;
        this.time = time;
        this.profilePic = null;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }
}