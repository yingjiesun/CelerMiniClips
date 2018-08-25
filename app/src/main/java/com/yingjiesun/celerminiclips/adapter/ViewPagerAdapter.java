package com.yingjiesun.celerminiclips.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import com.yingjiesun.celerminiclips.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context context;

    public ViewPagerAdapter(FragmentManager manager, Context context) {

        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);

    }

  //  @Override
  //  public CharSequence getPageTitle(int position) {
   //     return mFragmentTitleList.get(position);
   // }
    Drawable myDrawable;
    String title;

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                myDrawable = context.getResources().getDrawable(R.drawable.sample_3);
                title = "Test";
                break;

            default:
                myDrawable = context.getResources().getDrawable(R.drawable.sample_3);
                title = "Test";
                break;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder("   " + title); // space added before text for convenience
        try {
            myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sb;
    }
}
