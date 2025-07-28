package org.example.taskservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    // Auditing fields
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Assuming task-service is separate: store task IDs or use Rest client
    @ElementCollection
    @CollectionTable(name = "project_tasks", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "task_id")
    private List<UUID> taskIds;

    // Users assigned to this project (stored by UUID)
    @ElementCollection
    @CollectionTable(name = "project_users", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "user_id")
    private List<UUID> userIds;
}
