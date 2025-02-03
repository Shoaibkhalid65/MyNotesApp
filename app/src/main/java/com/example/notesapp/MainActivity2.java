package com.example.notesapp;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    List<Note> notes;
    Toolbar toolbar;
    EditText edTitle,edContent;
    boolean isEmptyFav=true;
    String title,content;
    boolean isTitleEmpty=true;
    boolean isSaveBtnClicked=false;
    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar=findViewById(R.id.top_bar_main_2);
        edTitle=findViewById(R.id.ed_title);
        edContent=findViewById(R.id.ed_content);
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
                if(id==R.id.empty_fav){
                    if(isEmptyFav){
                        item.setIcon(R.drawable.baseline_favorite_24_red);
                        showSnackBar("Added To Favourite");
                    }else{
                        item.setIcon(R.drawable.baseline_favorite_border_24);
                        showSnackBar("Removed From Favourite");
                    }
                    isEmptyFav=!isEmptyFav;
                }if(id==R.id.save){
                    title=edTitle.getText().toString();
                    if(title.isEmpty()){
                        Toast.makeText(MainActivity2.this, "Title can not be empty", Toast.LENGTH_SHORT).show();
                    }else {
                        if(!isSaveBtnClicked) {
                            saveNoteWorking();
                            isSaveBtnClicked=true;
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        if(!isSaveBtnClicked) {
            saveNoteWorking();
        }
    }
    public void showSnackBar(String message){
        View view=findViewById(R.id.main_2);
        Snackbar snackbar=Snackbar.make(this,view,message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }
    public void saveNoteWorking(){
        title=edTitle.getText().toString();
        content=edContent.getText().toString();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        if(title.isEmpty()){
            Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }else{
            note=new Note(title,content,!isEmptyFav);
            note.setTime(timestamp);
            DBHelper dbHelper=DBHelper.getInstance(this);
            dbHelper.insertNote(note);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
    
}