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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        recyclerView = view.findViewById(R.id.rv_matches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultMatches = new ArrayList<>();
        mMatchesAdapter = new MatchesAdapter(resultMatches, getContext());
        recyclerView.setAdapter(mMatchesAdapter);

        for(int i = 0 ; i < 100 ; i++){
            resultMatches.add(new MatchesObject("user "+i));
        }

        mMatchesAdapter.notifyDataSetChanged();


        return view;
    }


    private List<MatchesObject> getDataSetMatches(){
        return resultMatches;
    }
}
