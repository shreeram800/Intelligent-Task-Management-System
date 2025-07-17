package org.example.taskservice.repository;


import feign.Param;
import org.example.taskservice.dtos.AttachmentMetaProjection;
import org.example.taskservice.dtos.DownloadAttachmentProjection;
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

    @Query(value = """
    SELECT
        file_name AS fileName,
        file_type AS fileType,
        file_size AS fileSize,
        data AS data
    FROM attachments
    WHERE id = :id
""", nativeQuery = true)
    Optional<DownloadAttachmentProjection> findByIdCustom(@Param("id") Long id);
}
