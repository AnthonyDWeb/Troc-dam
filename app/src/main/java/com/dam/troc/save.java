//package com.dam.troc;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.dam.troc.search.SearchActivity;
//import com.dam.troc.tchat.TchatActivity;
//
//public class save {
//    private static final int NUM_PAGES = 3;
//    private ViewPager2 viewPager2;
//    private FragmentStateAdapter pagerAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        viewPager2 = findViewById(R.id.pager);
//        pagerAdapter = new MainActivity.ScreenSlidePageAdapter(this);
//        viewPager2.setAdapter(pagerAdapter);
//
//    }
//
//    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
//        public ScreenSlidePageAdapter(MainActivity mainActivity) {
//            super(mainActivity);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            switch (position) {
//                case 0:
//                    return new SearchActivity();
//                case 1:
//                    return new ProfilActivity();
//                case 2:
//                    return new TchatActivity();
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return NUM_PAGES;
//        }
//    }
//}
