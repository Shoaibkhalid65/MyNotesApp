package com.example.notesapp;

import java.security.PublicKey;
import java.util.Objects;

public class Note {
    private int id;
    private boolean isFav;
    private String title;
    private String content;
    private String time;

    public static final String KEY_ID="id";
    public static final String KEY_IS_FAV="isFav";
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_TIME="time";

    public static final String TABLE_NAME="Notes";
    public static final String CREATE_TABLE=String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT , %s INTEGER DEFAULT 0 , %s TEXT NOT NULL , %s TEXT , %s TEXT DEFAULT CURRENT_TIMESTAMP )",TABLE_NAME,KEY_ID,KEY_IS_FAV,KEY_TITLE,KEY_CONTENT,KEY_TIME);
    public static final String DROP_TABLE="DROP TABLE "+TABLE_NAME;
    public static final String SELECT_ALL_NOTES="SELECT * FROM "+TABLE_NAME;


    public Note() {
    }

    public Note(String title, String content, boolean isFav) {
        this.title = title;
        this.content = content;
        this.isFav = isFav;
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Note(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time=time;
    }

    public Note(int id, boolean isFav, String title, String content, String time) {
        this.id = id;
        this.isFav = isFav;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(title, note.title) && Objects.equals(content, note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
