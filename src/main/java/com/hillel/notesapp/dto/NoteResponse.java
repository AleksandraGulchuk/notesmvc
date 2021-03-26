package com.hillel.notesapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {

    private String status;
    private NoteStringParam note;
    private List<NoteStringParam> notes;
    private String error;
    private String message;

}
