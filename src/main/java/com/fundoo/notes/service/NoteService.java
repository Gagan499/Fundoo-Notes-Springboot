package com.fundoo.notes.service;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    // create note
    NoteResponseDTO createnote(NoteRequestDTO noteRequestDTO,Long userId);

    //get all notes by user id
    Page<NoteResponseDTO> getAllNotesByuserId(Long userId,Pageable pageable);

    // get note by note id and user id
    NoteResponseDTO getNoteById(Long noteId,Long userId);

    NoteResponseDTO editNote(NoteRequestDTO noteRequestDTO, Long noteId,Long userId);

    boolean isSoftDelete(Long noteId, Long userId);

    void isDeleteNoteById(Long noteId, Long userId);
}
