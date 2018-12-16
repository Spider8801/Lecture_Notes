package com.pythonanywhere.cozinfinitybehind.lecture_notes.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class PDFModel implements Serializable {

    String name;
    int materialId;
    String author_name;
    String author_institute;
    HashMap<Integer,String> pages;

    public PDFModel(String name, int materialId, String author_name, String author_institute, HashMap<Integer, String> pages) {
        this.name = name;
        this.materialId = materialId;
        this.author_name = author_name;
        this.author_institute = author_institute;
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_institute() {
        return author_institute;
    }

    public void setAuthor_institute(String author_institute) {
        this.author_institute = author_institute;
    }

    public HashMap<Integer, String> getPages() {
        return pages;
    }

    public void setPages(HashMap<Integer, String> pages) {
        this.pages = pages;
    }
}