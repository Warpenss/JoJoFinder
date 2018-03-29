package Main.Entities;

public class Company {
    public Company(String companyName, String searchUrl, String urlSelector, String titleSelector, String citySelector,
                   String typeSelector, String paginationType, String paginationSelector) {
        this.companyName = companyName;
        this.searchUrl = searchUrl;
        this.urlSelector = urlSelector;
        this.titleSelector = titleSelector;
        this.citySelector = citySelector;
        this.typeSelector = typeSelector;
        this.paginationType = paginationType;
        this.paginationSelector = paginationSelector;
    }

    private String companyName;
    private String searchUrl;
    private String urlSelector;
    private String titleSelector;
    private String citySelector;
    private String typeSelector;
    private String paginationType;
    private String paginationSelector;

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

    public String getUrlSelector() {
        return urlSelector;
    }

    public void setUrlSelector(String urlSelector) {
        this.urlSelector = urlSelector;
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

    public String getPaginationType() {
        return paginationType;
    }

    public void setPaginationType(String paginationType) {
        this.paginationType = paginationType;
    }

    public String getPaginationSelector() {
        return paginationSelector;
    }

    public void setPaginationSelector(String paginationSelector) {
        this.paginationSelector = paginationSelector;
    }
}
