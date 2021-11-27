package ru.julia.networkStorage.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.internal.expression.function.CurrentTimestampFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToDeleteFromServerAndClient;
import ru.julia.networkStorage.dto.FilesToTransferAndReceive;
import ru.julia.networkStorage.entities.Storage;
import ru.julia.networkStorage.repositories.StorageRepository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<String> filesToTransferToClient = new ArrayList<>();// с сервера на клиент
        List<String> filesToReceiveFromClient = new ArrayList<>();// с клиента на сервер
        Map<String, Integer> clientFilesFromServer = clientFilesFromServer(clientName);
        for (String s : clientFilesFromServer.keySet()) {
            if (mapFilesFromClient.containsKey(s)) {
            } else {
                filesToTransferToClient.add(s);
            }
        }
        for (String s : mapFilesFromClient.keySet()) {
            if (clientFilesFromServer.containsKey(s)) {
            } else {
                filesToReceiveFromClient.add(s);
            }
        }
        FilesToTransferAndReceive filesToTransferAndReceive = new FilesToTransferAndReceive(filesToTransferToClient,
                filesToReceiveFromClient);

        return filesToTransferAndReceive;
    }

    @Override
    public FilesToDeleteFromServerAndClient filesToDeleteFromServerAndClient(List<String> filesFromClient, String clientName) {
        Map<String, Integer> mapFilesFromClient = new HashMap<>();
        for (String s : filesFromClient) {
            mapFilesFromClient.put(s, 0);
        }
        List<String> filesToDeleteFromServer = new ArrayList<>();// с сервера на клиент
        List<String> filesToDeleteFromClient = new ArrayList<>();// с клиента на сервер
        Map<String, Integer> clientFilesFromServer = clientFilesFromServer(clientName);
        for (String s : clientFilesFromServer.keySet()) {
            if (mapFilesFromClient.containsKey(s)) {
            } else {
                filesToDeleteFromServer.add(s);
            }
        }
        for (String s : mapFilesFromClient.keySet()) {
            if (clientFilesFromServer.containsKey(s)) {
            } else {
                filesToDeleteFromClient.add(s);
            }
        }
        FilesToDeleteFromServerAndClient filesToDeleteFromServerAndClient = new FilesToDeleteFromServerAndClient(
                filesToDeleteFromServer, filesToDeleteFromClient);

        return filesToDeleteFromServerAndClient;
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

    @Override // File (название файла, имя клиента). Сервер отвечает на запрос клиента о передаче файла
    public String transferToClient(String clientName, String fileName) {
        return "Файлы переданы с сервера на клиент";
    }

    @Override
    public String receiveFromClient(String clientName, String fileName, MultipartFile file) {
        String dirPath = "C:/Users/julia/Programming/IdeaProjects/network_storage/" + clientName + "/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        storageRepository.save(new Storage(clientName, fileName, LocalDateTime.now()));
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(dirPath + fileName));
                stream.write(bytes);
                stream.close();
                return "Вы удачно загрузили " + fileName + " в " + fileName;
            } catch (Exception e) {
                return "Вам не удалось загрузить " + fileName + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + fileName + " потому что файл пустой.";
        }
    }

    @Override
    public String deleteFromServer(String clientName, String fileName) {
        File file = new File("C:/Users/julia/Programming/IdeaProjects/network_storage/" +
                clientName + "/" + fileName);
        file.delete();
        storageRepository.removeByFileName(fileName);
        return "файл удален с сервера";
    }

}

