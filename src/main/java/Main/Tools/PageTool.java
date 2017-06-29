package Main.Tools;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

//This tool handle the WebClient and return page
public class PageTool {
    //WebClient simulates Chrome browser that can handle Javascript on sites
    static private final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    //This method helps with page loading
    static public HtmlPage getPage(String url) {
        HtmlPage page = null;
        try {
            //Get page from WebClient
            page = webClient.getPage(url);
            //Wait for page to load
            Thread.sleep(3_000);
        }
        catch (IOException e) {
            System.out.println("IOException while loading the page");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while thread sleep");
            e.printStackTrace();
        }
        return page;
    }
}
