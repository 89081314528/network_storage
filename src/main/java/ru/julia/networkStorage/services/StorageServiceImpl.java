package ru.julia.networkStorage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToTransferAndReceive;
import ru.julia.networkStorage.entities.Storage;
import ru.julia.networkStorage.repositories.StorageRepository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    public FilesToTransferAndReceive filesToTransferAndReceive(List<String> filesFromClient,
                                                               String clientName) {
        Map<String, Integer> mapFilesFromClient = new HashMap<>();
        for (String s : filesFromClient) {
            mapFilesFromClient.put(s, 0);
        }
        List<String> filesToTransfer = new ArrayList<>();// с сервера на клиент
        List<String> filesToReceive = new ArrayList<>();// с клиента на сервер
        Map<String, Integer> clientFilesFromServer = clientFilesFromServer(clientName);
        for (String s : clientFilesFromServer.keySet()) {
            if (mapFilesFromClient.containsKey(s)) {
            } else {
                filesToTransfer.add(s);
            }
        }
        for (String s : mapFilesFromClient.keySet()) {
            if (clientFilesFromServer.containsKey(s)) {
            } else {
                filesToReceive.add(s);
            }
        }
        FilesToTransferAndReceive filesToTransferAndReceive = new FilesToTransferAndReceive(filesToTransfer,
                filesToReceive);

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

    @Override // File (название файла, имя клиента)
    public String transfer(String clientName, String fileName) {
        return "Файлы переданы с сервера на клиент";
    }

    @Override // аргументом должен быть файл название файла и клиент
    // надо написать еще метод клиента который передает файл
    public String receive(String clientName, String fileName, MultipartFile file) { // фиксируем в БД
        Storage storage = new Storage(clientName, fileName);
        storageRepository.save(storage);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "Вы удачно загрузили " + fileName + " в " + fileName + "-uploaded !";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + fileName + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + fileName + " потому что файл пустой.";
        }
    }
}

