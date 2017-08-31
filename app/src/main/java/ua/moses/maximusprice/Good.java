package ua.moses.maximusprice;

public class Good {
    private int id = 0;
    private String name = "";
    private double price = 0;
    private String group = "";
    private String subGroup = "";
    private String description = "";
    private String availability = "+";

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getGroup() {
        return group;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailability() {
        return availability;
    }

    @Override
    public String toString() {
        return name + " - " + price + " " + availability;
    }
}
