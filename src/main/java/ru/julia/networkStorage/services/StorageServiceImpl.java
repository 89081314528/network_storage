package ru.julia.networkStorage.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.julia.networkStorage.dto.FilesToSynchronized;
import ru.julia.networkStorage.entities.DeleteFile;
import ru.julia.networkStorage.entities.LastSyncDate;
import ru.julia.networkStorage.entities.Storage;
import ru.julia.networkStorage.entities.SyncLock;
import ru.julia.networkStorage.repositories.DeleteFileRepository;
import ru.julia.networkStorage.repositories.LastSyncDateRepository;
import ru.julia.networkStorage.repositories.StorageRepository;
import ru.julia.networkStorage.repositories.SyncLockRepository;

import java.io.*;
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
    private final LastSyncDateRepository lastSyncDateRepository;
    private final DeleteFileRepository deleteFileRepository;
    private final SyncLockRepository syncLockRepository;

    private final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Override
    public FilesToSynchronized filesToSynchronized(List<String> filesFromClient,
                                                   String clientName) {
        Map<String, Integer> mapFilesFromClient = new HashMap<>();
        for (String s : filesFromClient) {
            mapFilesFromClient.put(s, 0);
        }
        List<String> filesToTransferToClient = new ArrayList<>();
        List<String> filesToReceiveFromClient = new ArrayList<>();
        List<String> filesToDeleteFromServer = new ArrayList<>();
        List<String> filesToDeleteFromClient = new ArrayList<>();
        Map<String, Integer> clientFilesFromServer = clientFilesFromServer(clientName);

        // дата последней синхронизации
        List<LastSyncDate> lastSyncDateList = lastSyncDateRepository.findByClientName(clientName);
        LocalDateTime lastSyncDate = lastSyncDateList.get(0).getAddDate();

        for (String s : clientFilesFromServer.keySet()) {
            boolean fileNotFoundOnClient = !mapFilesFromClient.containsKey(s);
            if (fileNotFoundOnClient) {
                List<Storage> storageList = storageRepository.findByFileName(s);
                LocalDateTime addDate = storageList.get(0).getAddDate(); // пока файлы повторно не добавляем и он в списке один
                if (addDate.isAfter(lastSyncDate)) {
                    filesToTransferToClient.add(s);
                } else {
                    filesToDeleteFromServer.add(s);
                }
            }
        }
        for (String s : mapFilesFromClient.keySet()) {
            boolean fileNotFoundOnServer = !clientFilesFromServer.containsKey(s);
            if (fileNotFoundOnServer) {
                List<DeleteFile> deleteFileList = deleteFileRepository.findByFileName(s);
                if (deleteFileList.isEmpty()) {
                    filesToReceiveFromClient.add(s);
                } else {
                    LocalDateTime deleteDate = deleteFileList.get(0).getDeleteDate();
                    if (deleteDate.isAfter(lastSyncDate)) {
                        filesToDeleteFromClient.add(s);
                    } else {
                        filesToReceiveFromClient.add(s);
//                        deleteFileRepository.removeByFileName(s);
                    }
                }
            }
        }
        FilesToSynchronized filesToSynchronized = new FilesToSynchronized(filesToTransferToClient,
                filesToReceiveFromClient, filesToDeleteFromServer, filesToDeleteFromClient);
// сразу удаляем на сервере файлы из списка для удаления
        for (String s : filesToDeleteFromServer) {
            deleteFromServer(clientName, s);
            log.info("удален файл на сервере " + s);
        }
        return filesToSynchronized;
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
    // еще не написала этот метод
    public byte[] transferToClient(String clientName, String fileName) {
        String path = "C:/Users/julia/Programming/IdeaProjects/network_storage/" + clientName + "/" + fileName;
        File file = new File(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.transferTo(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String receiveFromClient(String clientName, String fileName, MultipartFile file) {
        // создаем директорию
        String dirPath = "C:/Users/julia/Programming/IdeaProjects/network_storage/" + clientName + "/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        storageRepository.save(new Storage(clientName, fileName, LocalDateTime.now()));
        deleteFileRepository.removeByFileName(fileName);
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(dirPath + fileName));
            stream.write(bytes);
            stream.close();
            log.info("Вы удачно загрузили " + fileName + " в " + clientName);
            return "Вы удачно загрузили " + fileName + " в " + clientName;
        } catch (Exception e) {
            log.info("При загрузке файла возникла ошибка");
            throw new RuntimeException(e);
        }
    }

    @Override // deleteFromClient только на клиенте, deleteFromServer только на сервере
    public String deleteFromServer(String clientName, String fileName) {
        File file = new File("C:/Users/julia/Programming/IdeaProjects/network_storage/" +
                clientName + "/" + fileName);
        file.delete();
        storageRepository.removeByFileName(fileName);
        deleteFileRepository.save(new DeleteFile(clientName, fileName, LocalDateTime.now()));
        return "файл удален с сервера";
    }

    @Override
    public void getLastSyncDate(String clientName) {
        lastSyncDateRepository.removeByClientName(clientName);
        lastSyncDateRepository.save(new LastSyncDate(clientName, LocalDateTime.now()));
    }

    @Override // нужно сделать транзакцию в явном виде!!!
    public void makeSyncLock(String clientName) {
        syncLockRepository.findByClientName(clientName).get(0).setLock(1);
    }

    @Override
    public void makeSyncOpen(String clientName) {
        syncLockRepository.findByClientName(clientName).get(0).setLock(0);
    }

    @Override
    public Integer getSyncLock(String clientName) {
        if (syncLockRepository.findByClientName(clientName).size() == 0) {
            syncLockRepository.save(new SyncLock(clientName, 0));
            return 0;
        } else {
            if (syncLockRepository.findByClientName(clientName).get(0).getLock() == 0) {
                return 0;
            }
            else return 1;
        }
    }
}

