package com.example.notesapp;

import android.annotation.SuppressLint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Note> notes;
    boolean isGrid=false;
    OnNoteClickListener onNoteClickListener;

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    public MainAdaptor(List<Note> notes, boolean isGrid) {
        this.notes = notes;
        this.isGrid = isGrid;
    }

    @Override
    public int getItemViewType(int position) {
       return isGrid? 1:0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if((viewType==1)){
           View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_grid_view,null);
           return new MainGridViewHolder(view);
       }else {
           View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_card_view,null);
           return new MainViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note note=notes.get(position);
        if(holder instanceof MainViewHolder){
            if(note.getTitle().length()<=28) {
                ((MainViewHolder) holder).tvTitle.setText(note.getTitle());
            }else {
                ((MainViewHolder) holder).tvTitle.setText(note.getTitle().substring(0,26)+"...");
            }
            if(note.getContent().length()<=36) {
                ((MainViewHolder) holder).tvContent.setText(note.getContent());
            }else {
                ((MainViewHolder) holder).tvContent.setText(note.getContent().substring(0,34)+"...");
            }
            ((MainViewHolder) holder).tvTime.setText(note.getTime());
        } else if(holder instanceof MainGridViewHolder){
            if(note.getTitle().length()<=26) {
                ((MainGridViewHolder) holder).tvTitle.setText(note.getTitle());
            }else{
                ((MainGridViewHolder) holder).tvTitle.setText(note.getTitle().substring(0,24)+"...");
            }
            if(note.getContent().length()<=90) {
                ((MainGridViewHolder) holder).tvContent.setText(note.getContent());
            }else {
                ((MainGridViewHolder) holder).tvContent.setText(note.getContent().substring(0,88)+"...");
            }
            ((MainGridViewHolder) holder).tvTime.setText(note.getTime());
        }
        setClickListeners(holder.itemView,note);
    }
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    class MainViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvContent,tvTime;
        @SuppressLint("CutPasteId")
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvContent=itemView.findViewById(R.id.tv_content);
            tvTime=itemView.findViewById(R.id.tv_time_date);
        }
    }
    class MainGridViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvContent,tvTime;
        public MainGridViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title_grid);
            tvContent=itemView.findViewById(R.id.tv_content_grid);
            tvTime=itemView.findViewById(R.id.tv_time_date_grid);
        }
    }
    interface OnNoteClickListener{
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
    }
    private void setClickListeners(View itemView, Note note) {
        itemView.setOnClickListener(view -> onNoteClickListener.onNoteClick(note));
        itemView.setOnLongClickListener(view -> {
            onNoteClickListener.onNoteLongClick(note);
            return true;
        });
    }
}
