package il.ac.hit.meepo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import il.ac.hit.meepo.Helpers.MatchesViewHolder;
import il.ac.hit.meepo.Models.MatchesObject;
import il.ac.hit.meepo.R;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder>  {
    private List<MatchesObject> matchesList;
    private Context context;

    public MatchesAdapter(List<MatchesObject> matchesOList, Context context) {
        this.matchesList = matchesOList;
        this.context = context;
    }



    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layotuView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_matches, viewGroup, false);
        MatchesViewHolder holder = new MatchesViewHolder(layotuView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int i) {
        holder.matchidTV.setText(matchesList.get(i).getUserId());
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
