package com.qboxus.hugme.All_Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VP_Adapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    public VP_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void add_fragment(Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }
}
