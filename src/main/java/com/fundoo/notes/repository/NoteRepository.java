package com.fundoo.notes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundoo.notes.entity.Note;
import java.util.Optional;

@Repository 
public interface NoteRepository extends JpaRepository<Note,Long>{
    
    Page<Note> findByUserUserId(Long userId,Pageable pageable);

    Optional<Note> findByNoteIdAndUserUserId(Long noteId, Long userId);
}
