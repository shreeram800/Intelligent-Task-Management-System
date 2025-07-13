package org.example.taskservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "assigned_to")
    private Long assignedToUserId; // Reference to User ID (can be a foreign key in future with @ManyToOne)

    @Column(name = "project_id")
    private Long projectId; // Link to a project (if multi-project system)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status; // e.g., TODO, IN_PROGRESS, DONE

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TaskPriority priority; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachmentList;

    @Column(name = "updated_by")
    private Long updatedBy;

}
