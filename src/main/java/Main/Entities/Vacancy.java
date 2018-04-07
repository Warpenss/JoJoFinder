package Main.Entities;

import Main.Tools.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

@Entity
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String url;

    private String title;

    private String company;

    private String location;

    private String type;

    @Column(columnDefinition="TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime time;

    @Transient
    private String displayDate;

    public Vacancy() {
    }

    public Vacancy(LocalDateTime time, String title, String url, String company, String location, String type) {
        this.time = time;
        this.title = title;
        this.url = url;
        this.company = company;
        this.location = location;
        this.type = type;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayDate() {
        return time.format(ofPattern("d MMM y HH:mm", new Locale("en", "EN")));
    }

    @Override
    public String toString() {
        return title + url ;
    }
}
