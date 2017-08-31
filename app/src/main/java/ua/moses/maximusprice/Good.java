package ua.moses.maximusprice;

public class Good {
    private int id = 0;
    private String name = "";
    private double price = 0;
    private String group = "";
    private String subGroup = "";
    private String description = "";
    private String availability = "+";

    void setId(int id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    void setPrice(double price) {
        this.price = price;
    }

    void setGroup(String group) {
        this.group = group;
    }

    void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setAvailability(String availability) {
        this.availability = availability;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    double getPrice() {
        return price;
    }

    String getGroup() {
        return group;
    }

    String getSubGroup() {
        return subGroup;
    }

    String getDescription() {
        return description;
    }

    String getAvailability() {
        return availability;
    }

    @Override
    public String toString() {
        return name + " - " + price + " " + availability;
    }

    String getInfo() {
        return "Цена: " + price + " Наличие: " + availability;
    }
}
