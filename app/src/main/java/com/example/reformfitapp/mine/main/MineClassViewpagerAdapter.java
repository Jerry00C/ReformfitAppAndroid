package com.example.reformfitapp.mine.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.reformfitapp.FragmentPaymentHistory;
import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MineInfoPage1;
import com.example.reformfitapp.MineInfoPage3;
import com.example.reformfitapp.NonSwipeableViewPager;
import com.example.reformfitapp.R;

public class MineClassViewpagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    private MineInfoPage1 mineInfoPage1;
    private FragmentPaymentHistory fragmentPaymentHistory;
    private MineInfoPage3 mineInfoPage3;
    private GlobalVariableApplication application;
    private NonSwipeableViewPager nonSwipeableViewPager;



    public MineClassViewpagerAdapter(Context context, FragmentManager fm, NonSwipeableViewPager nonSwipeableViewPagerEx, GlobalVariableApplication applicationEx) {
        super(fm);
        mContext = context;
        nonSwipeableViewPager = nonSwipeableViewPagerEx;
        application = applicationEx;
    }

    @Override
    public Fragment getItem(int position) {



            switch (position) {
                case 0:
                    mineInfoPage1 = new MineInfoPage1();
                    return mineInfoPage1;
                case 1:
                    fragmentPaymentHistory = new FragmentPaymentHistory();
                    return fragmentPaymentHistory;
                case 2:
                    mineInfoPage3 = new MineInfoPage3();
                    return mineInfoPage3;
                default:
                    return null;
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

    public void refreshAll(){
        if(mineInfoPage1 != null) mineInfoPage1.refresh();
        if(fragmentPaymentHistory != null) fragmentPaymentHistory.refreshPurchaseHistory();
        if(mineInfoPage3 != null) mineInfoPage3.refreshPage();
    }


}
