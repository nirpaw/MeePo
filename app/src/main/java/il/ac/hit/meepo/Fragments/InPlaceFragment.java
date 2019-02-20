package il.ac.hit.meepo.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import il.ac.hit.meepo.Adapters.UserInPlaceAdapter;
import il.ac.hit.meepo.Models.User;
import il.ac.hit.meepo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InPlaceFragment extends Fragment {
    RecyclerView recyclerView;
    UserInPlaceAdapter userInPlaceAdapter;
    List<User> ListOfUsersInPlaceNow;

    public InPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_place, container, false);

        ListOfUsersInPlaceNow = new ArrayList<>();

        // add test users
        ListOfUsersInPlaceNow.add(new User(null,"A",null,null,null,null,null,null,null));
        ListOfUsersInPlaceNow.add(new User(null,"b",null,null,null,null,null,null,null));
        ListOfUsersInPlaceNow.add(new User(null,"c",null,null,null,null,null,null,null));
        ListOfUsersInPlaceNow.add(new User(null,"d",null,null,null,null,null,null,null));


        userInPlaceAdapter = new UserInPlaceAdapter(ListOfUsersInPlaceNow);

        recyclerView = view.findViewById(R.id.rv_users_in_place);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(userInPlaceAdapter);
        return view;
    }

}
