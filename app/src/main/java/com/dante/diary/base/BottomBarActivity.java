package com.dante.diary.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.dante.diary.R;
import com.dante.diary.follow.TabsFragment;
import com.dante.diary.main.MainDiaryFragment;
import com.dante.diary.profile.ProfileFragment;
import com.dante.diary.utils.SpUtil;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;

public class BottomBarActivity extends BaseActivity implements FragNavController.RootFragmentListener {
    private final int MAIN = FragNavController.TAB1;
    private final int FOLLOWING = FragNavController.TAB2;
    private final int NOTIFICATION = FragNavController.TAB3;
    private final int ME = FragNavController.TAB4;

    public FragNavController controller;
    @BindView(R.id.bottomBar)
    public BottomBar bottomBar;
//    @BindView(R.id.container)
//    FrameLayout container;


    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        controller = new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.container, this, 4, MAIN);
        controller.setTransitionMode(FragmentTransaction.TRANSIT_NONE);
        initBottomBar();

    }

    public void hideBottomBar() {
        bottomBar.animate().translationY(bottomBar.getHeight())
                .setDuration(300)
                .start();
    }

    public void hideBottomBar(AnimatorListenerAdapter listenerAdapter) {
        bottomBar.animate().translationY(bottomBar.getHeight())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listenerAdapter.onAnimationEnd(animation);
                    }
                })
                .start();
    }

    public void showBottomBar(AnimatorListenerAdapter listenerAdapter) {
        bottomBar.animate().translationY(0)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listenerAdapter.onAnimationEnd(animation);
                    }
                })
                .start();
    }

    public void showBottomBar() {
        bottomBar.animate().translationY(0)
                .setDuration(300)
                .start();
    }

    private void initBottomBar() {
        bottomBar.selectTabAtPosition(MAIN);
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.main:
                    controller.switchTab(MAIN);
                    break;
                case R.id.following:
                    controller.switchTab(FOLLOWING);
                    break;
                case R.id.other:
                    controller.switchTab(NOTIFICATION);
                    break;
                case R.id.me:
                    controller.switchTab(ME);
                    break;

            }
        });
        bottomBar.setOnTabReselectListener(tabId -> {
            controller.clearStack();
            scrollToTop();
        });
    }

    private void scrollToTop() {
        Fragment fragment = controller.getCurrentFrag();
        if (fragment instanceof RecyclerFragment) {
            ((RecyclerFragment) fragment).scrollToTop();
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Fragment getRootFragment(int index) {

        switch (index) {
            case MAIN:
                return MainDiaryFragment.newInstance(index);
            case FOLLOWING:
                return TabsFragment.newInstance(new String[]{getString(R.string.my_following_diary), getString(R.string.my_following)});
            case NOTIFICATION:
                return TabsFragment.newInstance(new String[]{getString(R.string.my_notifications), getString(R.string.my_followers)});
            case ME:
                int id = SpUtil.getInt(Constants.ID);
                return ProfileFragment.newInstance(id);
        }
        throw new IllegalStateException("Need to send an index that we know");
    }
}
