package il.ac.hit.meepo.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import il.ac.hit.meepo.Models.Place;
import il.ac.hit.meepo.R;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>{
    private List<Place> places;
    private Context context;
    private IPlaceAdapterListener listener;

    public PlacesAdapter(List<Place> places , Context context) {
        this.places = places;
        this.context = context;
    }

    public void setClickListener(IPlaceAdapterListener clickListener) {
        listener = clickListener;
    }

    public interface IPlaceAdapterListener{
        void OnPlaceClicked(int position, View view);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imgeViewPlacePoto;
        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_place_name);
            imgeViewPlacePoto = itemView.findViewById(R.id.iv_placePicture);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.OnPlaceClicked(getAdapterPosition(),v);
                    }
                }
            });


        }
    }


    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_item,viewGroup,false);
        PlacesViewHolder holder = new PlacesViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int i) {
        Place place  = places.get(i);
        holder.textViewName.setText(place.getmPlaceName());
        String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + place.getPhotoRefference() + "&key=" + context.getResources().getString(R.string.google_maps_api_key);
        if(place.getPhotoRefference() == null){
            holder.imgeViewPlacePoto.setImageResource(R.drawable.ic_launcher_background);
        }
        else {
            Glide.with(context).load(imgUrl).into(holder.imgeViewPlacePoto);
        }
    }


    public int getItemCount() {
        return places.size();
    }
}


