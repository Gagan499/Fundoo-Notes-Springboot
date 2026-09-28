package com.fundoo.notes.service;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;

import java.util.List;

public interface NoteService {
    // create note
    NoteResponseDTO createnote(NoteRequestDTO noteRequestDTO,Long userId);

    //get all notes by user id
    List<NoteResponseDTO> getAllNotesByuserId(Long userId);

    // get note by note id and user id
    NoteResponseDTO getNoteById(Long noteId,Long userId);
}
