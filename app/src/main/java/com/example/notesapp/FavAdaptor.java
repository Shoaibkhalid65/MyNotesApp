package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavAdaptor extends RecyclerView.Adapter<FavAdaptor.FavViewHolder> {
    List<Note> notes;
    OnFavNoteClick onFavNoteClick;

    public FavAdaptor(List<Note> notes) {
        this.notes = notes;
    }

    public void setOnFavNoteClick(OnFavNoteClick onFavNoteClick) {
        this.onFavNoteClick = onFavNoteClick;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_card_view,null);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Note note=notes.get(position);
        holder.tvTime.setText(note.getTime());
        if(note.getContent().length()<=36) {
            holder.tvContent.setText(note.getContent());
        }else{
            holder.tvContent.setText(note.getContent().substring(0,34)+"...");
        }
        if(note.getTitle().length()<=28) {
            holder.tvTitle.setText(note.getTitle());
        }else{
            holder.tvTitle.setText(note.getTitle().substring(0,26)+"...");
        }
        holder.itemView.setOnClickListener(view -> onFavNoteClick.onNoteClick(note));
        holder.itemView.setOnLongClickListener(view -> {onFavNoteClick.onLongNoteClick(note); return true;});
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    class FavViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvContent,tvTime;
        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvContent=itemView.findViewById(R.id.tv_content);
            tvTime=itemView.findViewById(R.id.tv_time_date);
        }
    }
    interface OnFavNoteClick{
        void onNoteClick(Note note);
        void onLongNoteClick(Note note);
    }
}
