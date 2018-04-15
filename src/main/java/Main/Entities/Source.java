package Main.Entities;

public class Source {
    public Source(String sourceName, String companyNameSelector, String searchUrl, String urlSelector, String titleSelector, String locationSelector,
                  String typeSelector, String paginationType, String paginationSelector) {
        this.sourceName = sourceName;
        this.companyNameSelector = companyNameSelector;
        this.searchUrl = searchUrl;
        this.urlSelector = urlSelector;
        this.titleSelector = titleSelector;
        this.locationSelector = locationSelector;
        this.typeSelector = typeSelector;
        this.paginationType = paginationType;
        this.paginationSelector = paginationSelector;
    }

    private String sourceName;
    private String companyNameSelector;
    private String searchUrl;
    private String urlSelector;
    private String titleSelector;
    private String locationSelector;
    private String typeSelector;
    private String paginationType;
    private String paginationSelector;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getCompanyNameSelector() {
        return companyNameSelector;
    }

    public void setCompanyNameSelector(String companyNameSelector) {
        this.companyNameSelector = companyNameSelector;
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

    public String getLocationSelector() {
        return locationSelector;
    }

    public void setLocationSelector(String locationSelector) {
        this.locationSelector = locationSelector;
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
