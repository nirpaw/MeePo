package il.ac.hit.meepo.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import il.ac.hit.meepo.Models.User;
import il.ac.hit.meepo.R;

public class UserInPlaceAdapter extends RecyclerView.Adapter<UserInPlaceAdapter.UserInPlaceViewHolder> {
    List<User> movies;
    MyMovieListener listener;



    interface MyMovieListener {
        void onMovieClicked(int position, View view);
        void onMovieLongClicked(int position, View view);
    }

    public void setListener(MyMovieListener listener){
        this.listener = listener;
    }

    public UserInPlaceAdapter(List<User> movies) { this.movies = movies; }

    public class UserInPlaceViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv;
//        ImageView imageIv;

        public UserInPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onMovieClicked(getAdapterPosition(),v);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        listener.onMovieLongClicked(getAdapterPosition(),v);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserInPlaceViewHolder userInPlaceViewHolder, int i) {
        User user = movies.get(i);
        userInPlaceViewHolder.nameTv.setText(user.getFirstName() + " " + user.getLastName());
    }

    @NonNull
    @Override
    public UserInPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_in_place, viewGroup, false);
        UserInPlaceViewHolder movieViewHolder  = new UserInPlaceViewHolder(view);
        return movieViewHolder;
    }



    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



}
