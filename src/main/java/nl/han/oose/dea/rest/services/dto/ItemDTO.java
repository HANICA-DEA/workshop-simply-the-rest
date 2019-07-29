package nl.han.oose.dea.rest.services.dto;

public class ItemDTO {
    private String name;
    private String[] category;
    private String title;

    public ItemDTO() {
    }

    public ItemDTO(String name, String[] category, String title) {
        this.name = name;
        this.category = category;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
