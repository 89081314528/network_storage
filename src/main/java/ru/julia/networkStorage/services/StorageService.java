package ru.julia.networkStorage.services;

import ru.julia.networkStorage.dto.FilesToTransferAndReceive;

import java.util.List;
import java.util.Map;

public interface StorageService {
    FilesToTransferAndReceive filesToTransferAndReceive (List<String> filesFromClient, String clientName);
    Map<String, Integer> clientFilesFromServer(String clientName);

    String transfer(String clientNameName, String fileName);
    String receive(String clientName, String fileName);

}
