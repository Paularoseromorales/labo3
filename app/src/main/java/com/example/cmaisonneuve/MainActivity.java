package com.example.cmaisonneuve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cmaisonneuve.db.DatabaseHelper;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.ByteArrayOutputStream;



    public class MainActivity extends AppCompatActivity {

        private ViewPager2 viewPager;
        private TabLayout tabLayout;
        private ActivityResultLauncher<Intent> editCourseLauncher;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setIcon(R.mipmap.logo);

            viewPager = findViewById(R.id.viewPager);
            tabLayout = findViewById(R.id.tabLayout);

            DatabaseHelper dbHelper = new DatabaseHelper(this);


            if (!dbHelper.areCoursesAlreadyCreated()) {

                byte[] javaImage = getImageFromDrawable(R.drawable.java_image);
                byte[] webImage = getImageFromDrawable(R.drawable.web_image);
                byte[] mysqlImage = getImageFromDrawable(R.drawable.mysql_image);


                CourseItem cours1 = new CourseItem(100, "Introduction à Java", "Cours de base pour apprendre Java", "Prof. Dupont", "Debut: Automne 2024", javaImage);
                CourseItem cours2 = new CourseItem(101, "Développement Web ", "Apprentissage du développement web avec HTML et CSS", "Prof. Durand", "Debut:Automne 2024", webImage);
                CourseItem cours3 = new CourseItem(102, "BD MySQL", "Cours pour comprendre les bases de données avec MySQL", "Prof. Lefebvre", "Debut:Automne 2024", mysqlImage);

                dbHelper.insertCourses(cours1, javaImage, null);
                dbHelper.insertCourses(cours2, webImage, null);
                dbHelper.insertCourses(cours3, mysqlImage, null);


                Log.d("DatabaseHelper", "Cours avec des images insérées avec succès.");
            } else {

                Log.d("DatabaseHelper", "Les cours existent déjà, ils ne seront pas recréés.");
            }

            editCourseLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            PageAdapter adapter = (PageAdapter) viewPager.getAdapter();
                            ListCoursFragment fragment = (ListCoursFragment) adapter.getFragment(0);
                            // fragment.loadCoursesFromDatabase();
                        }
                    }
            );

            Intent intent = getIntent();
            User user = (User) intent.getSerializableExtra("user");

            PageAdapter pageAdapter = new PageAdapter(this, user);
            viewPager.setAdapter(pageAdapter);

            // Lier le ViewPager au TabLayout
            new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Liste Cours");
                        tab.setIcon(R.drawable.ic_lesson);
                        break;
                    case 1:
                        tab.setText("Mes Cours");
                        tab.setIcon(R.drawable.mcours);
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

            Intent intent = getIntent();
            User user = (User) intent.getSerializableExtra("user");


            MenuItem addCourseItem = menu.findItem(R.id.action_add_course);

            if (user != null && user.getId() == 1) {
                addCourseItem.setVisible(true);
            } else {
                addCourseItem.setVisible(false);
            }

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_logout) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            if (id == R.id.action_add_course) {
                Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }
        // Méthode pour convertir l'image de Drawable en octet[]
        private byte[] getImageFromDrawable(int drawableId) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

    }
