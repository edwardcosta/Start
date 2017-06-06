package com.android.usuario.start.DataObject;


import java.io.Serializable;
import java.util.Calendar;

public class Project implements Serializable{

    private String name = "Teste";
    private String description = "Projeto de teste.";
    private String author = "Anonymous";
    private Calendar startDate = Calendar.getInstance();
    private int duracao = 0;

    public Project() {}

    public Project(String name, String description, String author, Calendar startDate, int duracao) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.startDate = startDate;
        this.duracao = duracao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }
}
