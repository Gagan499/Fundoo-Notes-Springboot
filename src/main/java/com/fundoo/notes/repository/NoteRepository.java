package com.fundoo.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundoo.notes.entity.Note;
import java.util.List;
import java.util.Optional;

@Repository 
public interface NoteRepository extends JpaRepository<Note,Long>{
    
    List<Note> findByUserUserId(Long userId);

    Optional<Note> findByUserUserIdAndNoteId(Long userId,Long noteId);
}
