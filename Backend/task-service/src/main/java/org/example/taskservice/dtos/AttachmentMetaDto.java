package org.example.taskservice.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AttachmentMetaDto {
    Long id;
    String fileName;
    String fileType;
    Long fileSize;
    String downloadUrl;

}
