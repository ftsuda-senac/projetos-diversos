package br.senac.tads.dsw.exemplospringsecurity.sessao;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SessaoInfo implements Serializable {

    private LocalDateTime dataHoraInclusao;

    private String userAgent;

    private String ipAddress;
    
    public SessaoInfo() {
        
    }

    public SessaoInfo(LocalDateTime dataHoraInclusao, String userAgent, String ipAddress) {
        this.dataHoraInclusao = dataHoraInclusao;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
