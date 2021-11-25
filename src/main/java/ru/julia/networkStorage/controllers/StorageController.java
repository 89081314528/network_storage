package ru.julia.networkStorage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToTransferAndReceive;
import ru.julia.networkStorage.services.StorageService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StorageController {
private final StorageService storageService;

    @RequestMapping("/getFiles")
    public FilesToTransferAndReceive filesToTransferAndReceive(@RequestParam("filesFromClient")List<String> filesFromClient,
                                                               @RequestParam("clientName")String clientName) {
        return storageService.filesToTransferAndReceive(filesFromClient,clientName);
    }

    @RequestMapping("/receive")
    public String receive(String clientName, String fileName, MultipartFile file) {
        return storageService.receive(clientName, fileName, file);
    }

    @RequestMapping("/transfer")
    public String transfer(String clientName, String fileName) {
        return storageService.transfer(clientName, fileName);
    }

}
