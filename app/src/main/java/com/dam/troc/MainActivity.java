package com.dam.troc;

import static com.dam.troc.commons.Constants.CURRENT_USER;
import static com.dam.troc.commons.Constants.FIREBASE_AUTH;
import static com.dam.troc.commons.Constants.FIRESTORE_INSTANCE_JOBS;
import static com.dam.troc.commons.Constants.SKILLNAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.troc.auth.LoginActivity;
import com.dam.troc.profile.EditProfileActivity;
import com.dam.troc.profile.EditSkillAdapter;
import com.dam.troc.profile.EditSkillModel;
import com.dam.troc.utils.Gol;
import com.dam.troc.viewpager.FragmentAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter adapter;
    DrawerLayout drawer_layout;
    //public final FirebaseAuth FIREBASE_AUTH_INITIALISATION = FirebaseAuth.getInstance();

    // Gestion de la NavigationView
    private NavigationView navigationView;
    // Variable emplacement
    private static final String emplacement = MainActivity.class.getSimpleName();
    private RecyclerView rc_dialog_skill;
    private TextView job1,job2,job3;
    private EditSkillAdapter adapterSkill;

    private void getDataSearchFromFirestore(String searchValue) {
        Query query = FIRESTORE_INSTANCE_JOBS.orderBy(SKILLNAME).startAt(searchValue).endAt(searchValue+"\uf8ff");
        FirestoreRecyclerOptions<EditSkillModel> jobs = new FirestoreRecyclerOptions.Builder<EditSkillModel>().setQuery(query, EditSkillModel.class).build();
        adapterSkill = new EditSkillAdapter(jobs);
        rc_dialog_skill.setAdapter(adapterSkill);
        adapterSkill.startListening();
    }

    public void addValue(View view) {
        TextView itemSelection = view.findViewById(R.id.tv_card_skill);
        String skillnameSelected = itemSelection.getText().toString();
        if(job1.getText().equals("aucun")){
            job1.setText(skillnameSelected);
        } else if (!job1.getText().equals("aucun") && job2.getText().equals("aucun")) {
            job2.setText(skillnameSelected);
        } else if (!job1.getText().equals("aucun") && !job2.getText().equals("aucun")){
            job3.setText(skillnameSelected);
        }
    }
    
    public void addSkill(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.dialog_add_skill, null);
        rc_dialog_skill = (RecyclerView) subView.findViewById(R.id.rc_dialog_skill);
        final SearchView searchView = (SearchView) subView.findViewById(R.id.dialog_skill_searchview);
        job1 = (TextView) subView.findViewById(R.id.tv_dialog_skill_job1_selection);
        job2 = (TextView) subView.findViewById(R.id.tv_dialog_skill_job2_selection);
        job3 = (TextView) subView.findViewById(R.id.tv_dialog_skill_job3_selection);
        final LinearLayout job1_section = (LinearLayout) subView.findViewById(R.id.dialog_job1_section);
        final LinearLayout job2_section = (LinearLayout) subView.findViewById(R.id.dialog_job2_section);
        final LinearLayout job3_section = (LinearLayout) subView.findViewById(R.id.dialog_job3_section);
        final Button addAnotherSkill = (Button) subView.findViewById(R.id.btn_dialog_skill_add_another);

        rc_dialog_skill.setLayoutManager(new LinearLayoutManager(this));

        Query query = FIRESTORE_INSTANCE_JOBS;
        FirestoreRecyclerOptions<EditSkillModel> jobs = new FirestoreRecyclerOptions.Builder<EditSkillModel>()
                .setQuery(query, EditSkillModel.class).build();
        adapterSkill = new EditSkillAdapter(jobs);
        rc_dialog_skill.setAdapter(adapterSkill);
        adapterSkill.startListening();

        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        /** Gestion du bouton réponse valider **/
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        /** Gestion du bouton réponse négative **/
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "eddit annulé", Toast.LENGTH_LONG).show();
            }
        });
        builder.setView(subView);
        builder.create();
        builder.show();

        if (job1.getText().equals("aucun")) {
            job2_section.setVisibility(View.GONE);
            job3_section.setVisibility(View.GONE);
        }

        TextView itemSelection = view.findViewById(R.id.tv_card_skill);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDataSearchFromFirestore(newText.toString());
                return false;
            }
        });
        addAnotherSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!job1.getText().equals("aucun") && job2.getText().equals("aucun")) {
                    job2_section.setVisibility(View.VISIBLE);
                } else if (!job1.getText().equals("aucun") && !job2.getText().equals("aucun")) {
                    job3_section.setVisibility(View.VISIBLE);
                    addAnotherSkill.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, "Choisissez un métier", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "onCreate FIREBASE_AUTH: " + FIREBASE_AUTH);
        Log.i("TAG", "onCreate CURRENT_USER: " + CURRENT_USER);

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
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth fa = FirebaseAuth.getInstance();
        Log.i("TAG", "onStart 1 fa: " + fa);
        Log.i("TAG", "onStart 2 FIREBASE_AUTH: " + FIREBASE_AUTH);
        FirebaseUser CURRENT_USER = fa.getCurrentUser();


    }
}