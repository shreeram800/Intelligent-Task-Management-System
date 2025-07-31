package org.example.taskservice.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttachmentMetaProjection {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
}
