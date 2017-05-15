package com.android.usuario.start.View.Group;


class ProjectCard {

    private String name = "Teste";
    private String description = "Projeto de teste.";
    private String author = "Anonymous";

    public ProjectCard(String name, String description, String author) {
        this.name = name;
        this.description = description;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
