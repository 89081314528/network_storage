package ru.julia.networkStorage.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delete_files")
@Data
@NoArgsConstructor
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

    public DeleteFile(String clientName, String fileName, LocalDateTime deleteDate) {
        this.clientName = clientName;
        this.fileName = fileName;
        this.deleteDate = deleteDate;
    }
}
