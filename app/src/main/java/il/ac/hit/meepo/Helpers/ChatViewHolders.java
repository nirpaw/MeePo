package il.ac.hit.meepo.Helpers;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import il.ac.hit.meepo.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;
    public ImageView mProfileImage;

    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMessage = itemView.findViewById(R.id.show_message);
        mContainer = itemView.findViewById(R.id.container);
        mProfileImage = itemView.findViewById(R.id.profile_image);
    }

    @Override
    public void onClick(View view) {
    }
}