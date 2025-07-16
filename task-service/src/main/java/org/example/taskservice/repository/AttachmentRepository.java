package org.example.taskservice.repository;


import feign.Param;
import org.example.taskservice.dtos.AttachmentMetaProjection;
import org.example.taskservice.dtos.DownloadAttachmentDto;
import org.example.taskservice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query("""
    SELECT new org.example.taskservice.dtos.AttachmentMetaProjection(
        a.id, a.fileName, a.fileType, a.fileSize
    )
    FROM Attachment a
    WHERE a.task.id = :taskId
""")
    List<AttachmentMetaProjection> findAttachmentMetaByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT a.fileName AS fileName, a.fileType AS fileType, a.fileSize AS fileSize, a.data AS data " +
            "FROM Attachment a WHERE a.id = :id")
    DownloadAttachmentDto findDownloadAttachmentById(Long id);

}
