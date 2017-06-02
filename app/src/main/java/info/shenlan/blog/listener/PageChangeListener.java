package info.shenlan.blog.listener;

import android.app.Activity;
import android.support.v4.view.ViewPager;

import info.shenlan.blog.MainActivity;


public class PageChangeListener implements ViewPager.OnPageChangeListener {

    private Activity activity;

    public PageChangeListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void onPageSelected(int position) {
        //System.out.println("position:" + position);
//        activity.getSupportActionBar().setSelectedNavigationItem(position);
        //设置在其他页面滑动时slingmenu是边缘模式，最边上页面是全屏模式（防误触）
        switch (position) {
            case 0:
                ((MainActivity) activity).getMySlidingMenu().
                        setTouchModeAbove(1);
                break;
            default:
                ((MainActivity) activity).getMySlidingMenu().
                        setTouchModeAbove(0);
                break;
        }
    }
}
