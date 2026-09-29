package com.fundoo.notes.service.Impl;

import com.fundoo.notes.dto.NoteRequestDTO;
import com.fundoo.notes.dto.NoteResponseDTO;
import com.fundoo.notes.entity.Note;
import com.fundoo.notes.entity.User;
import com.fundoo.notes.execption.AlreadyNoteIsTrashed;
import com.fundoo.notes.execption.EmptyNoteException;
import com.fundoo.notes.execption.NoteNotFoundByIdException;
import com.fundoo.notes.execption.TitleNotEmptyOrNull;
import com.fundoo.notes.repository.NoteRepository;
import com.fundoo.notes.repository.UserRepository;
import com.fundoo.notes.service.NoteService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
            log.info("Title and Content both are not empty or blank");
            throw  new EmptyNoteException("Title and Content cannot both be blank | Empty");
        }
        if (StringUtils.isBlank(noteRequestDTO.getTitle())
                && StringUtils.isNotBlank(noteRequestDTO.getContent())) {

            String[] words = noteRequestDTO.getContent().trim().split("\\s+");

            String title = String.join(" ",
                    Arrays.copyOf(words, Math.min(4, words.length)));

            noteRequestDTO.setTitle(title);
        }
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not Found"));
        log.info("user found");
        Note note = mapToNoteEntity(noteRequestDTO,user);
        Note savedNote = noteRepository.save(note);
        log.info("note saved to the repo");
        return mapNoteToResponse(savedNote);
    }

    @Override
    public List<NoteResponseDTO> getAllNotesByuserId(Long userId) {
        List<Note> list = noteRepository.findByUserUserId(userId);
        log.info("All logs are founds");
        return  list.stream().filter(note -> note.isTrashed() != true ).map(e->mapNoteToResponse(e)).collect(Collectors.toList());
    }

    @Override
    public NoteResponseDTO getNoteById(Long noteId, Long userId) {
        Note note = noteRepository.findByNoteIdAndUserUserId(noteId,userId).orElseThrow(()-> new NoteNotFoundByIdException("can't find note using this note id"));
        if(note.isTrashed()){
            log.info("The particular note is in the Trashed");
            throw new AlreadyNoteIsTrashed("Note is not available\nReason :- note is not present of these noteID ");
        }
        return mapNoteToResponse(note);
    }

    public NoteResponseDTO editNote(NoteRequestDTO dto, Long noteId, Long userId) {
        Note note = noteRepository.findByNoteIdAndUserUserId(noteId, userId)
                .orElseThrow(() -> new NoteNotFoundByIdException("cannot found note using this note id"));

        if (dto.getTitle() != null) {
            if (StringUtils.isBlank(dto.getTitle())) {
                throw new TitleNotEmptyOrNull("Title not be Blank or Empty, Title must be provided");
            }
            note.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            note.setContent(dto.getContent());     // fixed
        }
        if (dto.getColor() != null) {
            note.setColour(dto.getColor());        // fixed (your entity field is "colour")
        }
        log.info("Note partial updates successfully");
        return mapNoteToResponse(noteRepository.save(note));
    }

    // soft delete the note
    public boolean isSoftDelete(Long noteId, Long userId){
        Note note = noteRepository.findByNoteIdAndUserUserId(noteId,userId)
                    .orElseThrow(()-> new NoteNotFoundByIdException("Cannot found the note using this noteId and userId"));
        note.setTrashed(!note.isTrashed());
        noteRepository.save(note);
        log.info("Note trashed successfully");
        return note.isTrashed();
    }

    // delete note by note id
    @Override
    public void isDeleteNoteById(Long noteId, Long userId) {
        Note note = noteRepository.findByNoteIdAndUserUserId(noteId,userId)
                .orElseThrow(()-> new NoteNotFoundByIdException("Cannot found the note using this noteId and userId"));
        noteRepository.delete(note);
        log.info("Particular note deleted successfully");

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
