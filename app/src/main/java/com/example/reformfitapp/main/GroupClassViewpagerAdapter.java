package com.example.reformfitapp.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.reformfitapp.MindbodyClass;
import com.example.reformfitapp.MindbodyClassModel;
import com.example.reformfitapp.R;
import com.example.reformfitapp.ui.main.PlaceholderFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupClassViewpagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<ArrayList<MindbodyClassModel>> models;
    private boolean fragment;

    public GroupClassViewpagerAdapter(Context context, FragmentManager fm, ArrayList<String> datesArray, ArrayList<ArrayList<MindbodyClassModel>> modelsFrom, boolean fragment) {
        super(fm);
        mContext = context;
        dates = datesArray;
        models = modelsFrom;
        this.fragment = fragment;

    }

    @Override
    public Fragment getItem(int position) {

        return new GroupClassPlaceholderFragment(models, position, fragment);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return dates.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 14;
    }
}