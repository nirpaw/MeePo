package il.ac.hit.meepo.Helpers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import il.ac.hit.meepo.NewChatActivity;
import il.ac.hit.meepo.R;

public class MatchesViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchidTV;
    public TextView mMatchNameTV;
    public ImageView mMatchImgIV;
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMatchidTV = itemView.findViewById(R.id.match_id);
        mMatchNameTV = itemView.findViewById(R.id.tv_match_name);
        mMatchImgIV = itemView.findViewById(R.id.iv_match_img);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext() , NewChatActivity.class);
        intent.putExtra("match_user_id", mMatchidTV.getText().toString());
        Bundle b = new Bundle();
        b.putString("matchId", mMatchidTV.getText().toString());
        b.putString("matchname", mMatchNameTV.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);    }
}
