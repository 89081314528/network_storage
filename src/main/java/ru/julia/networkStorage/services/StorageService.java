package ru.julia.networkStorage.services;

import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToSynchronized;

import java.util.List;
import java.util.Map;

public interface StorageService {
    FilesToSynchronized filesToSynchronized(List<String> filesFromClient, String clientName, String idDevice);

    Map<String, Integer> clientFilesFromServer(String clientName);

    byte[] transferToClient(String clientName, String fileName);
    String receiveFromClient(String clientName, String fileName, MultipartFile file);

    String deleteFromServer(String clientName, String fileName); // deleteFromClient только на клиенте

    void getLastSyncDate(String clientName);
    void makeSyncLock(String clientName);
    void makeSyncOpen(String clientName);
    public Integer getSyncLock(String clientName);

}
