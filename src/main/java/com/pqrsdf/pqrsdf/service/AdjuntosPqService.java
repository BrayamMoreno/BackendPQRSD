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
import com.pqrsdf.pqrsdf.models.AdjuntosPq;
import com.pqrsdf.pqrsdf.repository.AdjuntosPqRepository;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;


@Service
public class AdjuntosPqService extends GenericService <AdjuntosPq, Long>{

    private String uploadDir;

    private final AdjuntosPqRepository repository;

    public AdjuntosPqService(AdjuntosPqRepository repository, Dotenv dotenv){
        super(repository);
        this.repository = repository;
        this.uploadDir = dotenv.get("file.upload-dir");
    }

    public List<AdjuntosPq> findByPqId(Long pqId){
        return repository.findByPqId(pqId);
    }

    @Transactional
    public void createAdjuntosPqs(List<Map<String, String>> Adjuntos, Long pq_id) throws IOException{
        
        Path rutaSubida = Paths.get(uploadDir).toAbsolutePath().normalize();

        if(!Files.exists(rutaSubida)){
            Files.createDirectories(rutaSubida);
        }

        for (Map<String, String> archivo : Adjuntos){
            String nombreArchivo = archivo.get("filename");
            String base64 = archivo.get("base64");

            if(nombreArchivo == null || base64 == null){
                continue;
            }

            byte[] archivoBytes = Base64.getDecoder().decode(base64);
            Path rutaArchivo = rutaSubida.resolve(nombreArchivo);

            Files.write(rutaArchivo, archivoBytes);

            AdjuntosPq Adjunto = AdjuntosPq.builder()
                .nombreArchivo(nombreArchivo)
                .rutaArchivo(nombreArchivo)
                .pqId(pq_id)
                .build();

            repository.save(Adjunto);
        }
    }

}
