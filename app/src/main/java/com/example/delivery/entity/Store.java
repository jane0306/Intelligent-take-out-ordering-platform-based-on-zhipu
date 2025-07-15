package com.example.delivery.entity;

import java.io.Serializable;

public class Store implements Serializable {

    private Integer id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺图片
     */
    private String img;

    /**
     * 店铺描述
     */
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
