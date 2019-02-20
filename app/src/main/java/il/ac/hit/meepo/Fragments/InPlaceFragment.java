package il.ac.hit.meepo.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        listOfUsersInPlaceNow.add(new User(null,"A",null,null,"female","20",null,null,null));
        listOfUsersInPlaceNow.add(new User(null,"b",null,null,"male","23",null,null,null));
        listOfUsersInPlaceNow.add(new User(null,"c",null,null,"female","30",null,null,null));
        listOfUsersInPlaceNow.add(new User(null,"d",null,null,"female","12",null,null,null));

        userInPlaceAdapter = new UserInPlaceAdapter(listOfUsersInPlaceNow);


        userInPlaceAdapter.setListener(new UserInPlaceAdapter.MyUserInPlaceListener() {
            @Override
            public void onUserClicked(int position, View view) {
                Intent intent = new Intent(getContext(), OtherUserProfileActivity.class);
                intent.putExtra("user_object" ,listOfUsersInPlaceNow.get(position));
            }

            @Override
            public void onUserLongClicked(int position, View view) {

                //TODO : REPORT USER?
            }
        });

        recyclerView = view.findViewById(R.id.rv_users_in_place);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(userInPlaceAdapter);
        return view;
    }

}
