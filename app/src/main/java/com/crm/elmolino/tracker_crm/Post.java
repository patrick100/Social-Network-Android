package com.crm.elmolino.tracker_crm;

public class Post {


    private String id,image,description;
    private Long num_likes;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Post() {

    }

    public Post(String image, String description) {
        this.image = image;
        this.description = description;
    }

    public Long getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(Long num_likes) {
        this.num_likes = num_likes;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
