package il.ac.hit.meepo.Models;

import java.io.Serializable;
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


    public User(String id, String firstName, String lastName, String imageURL, String gender, String age, String looking, String status, String search, ArrayList<String> imagesUrlList, String about, String jobtitle) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.gender = gender;
        this.age = age;
        this.looking = looking;
        this.status = status;
        this.search = search;
        this.imagesUrlList = imagesUrlList;
        this.about = about;
        this.jobtitle = jobtitle;
    }

    public User() {
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
}
