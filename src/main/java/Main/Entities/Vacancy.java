package Main.Entities;

import Main.Tools.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "types", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "type")
    private List<String> type;

    private String source;

    @Column(columnDefinition="TIMESTAMP")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime time;

    @Transient
    private String displayDate;

    @Transient
    private String displayType;

    public Vacancy() {
    }

    public Vacancy(LocalDateTime time, String title, String url, String company, String location, List<String> type, String source) {
        this.time = time;
        this.title = title;
        this.url = url;
        this.company = company;
        this.location = location;
        this.type = type;
        this.source = source;
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

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDisplayDate() {
        return time.format(ofPattern("d MMM y HH:mm", new Locale("en", "EN")));
    }

    public String getDisplayType() {
        return type.toString().replaceAll("[\\[\\]]","");
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    @Override
    public String toString() {
        return title + url ;
    }
}
