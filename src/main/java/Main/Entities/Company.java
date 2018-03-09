package Main.Entities;

public class Company {
    public Company(String companyName, String searchUrl, String titleSelector, String citySelector, String typeSelector) {
        this.companyName = companyName;
        this.searchUrl = searchUrl;
        this.titleSelector = titleSelector;
        this.citySelector = citySelector;
        this.typeSelector = typeSelector;
    }

    private String companyName;
    private String searchUrl;
    private String titleSelector;
    private String citySelector;
    private String typeSelector;


}
