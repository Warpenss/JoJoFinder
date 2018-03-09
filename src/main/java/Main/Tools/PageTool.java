package Main.Tools;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.lang.reflect.Field;

//This tool handle the WebClient and returns page
public class PageTool {
    //WebClient simulates Chrome browser that can handle Javascript on sites
    static private WebClient webClient;

    static public void initiateClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPopupBlockerEnabled(true);
        System.out.println("New webClient is created");
    }

    static public void closeClient() {
        for (TopLevelWindow topLevelWindow : webClient.getTopLevelWindows()) {
            topLevelWindow.close();
            System.out.println(topLevelWindow.toString() + " - window is closed");
        }
        webClient.close();
        System.out.println("WebClient is closed");
    }

    //This method helps with page loading
    static public HtmlPage getPage(String url) {
        HtmlPage page = null;
        try {
            //Get page from WebClient
            page = webClient.getPage(url);
            //Wait for page to load
            Thread.sleep(3_000);
        } catch (IOException e) {
            System.out.println("IOException while loading the page");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while thread sleep");
            e.printStackTrace();
        }
        if (page == null) {
            System.out.println("Trouble with page loading");
        }

        return page;


    }


}
