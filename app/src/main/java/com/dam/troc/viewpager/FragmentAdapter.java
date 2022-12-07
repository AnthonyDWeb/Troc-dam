package com.dam.troc.viewpager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dam.troc.profile.ProfileActivity;
import com.dam.troc.search.SearchActivity;
import com.dam.troc.tchat.TchatActivity;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(
            @NonNull @org.jetbrains.annotations.NotNull FragmentManager fragmentManager,
            @NonNull @org.jetbrains.annotations.NotNull Lifecycle lifecycle
    ) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new SearchActivity();
            case 1:
                return new ProfileActivity();
            case 2:
                return new TchatActivity();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
