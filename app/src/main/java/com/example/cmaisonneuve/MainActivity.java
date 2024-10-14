package com.example.cmaisonneuve;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.cmaisonneuve.db.DatabaseHelper;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
  private ViewPager2 viewPager;
  private TabLayout tabLayout;
    private ActivityResultLauncher<Intent> editCourseLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // afficher la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // -- initialiser viewpager et tablayout
          viewPager = findViewById(R.id.viewPager);
          tabLayout = findViewById(R.id.tabLayout);

          editCourseLauncher = registerForActivityResult(
                  new ActivityResultContracts.StartActivityForResult(),
                  result -> {
                      if(result.getResultCode() == RESULT_OK){
                          PageAdapter adapter = (PageAdapter) viewPager.getAdapter();
                          ListCoursFragment fragment = (ListCoursFragment)adapter.getFragment(0);
                        //  fragment.loadCoursesFromDatabase();
                      }
                  }
          );

       //--- Configurer notre adapter

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
//
        PageAdapter pageAdapter = new PageAdapter(this, user);
        viewPager.setAdapter(pageAdapter);

        
        // Lier mon viewpager avec le tablayout
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {
              switch (position){
                  case 0:
                      tab.setText("Liste Cours");
                      tab.setIcon(R.drawable.ic_lesson);
                      break;
                  case 1:
                      tab.setText("Mes Cours");
                      tab.setIcon(R.drawable.quiz);
                      break;
                  case 2:
                      tab.setText("Inscrire");
                      tab.setIcon(R.drawable.ic_add);
                      break;
                  case 3:
                      tab.setText("Profil");
                      tab.setIcon(R.drawable.ic_profile);
                      break;

              }
        })).attach();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);

        // Obtener el ID del usuario actual desde el Intent
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        // Verificar si el usuario tiene ID 1
        MenuItem addCourseItem = menu.findItem(R.id.action_add_course);

        if (user != null && user.getId() == 1) {
            // Mostrar el ítem de añadir curso solo para el usuario con ID = 1
            addCourseItem.setVisible(true);

        } else {
            // Ocultar el ítem para otros usuarios
            addCourseItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


         if(id == R.id.action_logout){
             Intent intent = new Intent(MainActivity.this, LoginActivity.class);
             startActivity(intent);
         }

        if(id == R.id.action_add_course){
            Intent intent = new Intent(MainActivity.this, CourseActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    
    
    public ActivityResultLauncher<Intent> getEditCourseLauncher(){
        return editCourseLauncher;
    }
}