package com.fundoo.notes.service.Impl;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;
import com.fundoo.notes.entity.Note;
import com.fundoo.notes.entity.User;
import com.fundoo.notes.execption.EmptyNoteException;
import com.fundoo.notes.repository.NoteRepository;
import com.fundoo.notes.repository.UserRepository;
import com.fundoo.notes.service.NoteService;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private Long userId;

    public NoteServiceImpl(NoteRepository repository,UserRepository userRepository){
        this.noteRepository = repository;
        this.userRepository = userRepository;
    }
    @Override
    public NoteResponseDTO createnote(NoteRequestDTO noteRequestDTO,Long userId) {
        if(StringUtils.isBlank(noteRequestDTO.getContent()) && StringUtils.isBlank(noteRequestDTO.getTitle())){
            throw  new EmptyNoteException("Title and Content cannot both be blank | Empty");
        }
        if(StringUtils.isBlank(noteRequestDTO.getTitle()) && StringUtils.isNotBlank(noteRequestDTO.getContent())){
            noteRequestDTO.setTitle(noteRequestDTO.getContent().split("\\R", 2)[0]
                    .trim());
        }
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not Found"));
        Note note = mapToNoteEntity(noteRequestDTO,user);
        Note savedNote = noteRepository.save(note);
        return mapNoteToResponse(savedNote);
    }


    // Map Note to Entity
    private Note mapToNoteEntity(NoteRequestDTO requestDTO,User user) {

        Note.NoteBuilder builder = Note.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isArchived(false)
                .isTrashed(false)
                .user(user);

        if (requestDTO.getColor() != null &&
                !requestDTO.getColor().isBlank()) {

            builder.colour(requestDTO.getColor());
        }

        return builder.build();
    }

    //Map Note to Response dto
    private NoteResponseDTO mapNoteToResponse(Note note){
        return new NoteResponseDTO(
            note.getNoteId(),
            note.getTitle(),
            note.getContent(),
            note.getColour(),
            note.isArchived(),
            note.isTrashed(),
            note.getCreatedAt(),
            note.getUpdatedAt()
        );
    }
}
