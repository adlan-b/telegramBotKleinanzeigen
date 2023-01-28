package modul;

public class Product {

    private Long id;
    private String name;
    private String price;
    private String link;
    private String img;

    public Product() {
    }

    public Product(Long id, String name, String price, String link, String img) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.link = link;
        this.img = img;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "modul.ProductClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
