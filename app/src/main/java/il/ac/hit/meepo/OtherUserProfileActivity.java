package il.ac.hit.meepo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import il.ac.hit.meepo.Adapters.ViewPagerAdapter;
import il.ac.hit.meepo.Models.User;

public class OtherUserProfileActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    User mUserProfile;
    List<String> userImageArray = new ArrayList<>();
    private String []imgArr = {
            "https://www.gannett-cdn.com/presto/2019/02/20/USAT/f5987a1d-dd8a-47b6-b1e5-667e83d03d57-EPA_RUSSIA_PUTIN_STATE_OF_THE_NATION.JPG",
            "https://specials-images.forbesimg.com/imageserve/5aecc9a84bbe6f748688434d/416x416.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/President_Vladimir_Putin.jpg/300px-President_Vladimir_Putin.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        mUserProfile = (User)getIntent().getSerializableExtra("user_object");
        viewPager = findViewById(R.id.view_pager_pics_scrolling);
        if(!mUserProfile.getImageURL().equals("default")){
            userImageArray.add(mUserProfile.getImageURL());
        }
        for (String img: mUserProfile.getImagesUrlList()
             ) {
            if(!img.equals("default")) {
                userImageArray.add(img);
            }
        }
        
        adapter  = new ViewPagerAdapter(OtherUserProfileActivity.this, userImageArray.toArray(new String[userImageArray.size()]));
        viewPager.setAdapter(adapter);
        TextView nameTv = findViewById(R.id.tv_first_name_otheruserprofile);
        TextView ageTv = findViewById(R.id.tv_age_otheruserprofile);
        TextView about = findViewById(R.id.tv_about_otheruserprofile);
        nameTv.setText(mUserProfile.getFirstName());
        ageTv.setText(mUserProfile.getAge());
        about.setText(mUserProfile.getAbout());



    }

}
