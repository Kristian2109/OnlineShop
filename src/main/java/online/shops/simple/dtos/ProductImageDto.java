package online.shops.simple.dtos;

public class ProductImageDto {
    private String url;
    private String filename;
    private Integer position;

    public ProductImageDto(String url, String filename, Integer position) {
        this.url = url;
        this.filename = filename;
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
} 