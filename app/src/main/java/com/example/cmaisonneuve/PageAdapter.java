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
    private User user; // Agregar el objeto User

    // Modificar el constructor para recibir User
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
                return ProfilFragment.newInstance(user); // Pasar el User al ProfilFragment
            case 3:
                return new InscriptionFragment();
            default:
                return new ListCoursFragment();
        }
    }

    @Override
    public int getItemCount() {
        if (user != null && user.getId() == 1) {
            // Retorna 3 pestañas, excluyendo la página 3
            return 3;
        } else {
            // Retorna todas las pestañas (4 en total)
            return 4;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        registeredFragments.put(position, getFragment(position));
    }

    // Método para recuperar el fragmento que está visible
    public Fragment getFragment(int position) {
        return registeredFragments.get(position);
    }
}
