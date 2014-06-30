package nl.sogyo.jesper.webserver;

/**
 * Created by jvdberg on 10/06/2014.
 */
public class SearchParameter {

    public SearchParameter(String searchQuery){
        searchParameterName = searchQuery.substring(0,1).toUpperCase() +  searchQuery.substring(1, searchQuery.indexOf("="));
        searchParameterValue = searchQuery.substring(searchQuery.indexOf("=") + 1);
    }

    private String searchParameterName;
    private String searchParameterValue;

    public String getSearchParameterName() {
        return searchParameterName;
    }

    public String getSearchParameterValue() {
        return searchParameterValue;
    }
}

