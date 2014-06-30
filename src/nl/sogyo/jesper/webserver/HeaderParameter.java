package nl.sogyo.jesper.webserver;

/**
 * Created by jvdberg on 10/06/2014.
 */
public class HeaderParameter {

    public HeaderParameter(String headerParameter){
        headerParameterName = headerParameter.substring(0,headerParameter.indexOf(":")).trim();
        headerParameterValue = headerParameter.substring(headerParameter.indexOf(":")+2).trim();
    }

    private String headerParameterName;
    private String headerParameterValue;

    public String getHeaderParameterName() {
        return headerParameterName;
    }

    public String getHeaderParameterValue() {
        return headerParameterValue;
    }
}
