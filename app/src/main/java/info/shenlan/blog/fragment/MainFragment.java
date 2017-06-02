package info.shenlan.blog.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import info.shenlan.blog.MainActivity;
import info.shenlan.blog.R;
import info.shenlan.widget.transformer.ScaleInOutTransformer;


public class MainFragment extends Fragment {

    ViewPager viewPager;
    Activity[] activities;
    TabHost tabHost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        View view = inflater.inflate(R.layout.main_fragment, null);
        tabHost = (TabHost) view.findViewById(R.id.tabhost);
        // 关联TabHost
//        tabHost.setup(this,R.id.home_container);
        //注意，监听要设置在添加Tab之前
//        tabHost.setOnTabChangedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onActivityCreated(savedInstanceState);
        //设置actionbar
        initViewPager();
//        initActionBar();
    }

    private void initViewPager() {
        ArrayList<View> list = new ArrayList<View>();
        Intent tabLayoutIntent = new Intent(getContext(),BottomTabLayoutActivity.class);
        BottomTabLayoutActivity tabLayoutActivity = new BottomTabLayoutActivity();
        UserProfileActivity profileActivity = new UserProfileActivity();
        activities[0] = tabLayoutActivity;
        activities[1] = profileActivity;
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(list));
        final ScaleInOutTransformer transformer = new ScaleInOutTransformer(1);
        viewPager.setPageTransformer(true, transformer);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //        activity.getSupportActionBar().setSelectedNavigationItem(position);
                //设置在其他页面滑动时slingmenu是边缘模式，最边上页面是全屏模式（防误触）
                switch (position) {
                    case 0:
                        ((MainActivity) getActivity()).getMySlidingMenu().
                                setTouchModeAbove(1);
                        break;
                    default:
                        ((MainActivity) getActivity()).getMySlidingMenu().
                                setTouchModeAbove(0);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private TextView newTextView(String text) {
        TextView tv = new TextView(getActivity());
        tv.setText(text);
        tv.setTextSize(30);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    /**
     * @author:Jack Tony 这里配置适配器
     * @tips :这里传入一个list数组，从每个list中可以剥离一个view并显示出来
     * @date :2014-9-24
     */
    public class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> mViewList;
        private int pagerNum = 0;

        public MyPagerAdapter(ArrayList<View> viewList) {
            mViewList = viewList;
        }

        public int getPagerNum() {
            return pagerNum;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            if (mViewList.get(arg1) != null) {
                ((ViewPager) arg0).removeView(mViewList.get(arg1));
            }
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            try {
                if (mViewList.get(arg1).getParent() == null) {
                    ((ViewPager) arg0).addView(mViewList.get(arg1), 0);
                } else {
                    /*
                     * 很难理解新添加进来的view会自动绑定一个父类，由于一个儿子view不能与两个父类相关，
                     * 所以得解绑不这样做否则会产生 viewpager java.lang.IllegalStateException:
                     * The specified child already has a parent. You must call
                     * removeView() on the child's parent first.
                     */
                    ((ViewGroup) mViewList.get(arg1).getParent())
                            .removeView(mViewList.get(arg1));
                    ((ViewPager) arg0).addView(mViewList.get(arg1), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pagerNum = arg1;
            }
            return mViewList.get(arg1);
        }

    }


}
