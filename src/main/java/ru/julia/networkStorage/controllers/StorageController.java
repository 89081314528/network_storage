package ru.julia.networkStorage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToTransferReceiveDelete;
import ru.julia.networkStorage.services.StorageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StorageController {
private final StorageService storageService;

    @RequestMapping("/getFiles")
    public FilesToTransferReceiveDelete filesToTransferAndReceive(@RequestParam("filesFromClient")List<String> filesFromClient,
                                                                  @RequestParam("clientName")String clientName) {
        return storageService.filesToTransferReceiveDelete(filesFromClient,clientName);
    }

    @RequestMapping("/receiveFromClient")
    public String receiveFromClient(String clientName, String fileName, MultipartFile file) {
        return storageService.receiveFromClient(clientName, fileName, file);
    }

    @RequestMapping("/transferToClient")
    public String transferToClient(String clientName, String fileName) {
        return storageService.transferToClient(clientName, fileName);
    }

    @RequestMapping("/deleteFromServer")
    public String deleteFromServer(String clientName, String fileName) {
        return storageService.deleteFromServer(clientName, fileName);
    }
    @RequestMapping("/getLastSyncDate")
    public void getLastSyncDate(String clientName) {
        storageService.getLastSyncDate(clientName);
    }

}
