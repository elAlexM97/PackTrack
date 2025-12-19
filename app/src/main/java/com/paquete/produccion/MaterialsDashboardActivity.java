package com.paquete.produccion;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MaterialsDashboardActivity extends BaseDashboardActivity {

    @Override
    protected void setupNavigationBar() {
        List<AnimatedBottomNavBar.NavItem> items = new ArrayList<>();
        items.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_agenda, getString(R.string.nav_confirm)));
        items.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_edit, getString(R.string.nav_prepare)));
        items.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_recent_history, getString(R.string.nav_history)));

        setupNavBar(items);
    }

    @Override
    protected void loadInitialFragment() {
        replaceFragment(new ConfirmTicketsFragment());
        if (navItems.size() > 0) {
            navBar.selectItem(0);
        }
    }

    @Override
    protected void onNavItemSelected(int position, AnimatedBottomNavBar.NavItem item) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ConfirmTicketsFragment();
                break;
            case 1:
                fragment = new PrepareTicketsFragment();
                break;
            case 2:
                fragment = new HistoryFragment();
                break;
        }
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }
}