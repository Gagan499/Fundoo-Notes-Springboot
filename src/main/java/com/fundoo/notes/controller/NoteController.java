package com.fundoo.notes.controller;

import com.fundoo.notes.config.UserPrinciple;
import com.fundoo.notes.dto.ApiResponse;
import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;
import com.fundoo.notes.service.Impl.NoteServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteServiceImpl noteService;

    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/createNote")
    public ResponseEntity<ApiResponse<NoteResponseDTO>> create(@AuthenticationPrincipal UserPrinciple principle, @RequestBody NoteRequestDTO noteRequestDTO){
        Long userId = principle.getUser().getUserId();
        NoteResponseDTO result = noteService.createnote(noteRequestDTO,userId);
        ApiResponse<NoteResponseDTO> response = ApiResponse.success("Note created successfully",result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-notes")
    public ResponseEntity<ApiResponse<List<NoteResponseDTO>>> getAllNotesByuserId(@AuthenticationPrincipal UserPrinciple principle){
        Long userId = principle.getUser().getUserId();
        List<NoteResponseDTO> notes = noteService.getAllNotesByuserId(userId);
        String message = notes.isEmpty() ? "No notes found" : "Notes retrieved successfully";
        return ResponseEntity.ok(ApiResponse.success(message, notes));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteResponseDTO>> getNoteById(@AuthenticationPrincipal UserPrinciple userPrinciple,@PathVariable Long noteId){
        Long userId = userPrinciple.getUser().getUserId();
        NoteResponseDTO result = noteService.getNoteById(noteId,userId);
        ApiResponse<NoteResponseDTO> response = ApiResponse.success("notes retrieved successfully",result);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
