package info.shenlan.blog;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;

import info.shenlan.blog.fragment.MainFragment;
import info.shenlan.blog.fragment.MenuFragment;
import info.shenlan.widget.slidingmenu.SlidingMenu;


/**
 * @author:Jack Tony
 * @tips :简单介绍下思路 1.定义一个存放正文的容器，并写上控件id。可以放一个空的FrameLayout
 * 2.定义一个存放Menu的容器，写上控件id。也可以放一个空的FrameLayout
 * 3.通过setSlidingMenu()来设置一个sliding，并且用setMenu()方法来设置到menu容器上
 * example:menu.setMenu(R.layout.menu_frame);
 * 这样正文就可以和往常一样，给activity设置一个fragment，菜单的fragment直接设置到菜单容器的layout中即可。
 * <p>
 * 你可能会问，activity的布局中只有一个正文的容器啊，没有找到菜单的容器。给activity设置菜单容器的fragment，
 * 怎么不会报空指针。其实你在初始化SlidingMenu的时候就用activity对象初始化了SlidingMenu对象，这个菜单的
 * 框架时SlidingMenu负责维护的。所以肯定的说activity现在是包含两个框架的，正文框架+菜单框架
 * 因此，activity中目前有两个框架，并且两个框架都有各自的id，于是我们就可以照以往的方法来给两个框架中填入fragment了
 * @date :2014-9-24
 */
public class MainActivity extends AppCompatActivity {

    //通过数字来标识当前是哪个fragment，这样用于加载顶部的menu
    public static int NOW_FRAGMENT_NO = 0;

    private Fragment nowFragment;
    private SlidingMenu menu;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("dddddddd");
        // 设置正文容器
        setContentView(R.layout.content_frame);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击左上角打开抽屉
                menu.toggle();
            }
        });
        // 用代码来设置抽屉菜单
        setSlidingMenu();

        if (savedInstanceState != null) {
            nowFragment = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }
        // 默认载入的fragment
        else {
            nowFragment = new MainFragment();
        }
        initFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", nowFragment);
    }

    // 设置actionbar上面的按钮菜单------------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        switch (NOW_FRAGMENT_NO) {
            case 0:
                break;
            case 1:
                //保证兼容性
                MenuItem item = menu.add("Text").setIcon(R.mipmap.ic_launcher);
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
                break;
        }
        return true;
    }


    /**
     * 填入主界面和菜单的Fragment
     */
    private void initFragment() {
        // 设置正文容器
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_id, nowFragment).commit();

        // 设置菜单容器，这里用到了AppMenuFragment()
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame_id, new MenuFragment()).commit();
    }

    /**
     * slidingMenu属性详解
     */
    protected void setSlidingMenu() {
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置边缘阴影的宽度，通过dimens资源文件中的ID设置
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        // 设置阴影的图片
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置偏移量。说明：设置menu全部打开后，主界面剩余部分与屏幕边界的距离，值写在dimens里面:60dp
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置是否淡入淡出
        menu.setFadeEnabled(true);
        // 设置淡入淡出的值，只在setFadeEnabled设置为true时有效
        menu.setFadeDegree(0.5f);
        // 全屏：TOUCHMODE_FULLSCREEN ；边缘：TOUCHMODE_MARGIN ；不打开：TOUCHMODE_NONE
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置滑动时actionbar是否跟着移动，SLIDING_WINDOW=跟着移动;SLIDING_CONTENT=不跟着移动
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        // 设置滑动方向
        menu.setMode(SlidingMenu.LEFT);
        // 设置menu的背景
        // menu.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        // 设置菜单容器，注意这里只是容器，而不是菜单的界面
        menu.setMenu(R.layout.menu_frame);
    }

    public SlidingMenu getMySlidingMenu() {
        return menu;
    }

    /**
     * 切换正文fragment
     *
     * @param fragment
     */
    public void switchContent(Fragment fragment) {
        // 切换回默认的触控范围（全屏滑动有效）
        getMySlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        nowFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_id, fragment).commit();
        /**
         * 优化的代码，通过handler来避免滑动卡顿的情况。方法：延迟加载抽屉
         * 如果不优化也可以运行，就是将下面的代码改为：menu.showContent();
         */
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                menu.showContent();
            }
        }, 50);
    }

    private void initSlidingMenu() {
        menu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
    }
}
