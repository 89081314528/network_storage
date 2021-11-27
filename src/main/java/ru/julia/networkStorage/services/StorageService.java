package ru.julia.networkStorage.services;

import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToDeleteFromServerAndClient;
import ru.julia.networkStorage.dto.FilesToTransferAndReceive;

import java.util.List;
import java.util.Map;

public interface StorageService {
    FilesToTransferAndReceive filesToTransferAndReceive (List<String> filesFromClient, String clientName);
    FilesToDeleteFromServerAndClient filesToDeleteFromServerAndClient (List<String> filesFromClient, String clientName);
    Map<String, Integer> clientFilesFromServer(String clientName);

    String transferToClient(String clientNameName, String fileName);
    String receiveFromClient(String clientName, String fileName, MultipartFile file);

    String deleteFromServer(String clientName, String fileName);

}
