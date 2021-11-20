package ru.julia.networkStorage.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "storages")
@Data
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

    public Storage(String clientName, String fileName) {
        this.clientName = clientName;
        this.fileName = fileName;
    }

    public Storage() {
    }
}
