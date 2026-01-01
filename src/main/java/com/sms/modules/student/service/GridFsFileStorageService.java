package com.sms.modules.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;

@Service
public class GridFsFileStorageService implements FileStorageService {

    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public GridFsFileStorageService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public String storeFile(MultipartFile file, String filename) throws IOException {
        InputStream is = file.getInputStream();
        var id = gridFsTemplate.store(is, filename, file.getContentType());
        return id != null ? id.toString() : null;
    }

    @Override
    public Resource loadFileAsResource(String gridFsId) {
        GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(gridFsId)));
        if (file == null) {
            throw new RuntimeException("File not found");
        }
        GridFsResource resource = gridFsTemplate.getResource(file);
        return resource;
    }

    @Override
    public void deleteFile(String gridFsId) {
        gridFsTemplate.delete(query(where("_id").is(gridFsId)));
    }

    // utility static imports
    private static org.springframework.data.mongodb.core.query.Query query(org.springframework.data.mongodb.core.query.Criteria c){
        return new org.springframework.data.mongodb.core.query.Query(c);
    }
    private static org.springframework.data.mongodb.core.query.Criteria where(String key){
        return org.springframework.data.mongodb.core.query.Criteria.where(key);
    }
}
