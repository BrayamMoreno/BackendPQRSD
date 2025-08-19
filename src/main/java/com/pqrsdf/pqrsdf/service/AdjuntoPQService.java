package com.pqrsdf.pqrsdf.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.config.FileStorageConfig;
import com.pqrsdf.pqrsdf.dto.DocumentoDTO;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.repository.AdjuntoPQRepository;

import jakarta.transaction.Transactional;

@Service
public class AdjuntoPQService extends GenericService<AdjuntoPQ, Long> {

    private final Path uploadDir;
    private final AdjuntoPQRepository repository;

    public AdjuntoPQService(AdjuntoPQRepository repository, FileStorageConfig config) throws IOException {
        super(repository);
        this.uploadDir = config.getUploadDir();
        Files.createDirectories(uploadDir);
        this.repository = repository;
    }

    public List<AdjuntoPQ> findByPqId(Long pqId) {
        return repository.findByPqId(pqId);
    }

    @Transactional
    public void createAdjuntosPqs(List<DocumentoDTO> files, PQ pq) {
        try {
            for (DocumentoDTO file : files) {
                // 1. Decodificar base64
                byte[] data = Base64.getDecoder().decode(file.Contenido());

                // 2. Normalizar nombre del archivo
                String safeFileName = Path.of(file.Nombre()).getFileName().toString();

                // 3. Generar nombre único usando uploadDir de FileStorageConfig
                Path targetPath = generateUniqueFileName(this.uploadDir, safeFileName);

                // 4. Guardar archivo en disco
                Files.write(targetPath, data, StandardOpenOption.CREATE_NEW);

                AdjuntoPQ adjuntoPQ = AdjuntoPQ.builder()
                        .pq(pq)
                        .nombreArchivo(targetPath.getFileName().toString())
                        .rutaArchivo(targetPath.toString())
                        .build();
                
                
                if (file.isRespuesta()) {
                    adjuntoPQ.setRespuesta(true);
                } else {
                    adjuntoPQ.setRespuesta(false);
                }

                // 5. Guardar relación en BD
                repository.save(adjuntoPQ);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error general al procesar los adjuntos", e);
        }
    }

    private Path generateUniqueFileName(Path uploadDir, String originalName) {
        Path targetPath = uploadDir.resolve(originalName);

        String fileName = originalName;
        String extension = "";

        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex > 0) {
            fileName = originalName.substring(0, dotIndex);
            extension = originalName.substring(dotIndex);
        }

        int counter = 1;
        while (Files.exists(targetPath)) {
            String newName = String.format("%s(%d)%s", fileName, counter, extension);
            targetPath = uploadDir.resolve(newName);
            counter++;
        }
        return targetPath;
    }
}
