package il.ac.hit.meepo.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import il.ac.hit.meepo.Models.User;
import il.ac.hit.meepo.R;

public class UserInPlaceAdapter extends RecyclerView.Adapter<UserInPlaceAdapter.UserInPlaceViewHolder> {
    public  List<User> users;
    MyUserInPlaceListener listener;

    public interface MyUserInPlaceListener {
        void onUserClicked(int position, View view);
        void onUserLongClicked(int position, View view);
    }

    public void setListener(MyUserInPlaceListener listener){
        this.listener = listener;
    }

    public UserInPlaceAdapter(List<User> userList) {
        this.users = userList;
    }

    public class UserInPlaceViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv;
        TextView ageTv;
        TextView genderTv;
//        ImageView imageIv;

        public UserInPlaceViewHolder(@NonNull final View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_user_first_name);
            ageTv = itemView.findViewById(R.id.tv_user_age);
            genderTv = itemView.findViewById(R.id.tv_user_gendet);

            itemView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onUserClicked(getAdapterPosition(),v);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        listener.onUserLongClicked(getAdapterPosition(),v);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserInPlaceViewHolder userInPlaceViewHolder, int i) {
        User user = users.get(i);
        userInPlaceViewHolder.nameTv.setText(user.getFirstName());
        userInPlaceViewHolder.ageTv.setText(user.getAge());
        userInPlaceViewHolder.genderTv.setText(user.getGender());
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
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



}
