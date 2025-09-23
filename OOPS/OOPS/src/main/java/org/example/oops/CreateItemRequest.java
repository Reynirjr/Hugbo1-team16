package org.example.oops;

public class CreateItemRequest {
    private String name;
    private String description;
    private int priceIsk;
    private boolean available;
    private String tags;
    private Integer sectionId; // so we know which section this belongs to

    // getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPriceIsk() { return priceIsk; }
    public void setPriceIsk(int priceIsk) { this.priceIsk = priceIsk; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Integer getSectionId() { return sectionId; }
    public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }
}
