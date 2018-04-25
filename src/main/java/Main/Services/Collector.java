package Main.Services;

import Main.Entities.Source;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.Browser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geonames.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class Collector {
    private final VacancyRepository vacancyRepository;
    @Autowired
    public Collector(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public void collect(ArrayList<Source> sources) {
//    public ArrayList<Vacancy> collect(ArrayList<Source> sources) {
//        ArrayList<Vacancy> vacanciesReady = new ArrayList<>();
        for (Source source : sources) {
            Browser.initiateClient();

            System.out.println(source.getSourceName());
            List<HtmlElement> vacancies = new ArrayList<>();
            HtmlPage page = Browser.getPage(source.getSearchUrl());

            if (source.getPaginationType().equals("LOAD")) {
                for (int i = 0; i < 50; i++) {
                    try {
                        page = paginationClick(page, source.getPaginationSelector());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("While LOAD click");
                        e.printStackTrace();
                    }
                }
                vacancies = page.getByXPath(source.getTitleSelector());
            } else if (source.getPaginationType().equals("PAGE")) {
                // You must be logged in to see pages on djinni
                if (source.getSourceName().equals("djinni")) {
                    for (int i = 2; i < 10; i++) {
                        vacancies.addAll(page.getByXPath(source.getTitleSelector()));
                        page = Browser.getPage("https://djinni.co/jobs/?lang=en&page=" + i);
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        vacancies.addAll(page.getByXPath(source.getTitleSelector()));
                        try {
                            page = paginationClick(page, source.getPaginationSelector());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            System.out.println("While PAGE click");
                            e.printStackTrace();
                        }
                    }
                }
            }

            for (HtmlElement htmlElement : vacancies) {
                try {
                    LocalDateTime time;
                    String title;
                    String url;
                    String companyName;
                    String location;
                    String type;

                    url = page.getFullyQualifiedUrl(((HtmlElement) htmlElement.getByXPath(source.getUrlSelector())
                            .get(0)).getAttribute("href")).toString();
                    System.out.println(url);


                    if (vacancyRepository.findByUrl(url).size() == 0) {
                        time = LocalDateTime.now();
                        System.out.println(time);

                        if (source.getSourceName().equalsIgnoreCase("LUXOFT")){
                            title = htmlElement.getTextContent().replaceAll("Hot", "").trim();
                        } else {
                            title = htmlElement.getTextContent().trim();
                        }
                        if (containsNonEnglish(title)) {
                            String language = detectLanguage(title);
                            title = translateTitle(title, language);
                        }

                        System.out.println(title);

                        if (source.getCompanyNameSelector().equals("FROM_SOURCE_NAME")) {
                            companyName = source.getSourceName();
                        } else {
                            companyName = ((HtmlElement) htmlElement.getByXPath(source.getCompanyNameSelector()).get(0)).getTextContent();
                            if (source.getSourceName().equals("djinni")) {
                                if (companyName.contains(" at ")) {
                                    companyName = StringUtils.substringAfterLast(companyName, " at ");
                                    companyName = StringUtils.substringBefore(companyName, "\u00a0");
                                } else if (companyName.contains(" в ")) {
                                    companyName = StringUtils.substringAfterLast(companyName, " в ");
                                    companyName = StringUtils.substringBefore(companyName, "\u00a0");
                                } else {
                                    companyName = source.getSourceName();
                                }
                            }
                        }
                        companyName = companyName.replaceAll("[\u00A0\u2007\u202F\u200B]", " ").trim();
                        companyName = companyName.replaceAll("\"", "").trim();
                        System.out.println(companyName);

                        location = "Undefined";
                        List<HtmlElement> list = htmlElement.getByXPath(source.getLocationSelector());
                        if (!list.isEmpty()) {
                            location = getLocation(source, list);
                        }
                        System.out.println(location);

                        if (source.getTypeSelector().equals("FROM_TITLE")) {
                            type = title;
                        } else {
                            type = ((HtmlElement) htmlElement.getByXPath(source.getTypeSelector()).get(0)).getTextContent();
                        }
                        type = plainType(type);
                        System.out.println(type);

    //                            vacanciesReady.add(new Vacancy(time, title, url, companyName, location, type));
    //
                        vacancyRepository.save(new Vacancy(time, title, url, companyName, location, type));
                    } else {
                        System.out.println("Vacancy is already saved: " + url);
                    }
                } catch (Exception e) {
                    System.out.println("ERROR! NOT SAVED");
                    e.printStackTrace();
                }
            }
            Browser.closeClient();
        }
//        return vacanciesReady;
    }

    private boolean containsNonEnglish(String text) {
        return !(text.matches("[\\p{Punct}\\p{Space}\\p{Digit}a-zA-Z –]+$"));
    }

    private final String apiKey = "";

    private String detectLanguage(String text) throws ParseException, IOException {
        text = text.replaceAll("[\u00A0\u2007\u202F\u200B]", " ");
        String nonEnglishPart = text.replaceAll("[\\p{Punct}\\p{Digit}a-zA-Z]", "");
        System.out.println("Non english symbols: " + nonEnglishPart);
        String detectApiUrl = "https://translate.yandex.net/api/v1.5/tr.json/detect?";
        detectApiUrl += "&key=" + apiKey;
        nonEnglishPart = URLEncoder.encode(nonEnglishPart, "UTF-8");
        detectApiUrl += "&text=" + nonEnglishPart;
        String json = IOUtils.toString(new URL(detectApiUrl), "UTF-8");
        JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(json);
        String language = jsonObject.get("lang").toString();
        if (language.isEmpty()) {
            return "en-";
        } else {
            return language + "-";
        }
    }

    private String translateTitle(String text, String language) throws IOException, ParseException {
        text = text.replaceAll("[()]", "");
        text = text.replaceAll("—", "-");
        String translateApiUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        translateApiUrl += "&key=" + apiKey;
        translateApiUrl += "&lang=" + language + "en";
        String titleEncoded = URLEncoder.encode(text, "UTF-8");
        translateApiUrl += "&text=" + titleEncoded;
        String json = IOUtils.toString(new URL(translateApiUrl), "UTF-8");
        JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(json);
        text = ((JSONArray) jsonObject.get("text")).get(0).toString();
        return text;
    }

    private HtmlPage paginationClick(HtmlPage page, String paginationSelector) throws InterruptedException, IOException {
        List<HtmlElement> list = page.getByXPath(paginationSelector);

        if (!list.isEmpty()) {
            System.out.println("Before click");
            page = list.get(0).click();
            System.out.println("After click");
        } else {
            System.out.println("Empty");
        }
        Thread.sleep(1000);
        return page;
    }

    private String getLocation(Source source, List<HtmlElement> list) throws Exception {
        String location;
        if (source.getSourceName().equals("djinni")) {
            location = StringUtils.substringAfterLast((list.get(0)).getTextContent(), "\u00a0");
            location = StringUtils.substringBefore(location, ",").trim();
            if (location.contains(".")) {
                location = StringUtils.substringAfter(location, ".").trim();
            }
            location = StringUtils.substringBefore(location, "\\p{Space}");
        } else if (source.getSourceName().equals("WORK.ua")) {
            location = StringUtils.substringBefore(list.get(0).getTextContent(), "\u00B7").trim();
        } else {
            location = StringUtils.substringBefore((list.get(0)).getTextContent(), ",").trim();
            if (location.contains(".")) {
                location = StringUtils.substringAfter(location, ".").trim();
            }
            location = StringUtils.substringBefore(location, "\\p{Space}");
        }
        if (vacancyRepository.findByLocation(location).size() == 0) {
            System.out.println("Raw location = " + location);
            location = plainCity(location);
        }

        return location;
    }

    private String plainCity(String rawCity) throws Exception {
        rawCity = rawCity.replaceAll("[\u00A0\u2007\u202F\u200B]", " ").trim();
        if (rawCity.isEmpty()) {
            return "Undefined";
        } else {
            String plainCity = rawCity;
            WebService.setUserName("warpenss"); // add your username here
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setName(rawCity);
            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            List<Toponym> searchResultToponyms = searchResult.getToponyms();
            if (!searchResultToponyms.isEmpty()) {
                plainCity = searchResultToponyms.get(0).getName();
            }
            return plainCity;
        }
    }

    private String plainType(String rawType) {
        String plainType = "Other";

        if (StringUtils.containsIgnoreCase(rawType, "JavaScript") ||
                StringUtils.containsIgnoreCase(rawType, "JS ")) {
            plainType = "JavaScript";
        } else if (StringUtils.containsIgnoreCase(rawType, "Full Stack") ||
                StringUtils.containsIgnoreCase(rawType, "Fullstack") ||
                StringUtils.containsIgnoreCase(rawType, "Full-stack")) {
            plainType = "Full Stack";
        } else if (StringUtils.containsIgnoreCase(rawType, "Java") ||
                StringUtils.containsIgnoreCase(rawType, "J2EE")) {
            plainType = "Java";
        } else if (StringUtils.containsIgnoreCase(rawType, "C++")) {
            plainType = "C++";
        } else if (StringUtils.containsIgnoreCase(rawType, "C#")) {
            plainType = "C#";
        } else if (StringUtils.containsIgnoreCase(rawType, ".NET")) {
            plainType = ".NET";
        } else if (StringUtils.containsIgnoreCase(rawType, "Python")) {
            plainType = "Python";
        } else if (StringUtils.containsIgnoreCase(rawType, "UI ")) {
            plainType = "UI";
        } else if (StringUtils.containsIgnoreCase(rawType, "iOS")) {
            plainType = "iOS";
        } else if (StringUtils.containsIgnoreCase(rawType, "Manager") ||
                StringUtils.containsIgnoreCase(rawType, "PM ")) {
            plainType = "Manager";
        } else if (StringUtils.containsIgnoreCase(rawType, "Test")) {
            plainType = "Testing";
        } else if (StringUtils.containsIgnoreCase(rawType, "Consultant")) {
            plainType = "Consultant";
        } else if (StringUtils.containsIgnoreCase(rawType, "Assistant")) {
            plainType = "Assistant";
        } else if (StringUtils.containsIgnoreCase(rawType, "SQL")) {
            plainType = "SQL ";
        } else if (StringUtils.containsIgnoreCase(rawType, "Analyst")) {
            plainType = "Analyst ";
        } else if (StringUtils.containsIgnoreCase(rawType, "Big Data")) {
            plainType = "Big Data";
        } else if (StringUtils.containsIgnoreCase(rawType, "Recruit")) {
            plainType = "Recruiter";
        } else if (StringUtils.containsIgnoreCase(rawType, "Solution")) {
            plainType = "Solution Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Director")) {
            plainType = "Director";
        } else if (StringUtils.containsIgnoreCase(rawType, "Designer")) {
            plainType = "Designer";
        } else if (StringUtils.containsIgnoreCase(rawType, "DevOps") ||
                StringUtils.containsIgnoreCase(rawType, "Development Operations")) {
            plainType = "DevOps";
        } else if (StringUtils.containsIgnoreCase(rawType, "Front-end") ||
                StringUtils.containsIgnoreCase(rawType, "Front end") ||
                StringUtils.containsIgnoreCase(rawType, "Frontend")) {
            plainType = "Front-end";
        } else if (StringUtils.containsIgnoreCase(rawType, "Automation")) {
            plainType = "Automation";
        } else if (StringUtils.containsIgnoreCase(rawType, "Accountant")) {
            plainType = "Accountant";
        } else if (StringUtils.containsIgnoreCase(rawType, "HR") ||
                StringUtils.containsIgnoreCase(rawType, "Human Resources")) {
            plainType = "HR";
        } else if (StringUtils.containsIgnoreCase(rawType, "Security")) {
            plainType = "Security";
        } else if (StringUtils.containsIgnoreCase(rawType, "QA") ||
                StringUtils.containsIgnoreCase(rawType, "Quality Assurance")) {
            plainType = "QA";
        } else if (StringUtils.containsIgnoreCase(rawType, "Android")) {
            plainType = "Android";
        } else if (StringUtils.containsIgnoreCase(rawType, "PHP")) {
            plainType = "PHP";
        } else if (StringUtils.containsIgnoreCase(rawType, "Sales")) {
            plainType = "Sales";
        } else if (StringUtils.containsIgnoreCase(rawType, "Angular")) {
            plainType = "Angular";
        } else if (StringUtils.containsIgnoreCase(rawType, "Node.js") ||
                StringUtils.containsIgnoreCase(rawType, "NodeJS") ||
                StringUtils.containsIgnoreCase(rawType, "Node. JS")) {
            plainType = "Node.js";
        } else if (StringUtils.containsIgnoreCase(rawType, " C ")) {
            plainType = "C";
        } else if (StringUtils.containsIgnoreCase(rawType, "Scala")) {
            plainType = "Scala";
        } else if (StringUtils.containsIgnoreCase(rawType, "Ruby") ||
                StringUtils.containsIgnoreCase(rawType, "RoR")) {
            plainType = "Ruby";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Scientist")) {
            plainType = "Data Scientist";
        } else if (StringUtils.containsIgnoreCase(rawType, "TypeScript")) {
            plainType = "TypeScript";
        } else if (StringUtils.containsIgnoreCase(rawType, "React")) {
            plainType = "React";
        } else if (StringUtils.containsIgnoreCase(rawType, "Unity")) {
            plainType = "Unity3D";
        } else if (StringUtils.containsIgnoreCase(rawType, "1С") ||
                StringUtils.containsIgnoreCase(rawType, "1C") ||
                StringUtils.containsIgnoreCase(rawType, "1 С") ||
                StringUtils.containsIgnoreCase(rawType, "1S")) {
            plainType = "1C";
        } else if (StringUtils.containsIgnoreCase(rawType, "Blockchain")) {
            plainType = "Blockchain";
        } else if (StringUtils.containsIgnoreCase(rawType, "Back-end") ||
                StringUtils.containsIgnoreCase(rawType, "Back end") ||
                StringUtils.containsIgnoreCase(rawType, "Backend")) {
            plainType = "Back-end";
        } else if (StringUtils.containsIgnoreCase(rawType, "Service Desk") ||
                StringUtils.containsIgnoreCase(rawType, "HelpDesk")) {
            plainType = "HelpDesk";
        } else if (StringUtils.containsIgnoreCase(rawType, "Go ") ||
                StringUtils.containsIgnoreCase(rawType, "Golang")) {
            plainType = "Go";
        } else if (StringUtils.containsIgnoreCase(rawType, "R&D")) {
            plainType = "R&D";
        } else if (StringUtils.containsIgnoreCase(rawType, "Administrator")) {
            plainType = "Administrator";
        } else if (StringUtils.containsIgnoreCase(rawType, "ELT")) {
            plainType = "ELT";
        } else if (StringUtils.containsIgnoreCase(rawType, "Coordinator")) {
            plainType = "Coordinator";
        } else if (StringUtils.containsIgnoreCase(rawType, "Delphi")) {
            plainType = "Delphi";
        } else if (StringUtils.containsIgnoreCase(rawType, "Splunk")) {
            plainType = "Splunk";
        } else if (StringUtils.containsIgnoreCase(rawType, "Tableau")) {
            plainType = "Tableau";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Warehouse") ||
                StringUtils.containsIgnoreCase(rawType, "DWH")) {
            plainType = "Data Warehouse";
        } else if (StringUtils.containsIgnoreCase(rawType, "Teacher")) {
            plainType = "Teacher";
        } else if (StringUtils.containsIgnoreCase(rawType, "Training and Development")) {
            plainType = "Training and Development";
        } else if (StringUtils.containsIgnoreCase(rawType, "Experience Design")) {
            plainType = "Experience Design";
        } else if (StringUtils.containsIgnoreCase(rawType, "OPS")) {
            plainType = "OPS";
        } else if (StringUtils.containsIgnoreCase(rawType, "Support") ||
                StringUtils.containsIgnoreCase(rawType, "SLS")) {
            plainType = "Support";
        } else if (StringUtils.containsIgnoreCase(rawType, "Quality Control") ||
                StringUtils.containsIgnoreCase(rawType, "QС")) {
            plainType = "Quality Control";
        } else if (StringUtils.containsIgnoreCase(rawType, "Procurement")) {
            plainType = "Procurement";
        } else if (StringUtils.containsIgnoreCase(rawType, "Linux")) {
            plainType = "Linux";
        } else if (StringUtils.containsIgnoreCase(rawType, "UX")) {
            plainType = "UX";
        } else if (StringUtils.containsIgnoreCase(rawType, "Hadoop")) {
            plainType = "Hadoop";
        } else if (StringUtils.containsIgnoreCase(rawType, "Database administrator") ||
                StringUtils.containsIgnoreCase(rawType, "DBA")) {
            plainType = "Database administrator";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Engineer")) {
            plainType = "Data Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Market")) {
            plainType = "Marketing";
        } else if (StringUtils.containsIgnoreCase(rawType, "Research") ||
                StringUtils.containsIgnoreCase(rawType, "RnD")) {
            plainType = "Research";
        } else if (StringUtils.containsIgnoreCase(rawType, "Embedded")) {
            plainType = "Embedded";
        } else if (StringUtils.containsIgnoreCase(rawType, "Vice President") ||
                StringUtils.containsIgnoreCase(rawType, "VP ")) {
            plainType = "Vice President";
        } else if (StringUtils.containsIgnoreCase(rawType, "Magento")) {
            plainType = "Magento";
        } else if (StringUtils.containsIgnoreCase(rawType, "Copywriter") ||
                StringUtils.containsIgnoreCase(rawType, "Writer")) {
            plainType = "Writer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Drupal")) {
            plainType = "Drupal";
        } else if (StringUtils.containsIgnoreCase(rawType, "WordPress")) {
            plainType = "Wordpress";
        } else if (StringUtils.containsIgnoreCase(rawType, "Affiliate")) {
            plainType = "Affiliate";
        } else if (StringUtils.containsIgnoreCase(rawType, "Scrum")) {
            plainType = "Scrum";
        } else if (StringUtils.containsIgnoreCase(rawType, "Art")) {
            plainType = "Artist";
        } else if (StringUtils.containsIgnoreCase(rawType, "Google Cloud")) {
            plainType = "Google Cloud";
        } else if (StringUtils.containsIgnoreCase(rawType, "Electrical Engineer")) {
            plainType = "Electrical Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Electric")) {
            plainType = "Electrical Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "SEO")) {
            plainType = "SEO";
        } else if (StringUtils.containsIgnoreCase(rawType, "Media")) {
            plainType = "Media";
        } else if (StringUtils.containsIgnoreCase(rawType, "Radio")) {
            plainType = "Radio";
        } else if (StringUtils.containsIgnoreCase(rawType, "Linkbuilder")) {
            plainType = "Linkbuilder";
        } else if (StringUtils.containsIgnoreCase(rawType, "Erlang")) {
            plainType = "Erlang";
        } else if (StringUtils.containsIgnoreCase(rawType, "Product Owner")) {
            plainType = "Product Owner";
        } else if (StringUtils.containsIgnoreCase(rawType, "Content")) {
            plainType = "Content";
        } else if (StringUtils.containsIgnoreCase(rawType, "SMM")) {
            plainType = "SMM";
        } else if (StringUtils.containsIgnoreCase(rawType, "Machine Learning")) {
            plainType = "Machine Learning";
        } else if (StringUtils.containsIgnoreCase(rawType, "Yii")) {
            plainType = "Yii";
        } else if (StringUtils.containsIgnoreCase(rawType, "Xamarin")) {
            plainType = "Xamarin";
        } else if (StringUtils.containsIgnoreCase(rawType, "Laravel")) {
            plainType = "Laravel";
        } else if (StringUtils.containsIgnoreCase(rawType, "Flash")) {
            plainType = "Flash";
        } else if (StringUtils.containsIgnoreCase(rawType, "Data Science")) {
            plainType = "Data Science";
        } else if (StringUtils.containsIgnoreCase(rawType, "ETL")) {
            plainType = "ETL";
        } else if (StringUtils.containsIgnoreCase(rawType, "Business intelligence") ||
                StringUtils.containsIgnoreCase(rawType, "BI ")) {
            plainType = "Business intelligence";
        } else if (StringUtils.containsIgnoreCase(rawType, "MS Dynamics")) {
            plainType = "MS Dynamics";
        } else if (StringUtils.containsIgnoreCase(rawType, "Moderator")) {
            plainType = "Moderator";
        } else if (StringUtils.containsIgnoreCase(rawType, "Wireframe")) {
            plainType = "Wireframe";
        } else if (StringUtils.containsIgnoreCase(rawType, "VOIP")) {
            plainType = "VOIP";
        } else if (StringUtils.containsIgnoreCase(rawType, "HTML5")) {
            plainType = "HTML5";
        } else if (StringUtils.containsIgnoreCase(rawType, "Graphic")) {
            plainType = "Graphic";
        } else if (StringUtils.containsIgnoreCase(rawType, "HTML/CSS")) {
            plainType = "HTML/CSS";
        } else if (StringUtils.containsIgnoreCase(rawType, "HTML")) {
            plainType = "HTML";
        } else if (StringUtils.containsIgnoreCase(rawType, "CSS")) {
            plainType = "CSS";
        } else if (StringUtils.containsIgnoreCase(rawType, "Animator")) {
            plainType = "Animator";
        } else if (StringUtils.containsIgnoreCase(rawType, "Oracle")) {
            plainType = "Oracle";
        } else if (StringUtils.containsIgnoreCase(rawType, "Azure")) {
            plainType = "Azure";
        } else if (StringUtils.containsIgnoreCase(rawType, "DB Developer")) {
            plainType = "DB Developer";
        } else if (StringUtils.containsIgnoreCase(rawType, "Talent Acquisition")) {
            plainType = "Talent Acquisition";
        } else if (StringUtils.containsIgnoreCase(rawType, "Network")) {
            plainType = "Network Engineer";
        } else if (StringUtils.containsIgnoreCase(rawType, "FPGA")) {
            plainType = "FPGA";
        } else if (StringUtils.containsIgnoreCase(rawType, "PR ")) {
            plainType = "PR";
        } else if (StringUtils.containsIgnoreCase(rawType, "IndySoft")) {
            plainType = "IndySoft";
        }

        return plainType;

    }
}

