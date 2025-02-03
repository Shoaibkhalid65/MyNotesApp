package com.example.notesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {
    List<Note> notes;
    Toolbar toolbar;
    EditText edTitle,edContent;
    boolean isEmptyFav=true;
    Note note;
    DBHelper dbHelper;
    int noteId;
    boolean isFav;
    String oldTitle,oldContent;
    private MenuItem favIcon;
    boolean isSaveBtnClicked=false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toolbar=findViewById(R.id.top_bar_main_3);
        setSupportActionBar(toolbar);
        edTitle=findViewById(R.id.ed_title_3);
        edContent=findViewById(R.id.ed_content_3);
        dbHelper=DBHelper.getInstance(this);
        noteId=getIntent().getIntExtra(MainActivity.KEY_NOTE_ID,-1);
        oldTitle=getIntent().getStringExtra(MainActivity.KEY_NOTE_TITLE);
        edTitle.setText(oldTitle);
        oldContent=getIntent().getStringExtra(MainActivity.KEY_NOTE_CONTENT);
        edContent.setText(oldContent);
        isFav=getIntent().getBooleanExtra(MainActivity.KEY_NOTE_IS_FAV,false);
        if(isFav){
            changeSearchIcon(R.drawable.baseline_favorite_24_red);
            isEmptyFav=false;
        }else {
            changeSearchIcon(R.drawable.baseline_favorite_border_24);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.empty_fav_3){
                    if(isEmptyFav){
                        item.setIcon(R.drawable.baseline_favorite_24_red);
                        showSnackBar("Added To Favourite");
                    }else{
                        item.setIcon(R.drawable.baseline_favorite_border_24);
                        showSnackBar("Removed From Favourite");
                    }
                    isEmptyFav=!isEmptyFav;
                }
                if(id==R.id.save_3){
                    updateNoteWorking();
                    isSaveBtnClicked=true;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar_main3, menu);
        favIcon = menu.findItem(R.id.empty_fav_3);
        if (favIcon != null) {
            if (isFav) {
                favIcon.setIcon(R.drawable.baseline_favorite_24_red);
            } else {
                favIcon.setIcon(R.drawable.baseline_favorite_border_24);
            }
        }
        return true;
    }
    public void changeSearchIcon(int newIconResId) {
        if (favIcon != null) {
            favIcon.setIcon(newIconResId);
        }
    }


    @Override
    public void finish() {
        super.finish();
        if(!isSaveBtnClicked){
            updateNoteWorking();
        }
    }
    public void showSnackBar(String message){
        View view=findViewById(R.id.main_2);
        Snackbar snackbar=Snackbar.make(this,view,message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }
    public void updateNoteWorking(){
        String newTitle = edTitle.getText().toString();
        String newContent = edContent.getText().toString();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        if(!newTitle.equals(oldTitle)||!newContent.equals(oldContent)||isFav==isEmptyFav) {
            Note note1 = new Note(noteId, !isEmptyFav, newTitle, newContent, timestamp);
            boolean updated = dbHelper.updateNote(note1);
            if (updated) {
                Toast.makeText(MainActivity3.this, "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity3.this, "Failed to update note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

