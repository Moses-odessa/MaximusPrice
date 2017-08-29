package ua.moses.maximusprice.model;

public class Good {
    private int id = 0;
    private String name = "";
    private double price = 0;
    private String group = "";
    private String subGroup = "";
    private String description = "";
    private Availability availability = Availability.OutOfStock;

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

    public void setAvailability(Availability availability) {
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

    public Availability getAvailability() {
        return availability;
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }
}
