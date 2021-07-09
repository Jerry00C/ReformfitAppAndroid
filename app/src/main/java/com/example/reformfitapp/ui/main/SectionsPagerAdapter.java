package com.example.reformfitapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.reformfitapp.GroupClass;
import com.example.reformfitapp.MindbodyClass;
import com.example.reformfitapp.MindbodyClassModel;
import com.example.reformfitapp.OnlineTraining;
import com.example.reformfitapp.PrivateClass;
import com.example.reformfitapp.R;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private ArrayList<ArrayList<MindbodyClassModel>> models;
    private ArrayList<ArrayList<MindbodyClassModel>> modelsOnline;
    private boolean fragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm, ArrayList<ArrayList<MindbodyClassModel>> modelsFrom,ArrayList<ArrayList<MindbodyClassModel>> modelsOnlineFrom, boolean fragment) {
        super(fm);
        mContext = context;
        models = modelsFrom;
        modelsOnline = modelsOnlineFrom;
        this.fragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GroupClass(models, fragment);
            case 1:
                return new PrivateClass(fragment);
           case 2:
                return new OnlineTraining(modelsOnline, fragment);
            default:
                return new PlaceholderFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}