package com.example.cmaisonneuve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
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

            // Mostrar la toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            viewPager = findViewById(R.id.viewPager);
            tabLayout = findViewById(R.id.tabLayout);

            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Verificar si los cursos ya están creados
            if (!dbHelper.areCoursesAlreadyCreated()) {
                // Obtener las imágenes predeterminadas desde drawable
                byte[] javaImage = getImageFromDrawable(R.drawable.java_image);
                byte[] webImage = getImageFromDrawable(R.drawable.web_image);
                byte[] mysqlImage = getImageFromDrawable(R.drawable.mysql_image);

                // Crear tres cursos con imágenes predeterminadas
                CourseItem cours1 = new CourseItem(100, "Introduction à Java", "Cours de base pour apprendre Java", "Prof. Dupont", "Automne 2024", javaImage);
                CourseItem cours2 = new CourseItem(101, "Développement Web ", "Apprentissage du développement web avec HTML et CSS", "Prof. Durand", "Automne 2024", webImage);
                CourseItem cours3 = new CourseItem(102, "BD MySQL", "Cours pour comprendre les bases de données avec MySQL", "Prof. Lefebvre", "Automne 2024", mysqlImage);

                // Insertar los cursos en la base de datos con imágenes
                dbHelper.insertCourses(cours1, javaImage, null);
                dbHelper.insertCourses(cours2, webImage, null);
                dbHelper.insertCourses(cours3, mysqlImage, null);

                // Log de verificación
                Log.d("DatabaseHelper", "Cursos con imágenes insertados con éxito.");
            } else {
                // Ya están creados los cursos
                Log.d("DatabaseHelper", "Los cursos ya existen, no se volverán a crear.");
            }

            // Configurar el ViewPager y TabLayout
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

            // Ligar el ViewPager con el TabLayout
            new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {
                switch (position) {
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

        // Método para convertir la imagen desde Drawable a byte[]
        private byte[] getImageFromDrawable(int drawableId) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

    }
