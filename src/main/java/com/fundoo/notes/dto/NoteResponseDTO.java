package com.fundoo.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponseDTO {
    private Long noteId;
    private String title;
    private String content;
    private String color;
    private boolean isArchived;
    private boolean isTrashed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
