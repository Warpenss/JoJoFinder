package Main.Services;

import Main.Entities.Source;
import Main.Entities.Vacancy;
import Main.Repository.VacancyRepository;
import Main.Tools.Browser;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
        for (Source source : sources) {
            if (source.getSourceName().equals("EPAM")) {
                Browser.initiateClientWithJS();
            } else {
                Browser.initiateClientWithoutJS();
            }

            System.out.println(source.getSourceName());
            List<HtmlElement> vacancies = new ArrayList<>();
            HtmlPage page = Browser.getPage(source.getSearchUrl());

            if (source.getPaginationType().equals("LOAD")) {
                for (int i = 0; i < 10; i++) {
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
                    List<String> type;
                    String vacancySource;

                    url = page.getFullyQualifiedUrl(((HtmlAnchor) htmlElement.getByXPath(source.getUrlSelector())
                            .get(0)).getHrefAttribute()).toString();
                    if (source.getSourceName().equals("glassdoor")) {
                        String rawUrl = url;
                        String ao = StringUtils.substringAfter(rawUrl, "ao=");
                        ao = StringUtils.substringBefore(ao, "&");
                        String jobListingId = StringUtils.substringAfter(rawUrl, "jobListingId=");
                        jobListingId = StringUtils.substringBefore(jobListingId, "&");

                        url = "https://www.glassdoor.com/partner/jobListing.htm?";
                        url += "ao=" + ao;
                        url += "&jobListingId=" + jobListingId;

//                        url = "https://www.glassdoor.com/job-listing/-JV.htm?";
//                        url += "jl=" + jobListingId;
                    }
                    System.out.println(url);
                    if (url.contains("connect.facebook.net") || (url.contains("googleads"))) {
                        System.out.println("url bug, pls fix");
                        throw new Exception();
                    }


                    if (vacancyRepository.findByUrl(url).size() == 0) {
                        time = LocalDateTime.now();
                        System.out.println(time);

                        if (source.getSourceName().equalsIgnoreCase("LUXOFT")) {
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
                                } else if (companyName.contains("компании")) {
                                    companyName = StringUtils.substringAfterLast(companyName, "компании");
                                    companyName = StringUtils.substringBefore(companyName, "\u00a0");
                                } else if (companyName.contains(" в ")) {
                                    companyName = StringUtils.substringAfterLast(companyName, " в ");
                                    companyName = StringUtils.substringBefore(companyName, "\u00a0");
                                } else {
                                    companyName = source.getSourceName();
                                }
                            } else if (source.getSourceName().equals("glassdoor")) {
                                companyName = StringUtils.substringBefore(companyName, " – ");
                            }
                        }
                        companyName = companyName.replaceAll("[\u00A0\u2007\u202F\u200B]", " ").trim();
                        companyName = companyName.replaceAll("\"", "").trim();
                        if (companyName.contains(",")) {
                            companyName = StringUtils.substringBefore(companyName, ",").trim();
                        }
                        if (companyName.contains("/")) {
                            companyName = StringUtils.substringBefore(companyName, "/").trim();
                        }
                        if (companyName.contains("|")) {
                            companyName = StringUtils.substringBefore(companyName, "|").trim();
                        }
                        if (companyName.contains("(")) {
                            companyName = StringUtils.substringBefore(companyName, "|").trim();
                        }
                        System.out.println(companyName);

                        location = "Undefined";
                        List<HtmlElement> list = htmlElement.getByXPath(source.getLocationSelector());
                        if (!list.isEmpty()) {
                            location = getLocation(source, list).trim();
                        }
                        System.out.println(location);

                        String rawType;
                        if (source.getTypeSelector().equals("FROM_TITLE")) {
                            rawType = title;
                        } else {
                            rawType = ((HtmlElement) htmlElement.getByXPath(source.getTypeSelector()).get(0)).getTextContent();
                        }
                        type = processType(rawType);
                        System.out.println(type);

                        vacancySource = source.getSourceName();

                        vacancyRepository.save(new Vacancy(time, title, url, companyName, location, type, vacancySource));
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
            if (location.contains("ищем в")) {
                location = StringUtils.substringAfter(location, "ищем в");
            }
        } else {
            location = StringUtils.substringBefore((list.get(0)).getTextContent(), ",").trim();
            if (location.contains(".")) {
                location = StringUtils.substringAfter(location, ".").trim();
            }
            if (location.contains("(")) {
                location = StringUtils.substringBefore(location, "(").trim();
            }
            location = StringUtils.substringBefore(location, "\\p{Space}");
        }
        if (vacancyRepository.findByLocation(location).size() == 0) {
            System.out.println("Raw location = " + location);
            String language = detectLanguage(location);
            location = translateTitle(location, language);
            if (location.equals("Lions")) {
                location = "Lviv";
            }

//            location = plainCity(location);
        }

        return location;
    }

