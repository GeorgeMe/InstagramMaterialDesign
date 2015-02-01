package com.apps.instagrammateraildesign.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.apps.instagrammateraildesign.R;
import com.apps.instagrammateraildesign.Utils.Utils;
import com.apps.instagrammateraildesign.views.widget.CircleTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mohamed on 01/02/15.
 */
public class UserProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_PROFILE_HEADER = 0;
    public static final int TYPE_PROFILE_OPTION = 1;
    public static final int TYPE_PHOTO = 2;

    private static final int USER_OPTION_ANIMATION_DELAY = 300;
    private static final int MAX_PHOTO_ANIMATION_DELAY = 600;

    private static final int MIN_ITEMS_COUNT = 2;
    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();

    private final Context context;
    private final int cellSize;
    private final int avaterSize;

    private final String profilePhoto;
    private final List<String> photos;

    private boolean lockedAnimation = false;
    private long profileHeaderAnimationStartTime = 0;
    private int lastAnimateditem = 0;

    public UserProfileAdapter(Context context) {
        this.context = context;
        this.cellSize = Utils.getScreenWidth(context) / 3;
        this.avaterSize = context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        this.profilePhoto = context.getString(R.string.user_profile_photo);
        this.photos = Arrays.asList(context.getResources().getStringArray(R.array.user_photos));
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_PROFILE_HEADER;
            case 1:
                return TYPE_PROFILE_OPTION;
            default:
                return TYPE_PHOTO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_PROFILE_HEADER: {
                final View view = LayoutInflater.from(context).inflate(R.layout.view_user_profile_header, parent, false);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return new ProfileHeaderViewHolder(view);
            }

            case TYPE_PROFILE_OPTION: {
                final View view = LayoutInflater.from(context).inflate(R.layout.view_user_profile_options, parent, false);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return new ProfileOptionViewHolder(view);
            }
            default: {
                final View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                layoutParams.height = cellSize;
                layoutParams.width = cellSize;
                layoutParams.setFullSpan(false);
                view.setLayoutParams(layoutParams);
                return new PhotoViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case TYPE_PROFILE_HEADER:
                bindProfileHeader((ProfileHeaderViewHolder) holder);
                break;
            case TYPE_PROFILE_OPTION:
                bindProfileOption((ProfileOptionViewHolder) holder);
                break;
            default:
                bindPhoto((PhotoViewHolder) holder, holder.getPosition());

        }
    }

    @Override
    public int getItemCount() {
        return MIN_ITEMS_COUNT + photos.size();
    }

    private void bindProfileHeader(final ProfileHeaderViewHolder holder) {
        Picasso.with(context)
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avaterSize, avaterSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(holder.ivUserProfilePhoto);
        holder.vUserProfileRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.vUserProfileRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                animateUserProfileHeader(holder);
                return false;
            }
        });
    }

    private void bindProfileOption(final ProfileOptionViewHolder holder) {
        holder.vButtons.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.vButtons.getViewTreeObserver().removeOnPreDrawListener(this);
                holder.vUnderline.getLayoutParams().width = holder.btnGrid.getWidth();
                holder.vUnderline.requestLayout();
                animateUserProfileOptions(holder);
                return false;
            }
        });
    }

    private void bindPhoto(final PhotoViewHolder holder, int position) {
        Picasso.with(context)
                .load(photos.get(position - MIN_ITEMS_COUNT))
                .resize(cellSize, cellSize)
                .centerCrop().into(holder.ivPhoto, new Callback() {
            @Override
            public void onSuccess() {
                animatePhoto(holder);
            }

            @Override
            public void onError() {

            }
        });
        if (lastAnimateditem < position) lastAnimateditem = position;
    }

    private void animateUserProfileHeader(final ProfileHeaderViewHolder viewHolder) {
        if (!lockedAnimation) {
            profileHeaderAnimationStartTime = System.currentTimeMillis();

            viewHolder.vUserProfileRoot.setTranslationY(-viewHolder.vUserProfileRoot.getHeight());
            viewHolder.ivUserProfilePhoto.setTranslationY(-viewHolder.ivUserProfilePhoto.getHeight());
            viewHolder.vUserDetails.setTranslationY(-viewHolder.vUserDetails.getHeight());
            viewHolder.vUserStats.setAlpha(0);

            viewHolder.vUserProfileRoot.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(INTERPOLATOR);
            viewHolder.ivUserProfilePhoto.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setStartDelay(100)
                    .setInterpolator(INTERPOLATOR);
            viewHolder.vUserDetails.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setStartDelay(200)
                    .setInterpolator(INTERPOLATOR);
            viewHolder.vUserStats.animate()
                    .alpha(1)
                    .setDuration(200)
                    .setStartDelay(400)
                    .setInterpolator(INTERPOLATOR)
                    .start();
        }
    }

    private void animateUserProfileOptions(final ProfileOptionViewHolder viewHolder) {
        if (!lockedAnimation) {
            viewHolder.vButtons.setTranslationY(-viewHolder.vButtons.getHeight());
            viewHolder.vUnderline.setScaleX(0);

            viewHolder.vButtons.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setStartDelay(USER_OPTION_ANIMATION_DELAY)
                    .setInterpolator(INTERPOLATOR);
            viewHolder.vUnderline.animate()
                    .scaleX(1)
                    .setDuration(200)
                    .setStartDelay(USER_OPTION_ANIMATION_DELAY + 300)
                    .setInterpolator(INTERPOLATOR)
                    .start();
        }
    }

    private void animatePhoto(final PhotoViewHolder viewHolder) {
        if (!lockedAnimation) {
            if (lastAnimateditem == viewHolder.getPosition()) {
                setLockedAnimation(true);
            }

            long animationDelay = profileHeaderAnimationStartTime + MAX_PHOTO_ANIMATION_DELAY - System.currentTimeMillis();
            if (profileHeaderAnimationStartTime == 0) {
                animationDelay = viewHolder.getPosition() * 30 + MAX_PHOTO_ANIMATION_DELAY;
            } else if (animationDelay < 0) {
                animationDelay = viewHolder.getPosition() * 30;
            } else {
                animationDelay += viewHolder.getPosition() * 30;
            }

            viewHolder.flRoot.setScaleY(0);
            viewHolder.flRoot.setScaleX(0);
            viewHolder.flRoot.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    static class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ivUserProfilePhoto)
        ImageView ivUserProfilePhoto;
        @InjectView(R.id.vUserDetails)
        View vUserDetails;
        @InjectView(R.id.btnFolow)
        Button btnFollow;
        @InjectView(R.id.vUserStats)
        View vUserStats;
        @InjectView(R.id.vUserProfileRoot)
        View vUserProfileRoot;


        public ProfileHeaderViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    static class ProfileOptionViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.btnGrid)
        ImageButton btnGrid;
        @InjectView(R.id.btnList)
        ImageButton btnList;
        @InjectView(R.id.btnMap)
        ImageButton btnMap;
        @InjectView(R.id.btnTagged)
        ImageButton btnTagged;
        @InjectView(R.id.vUnderline)
        View vUnderline;
        @InjectView(R.id.vButtons)
        View vButtons;

        public ProfileOptionViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.flRoot)
        FrameLayout flRoot;
        @InjectView(R.id.ivPhoto)
        ImageView ivPhoto;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public void setLockedAnimation(boolean lockedAnimation) {
        this.lockedAnimation = lockedAnimation;
    }

 }
