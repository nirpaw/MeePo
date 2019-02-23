package il.ac.hit.meepo.Models;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String imageURL;
    private String gender;
    private String age;
    private String looking;
    private String status;
    private String search;
    private String about;
    private String jobtitle;
    private List<String> imagesUrlList;
    private String lastSeen;
    private String lastLocationPlaceId;
    private List<String> likedByUserList;

    public User(String id, String firstName, String lastName, String imageURL, String gender, String age, String looking, String status, String search, String about, String jobtitle, List<String> imagesUrlList, String lastSeen, String lastLocationPlaceId, List<String> likedByUserList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.gender = gender;
        this.age = age;
        this.looking = looking;
        this.status = status;
        this.search = search;
        this.about = about;
        this.jobtitle = jobtitle;
        this.imagesUrlList = imagesUrlList;
        this.lastSeen = lastSeen;
        this.lastLocationPlaceId = lastLocationPlaceId;
        this.likedByUserList = likedByUserList;
    }

    public User() {
    }

    public User(User other) {

        this.id = other.id;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.imageURL = other.imageURL;
        this.gender = other.gender;
        this.age = other.age;
        this.looking = other.looking;
        this.status = other.status;
        this.search = other.search;
        this.imagesUrlList = new ArrayList<>();
        if(other.imagesUrlList != null) {
            for (String s : other.imagesUrlList) {
                this.imagesUrlList.add(s);
            }
        }
        this.about = other.about;
        this.jobtitle = other.jobtitle;
        this.lastSeen = other.lastSeen;
        this.lastLocationPlaceId = other.lastLocationPlaceId;
        this.likedByUserList = new ArrayList<>();
        if(other.likedByUserList != null) {
            for (String s : other.likedByUserList) {
                this.likedByUserList.add(s);
            }
        }
    }


    public List<String> getLikedByUserList() {
        return likedByUserList;
    }

    public void setLikedByUserList(List<String> likedByUserList) {
        this.likedByUserList = likedByUserList;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getLastLocationPlaceId() {
        return lastLocationPlaceId;
    }

    public void setLastLocationPlaceId(String lastLocationPlaceId) {
        this.lastLocationPlaceId = lastLocationPlaceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLooking() {
        return looking;
    }

    public void setLooking(String looking) {
        this.looking = looking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<String> getImagesUrlList() {
        return imagesUrlList;
    }

    public void setImagesUrlList(List<String> imagesUrlList) {
        this.imagesUrlList = imagesUrlList;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", looking='" + looking + '\'' +
                ", status='" + status + '\'' +
                ", search='" + search + '\'' +
                ", about='" + about + '\'' +
                ", jobtitle='" + jobtitle + '\'' +
                ", imagesUrlList=" + imagesUrlList +
                '}';
    }
}
