package nl.sogyo.jesper.webserver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jvdberg on 06/06/2014.
 */
public class Request implements Requestable {

    private String HTTPMethod, resourcePath, searchQuery;
    private ArrayList<HeaderParameter> headerParameters = new ArrayList<>();
    private ArrayList<SearchParameter> searchParameters = new ArrayList<>();
    private ArrayList<Cookie> webserverCookieList = new ArrayList<>();


    public Request(String line) {
        setHTTPMethod(line);
        setResourcePath(line);
        setSearchQuery(line);
    }

    @Override
    public String getHTTPMethod() {
        return this.HTTPMethod;
    }
    public void setHTTPMethod(String line) {
        if (line.startsWith("G")) this.HTTPMethod = Requestable.HTTPMethod.GET.toString();
        if (line.startsWith("P")) this.HTTPMethod = Requestable.HTTPMethod.POST.toString();
    }

    @Override
    public String getResourcePath() {
        return this.resourcePath;
    }

    @Override
    public List<Cookie> getCookies() {
        return webserverCookieList;
    }

    public void setResourcePath(String line) {
        if (line.contains("search?")) this.resourcePath = line.substring(4, line.indexOf("search?"));
        else this.resourcePath = line.substring(4, (line.indexOf("HTTP") - 1));
    }

    public void addHeaderParameter(HeaderParameter headerparameter) {
        this.headerParameters.add(headerparameter);
        if (headerparameter.getHeaderParameterName().equals("Cookie")) cookieStringToList();
    }
    public ArrayList<HeaderParameter> getHeaderParameters() {
        return this.headerParameters;
    }

    public void setSearchQuery(String line) {
        if (line.contains("search?")) {
            this.searchQuery = line.substring(line.indexOf("search?") + 7, line.indexOf("HTTP") -1);
            setSearchParameters();
        }
    }


    public ArrayList<SearchParameter> getSearchParameters() {
        return searchParameters;
    }
    public String getSearchParametersString() {
        String returnString = "<br><u>The following parameters were passed:</u><br>\r\n";
        for (SearchParameter parameter: searchParameters) {
            returnString = returnString +
                    parameter.getSearchParameterName() +
                    ": " +
                    parameter.getSearchParameterValue() +
                    "<br>\r\n";
        }
        return returnString;
    }
    public void setSearchParameters() {
        if (this.searchQuery.contains("&")) {
        String[] queries = this.searchQuery.split("&");
            for (String query : queries) {
                this.searchParameters.add(new SearchParameter(query.replace("%20", " ")));
            }
        } else {
            this.searchParameters.add(new SearchParameter(this.searchQuery.replace("%20", " ")));
        }
    }


    public String giveStandardBody() {
        String bodyTemp = "";

        bodyTemp = "You did an HTTP " +
                getHTTPMethod() +
                " request.<br>\r\n" +
                "You requested the following resource: " +
                getResourcePath() +
                "<br><br>\r\n";

        for (HeaderParameter headerParameter : getHeaderParameters()) {
            bodyTemp = bodyTemp.concat(headerParameter.getHeaderParameterName() + ": " + headerParameter.getHeaderParameterValue() + "<br>\r\n");
        }

        return bodyTemp;
    }

    public void cookieStringToList() {
        for (HeaderParameter hp : getHeaderParameters()) {
            if (hp.getHeaderParameterName().equals("Cookie")) {
                convertCookiesToList(hp.getHeaderParameterValue());
            }
        }
    }

    private void convertCookiesToList(String cookieHeaderParameterValue) {
        if (!cookieHeaderParameterValue.contains(";")) {
            convertSingleRequestCookieStringToList(cookieHeaderParameterValue);
        } else {
            convertMultipleRequestCookiesStringToList(cookieHeaderParameterValue);
        }
    }
    private void convertSingleRequestCookieStringToList(String cookieHeaderParameterValue) {
        String[] splitSingleCookie = cookieHeaderParameterValue.split("=");
        webserverCookieList.add(new WebserverCookie(splitSingleCookie[0].trim(), splitSingleCookie[1]));
    }
    private void convertMultipleRequestCookiesStringToList(String cookieHeaderParameterValue) {
        String[] splitCompleteCookie = cookieHeaderParameterValue.split(";");
        for (String singleCookie : splitCompleteCookie) {
            String[] splitSingleCookie = singleCookie.split("=");
            webserverCookieList.add(new WebserverCookie(splitSingleCookie[0].trim(), splitSingleCookie[1]));
        }
    }
}