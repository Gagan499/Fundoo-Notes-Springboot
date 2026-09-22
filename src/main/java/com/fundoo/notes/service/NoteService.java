package com.fundoo.notes.service;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;

public interface NoteService {
    NoteResponseDTO createnote(NoteRequestDTO noteRequestDTO,Long userId);
}
