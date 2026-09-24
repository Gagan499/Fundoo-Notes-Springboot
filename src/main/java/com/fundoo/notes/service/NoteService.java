package com.fundoo.notes.service;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;

import java.util.List;

public interface NoteService {
    NoteResponseDTO createnote(NoteRequestDTO noteRequestDTO,Long userId);

    List<NoteResponseDTO> getAllNotesByuserId(Long userId);
}
