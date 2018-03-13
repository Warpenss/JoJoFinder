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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getTitleSelector() {
        return titleSelector;
    }

    public void setTitleSelector(String titleSelector) {
        this.titleSelector = titleSelector;
    }

    public String getCitySelector() {
        return citySelector;
    }

    public void setCitySelector(String citySelector) {
        this.citySelector = citySelector;
    }

    public String getTypeSelector() {
        return typeSelector;
    }

    public void setTypeSelector(String typeSelector) {
        this.typeSelector = typeSelector;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", searchUrl='" + searchUrl + '\'' +
                ", titleSelector='" + titleSelector + '\'' +
                ", citySelector='" + citySelector + '\'' +
                ", typeSelector='" + typeSelector + '\'' +
                '}';
    }
}
