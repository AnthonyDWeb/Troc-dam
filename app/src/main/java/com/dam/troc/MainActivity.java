package com.dam.troc;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.dam.troc.utils.Gol;
import com.dam.troc.viewpager.FragmentAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter adapter;
    DrawerLayout drawer_layout;

    // Gestion de la NavigationView
    private NavigationView navigationView;

    // Variable emplacement
    private static final String emplacement
            = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Recherche"));
        tabLayout.addTab(tabLayout.newTab().setText("Profil"));
        tabLayout.addTab(tabLayout.newTab().setText("Message"));

        viewPager2.setCurrentItem(1);
        tabLayout.selectTab(tabLayout.getTabAt(1));

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull
                                                    MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_fragment_1:
                viewPager2.setCurrentItem(0);
                tabLayout.selectTab(tabLayout.getTabAt(0));
                break;
            case R.id.nav_fragment_2:
                viewPager2.setCurrentItem(1);
                tabLayout.selectTab(tabLayout.getTabAt(1));
                break;
            case R.id.nav_fragment_3:
                viewPager2.setCurrentItem(2);
                tabLayout.selectTab(tabLayout.getTabAt(2));
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Méthodes du cycles de vies **/
    @Override
    protected void onStart() {
        super.onStart();
        Gol.addLog(emplacement, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Gol.addLog(emplacement, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gol.addLog(emplacement, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Gol.addLog(emplacement, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Gol.addLog(emplacement, "onDestroy");
    }
}