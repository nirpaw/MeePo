package il.ac.hit.meepo.Models;


public class NewChatObject {
    private String message;
    private Boolean currentUser;
    private String profilePic;
    public NewChatObject(String message, Boolean currentUser){
        this.message = message;
        this.currentUser = currentUser;
        profilePic = null;
    }

    public NewChatObject(String message, Boolean currentUser, String profilePic) {
        this.message = message;
        this.currentUser = currentUser;
        this.profilePic = profilePic;
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