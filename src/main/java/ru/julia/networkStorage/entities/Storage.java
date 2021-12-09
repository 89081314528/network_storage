package ru.julia.networkStorage.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "storages")
@Data
@NoArgsConstructor
public class Storage {
    @Column(name = "storage_id")
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID storageId = UUID.randomUUID();
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "file_name")
    private String fileName;
    @Column (name = "add_date")
    private LocalDateTime addDate;

    public Storage(String clientName, String fileName, LocalDateTime addDate) {
        this.clientName = clientName;
        this.fileName = fileName;
        this.addDate = addDate;
    }
}
