package ru.julia.networkStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.julia.networkStorage.entities.LastSyncDate;
import ru.julia.networkStorage.entities.SyncLock;

import java.util.List;
import java.util.UUID;

public interface SyncLockRepository extends JpaRepository<SyncLock, UUID> {
    @Transactional
    @Modifying
    @Query("delete from SyncLock u where u.clientName = ?1")
    void removeByFileName(String clientName);

    @Query("select u from SyncLock u where u.clientName = ?1")
    List<SyncLock> findByClientName(String clientName);
}
