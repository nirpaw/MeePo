package il.ac.hit.meepo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import il.ac.hit.meepo.Adapters.NewChatAdapter;
import il.ac.hit.meepo.Models.NewChatObject;

public class NewChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private static final String TAG = "NewChatActivity";
    private EditText mSendEditText;

    private Button mSendButton;

    private String currentUserID, matchId, chatId;

    private String otherUserProfilePic;

    private String tbotherUserProfilePic;
    private String OtherUserFirstName;


    private TextView matchNameTv;
    private CircleImageView matachImageCiv;
    private Toolbar mToolbar;

    private  boolean first = true;


    DatabaseReference mDatabaseUser, mDatabaseChat, mDatabaseOtherUser, mDatabaseOtherUserPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        mToolbar = findViewById(R.id.toolbar_inchat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");


        matchId = getIntent().getExtras().getString("matchId");
        OtherUserFirstName = getIntent().getExtras().getString("matchname");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseOtherUser = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId);
        mDatabaseOtherUserPic = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId).child("imageURL");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");

        mDatabaseOtherUserPic.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tbotherUserProfilePic = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("NewChat");
        mDatabaseOtherUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    otherUserProfilePic = dataSnapshot.child("imageURL").getValue().toString();
                    Log.d(TAG, "onDataChange: otherUserProfilePic seted  ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getChatId();



        matachImageCiv=findViewById(R.id.oprofile_image_chat);
        matchNameTv = findViewById(R.id.tv_match_name_chat);

//        if(tbotherUserProfilePic.equals("default")){
//            // TODO: ADD SOME DEFAULT IMAGE
//
//        }else {
//            Glide.with(this).load(tbotherUserProfilePic).into(matachImageCiv);
//        }
        matchNameTv.setText(OtherUserFirstName);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(NewChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new NewChatAdapter(getDataSetChat(), NewChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.item_unmatch_inchat) {
            //UNMACH

        }
        return true;
    }

    @Override
    public void setSupportActionBar(@Nullable android.support.v7.widget.Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        NewChatObject newMessage;
                        if(!createdByUser.equals(currentUserID)){
                            newMessage = new NewChatObject(message, currentUserBoolean, otherUserProfilePic);
                            Log.d(TAG, "onChildAdded: With pic : "+otherUserProfilePic);

                        }
                        else {
                            newMessage = new NewChatObject(message, currentUserBoolean);
                            Log.d(TAG, "onChildAdded: Without pic");

                        }
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private ArrayList<NewChatObject> resultsChat = new ArrayList<NewChatObject>();
    private List<NewChatObject> getDataSetChat() {
        return resultsChat;
    }
}