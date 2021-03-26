package com.hillel.notesapp.services;

import com.hillel.notesapp.dto.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ListService implements Service {

    List<Note> notes = new ArrayList<>();

    @Override
    public List<Note> getAllNotes() {
        return notes;
    }

    @Override
    public void delete(int index) {
        int delIndex = -1;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == index) {
                delIndex = i;
                break;
            }
        }
        if (delIndex >= 0)
            notes.remove(delIndex);
    }

    @Override
    public void add(Note note) {
        notes.add(new Note()
                .setId(newId())
                .setTitle(note.getTitle())
                .setDescription(note.getDescription())
                .setDateTime(LocalDateTime.now()));
    }

    @Override
    public Note getNote(int id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                return note;
            }
        }
        return null;
    }

    private int newId() {
        return notes.stream().map(Note::getId)
                .max(Comparator.comparingInt(a -> a))
                .map(id -> id + 1).orElse(1);
    }

}
