package com.sms.modules.student.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "student_documents")
@Data
public class DocumentMeta {
    @Id
    private String id;
    private String studentId;
    private String filename;
    private String gridFsId; // reference to file stored in GridFS
    private String docType; // BIRTH_CERT, TC, PHOTO etc
    private long uploadedAt;
    private String uploadedBy;
    // getters/setters
}
