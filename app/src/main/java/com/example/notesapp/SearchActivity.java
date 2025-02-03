package com.example.notesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rvSearch;
    SearchView searchView;
    Toolbar toolbar;
    SearchAdapter searchAdapter;
    List<Note> noteList=new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar=findViewById(R.id.toolbar);
        rvSearch=findViewById(R.id.rv_search);
        setSupportActionBar(toolbar);
        rvWorking();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void rvWorking(){
        DBHelper dbHelper=DBHelper.getInstance(this);
        noteList.addAll(dbHelper.fetchNotes());
        searchAdapter=new SearchAdapter(noteList);
        searchAdapter.setOnSearchNoteClick(new SearchAdapter.OnSearchNoteClick() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent=new Intent(SearchActivity.this,MainActivity3.class);
                intent.putExtra(MainActivity.KEY_NOTE_ID,note.getId());
                intent.putExtra(MainActivity.KEY_NOTE_CONTENT,note.getContent());
                intent.putExtra(MainActivity.KEY_NOTE_TITLE,note.getTitle());
                intent.putExtra(MainActivity.KEY_NOTE_TIME,note.getTime());
                intent.putExtra(MainActivity.KEY_NOTE_IS_FAV,note.isFav());
                startActivity(intent);
            }

            @Override
            public void onLongNoteClick(Note note) {
                new AlertDialog.Builder(SearchActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBHelper dbHelper1=DBHelper.getInstance(SearchActivity.this);
                                dbHelper1.deleteNote(note);

                                int position = noteList.indexOf(note);
                                if (position != -1) {
                                    noteList.remove(position); // Remove from the list
                                    searchAdapter.notifyItemRemoved(position);
                                }

                                Toast.makeText(SearchActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        rvSearch.setAdapter(searchAdapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setHasFixedSize(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();

        assert searchView != null;
        searchView.setQueryHint("Search here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchAdapter.filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.filter(s);
                return true;
            }
        });
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();
    }
    private void refreshRecyclerView() {
        DBHelper dbHelper = DBHelper.getInstance(this);
        List<Note> updatedNotes = dbHelper.fetchNotes();

        noteList.clear();
        noteList.addAll(updatedNotes);
        searchAdapter.notifyDataSetChanged();
    }
}