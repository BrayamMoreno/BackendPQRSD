package com.pqrsdf.pqrsdf.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
            
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.repository.AdjuntoPQRepository;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;


@Service
public class AdjuntoPQService extends GenericService <AdjuntoPQ, Long>{

    private String uploadDir;

    private final AdjuntoPQRepository repository;

    public AdjuntoPQService(AdjuntoPQRepository repository, Dotenv dotenv){
        super(repository);
        this.repository = repository;
        this.uploadDir = dotenv.get("file.upload-dir");
    }

    public List<AdjuntoPQ> findByPqId(Long pqId){
        return repository.findByPqId(pqId);
    }
}
