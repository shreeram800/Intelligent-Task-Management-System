package org.example.taskservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentMetaDto {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String downloadUrl; // Optional for frontend to download
}
