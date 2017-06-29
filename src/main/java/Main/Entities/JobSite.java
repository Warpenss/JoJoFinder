package Main.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobSite {
    private String title;
    @Id
    private String url;

    public JobSite() {
    }

    public JobSite(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return title + url ;
    }
}
