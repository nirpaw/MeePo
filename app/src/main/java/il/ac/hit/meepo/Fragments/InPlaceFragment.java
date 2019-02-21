package il.ac.hit.meepo.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import il.ac.hit.meepo.Adapters.UserInPlaceAdapter;
import il.ac.hit.meepo.Models.User;
import il.ac.hit.meepo.OtherUserProfileActivity;
import il.ac.hit.meepo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InPlaceFragment extends Fragment {
    RecyclerView recyclerView;
    UserInPlaceAdapter userInPlaceAdapter;
    List<User> listOfUsersInPlaceNow;
    private static final String TAG = "InPlaceFragment";

    public InPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_place, container, false);

        listOfUsersInPlaceNow = new ArrayList<>();

        // add test users
        listOfUsersInPlaceNow.add(new User(null,"A",null,null,"female","20",null,null,null,null,null,null));
        listOfUsersInPlaceNow.add(new User(null,"A",null,null,"female","20",null,null,null,null,null,null));
        listOfUsersInPlaceNow.add(new User(null,"A",null,null,"female","20",null,null,null,null,null,null));
        listOfUsersInPlaceNow.add(new User(null,"A",null,null,"female","20",null,null,null,null,null,null));

        userInPlaceAdapter = new UserInPlaceAdapter(listOfUsersInPlaceNow);


        userInPlaceAdapter.setListener(new UserInPlaceAdapter.MyUserInPlaceListener() {
            @Override
            public void onUserClicked(int position, View view) {
                Intent intent = new Intent(getContext(), OtherUserProfileActivity.class);
                intent.putExtra("user_object" ,listOfUsersInPlaceNow.get(position));
                startActivity(intent);
            }

            @Override
            public void onUserLongClicked(int position, View view) {

                //TODO : REPORT USER?
            }
        });

        recyclerView = view.findViewById(R.id.rv_users_in_place);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override

            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                listOfUsersInPlaceNow.remove(viewHolder.getAdapterPosition());
                userInPlaceAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(userInPlaceAdapter);
        return view;
    }

}
