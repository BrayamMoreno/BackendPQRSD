package com.pqrsdf.pqrsdf.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.config.FileStorageConfig;
import com.pqrsdf.pqrsdf.dto.DocumentoDTO;
import com.pqrsdf.pqrsdf.dto.UpdateAdjuntoRequest;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.repository.AdjuntoPQRepository;
import com.pqrsdf.pqrsdf.repository.PQRepository;

import jakarta.transaction.Transactional;

@Service
public class AdjuntoPQService extends GenericService<AdjuntoPQ, Long> {

    private final Path uploadDir;
    private final AdjuntoPQRepository repository;
    private final PQRepository pqRepository;

    public AdjuntoPQService(AdjuntoPQRepository repository, PQRepository pqRepository, FileStorageConfig config)
            throws IOException {
        super(repository);
        this.uploadDir = config.getUploadDir();
        Files.createDirectories(uploadDir);
        this.repository = repository;
        this.pqRepository = pqRepository;
    }

    public List<AdjuntoPQ> findByPqId(Long pqId) {
        return repository.findByPqId(pqId);
    }

    @Transactional
    public List<File> createAdjuntosPqs(List<DocumentoDTO> files, PQ pq) {
        List<File> createdFiles = new ArrayList<>();
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

                // 5. Crear entidad AdjuntoPQ
                AdjuntoPQ adjuntoPQ = AdjuntoPQ.builder()
                        .pq(pq)
                        .nombreArchivo(targetPath.getFileName().toString())
                        .rutaArchivo(targetPath.toString())
                        .respuesta(file.isRespuesta())
                        .build();

                // 6. Guardar en BD
                repository.save(adjuntoPQ);

                // 7. Agregar el File a la lista de retorno
                createdFiles.add(targetPath.toFile());
            }
            return createdFiles;
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

    public void updateAdjunto(UpdateAdjuntoRequest request) {
        AdjuntoPQ adjuntoPq = repository.findById(request.adjuntoPqId()).orElseThrow();
        adjuntoPq.setNombreArchivo(request.nombreArchivo());
        adjuntoPq.setPq(pqRepository.findById(request.pqId()).orElseThrow());
        adjuntoPq.setRespuesta(request.esRespuesta());
        repository.save(adjuntoPq);
    }
}
