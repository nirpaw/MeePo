package il.ac.hit.meepo.Helpers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import il.ac.hit.meepo.R;

public class MatchesViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView matchidTV;
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        matchidTV = itemView.findViewById(R.id.match_id);
    }

    @Override
    public void onClick(View v) {

    }
}
