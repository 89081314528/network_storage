package ru.julia.networkStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.julia.networkStorage.entities.DeleteFile;
import ru.julia.networkStorage.entities.LastSyncDate;

import java.util.List;
import java.util.UUID;

public interface DeleteFileRepository extends JpaRepository<DeleteFile, UUID> {
    @Query("select u from DeleteFile u where u.fileName = ?1")
    List<DeleteFile> findByFileName(String fileName);

    @Transactional
    @Modifying
    @Query("delete from DeleteFile u where u.fileName = ?1")
    void removeByFileName(String name);
}
