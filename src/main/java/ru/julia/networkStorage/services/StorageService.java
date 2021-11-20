package ru.julia.networkStorage.services;

import ru.julia.networkStorage.dto.FilesToTransferAndReceive;

import java.util.Map;

public interface StorageService {
    FilesToTransferAndReceive filesToTransferAndReceive (Map<String, Integer> filesFromClient, String clientName);
    Map<String, Integer> clientFilesFromServer(String clientName);
    void transfer(FilesToTransferAndReceive filesToTransferAndReceive);
    String receive(FilesToTransferAndReceive filesToTransferAndReceive);

}
