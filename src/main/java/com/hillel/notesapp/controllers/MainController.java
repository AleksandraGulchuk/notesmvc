package com.hillel.notesapp.controllers;

import com.hillel.notesapp.dto.Note;
import com.hillel.notesapp.infrastructure.annotations.Autowired;
import com.hillel.notesapp.infrastructure.annotations.Controller;
import com.hillel.notesapp.infrastructure.annotations.GetMapping;
import com.hillel.notesapp.infrastructure.annotations.PostMapping;
import com.hillel.notesapp.services.DatabaseService;
import com.hillel.notesapp.util.Checkers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class MainController {


    @Autowired
    private DatabaseService service;

    @GetMapping("")
    public void doGetMainPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("notes", service.getAllNotes());
        request.getRequestDispatcher("WEB-INF/views/main.jsp").forward(request, response);
    }

    @GetMapping("notes")
    public void doGetNotePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idFromAttribute = (String) request.getAttribute("id");
        if (idFromAttribute != null) {
            int id = Integer.parseInt(idFromAttribute);
            Note note = service.getNote(id);
            if (note != null) {
                request.setAttribute("note", note);
                request.getRequestDispatcher("WEB-INF/views/note.jsp").forward(request, response);
                return;
            }
        }
        request.setAttribute("message", "Note not found");
        request.getRequestDispatcher("WEB-INF/views/resultmessage.jsp").forward(request, response);
    }

    @PostMapping("add")
    public void doPostAdd(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        if (title.length() != 0 && description.length() != 0
                && title.length() <= 50 && description.length() <= 250) {
            service.add(new Note()
                    .setTitle(title)
                    .setDescription(description));
            request.setAttribute("message", "Note added successfully");
        } else {
            request.setAttribute("message",
                    "Note not added: Note must contain title (no more than 50 characters) " +
                            "and description (no more than 250 characters)");
        }
        request.getRequestDispatcher("WEB-INF/views/resultmessage.jsp").forward(request, response);
    }

    @PostMapping("delete")
    public void doPostDelete(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr.length() == 0) {
            request.setAttribute("message", "Note not deleted: note not found");
            request.getRequestDispatcher("WEB-INF/views/resultmessage.jsp").forward(request, response);
            return;
        }
        int id = Integer.parseInt(idStr);
        if (Checkers.checkId(id, service)) {
            service.delete(id);
            request.setAttribute("message", "Note deleted successfully");
        } else {
            request.setAttribute("message", "Note not deleted: note not found");
        }
        request.getRequestDispatcher("WEB-INF/views/resultmessage.jsp").forward(request, response);
    }

}
