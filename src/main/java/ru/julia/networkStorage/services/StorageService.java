package ru.julia.networkStorage.services;

import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToTransferReceiveDelete;

import java.util.List;
import java.util.Map;

public interface StorageService {
    FilesToTransferReceiveDelete filesToTransferReceiveDelete(List<String> filesFromClient, String clientName);

    Map<String, Integer> clientFilesFromServer(String clientName);

    String transferToClient(String clientName, String fileName);
    String receiveFromClient(String clientName, String fileName, MultipartFile file);

    String deleteFromServer(String clientName, String fileName); // deleteFromClient только на клиенте

    void getLastSyncDate(String clientName);

}
