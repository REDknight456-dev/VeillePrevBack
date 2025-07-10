package com.cww.veille_springboot.DTO;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.util.Date;

@Getter
public class TermeJuridiqueDTO {
    // Getters and setters
    private Integer id;
    private Integer lawId;
    private Integer themeId;
    private int serialNumber;
    private String type;
    private String source;
    private String name;
    private String resume;
    private String title;
    private byte[] file;
    private Date termeDate;

    public void setId(Integer id) { this.id = id; }

    public void setLawId(Integer lawId) { this.lawId = lawId; }

    public void setThemeId(Integer themeId) { this.themeId = themeId; }

    public void setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; }


    @JsonSetter("type")
    public void setType(String type) {
        // Accept any case from user, store as uppercase for consistency
        this.type = (type != null) ? type.trim().toUpperCase() : null;
    }

    public void setSource(String source) { this.source = source; }

    public void setName(String name) { this.name = name; }

    public void setResume(String resume) { this.resume = resume; }

    public void setTitle(String title) { this.title = title; }

    public void setFile(byte[] file) { this.file = file; }

    public void setTermeDate(Date termeDate) { this.termeDate = termeDate; }


}
