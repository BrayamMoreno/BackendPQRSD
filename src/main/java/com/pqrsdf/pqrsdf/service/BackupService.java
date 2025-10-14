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

    public BackupService(Dotenv dotenv) {
        this.backupDirectory = dotenv.get("BACKUPS_DIR", "/default/backup/path");
        this.dbName = dotenv.get("SPRING_DATASOURCE_NAME", "mydatabase");
        this.dbUser = dotenv.get("SPRING_DATASOURCE_USERNAME");
        this.dbPassword = dotenv.get("SPRING_DATASOURCE_PASSWORD");
    }

    public void createBackup() throws Exception {
        File dir = new File(backupDirectory);
        if (!dir.exists())
            dir.mkdirs();

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFile = backupDirectory + "/backup_" + timestamp + ".dump";

        String command = String.format("pg_dump -U %s -d %s -F c -f %s", dbUser, dbName, backupFile);

        ProcessBuilder pb = new ProcessBuilder(
                isWindows() ? "cmd" : "sh",
                isWindows() ? "/c" : "-c",
                command);
        pb.environment().put("PGPASSWORD", dbPassword);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        if (process.waitFor() != 0) {
            throw new RuntimeException("Error al crear el backup: " + new String(process.getInputStream().readAllBytes()));
        }
    }

    public List<Map<String, String>> listBackups() {
        File dir = new File(backupDirectory);
        if (!dir.exists())
            return Collections.emptyList();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".dump"));
        if (files == null)
            return Collections.emptyList();

        List<Map<String, String>> backups = new ArrayList<>();
        for (File file : files) {
            Map<String, String> info = new HashMap<>();
            info.put("name", file.getName());
            info.put("size", (file.length() / 1024) + " KB");
            info.put("date", new Date(file.lastModified()).toString());
            backups.add(info);
        }

        backups.sort((a, b) -> b.get("name").compareTo(a.get("name"))); // más recientes primero
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
