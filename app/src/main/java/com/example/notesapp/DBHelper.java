package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Notes App";
    public static final int VERSION=1;
    public static DBHelper instance;

    public static DBHelper  getInstance(Context context) {
        if(instance==null){
            instance=new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Note.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i!=i1) {
            sqLiteDatabase.execSQL(Note.DROP_TABLE);
            sqLiteDatabase.execSQL(Note.CREATE_TABLE);
        }
    }
    public boolean insertNote(Note note){
        long noOfRowsAffected;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Note.KEY_TITLE,note.getTitle());
        contentValues.put(Note.KEY_CONTENT,note.getContent());
        contentValues.put(Note.KEY_IS_FAV,note.isFav());
        contentValues.put(Note.KEY_TIME,note.getTime());
        try {
           noOfRowsAffected=sqLiteDatabase.insert(Note.TABLE_NAME,null,contentValues);
        }catch (android.database.sqlite.SQLiteException e){
            Log.d("Entry Insertion", Objects.requireNonNull(e.getMessage()));
            return false;
        }
        return noOfRowsAffected==1;
    }
    public boolean updateNote(Note note){
        long noOfRowsAffected;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Note.KEY_TITLE,note.getTitle());
        contentValues.put(Note.KEY_CONTENT,note.getContent());
        contentValues.put(Note.KEY_IS_FAV,note.isFav());
        contentValues.put(Note.KEY_TIME,note.getTime());
        try {
            noOfRowsAffected=sqLiteDatabase.update(Note.TABLE_NAME,contentValues,Note.KEY_ID+" =?",new String[]{String.valueOf(note.getId())});
        }catch (android.database.sqlite.SQLiteException e){
            Log.d("Entry Update",Objects.requireNonNull(e.getMessage()));
            return false;
        }
        return noOfRowsAffected==1;
    }
    public boolean deleteNote(Note note){
        long noOfRowsAffected;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        try {
            noOfRowsAffected=sqLiteDatabase.delete(Note.TABLE_NAME,Note.KEY_ID+"=?",new String[]{String.valueOf(note.getId())});
        }catch (android.database.sqlite.SQLiteException e){
            Log.d("Entry Deletion Failed",Objects.requireNonNull(e.getMessage()));
            return false;
        }
        return noOfRowsAffected==1;
    }
    public List<Note> fetchNotes(){
       SQLiteDatabase sqLiteDatabase=getReadableDatabase();
       Cursor cursor=sqLiteDatabase.rawQuery(Note.SELECT_ALL_NOTES,null);
       List<Note> notes=new ArrayList<>(cursor.getCount());
       List<Note> reverseNotes=new ArrayList<>(cursor.getCount());
       if(cursor.moveToFirst()){
           do {
              Note note=new Note();
              int index=cursor.getColumnIndex(Note.KEY_ID);
              note.setId(cursor.getInt(index));
              index=cursor.getColumnIndex(Note.KEY_IS_FAV);
              note.setFav(cursor.getInt(index) != 0);
              index=cursor.getColumnIndex(Note.KEY_TITLE);
              note.setTitle(cursor.getString(index));
              index=cursor.getColumnIndex(Note.KEY_CONTENT);
              note.setContent(cursor.getString(index));
              index=cursor.getColumnIndex(Note.KEY_TIME);
              note.setTime(cursor.getString(index));
              notes.add(note);
           }while (cursor.moveToNext());
         cursor.close();
       }
        for(int i=notes.size()-1;i>=0;i--){
            reverseNotes.add(notes.get(i));
        }
        return reverseNotes;
    }
}
