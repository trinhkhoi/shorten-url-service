package com.example.shortenurl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created_at", columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "updated_at", columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "deleted_at", columnDefinition = "datetime")
    private LocalDateTime deletedAt;

}
