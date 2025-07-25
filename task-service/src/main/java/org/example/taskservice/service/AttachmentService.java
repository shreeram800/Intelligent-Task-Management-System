package org.example.taskservice.service;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.example.taskservice.dtos.AttachmentMetaProjection;
import org.example.taskservice.dtos.DownloadAttachmentProjection;
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
                    .setParameter("data", attachment.getData())
                    .setParameter("taskId", attachment.getTask().getId())
                    .executeUpdate();
        }
    }
    public List<AttachmentMetaProjection> getProjection(Long taskId){
        return repository.findAttachmentMetaByTaskId(taskId);
    }

    public DownloadAttachmentProjection getAttachmentById(Long id) {
        return repository.findByIdCustom(id).orElseThrow(()->new ResourceNotFoundException("Attachment with id " + id + " not found"));
    }
}
