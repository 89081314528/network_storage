package ru.julia.networkStorage.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "last_sync_dates")
@Data
@NoArgsConstructor
public class LastSyncDate {
    @Column(name = "last_sync_date_id")
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID lastSyncDateId = UUID.randomUUID();
    @Column(name = "client_name")
    private String clientName;
    @Column (name = "add_date")
    private LocalDateTime addDate;

   public LastSyncDate(String clientName, LocalDateTime addDate) {
        this.clientName = clientName;
        this.addDate = addDate;
    }
}
