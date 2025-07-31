package org.example.taskservice.dtos;



public interface DownloadAttachmentProjection {
    String getFileName();
    String getFileType();
    Long getFileSize();
    byte[] getData();
}
