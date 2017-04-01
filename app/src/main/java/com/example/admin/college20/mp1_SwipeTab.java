package com.example.admin.college20;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;

public class mp1_SwipeTab extends Fragment {
    public static ViewPager mp1_ViewPager;
    public static TabLayout mp1_TabLayout;
    public static int swipe_items = 4;

    final int[] ICONS = new int[]{
            R.drawable.mein,
            R.drawable.home,
            R.drawable.news,
            R.drawable.forum
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.activity_mp1__swipe_tab, null);
        mp1_ViewPager = (ViewPager) x.findViewById(R.id.Tab_ViewPager);
        mp1_TabLayout = (TabLayout) x.findViewById(R.id.mp1_tab);


        /**Setting up an adapter for your ViewPager so that you can combine your data and
         * view to see your data+Adapter View**/

        /*** getChildFragmentManager() gets back the private FragmentManager for placing
         * and managing Fragments inside  of this Fragment
        ***/
        mp1_ViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));


        /**TabLayout is setupWithViewPager but using a Runnable as it may be
         * due to a library bug. We could have done this w/o the Runnable as well as done below, although
         * this may not work with all systems
         *
         */

        mp1_TabLayout.setupWithViewPager(mp1_ViewPager);

        mp1_TabLayout.getTabAt(0).setIcon(ICONS[0]);
        mp1_TabLayout.getTabAt(1).setIcon(ICONS[1]);
        mp1_TabLayout.getTabAt(2).setIcon(ICONS[2]);
        mp1_TabLayout.getTabAt(3).setIcon(ICONS[3]);

        mp1_ViewPager.setPageTransformer(true, new CubeOutTransformer());
        return x;
    }

     class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            switch(position){
                case 0 : return new Moi_Frag();
                case 1 : return new HomeFragment();
                case 2 : return new UnderConstruction();
                case 3 : return new UnderConstruction();
            }

            return null;
        }

        @Override
        public int getCount(){
            return swipe_items;
        }


        @Override
        public CharSequence getPageTitle(int position){
            switch(position){
                case 0:
                    return "Me";
                case 1:
                    return "Home";
                case 2:
                    return "University News";
                case 3:
                    return "Forums";
            }
            return null;
        }

    }
}

