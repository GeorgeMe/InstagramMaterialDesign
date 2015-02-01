package com.apps.instagrammateraildesign.managers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.apps.instagrammateraildesign.Utils.Utils;
import com.apps.instagrammateraildesign.views.widget.FeedContextMenu;

/**
 * Created by Mohamed on 29/01/15.
 */
public class FeedContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private static FeedContextMenuManager instance;

    public FeedContextMenu feedContextMenu;

    private boolean isContextMenuShowing;
    private boolean isContextMenuDismissing;

    public static FeedContextMenuManager getInstance() {
        if (instance == null) {
            instance = new FeedContextMenuManager();
        }
        return instance;
    }

    private FeedContextMenuManager() {

    }


    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        feedContextMenu = null;
    }

    public void toggleContextMenuFromView(View openingView, int feedItem, FeedContextMenu.OnFeedContextMenuItemClickListner listner) {
        if (feedContextMenu == null) {
         showContextMenuFromView(openingView, feedItem, listner);
        } else {
          hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView, int feedItem, FeedContextMenu.OnFeedContextMenuItemClickListner listner) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            feedContextMenu = new FeedContextMenu(openingView.getContext());
            feedContextMenu.bindToItem(feedItem);
            feedContextMenu.addOnAttachStateChangeListener(this);
            feedContextMenu.setOnFeedContextMenuItemClickListner(listner);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(feedContextMenu);

            feedContextMenu.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    feedContextMenu.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = Utils.dpToPx(16);
        feedContextMenu.setTranslationX(openingViewLocation[0] - feedContextMenu.getWidth() / 3);
        feedContextMenu.setTranslationY(openingViewLocation[1] - feedContextMenu.getHeight() - additionalBottomMargin);
    }

    private void performShowAnimation() {
        feedContextMenu.setPivotX(feedContextMenu.getWidth() / 2);
        feedContextMenu.setPivotY(feedContextMenu.getHeight());
        feedContextMenu.setScaleX(0.1f);
        feedContextMenu.setScaleY(0.1f);
        feedContextMenu.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(openingAnimatorListenerAdapter)
                .start();
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            perfomDismissAnimation();
        }
    }

    private void perfomDismissAnimation() {
        feedContextMenu.setPivotX(feedContextMenu.getWidth() / 2);
        feedContextMenu.setPivotY(feedContextMenu.getHeight());
        feedContextMenu.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(closingAnimatorListenerAdapter)
                .start();

    }

    private AnimatorListenerAdapter openingAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            isContextMenuShowing = false;
        }
    };

    private AnimatorListenerAdapter closingAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (feedContextMenu != null) {
                feedContextMenu.dismiss();
            }

            isContextMenuDismissing = false;
        }
    };

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (feedContextMenu != null) {
            hideContextMenu();
            feedContextMenu.setTranslationY(feedContextMenu.getTranslationY() - dy);
        }
    }

}
