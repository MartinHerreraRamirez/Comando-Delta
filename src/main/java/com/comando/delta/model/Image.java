package com.comando.delta.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;


@Entity
public class Image {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    private String mime;
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    public Image() {
    }

    public Image(String mime, String name, byte[] content) {
        this.mime = mime;
        this.name = name;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
