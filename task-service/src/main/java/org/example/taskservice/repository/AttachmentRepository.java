package org.example.taskservice.repository;


import org.example.taskservice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {


}
