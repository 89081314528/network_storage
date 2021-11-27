package ru.julia.networkStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.julia.networkStorage.entities.Storage;
import java.util.UUID;

public interface StorageRepository extends JpaRepository<Storage, UUID> {
    @Transactional
    @Modifying
    @Query("delete from Storage u where u.fileName = ?1")
    void removeByFileName(String name);
}
