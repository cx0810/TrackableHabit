package com.example.trackablehabit;

class FeedModel {
    private int id, likes, comments;
    private String message, time;

    FeedModel(int id, int likes, int comments, String message, String time) {
        this.id = id;
        this.likes = likes;
        this.comments = comments;
        this.message = message;
        this.time = time;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    int getLikes() {
        return likes;
    }

    void setLikes(int likes) {
        this.likes = likes;
    }

    int getComments() {
        return comments;
    }

    void setComments(int comments) {
        this.comments = comments;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String msg) {
        this.message = msg;
    }

    String getTime() {
        return time;
    }

    void setTime(String time) {
        this.time = time;
    }
}
