package Main.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

@Entity
public class JobSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String url;

    private String title;

    private String company;

    private String city;

    private String language;

    private LocalDateTime time;

    @Transient
    private String displayDate;

    public JobSite() {
    }

    public JobSite(LocalDateTime time, String title, String url, String company, String city, String language) {
        this.time = time;
        this.title = title;
        this.url = url;
        this.company = company;
        this.city = city;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDisplayDate() {
        return time.format(ofPattern("d MMM y HH:mm", new Locale("en", "EN")));
    }

    @Override
    public String toString() {
        return title + url ;
    }
}
