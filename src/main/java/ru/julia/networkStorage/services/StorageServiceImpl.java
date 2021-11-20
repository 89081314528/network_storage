package ru.julia.networkStorage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.julia.networkStorage.dto.FilesToTransferAndReceive;
import ru.julia.networkStorage.entities.Storage;
import ru.julia.networkStorage.repositories.StorageRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Написать сервер, у сервера написать метод в контроллере, который принимает список файлов и имя клиента,
 * в качестве возвращаемого значения объект, в котором внутри 2 листа строк – один лист, это названия файлов,
 * которые нужно передать на сервер, другой, названия файлов, которые нужно получить с сервера. Для этого
 * нужно посмотреть в БД и по имени клиента(пользователя) сравнить, какие файлы есть на сервере для этого
 * клиента со списком файлов на клиенте. Здесь запись в БД не делается.
 * Список файлов находится в БД где нужно сделать таблицу (клиент, названия файлов)
 * На сервере сделать еще 2 метода. Один – получить файл, другой передать файл. Эти оба метода в качестве
 * аргументов будут принимать названия файлов и сервер будет фиксировать в БД, что этот файл получен.
 * А что файл был отправлен на клиент сервер фиксировать не будет потому что клиент периодически на сервер
 * отсылает список файлов.
 * В БД запись делается только когда клиент вызывает метод acceptFile на сервере.
 */
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;

    @Override
    public FilesToTransferAndReceive filesToTransferAndReceive(Map<String, Integer> filesFromClient, String clientName) {
        filesFromClient.put("file1", 0);
        filesFromClient.put("file2", 0);
        clientName = "julia";
        List<String> filesToTransfer = new ArrayList<>();// с сервера на клиент
        List<String> filesToReceive = new ArrayList<>();// с клиента на сервер
        FilesToTransferAndReceive filesToTransferAndReceive;
        Map<String, Integer> clientFilesFromServer = clientFilesFromServer(clientName);
        for (String s : clientFilesFromServer.keySet()) {
            if (filesFromClient.containsKey(s)) {
            } else {
                filesToTransfer.add(s);
            }
        }
        for (String s : filesFromClient.keySet()) {
            if (clientFilesFromServer.containsKey(s)) {
            } else {
                filesToReceive.add(s);
            }
        }
        filesToTransferAndReceive = new FilesToTransferAndReceive(filesToTransfer, filesToReceive, clientName);
        return filesToTransferAndReceive;
    }

    @Override
    public Map<String, Integer> clientFilesFromServer(String clientName) {
        List<Storage> list = storageRepository.findAll();
        Map<String, Integer> clientFilesFromServer = new HashMap<>();
        for (Storage storage : list) {
            if (storage.getClientName().equals(clientName)) {
                clientFilesFromServer.put(storage.getFileName(), 0);
            }
        }
        return clientFilesFromServer;
    }

    @Override
    public void transfer(FilesToTransferAndReceive filesToTransferAndReceive) {
        // File (название файла, имя клиента)

    }

    @Override // аргументом должен быть файл название файла и клиент
    public String receive(FilesToTransferAndReceive filesToTransferAndReceive) { // фиксируем в БД
        Map<String, Integer> filesFromClient = new HashMap<>();
        String client = "julia";
        filesFromClient.put("file1", 0);
        filesFromClient.put("file2", 0);
        filesToTransferAndReceive = filesToTransferAndReceive(filesFromClient, client);

        List<Storage> storageList = new ArrayList<>();
        List<String> filesToReceive = filesToTransferAndReceive.getFilesToReceive();
        String clientName = filesToTransferAndReceive.getClientName();
        for (String s : filesToReceive) {
            Storage storage = new Storage(clientName, s);
            storageList.add(storage);
        }
        storageRepository.saveAll(storageList);
        return "Файлы от клиента получены";
    }
}

