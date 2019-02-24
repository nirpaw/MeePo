package il.ac.hit.meepo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

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
        holder.mMatchidTV.setText(matchesList.get(i).getUserId());
        holder.mMatchNameTV.setText(matchesList.get(i).getUserFirsName());
        if(matchesList.get(i).getUserProfileImgUrl().equals("default")){
            // TODO: ADD SOME DEFAULT IMAGE
        }
        else {
            String imgUrl = matchesList.get(i).getUserProfileImgUrl();
            Glide.with(context).load(imgUrl).into(holder.mMatchImgIV);
        }

    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
