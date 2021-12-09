package ru.julia.networkStorage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToSynchronized;
import ru.julia.networkStorage.services.StorageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StorageController {
private final StorageService storageService;


    @RequestMapping("/getFiles")
    public FilesToSynchronized filesToSynchronized(@RequestParam("filesFromClient")List<String> filesFromClient,
                                                   @RequestParam("clientName")String clientName,
                                                   @RequestParam("idDevice")String idDevice) {
        return storageService.filesToSynchronized(filesFromClient,clientName,idDevice);
    }

    @RequestMapping("/receiveFromClient")
    public String receiveFromClient(String clientName, String fileName, MultipartFile file) {
        return storageService.receiveFromClient(clientName, fileName, file);
    }

    @GetMapping(
            value = "/transferToClient",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
//    public @ResponseBody аннотация не нужна
    byte[] transferToClient(String clientName, String fileName) {
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

    @RequestMapping("/makeSyncLock")
    void makeSyncLock(String clientName) {
        storageService.makeSyncLock(clientName);
    }
    @RequestMapping("/makeSyncOpen")
    void makeSyncOpen(String clientName) {
        storageService.makeSyncOpen(clientName);
    }
    @RequestMapping("/getSyncLock")
    public Integer getSyncLock(String clientName) {
        return storageService.getSyncLock(clientName);
    }
}
