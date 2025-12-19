package com.paquete.produccion;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class ProductionDashboardActivity extends BaseDashboardActivity {

    @Override
    protected void setupNavigationBar() {
        List<AnimatedBottomNavBar.NavItem> items = new ArrayList<>();
        items.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_add, getString(R.string.nav_request)));
        items.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_view, getString(R.string.nav_tickets)));
        items.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_recent_history, getString(R.string.nav_history)));

        setupNavBar(items);
    }

    @Override
    protected void loadInitialFragment() {
        replaceFragment(new RequestMaterialFragment());
        if (navItems.size() > 0) {
            navBar.selectItem(0);
        }
    }

    @Override
    protected void onNavItemSelected(int position, AnimatedBottomNavBar.NavItem item) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RequestMaterialFragment();
                break;
            case 1:
                fragment = new TicketsFragment(); // Se creará después
                break;
            case 2:
                fragment = new HistoryFragment(); // Se creará después
                break;
        }
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }
}