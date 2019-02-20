package il.ac.hit.meepo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import il.ac.hit.meepo.Adapters.ViewPagerAdapter;

public class OtherUserProfileActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;

    private String []imgArr = {
            "https://www.gannett-cdn.com/presto/2019/02/20/USAT/f5987a1d-dd8a-47b6-b1e5-667e83d03d57-EPA_RUSSIA_PUTIN_STATE_OF_THE_NATION.JPG",
            "https://specials-images.forbesimg.com/imageserve/5aecc9a84bbe6f748688434d/416x416.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/President_Vladimir_Putin.jpg/300px-President_Vladimir_Putin.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        viewPager = findViewById(R.id.view_pager_pics_scrolling);
        adapter  = new ViewPagerAdapter(OtherUserProfileActivity.this, imgArr);
        viewPager.setAdapter(adapter);
    }

}
