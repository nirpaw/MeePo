package il.ac.hit.meepo.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import il.ac.hit.meepo.Adapters.MatchesAdapter;
import il.ac.hit.meepo.Adapters.UsersAdapter;
import il.ac.hit.meepo.Models.MatchesObject;
import il.ac.hit.meepo.R;

public class MatchesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MatchesAdapter mMatchesAdapter  ;
    private RecyclerView.LayoutManager mMatchesLayoutManager ;

    private List<MatchesObject> resultMatches;

    private  String cuurentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        cuurentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.rv_matches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultMatches = new ArrayList<>();
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), getContext());
        recyclerView.setAdapter(mMatchesAdapter);

        getUserMatchId();

        return view;
    }

    private void getUserMatchId() {
        DatabaseReference matchDB = FirebaseDatabase.getInstance().getReference().child("Users").child(cuurentUserId).child("connections").child("matches") ;
        matchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        FetchMatchInFormation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInFormation(String key) {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(key) ;
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImgUrl = "";
                    if(dataSnapshot.child("firstName").getValue()!=null) {
                        name = dataSnapshot.child("firstName").getValue().toString();
                    }
                    if(dataSnapshot.child("imgURL").getValue()!=null) {
                        profileImgUrl = dataSnapshot.child("imgURL").getValue().toString();
                    }


                    MatchesObject object = new MatchesObject(userId, name, profileImgUrl);
                    resultMatches.add(object);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private List<MatchesObject> getDataSetMatches(){
        return resultMatches;
    }
}
