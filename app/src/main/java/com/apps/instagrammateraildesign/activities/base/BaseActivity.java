package com.apps.instagrammateraildesign.activities.base;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.apps.instagrammateraildesign.R;
import com.apps.instagrammateraildesign.Utils.DrawerLayoutInstaller;
import com.apps.instagrammateraildesign.Utils.Utils;
import com.apps.instagrammateraildesign.activities.UserProfileActivity;
import com.apps.instagrammateraildesign.views.widget.GlobalMenuView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BaseActivity extends ActionBarActivity implements GlobalMenuView.OnHeardClickListner{

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.ivLogo)
    ImageView ivLogo;

    private MenuItem inboxMenuItem;
    private DrawerLayout drawerLayout;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
        setupToolbar();
        setupDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public MenuItem getInboxMenuItem() {
        return inboxMenuItem;
    }

    private void setupDrawer() {
        GlobalMenuView menuView = new GlobalMenuView(this);
        menuView.setOnHeardClickListner(this);

        drawerLayout = DrawerLayoutInstaller.from(this)
                .drawerRoot(R.layout.drawer_root)
                .drawerLeftView(menuView)
                .drawerLeftWidth(Utils.dpToPx(300))
                .withNavigationIconToggler(getToolbar())
                .build();
    }

    @Override
    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseActivity.this);
                overridePendingTransition(0, 0);
            }
        }, 200);
    }
}
