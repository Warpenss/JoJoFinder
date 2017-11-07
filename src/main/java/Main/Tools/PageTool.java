package Main.Tools;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.lang.reflect.Field;

//This tool handle the WebClient and return page
public class PageTool {
    //WebClient simulates Chrome browser that can handle Javascript on sites
    static private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    static {
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPopupBlockerEnabled(true);
    }

    //This method helps with page loading
    static public HtmlPage getPage(String url) {
        try {
            History window = webClient.getWebWindows().get(0).getHistory();
            Field f = window.getClass().getDeclaredField("ignoreNewPages_"); //NoSuchFieldException
            f.setAccessible(true);
            ((ThreadLocal<Boolean>) f.get(window)).set(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Can't disable history");
        }

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