//    private String plainCity(String rawCity) throws Exception {
//        rawCity = rawCity.replaceAll("[\u00A0\u2007\u202F\u200B]", " ").trim();
//        if (rawCity.isEmpty()) {
//            return "Undefined";
//        } else {
//            String plainCity = rawCity;
//            WebService.setUserName("warpenss"); // add your username here
//            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
//            searchCriteria.setName(rawCity);
//            ToponymSearchResult searchResult = WebService.search(searchCriteria);
//            List<Toponym> searchResultToponyms = searchResult.getToponyms();
//            if (!searchResultToponyms.isEmpty()) {
//                plainCity = searchResultToponyms.get(0).getName();
//            }
//            return plainCity;
//        }
//    }

    private List<String> processType(String rawType) {
        List<String> type = new ArrayList<>();

        if (StringUtils.containsIgnoreCase(rawType, "JavaScript") ||
                StringUtils.containsIgnoreCase(rawType, "JS ")) {
            type.add("JavaScript");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Full Stack") ||
                StringUtils.containsIgnoreCase(rawType, "Fullstack") ||
                StringUtils.containsIgnoreCase(rawType, "Full-stack")) {
            type.add("Full Stack");
        }
        if ((StringUtils.containsIgnoreCase(rawType, "Java") ||
                StringUtils.containsIgnoreCase(rawType, "J2EE")) &&
                (!StringUtils.containsIgnoreCase(rawType, "script"))) {
            type.add("Java");
        }
        if (StringUtils.containsIgnoreCase(rawType, "C++")) {
            type.add("C++");
        }
        if (StringUtils.containsIgnoreCase(rawType, "C#")) {
            type.add("C#");
        }
        if (StringUtils.containsIgnoreCase(rawType, ".NET")) {
            type.add(".NET");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Python")) {
            type.add("Python");
        }
        if (StringUtils.containsIgnoreCase(rawType, "UI ")) {
            type.add("UI");
        }
        if (StringUtils.containsIgnoreCase(rawType, "iOS")) {
            type.add("iOS");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Manager") ||
                StringUtils.containsIgnoreCase(rawType, "PM ")) {
            type.add("Manager");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Test")) {
            type.add("Testing");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Consultant")) {
            type.add("Consultant");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Assistant")) {
            type.add("Assistant");
        }
        if (StringUtils.containsIgnoreCase(rawType, "SQL")) {
            type.add("SQL ");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Analyst")) {
            type.add("Analyst ");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Big Data")) {
            type.add("Big Data");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Recruit")) {
            type.add("Recruiter");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Solution")) {
            type.add("Solution Engineer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Director")) {
            type.add("Director");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Designer")) {
            type.add("Designer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "DevOps") ||
                StringUtils.containsIgnoreCase(rawType, "Development Operations")) {
            type.add("DevOps");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Front-end") ||
                StringUtils.containsIgnoreCase(rawType, "Front end") ||
                StringUtils.containsIgnoreCase(rawType, "Frontend")) {
            type.add("Front-end");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Automation")) {
            type.add("Automation");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Accountant")) {
            type.add("Accountant");
        }
        if (StringUtils.containsIgnoreCase(rawType, "HR") ||
                StringUtils.containsIgnoreCase(rawType, "Human Resources")) {
            type.add("HR");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Security")) {
            type.add("Security");
        }
        if (StringUtils.containsIgnoreCase(rawType, "QA") ||
                StringUtils.containsIgnoreCase(rawType, "Quality Assurance")) {
            type.add("QA");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Android")) {
            type.add("Android");
        }
        if (StringUtils.containsIgnoreCase(rawType, "PHP")) {
            type.add("PHP");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Sales")) {
            type.add("Sales");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Angular")) {
            type.add("Angular");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Node.js") ||
                StringUtils.containsIgnoreCase(rawType, "NodeJS") ||
                StringUtils.containsIgnoreCase(rawType, "Node. JS")) {
            type.add("Node.js");
        }
        if (StringUtils.containsIgnoreCase(rawType, " C ")) {
            type.add("C");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Scala")) {
            type.add("Scala");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Ruby") ||
                StringUtils.containsIgnoreCase(rawType, "RoR")) {
            type.add("Ruby");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Data Scientist")) {
            type.add("Data Scientist");
        }
        if (StringUtils.containsIgnoreCase(rawType, "TypeScript")) {
            type.add("TypeScript");
        }
        if (StringUtils.containsIgnoreCase(rawType, "React")) {
            type.add("React");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Unity")) {
            type.add("Unity3D");
        }
        if (StringUtils.containsIgnoreCase(rawType, "1С") ||
                StringUtils.containsIgnoreCase(rawType, "1C") ||
                StringUtils.containsIgnoreCase(rawType, "1 С") ||
                StringUtils.containsIgnoreCase(rawType, "1S")) {
            type.add("1C");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Blockchain")) {
            type.add("Blockchain");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Back-end") ||
                StringUtils.containsIgnoreCase(rawType, "Back end") ||
                StringUtils.containsIgnoreCase(rawType, "Backend")) {
            type.add("Back-end");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Service Desk") ||
                StringUtils.containsIgnoreCase(rawType, "HelpDesk")) {
            type.add("HelpDesk");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Go ") ||
                StringUtils.containsIgnoreCase(rawType, "Golang")) {
            type.add("Go");
        }
        if (StringUtils.containsIgnoreCase(rawType, "R&D")) {
            type.add("R&D");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Administrator")) {
            type.add("Administrator");
        }
        if (StringUtils.containsIgnoreCase(rawType, "ELT")) {
            type.add("ELT");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Coordinator")) {
            type.add("Coordinator");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Delphi")) {
            type.add("Delphi");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Splunk")) {
            type.add("Splunk");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Tableau")) {
            type.add("Tableau");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Data Warehouse") ||
                StringUtils.containsIgnoreCase(rawType, "DWH")) {
            type.add("Data Warehouse");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Teacher")) {
            type.add("Teacher");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Training and Development")) {
            type.add("Training and Development");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Experience Design")) {
            type.add("Experience Design");
        }
        if (StringUtils.containsIgnoreCase(rawType, "OPS")) {
            type.add("OPS");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Support") ||
                StringUtils.containsIgnoreCase(rawType, "SLS")) {
            type.add("Support");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Quality Control") ||
                StringUtils.containsIgnoreCase(rawType, "QС")) {
            type.add("Quality Control");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Procurement")) {
            type.add("Procurement");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Linux")) {
            type.add("Linux");
        }
        if (StringUtils.containsIgnoreCase(rawType, "UX")) {
            type.add("UX");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Hadoop")) {
            type.add("Hadoop");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Database administrator") ||
                StringUtils.containsIgnoreCase(rawType, "DBA")) {
            type.add("Database administrator");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Data Engineer")) {
            type.add("Data Engineer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Market")) {
            type.add("Marketing");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Research") ||
                StringUtils.containsIgnoreCase(rawType, "RnD")) {
            type.add("Research");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Embedded")) {
            type.add("Embedded");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Vice President") ||
                StringUtils.containsIgnoreCase(rawType, "VP ")) {
            type.add("Vice President");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Magento")) {
            type.add("Magento");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Copywriter") ||
                StringUtils.containsIgnoreCase(rawType, "Writer")) {
            type.add("Writer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Drupal")) {
            type.add("Drupal");
        }
        if (StringUtils.containsIgnoreCase(rawType, "WordPress")) {
            type.add("Wordpress");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Affiliate")) {
            type.add("Affiliate");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Scrum")) {
            type.add("Scrum");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Art")) {
            type.add("Artist");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Google Cloud")) {
            type.add("Google Cloud");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Electrical Engineer")) {
            type.add("Electrical Engineer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Electric")) {
            type.add("Electrical Engineer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "SEO")) {
            type.add("SEO");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Media")) {
            type.add("Media");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Radio")) {
            type.add("Radio");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Linkbuilder")) {
            type.add("Linkbuilder");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Erlang")) {
            type.add("Erlang");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Product Owner")) {
            type.add("Product Owner");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Content")) {
            type.add("Content");
        }
        if (StringUtils.containsIgnoreCase(rawType, "SMM")) {
            type.add("SMM");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Machine Learning")) {
            type.add("Machine Learning");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Yii")) {
            type.add("Yii");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Xamarin")) {
            type.add("Xamarin");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Laravel")) {
            type.add("Laravel");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Flash")) {
            type.add("Flash");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Data Science")) {
            type.add("Data Science");
        }
        if (StringUtils.containsIgnoreCase(rawType, "ETL")) {
            type.add("ETL");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Business intelligence") ||
                StringUtils.containsIgnoreCase(rawType, "BI ")) {
            type.add("Business intelligence");
        }
        if (StringUtils.containsIgnoreCase(rawType, "MS Dynamics")) {
            type.add("MS Dynamics");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Moderator")) {
            type.add("Moderator");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Wireframe")) {
            type.add("Wireframe");
        }
        if (StringUtils.containsIgnoreCase(rawType, "VOIP")) {
            type.add("VOIP");
        }
        if (StringUtils.containsIgnoreCase(rawType, "HTML5")) {
            type.add("HTML5");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Graphic")) {
            type.add("Graphic");
        }
        if (StringUtils.containsIgnoreCase(rawType, "HTML/CSS")) {
            type.add("HTML/CSS");
        }
        if (StringUtils.containsIgnoreCase(rawType, "HTML")) {
            type.add("HTML");
        }
        if (StringUtils.containsIgnoreCase(rawType, "CSS")) {
            type.add("CSS");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Animator")) {
            type.add("Animator");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Oracle")) {
            type.add("Oracle");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Azure")) {
            type.add("Azure");
        }
        if (StringUtils.containsIgnoreCase(rawType, "DB Developer")) {
            type.add("DB Developer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Talent Acquisition")) {
            type.add("Talent Acquisition");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Network")) {
            type.add("Network Engineer");
        }
        if (StringUtils.containsIgnoreCase(rawType, "FPGA")) {
            type.add("FPGA");
        }
        if (StringUtils.containsIgnoreCase(rawType, "PR ")) {
            type.add("PR");
        }
        if (StringUtils.containsIgnoreCase(rawType, "IndySoft")) {
            type.add("IndySoft");
        }
        if (StringUtils.containsIgnoreCase(rawType, "Unreal")) {
            type.add("Unreal Engine");
        }

        if (type.isEmpty()) {
            type.add("Other");
        }

        return type;

    }
}