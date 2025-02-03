package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.FavViewHolder> {
    List<Note> notes;
    List<Note> filteredNotes;
    OnSearchNoteClick onSearchNoteClick;

    public SearchAdapter(List<Note> notes) {
        this.notes = notes;
        this.filteredNotes=new ArrayList<>(notes);
    }
    public void setOnSearchNoteClick(OnSearchNoteClick onSearchNoteClick) {
        this.onSearchNoteClick = onSearchNoteClick;
    }
    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_search_view,null);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Note note = filteredNotes.get(position);
        if(note.getTitle().length()<=28) {
            holder.tvTitle.setText(note.getTitle());
        }else{
            holder.tvTitle.setText(note.getTitle().substring(0,26)+"...");
        }
        holder.tvTime.setText(note.getTime());
        holder.itemView.setOnClickListener(view -> onSearchNoteClick.onNoteClick(note));
        holder.itemView.setOnLongClickListener(view -> {onSearchNoteClick.onLongNoteClick(note); return true;});
    }

    @Override
    public int getItemCount() {
        return filteredNotes.size();
    }
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }
    public void filter(String query){
        filteredNotes.clear();
        if(query.isEmpty()){
            filteredNotes.addAll(notes);
        }else{
            for(Note note:notes){
                if(note.getTitle().toLowerCase().contains(query.toLowerCase())){
                    filteredNotes.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    class FavViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvTime;
        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title_fav);
            tvTime=itemView.findViewById(R.id.tv_time_date_fav);
        }
    }
    interface OnSearchNoteClick{
        void onNoteClick(Note note);
        void onLongNoteClick(Note note);
    }

}
