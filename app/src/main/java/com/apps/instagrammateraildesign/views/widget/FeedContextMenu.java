package com.apps.instagrammateraildesign.views.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.instagrammateraildesign.R;
import com.apps.instagrammateraildesign.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohamed on 29/01/15.
 */
public class FeedContextMenu extends LinearLayout {
    private static final int CONTEXT_MENU_WIDTH  = Utils.dpToPx(240);

    private int feedItem = -1;

    private OnFeedContextMenuItemClickListner onFeedContextMenuItemClickListner;

    public FeedContextMenu(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void bindToItem(int feedItem) {
        this.feedItem = feedItem;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(FeedContextMenu.this);
    }

    @OnClick(R.id.btnReport)
    public void onReportClick() {
        if (onFeedContextMenuItemClickListner != null) {
            onFeedContextMenuItemClickListner.onReportClick(feedItem);
        }
    }

    @OnClick(R.id.btnSharePhoto)
    public void onSharePhotoClick() {
        if (onFeedContextMenuItemClickListner != null) {
            onFeedContextMenuItemClickListner.onSharePhotoClick(feedItem);
        }
    }

    @OnClick(R.id.btnCopyShareUrl)
    public void onCopyShareUrlClick() {
        if (onFeedContextMenuItemClickListner != null) {
            onFeedContextMenuItemClickListner.onCopyShareClick(feedItem);
        }
    }

    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        if (onFeedContextMenuItemClickListner != null) {
            onFeedContextMenuItemClickListner.onCancelClick(feedItem);
        }
    }

    public void setOnFeedContextMenuItemClickListner(OnFeedContextMenuItemClickListner onFeedContextMenuItemClickListner) {
        this.onFeedContextMenuItemClickListner = onFeedContextMenuItemClickListner;
    }

    public interface OnFeedContextMenuItemClickListner {
        public void onReportClick(int feedItem);
        public void onSharePhotoClick(int feedItem);
        public void onCopyShareClick(int feedItem);
        public void onCancelClick(int feedItem);

    }
}
