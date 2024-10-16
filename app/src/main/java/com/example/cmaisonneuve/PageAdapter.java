package com.example.cmaisonneuve;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.List;

public class PageAdapter extends FragmentStateAdapter {

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private User user;


    public PageAdapter(@NonNull FragmentActivity fragmentActivity, User user) {
        super(fragmentActivity);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ListCoursFragment();
            case 1:
                return new MesCoursFragment();
            case 2:
                return new InscriptionFragment();
            case 3:
                return ProfilFragment.newInstance(user);
            default:
                return new ListCoursFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        registeredFragments.put(position, getFragment(position));
    }

    // Méthode pour récupérer le fragment visible
    public Fragment getFragment(int position) {
        return registeredFragments.get(position);
    }
}