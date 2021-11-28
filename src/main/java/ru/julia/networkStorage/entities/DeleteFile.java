package ru.julia.networkStorage.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delete_files")
@Data
public class DeleteFile {
    @Column(name = "delete_file_id")
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID DeleteFileId = UUID.randomUUID();
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "file_name")
    private String fileName;
    @Column (name = "delete_date")
    private LocalDateTime deleteDate;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DeleteFile(String clientName, String fileName, LocalDateTime deleteDate) {
        this.clientName = clientName;
        this.fileName = fileName;
        this.deleteDate = deleteDate;
    }

    public UUID getDeleteFileId() {
        return DeleteFileId;
    }

    public void setDeleteFileId(UUID deleteFileId) {
        DeleteFileId = deleteFileId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDateTime getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(LocalDateTime deleteDate) {
        this.deleteDate = deleteDate;
    }

    public DeleteFile(){}
}
