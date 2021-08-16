package be.ehb.myprojectexam.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import be.ehb.myprojectexam.R;


public class SearchFragment extends Fragment {

        public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            // verwijzen naar layout -
        View rootView= inflater.inflate(R.layout.fragment_search,container,false
        );

        // componenten van zoals rootView=findViewByid
        return rootView;

    }
}
