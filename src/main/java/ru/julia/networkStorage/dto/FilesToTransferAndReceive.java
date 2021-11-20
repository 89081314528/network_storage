package ru.julia.networkStorage.dto;

import java.util.List;

public class FilesToTransferAndReceive {
    List<String> filesToTransfer;
    List<String> filesToReceive;
    String clientName;

    public FilesToTransferAndReceive(List<String> filesToTransfer, List<String> filesToReceive, String clientName) {
        this.filesToTransfer = filesToTransfer;
        this.filesToReceive = filesToReceive;
        this.clientName = clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public List<String> getFilesToTransfer() {
        return filesToTransfer;
    }

    public void setFilesToTransfer(List<String> filesToTransfer) {
        this.filesToTransfer = filesToTransfer;
    }

    public List<String> getFilesToReceive() {
        return filesToReceive;
    }

    public void setFilesToReceive(List<String> filesToReceive) {
        this.filesToReceive = filesToReceive;
    }


}
