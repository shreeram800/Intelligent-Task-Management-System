package org.example.taskservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadAttachmentDto {

    private String fileName;
    private String fileType;
    private Long fileSize;
    private byte[] data;
}
