package ru.julia.networkStorage.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "synchronization_lock")
@Data
public class SyncLock {
    @Column(name = "synchronization_lock_id")
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID DeleteFileId = UUID.randomUUID();
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "lock")
    private Integer lock;

    public SyncLock(String clientName, Integer lock) {
        this.clientName = clientName;
        this.lock = lock;
    }
    public SyncLock(){};
}
