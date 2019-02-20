package il.ac.hit.meepo.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import il.ac.hit.meepo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContainerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PlacesFragment placesFragment = new PlacesFragment();
        transaction.replace(R.id.container_frame, placesFragment);
        transaction.commit();
        return view;
    }
}
