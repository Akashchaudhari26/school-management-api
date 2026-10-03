package com.sms.modules.student.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "student_documents")
@Data
public class DocumentMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(length = 36)
    private String studentId;
    private String filename;
    @Column(length = 512)
    private String storageKey; // key returned by the configured file-storage provider
    private String docType; // BIRTH_CERT, TC, PHOTO etc
    private long uploadedAt;
    private String uploadedBy;
    // getters/setters
}
