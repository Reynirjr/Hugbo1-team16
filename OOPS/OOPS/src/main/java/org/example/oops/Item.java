package org.example.oops;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @Column(name = "price_isk")
    private int priceIsk;

    private boolean available;

    @Column
    private String tags;

    @Column(name = "image_data", columnDefinition = "BYTEA", nullable = true)
    private byte[] imageData = new byte[0];


    @ManyToOne
    @JoinColumn(name = "section_id")
    @JsonBackReference
    private MenuSection section;

    public Item() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriceIsk() {
        return priceIsk;
    }

    public void setPriceIsk(int priceIsk) {
        this.priceIsk = priceIsk;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public MenuSection getSection() {
        return section;
    }

    public void setSection(MenuSection section) {
        this.section = section;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
