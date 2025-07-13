package org.example.taskservice.service;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.taskservice.entity.Attachment;
import org.example.taskservice.repository.AttachmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {

    private final AttachmentRepository repository;

    private final EntityManager entityManager;

    public AttachmentService(AttachmentRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }
    @Transactional
    public void uploadFile(List<Attachment> attachments) {
        for (Attachment attachment : attachments) {
            entityManager.createNativeQuery(
                            "INSERT INTO attachments (file_name, file_type, file_size, data, task_id) " +
                                    "VALUES (:fileName, :fileType, :fileSize, :data, :taskId)")
                    .setParameter("fileName", attachment.getFileName())
                    .setParameter("fileType", attachment.getFileType())
                    .setParameter("fileSize", attachment.getFileSize())
                    .setParameter("data", attachment.getData()) // byte[]
                    .setParameter("taskId", attachment.getTask().getId())
                    .executeUpdate();
        }
    }



    public Attachment getFile(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }
}
