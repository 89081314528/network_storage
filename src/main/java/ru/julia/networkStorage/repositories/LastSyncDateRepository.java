package ru.julia.networkStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.julia.networkStorage.entities.LastSyncDate;

import java.util.List;
import java.util.UUID;

public interface LastSyncDateRepository extends JpaRepository<LastSyncDate, UUID> {
    @Transactional
    @Modifying
    @Query("delete from LastSyncDate u where u.clientName = ?1")
    void removeByClientName(String clientName);

    @Query("select u from LastSyncDate u where u.clientName = ?1")
    List<LastSyncDate> findByClientName(String clientName);

}
