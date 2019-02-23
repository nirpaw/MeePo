package il.ac.hit.meepo.Fragments;


import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import il.ac.hit.meepo.Adapters.UserInPlaceAdapter;
import il.ac.hit.meepo.Helpers.SwipeController;
import il.ac.hit.meepo.Helpers.SwipeControllerActions;
import il.ac.hit.meepo.Models.Place;
import il.ac.hit.meepo.Models.User;
import il.ac.hit.meepo.OtherUserProfileActivity;
import il.ac.hit.meepo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InPlaceFragment extends Fragment {
    RecyclerView recyclerView;
    UserInPlaceAdapter userInPlaceAdapter = null;
    List<User> listOfUsersInPlaceNow;
    View view;
    SwipeController swipeController = null;
    boolean watchOtherUserDeatails;

    boolean alreadyDeltedFromList = false;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDataBase;
    private DatabaseReference reference;
    private String LogedInUserId;

    Place currentPlace;

    boolean swipeBack;
    private static final String TAG = "InPlaceFragment";

    public InPlaceFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView:  start ");
        watchOtherUserDeatails = false;

        view = inflater.inflate(R.layout.fragment_in_place, container, false);
        recyclerView = view.findViewById(R.id.rv_users_in_place);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Bundle arguments = getArguments();
        currentPlace = (Place)arguments.getSerializable("currentPlace");
        listOfUsersInPlaceNow = new ArrayList<>();

        setupRecyclerView();
        setFireBaseDetails();
        setUsersDataAdapter();


//
//            @Override
//            public void onUserLongClicked(int position, View view) {
//
//                //TODO : REPORT USER?
//            }
//        });


//        final ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
//            @Override
//
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                listOfUsersInPlaceNow.remove(viewHolder.getAdapterPosition());
//                userInPlaceAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//            }
//
//            @Override
//            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
//            }
//
//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                if (actionState == ACTION_STATE_SWIPE) {
//                    setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//
//            private void setTouchListener(Canvas c,
//                                          RecyclerView recyclerView,
//                                          RecyclerView.ViewHolder viewHolder,
//                                          float dX, float dY,
//                                          int actionState, boolean isCurrentlyActive) {
//
//                recyclerView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
//                        return false;
//                    }
//                });
//
//            }
//        };
//
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void setFireBaseDetails() {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance();

        LogedInUserId = mFirebaseUser.getUid();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(!watchOtherUserDeatails) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseUser.getUid());
            HashMap<String, Object> map = new HashMap<>();
            map.put("lastLocationPlaceId", "Not in place");
            myRef.updateChildren(map);
            HashMap<String, Object> map2 = new HashMap<>();
            List<String> dummyList = new ArrayList<>();
            String d = "default";
            dummyList.add(d);
            map2.put("likedByUserList", dummyList);
            myRef.updateChildren(map2);
            Log.d(TAG, "onPause: im Not in place");
        }
        Log.d(TAG, "onPause: ");
    }

    private void setupRecyclerView() {


        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                userInPlaceAdapter.users.remove(position);
                userInPlaceAdapter.notifyItemRemoved(position);
                userInPlaceAdapter.notifyItemRangeChanged(position, userInPlaceAdapter.getItemCount());
            }

            @Override
            public void onLeftClicked(final int position) {
                alreadyDeltedFromList = false;
                reference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!listOfUsersInPlaceNow.isEmpty()) {
                            User thisUser = dataSnapshot.getValue(User.class);
                            List<String> myLikedUsers;
                            myLikedUsers = thisUser.getLikedByUserList();
                            if (position <= listOfUsersInPlaceNow.size() - 1 && !alreadyDeltedFromList) {
                                User likedOtherUser = listOfUsersInPlaceNow.get(position);
                                boolean alreadyLikedUser = false;
                                //match check
                                boolean isMatch = false;
                                for (String likesID : likedOtherUser.getLikedByUserList() // check if match
                                ) {
                                    if (thisUser.getId().equals(likesID) && !likesID.equals("default")) {
                                        isMatch = true; // TODO: MATCH (ADD MATCH)
                                        Log.d(TAG, "onDataChange: match!!!");
                                    }
                                }
                                for(int i = 0 ; i < myLikedUsers.size() ; i++){
                                    if(myLikedUsers.get(i).equals(likedOtherUser.getId()))
                                    {
                                        alreadyLikedUser = true;
                                    }
                                }
//                                for (String likedBefore : myLikedUsers // check endless update
//                                ) {
//                                    if (likedOtherUser.getId().equals(likedBefore) ) {
//                                        alreadyLikedUser = true;
//                                    }
//                                }
                                if (!alreadyLikedUser) {
                                    myLikedUsers.add(likedOtherUser.getId());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("likedByUserList", myLikedUsers);
                                    reference.updateChildren(hashMap);
                                }
                                if (!userInPlaceAdapter.users.isEmpty()) {
                                    userInPlaceAdapter.users.remove(position);
                                   userInPlaceAdapter.notifyItemRemoved(position);
                                    userInPlaceAdapter.notifyItemRangeChanged(position, userInPlaceAdapter.getItemCount());
                                    alreadyDeltedFromList = true;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void setUsersDataAdapter() {
        Log.d(TAG, "setUsersDataAdapter: ");

        reference = mDataBase.getReference("Users");
        userInPlaceAdapter = new UserInPlaceAdapter(listOfUsersInPlaceNow);
        recyclerView.setAdapter(userInPlaceAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfUsersInPlaceNow.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    if (!user.getId().equals(LogedInUserId) && user.getLastLocationPlaceId().equals(currentPlace.getmPlaceId())) {
                        listOfUsersInPlaceNow.add(user);
                        userInPlaceAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        userInPlaceAdapter.setListener(new UserInPlaceAdapter.MyUserInPlaceListener() {
            @Override
            public void onUserClicked(int position, View view) {
                Intent intent = new Intent(getContext(), OtherUserProfileActivity.class);
                intent.putExtra("user_object" ,listOfUsersInPlaceNow.get(position));
                watchOtherUserDeatails = true;
                startActivity(intent);
            }

            @Override
            public void onUserLongClicked(int position, View view) {

            }
        });

    }
}
