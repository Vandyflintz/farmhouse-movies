package com.vandyflintz.farmhousemovies;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Description extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Description() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Description.
     */
    // TODO: Rename and change types and number of parameters
    public static Description newInstance(String param1, String param2) {
        Description fragment = new Description();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    String desc,date;
    TextView tvdesc, tvdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_description, container, false);

        tvdate = v.findViewById(R.id.infohead);
        tvdesc = v.findViewById(R.id.infobody);

        SharedPreferences sp = getActivity().getSharedPreferences("MoviePartDetails",
                Context.MODE_PRIVATE);
        desc = (sp.getString("MovieDescription", ""));
        date = (sp.getString("MovieDate", ""));

        tvdate.setText(date);
        tvdesc.setText(desc);

        return v;
    }





}
