package com.example.notesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class FavouriteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    FavAdaptor favAdaptor;
    List<Note> favNotes;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        recyclerView=findViewById(R.id.rv_fav);
        toolbar=findViewById(R.id.top_bar_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvSetting();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void rvSetting(){
        favNotes=new ArrayList<>();
        DBHelper dbHelper=DBHelper.getInstance(this);
        List<Note> notes=dbHelper.fetchNotes();
        for(Note note:notes){
            if(note.isFav()){
                favNotes.add(note);
            }
        }
        favAdaptor=new FavAdaptor(favNotes);
        favAdaptor.setOnFavNoteClick(new FavAdaptor.OnFavNoteClick() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent=new Intent(FavouriteActivity.this,MainActivity3.class);
                intent.putExtra(MainActivity.KEY_NOTE_ID,note.getId());
                intent.putExtra(MainActivity.KEY_NOTE_CONTENT,note.getContent());
                intent.putExtra(MainActivity.KEY_NOTE_TITLE,note.getTitle());
                intent.putExtra(MainActivity.KEY_NOTE_TIME,note.getTime());
                intent.putExtra(MainActivity.KEY_NOTE_IS_FAV,note.isFav());
                startActivity(intent);
            }

            @Override
            public void onLongNoteClick(Note note) {
                new AlertDialog.Builder(FavouriteActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBHelper dbHelper1=DBHelper.getInstance(FavouriteActivity.this);
                                dbHelper1.deleteNote(note);
                                notes.remove(note);
                                favNotes.remove(note);
                                favAdaptor.notifyDataSetChanged();
                                Toast.makeText(FavouriteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(favAdaptor);
        recyclerView.setHasFixedSize(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        favAdaptor.updateNotes(favNotes);
        rvSetting();
    }

}