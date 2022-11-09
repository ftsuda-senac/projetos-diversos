package br.senac.tads.dsw.exemplosspring.layout;

public class FotoInfo {

    private String title;

    private String fileName;

    public FotoInfo() {

    }

    public FotoInfo(String title, String fileName) {
        this.title = title;
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getUrl() {
        return "/img/fotos/" + fileName;
    }

}
