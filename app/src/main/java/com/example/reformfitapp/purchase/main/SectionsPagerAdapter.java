package com.example.reformfitapp.purchase.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.reformfitapp.R;
import com.example.reformfitapp.purchaseFragment.GroupClassFragmentPurchase;
import com.example.reformfitapp.purchaseFragment.PrivateLessonFragmentPurchase;
import com.example.reformfitapp.purchaseFragment.VirtualLessonFragmentPurchase;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;


    private ChatWindowView fullScreenChatWindow;

    public SectionsPagerAdapter(Context context, FragmentManager fm, ChatWindowView fullScreenChatWindow) {
        super(fm);
        mContext = context;

        this.fullScreenChatWindow = fullScreenChatWindow;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position) {
            case 0:
                return new GroupClassFragmentPurchase(fullScreenChatWindow);
            case 1:
                return new PrivateLessonFragmentPurchase();
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
        return 2;
    }
}