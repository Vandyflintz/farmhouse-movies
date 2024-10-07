package com.vandyflintz.farmhousemovies;

import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * {@link Series.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Series#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Series extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Series() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Series.
     */
    // TODO: Rename and change types and number of parameters
    public static Series newInstance(String param1, String param2) {
        Series fragment = new Series();
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
        View v = inflater.inflate(R.layout.fragment_series, container, false);

        viewpager = v.findViewById(R.id.pager);
        tabLayout = v.findViewById(R.id.tablayout);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

        return v;
    }

    public void reassignvalues(String sfragment, String result){
        if(sfragment == "Seasons"){
            FragmentManager fm = getChildFragmentManager();
            SeasonAnnexEpisodes fragment =
                    (SeasonAnnexEpisodes) fm.findFragmentByTag("seasannex");
            fragment.onresultreceived(result);
        }

        if(sfragment == "Telenovelas"){
            FragmentManager fm = getChildFragmentManager();
            TelenovelaAnnexEpisodes fragment =
                    (TelenovelaAnnexEpisodes) fm.findFragmentByTag("teleannex");
            fragment.onresultreceived(result);
        }
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {




        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }




        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:


                    return new Season();

                case 1:
                    return new Telenovela();



                default:
                    return new Season();

            }
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle ( int position) {
            switch (position) {
                case 0:
                    return "Seasonal Movies";
                case 1:
                    return "Telenovela ";


                default:
                    return "Seasonal";
            }
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
