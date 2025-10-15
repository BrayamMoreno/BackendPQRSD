package com.pqrsdf.pqrsdf.service;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class BackupService {

    private String backupDirectory;
    private String dbName;
    private String dbUser;
    private String dbPassword;
    private String dbHost;
    private String dbPort;

    public BackupService(Dotenv dotenv) {
        this.backupDirectory = dotenv.get("BACKUPS_DIR", "/default/backup/path");
        this.dbName = dotenv.get("SPRING_DATASOURCE_NAME", "mydatabase");
        this.dbUser = dotenv.get("SPRING_DATASOURCE_USERNAME");
        this.dbPassword = dotenv.get("SPRING_DATASOURCE_PASSWORD");
        this.dbHost = dotenv.get("SPRING_DATASOURCE_HOST");
        this.dbPort = dotenv.get("SPRING_DATASOURCE_PORT");
    }

    public void createBackup() throws Exception {
        File dir = new File(backupDirectory);
        if (!dir.exists())
            dir.mkdirs();

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("HH-mm-ss").format(new Date());

        String timestamp = date + "_" + time;

        String backupFile = backupDirectory + "/backup_" + timestamp + ".dump";

        String command = String.format(
                "pg_dump -h %s -p %s -U %s -d %s -F c -f %s",
                dbHost, dbPort, dbUser, dbName, backupFile);

        ProcessBuilder pb = new ProcessBuilder(
                isWindows() ? "cmd" : "sh",
                isWindows() ? "/c" : "-c",
                command);
        pb.environment().put("PGPASSWORD", dbPassword);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        if (process.waitFor() != 0) {
            throw new RuntimeException(
                    "Error al crear el backup: " + new String(process.getInputStream().readAllBytes()));
        }
    }

    public List<Map<String, String>> listarBackups() {
        File folder = new File(backupDirectory);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".dump"));

        List<Map<String, String>> backups = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();

                // Extraer timestamp: backup_2025-10-14_18-42-35+0000.dump
                String baseName = fileName.replace("backup_", "").replace(".dump", "");

                // Dividir fecha y hora
                String[] parts = baseName.split("_");
                String date = parts.length > 0 ? parts[0] : "N/A";
                String time = parts.length > 1 ? parts[1] : "N/A";

                Map<String, String> info = new HashMap<>();
                info.put("nombre", fileName);
                info.put("fecha", date);
                info.put("hora", time);
                info.put("ruta", file.getAbsolutePath());info.put("size", (file.length() / 1024) + " KB");
                info.put("tamaño", (file.length() / 1024) + " KB");

                backups.add(info);
            }
        }

        // Ordenar por fecha de modificación (más recientes primero)
        backups.sort((a, b) -> {
            File f1 = new File(a.get("ruta"));
            File f2 = new File(b.get("ruta"));
            return Long.compare(f2.lastModified(), f1.lastModified());
        });

        return backups;
    }

    public Resource downloadBackup(String fileName) throws IOException {
        Path path = Paths.get(backupDirectory, fileName);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Backup no encontrado");
        }
        return new InputStreamResource(Files.newInputStream(path));
    }

    public boolean deleteBackup(String fileName) {
        File file = new File(backupDirectory, fileName);
        return file.exists() && file.delete();
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
