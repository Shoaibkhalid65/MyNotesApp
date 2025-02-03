package com.example.notesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.DeleteGesture;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView rvMain;
    Toolbar toolbar;
    NavigationView navigationView;
    ConstraintLayout layout;
    DrawerLayout drawerLayout;
    boolean isGrid=false;
    MainAdaptor mainAdaptor;
    boolean isGridIcon=true;
    List<Note> notes;
    public static final String KEY_NOTE_ID="id";
    public static final String KEY_NOTE_TITLE="title";
    public static final String KEY_NOTE_CONTENT="content";
    public static final String KEY_NOTE_IS_FAV="is_fav";
    public static final String KEY_NOTE_TIME="time";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.top_bar);
        navigationView=findViewById(R.id.navigation_view);
        layout=findViewById(R.id.constraint_layout);
        drawerLayout=findViewById(R.id.drawer_layout);
        rvMain=findViewById(R.id.rv_main);
        floatingActionButton=findViewById(R.id.btn_fab);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.favourite){
                    Intent intent=new Intent(MainActivity.this,FavouriteActivity.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        rvSetting();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.grid_view_icon){
                    if(isGridIcon){
                        item.setIcon(R.drawable.baseline_format_list_bulleted_24);
                    }else{
                        item.setIcon(R.drawable.baseline_window_24);
                    }
                    isGridIcon=!isGridIcon;
                    changeLayout();
                }else if(id==R.id.search_icon){
                    Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    public void rvSetting(){
        DBHelper dbHelper=DBHelper.getInstance(this);
        notes=dbHelper.fetchNotes();
        mainAdaptor=new MainAdaptor(notes,isGrid);
        mainAdaptor.setOnNoteClickListener(new MainAdaptor.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                intent.putExtra(KEY_NOTE_ID,note.getId());
                intent.putExtra(KEY_NOTE_CONTENT,note.getContent());
                intent.putExtra(KEY_NOTE_TITLE,note.getTitle());
                intent.putExtra(KEY_NOTE_TIME,note.getTime());
                intent.putExtra(KEY_NOTE_IS_FAV,note.isFav());
                startActivity(intent);
            }

            @Override
            public void onNoteLongClick(Note note) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBHelper dbHelper1=DBHelper.getInstance(MainActivity.this);
                                dbHelper1.deleteNote(note);
                                notes.remove(note);
                                mainAdaptor.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        if(isGrid){
            rvMain.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        }else {
            rvMain.setLayoutManager(new LinearLayoutManager(this));
        }
        rvMain.setAdapter(mainAdaptor);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No move functionality needed
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notes.get(position);

                notes.remove(position);
                mainAdaptor.notifyItemRemoved(position);

                Snackbar.make(rvMain, "Note deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            notes.add(position, note);
                            mainAdaptor.notifyItemInserted(position);
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                                    DBHelper dbHelper=DBHelper.getInstance(MainActivity.this);
                                    dbHelper.deleteNote(note); // Delete from database if not undone
                                }
                            }
                        })
                        .show();
            }
        });

        itemTouchHelper.attachToRecyclerView(rvMain);
        rvMain.setHasFixedSize(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mainAdaptor.updateNotes(notes);
        if(isGrid) {
            changeLayout();
        }
        rvSetting();

    }
    private void changeLayout(){
        isGrid=!isGrid;
        if(isGrid){
            rvMain.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        }else{
            rvMain.setLayoutManager(new LinearLayoutManager(this));
        }
        mainAdaptor=new MainAdaptor(notes,isGrid);
        mainAdaptor.setOnNoteClickListener(new MainAdaptor.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                intent.putExtra(KEY_NOTE_ID,note.getId());
                intent.putExtra(KEY_NOTE_CONTENT,note.getContent());
                intent.putExtra(KEY_NOTE_TITLE,note.getTitle());
                intent.putExtra(KEY_NOTE_TIME,note.getTime());
                intent.putExtra(KEY_NOTE_IS_FAV,note.isFav());
                startActivity(intent);
            }

            @Override
            public void onNoteLongClick(Note note) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBHelper dbHelper1=DBHelper.getInstance(MainActivity.this);
                                dbHelper1.deleteNote(note);
                                notes.remove(note);
                                mainAdaptor.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        rvMain.setAdapter(mainAdaptor);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No move functionality needed
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notes.get(position);

                // Remove from list but don't delete immediately
                notes.remove(position);
                mainAdaptor.notifyItemRemoved(position);

                // Show Snackbar for Undo
                Snackbar.make(rvMain, "Note deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            notes.add(position, note);
                            mainAdaptor.notifyItemInserted(position);
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                                    DBHelper dbHelper=DBHelper.getInstance(MainActivity.this);
                                    dbHelper.deleteNote(note); // Delete from database if not undone
                                }
                            }
                        })
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(rvMain);
        rvSetting();
    }

}