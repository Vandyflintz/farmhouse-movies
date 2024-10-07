package com.vandyflintz.farmhousemovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Payments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Payments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Payments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Payments.
     */
    // TODO: Rename and change types and number of parameters
    public static Payments newInstance(String param1, String param2) {
        Payments fragment = new Payments();
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

    ViewPager viewpager;
    TabLayout tabLayout;
    public SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payments, container, false);
        viewpager = v.findViewById(R.id.pager);
        tabLayout = v.findViewById(R.id.tablayout);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        return v;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {




        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }




        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Season_Sub_Level_One();
            }
            return new Season_Sub_Level_One();
        }


        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle ( int position) {
            if (position == 0) {
                return "Subscriptions";
            }
            return "Subscriptions";
        }
    }

}