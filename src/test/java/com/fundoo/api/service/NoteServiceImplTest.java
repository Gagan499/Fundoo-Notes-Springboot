package com.fundoo.api.service;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;
import com.fundoo.notes.entity.Note;
import com.fundoo.notes.entity.User;
import com.fundoo.notes.execption.EmptyNoteException;
import com.fundoo.notes.repository.NoteRepository;
import com.fundoo.notes.repository.UserRepository;
import com.fundoo.notes.service.Impl.NoteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    private NoteRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        requestDTO = new NoteRequestDTO();

        requestDTO.setTitle("My First Note");
        requestDTO.setContent("This is my note content");
        requestDTO.setColor("blue");
    }

    @Test
    void notecreate_shouldThrow_EmptyNoteException_WhenContentAndTitleEmpty() {

        requestDTO.setTitle("");
        requestDTO.setContent("");

        assertThrows(
                EmptyNoteException.class,
                () -> noteService.createnote(requestDTO, 1L)
        );
    }

    @Test
    void notecreate_shouldCreateSuccessfully() {

        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Note savedNote = new Note();
        savedNote.setTitle(requestDTO.getTitle());
        savedNote.setContent(requestDTO.getContent());
        savedNote.setColour(requestDTO.getColor());

        when(noteRepository.save(any(Note.class)))
                .thenReturn(savedNote);
        NoteResponseDTO response =
                noteService.createnote(requestDTO, 1L);
        assertNotNull(response);
        assertEquals("My First Note", response.getTitle());
        assertEquals("This is my note content", response.getContent());
        assertEquals("blue", response.getColor());
    }
}