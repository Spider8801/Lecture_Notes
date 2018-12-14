package com.pythonanywhere.cozinfinitybehind.lecture_notes.models;

import java.util.HashMap;
import java.util.LinkedList;

public class PDFModel {

    String name;
    int materialId;
    HashMap<String, String> author = new HashMap<String, String>();
    LinkedList<Page> pages = new LinkedList<Page>();

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

    public HashMap<String, String> getAuthor() {
        return author;
    }

    public void setAuthor(HashMap<String, String> author) {
        this.author = author;
    }

    public LinkedList<Page> getPages() {
        return pages;
    }

    public void setPages(LinkedList<Page> pages) {
        this.pages = pages;
    }
}


class Page{
    int page;
    String url;
}
