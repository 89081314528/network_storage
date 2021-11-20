package ru.julia.networkStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.julia.networkStorage.entities.Storage;
import java.util.UUID;

public interface StorageRepository extends JpaRepository<Storage, UUID> {
}
