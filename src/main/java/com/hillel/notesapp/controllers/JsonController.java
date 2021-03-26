package com.hillel.notesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.notesapp.dto.Note;
import com.hillel.notesapp.dto.NoteResponse;
import com.hillel.notesapp.dto.NoteStringParam;
import com.hillel.notesapp.infrastructure.annotations.Autowired;
import com.hillel.notesapp.infrastructure.annotations.Controller;
import com.hillel.notesapp.infrastructure.annotations.GetMapping;
import com.hillel.notesapp.services.DatabaseService;
import com.hillel.notesapp.util.NoteSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class JsonController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DatabaseService service;

    @GetMapping("demo")
    public void doGetJson(Object object, HttpServletResponse response) {
        NoteResponse noteResponse = new NoteResponse()
                .setNotes(NoteSerializer.convertListNoteToStringParam(service.getAllNotes()))
                .setStatus("ok");
        writeJson(noteResponse, response);
    }


    private void writeJson(Object object, HttpServletResponse response) {
        try {
            response.setHeader("Content-Type", "application/json");
            String strResponse = objectMapper.writeValueAsString(object);
            response.getWriter().write(strResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> T readJson(Class<T> clazz, HttpServletRequest request) {
        try {
            String requestString = new String(request.getInputStream().readAllBytes());
            return objectMapper.readValue(requestString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void doPostJson(HttpServletRequest request, HttpServletResponse response) {
        NoteStringParam note = readJson(NoteStringParam.class, request);
        service.add(new Note().setTitle(note.getTitle())
                .setDescription(note.getDescription()));
        NoteResponse noteResponse = new NoteResponse()
                .setStatus("ok")
                .setMessage("Note added successfully");
        writeJson(noteResponse, response);
    }
}
