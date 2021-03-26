package com.hillel.notesapp.util;

import com.hillel.notesapp.dto.Note;
import com.hillel.notesapp.services.Service;

import java.util.List;
import java.util.stream.Collectors;


public class Checkers {

    private Checkers() {
    }

    public static boolean checkId(int id, Service service) {
        List<Note> notes = service.getAllNotes();
        List<Integer> result = notes.stream()
                .map(Note::getId)
                .filter(noteId -> noteId.equals(id))
                .collect(Collectors.toList());
        return !result.isEmpty();
    }
}
