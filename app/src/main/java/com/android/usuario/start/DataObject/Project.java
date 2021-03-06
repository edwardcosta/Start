package com.android.usuario.start.DataObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Project implements Serializable{

    private String id;
    private String name = "Teste";
    private String description = "Projeto de teste.";
    private String author = "Anonymous";
    private List<String> hashtags = new ArrayList<>();
    private int startDay;
    private int startMonth;
    private int startYear;
    private int duration = 0;
    private int difficulty;
    private int nHackers;
    private int nHustlers;
    private int nHippies;
    private int maxHackers;
    private int maxHustlers;
    private int maxHippies;

    private List<String> wantToParticipate = new ArrayList<>();
    private List<String> participating = new ArrayList<>();

    private List<String> images = Arrays.asList("","","","","","");

    public Project() {}

    public Project(String name, String description, String author, int day, int month, int year, int duration, int difficulty, ArrayList<String> hashtags, int nHackers, int nHustlers, int nHippies) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.startDay = day;
        this.startMonth = month;
        this.startYear = year;
        this.duration = duration;
        this.hashtags = hashtags;
        this.difficulty = difficulty;
        this.nHackers = nHackers;
        this.nHippies = nHippies;
        this.nHustlers = nHustlers;
    }

    public void addWantToParticipateOsProject(String userId){
        wantToParticipate.add(userId);
    }

    public void addParticipants(String userId){
        participating.add(userId);
    }

    public boolean removeWantToParticipateUser(String userId){
        return wantToParticipate.remove(userId);
    }

    public boolean removeParticipant(String userId){
        return participating.remove(userId);
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public int getnHackers() {
        return nHackers;
    }

    public void setnHackers(int nHackers) {
        this.nHackers = nHackers;
    }

    public int getnHippies() {
        return nHippies;
    }

    public void setnHippies(int nHippies) {
        this.nHippies = nHippies;
    }

    public int getnHustlers() {
        return nHustlers;
    }

    public void setnHustlers(int nHustlers) {
        this.nHustlers = nHustlers;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getMaxHackers() {
        return maxHackers;
    }

    public void setMaxHackers(int maxHackers) {
        this.maxHackers = maxHackers;
    }

    public int getMaxHustlers() {
        return maxHustlers;
    }

    public void setMaxHustlers(int maxHustlers) {
        this.maxHustlers = maxHustlers;
    }

    public int getMaxHippies() {
        return maxHippies;
    }

    public void setMaxHippies(int maxHippies) {
        this.maxHippies = maxHippies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getWantToParticipate() {
        return wantToParticipate;
    }

    public List<String> getParticipating() {
        return participating;
    }
}
