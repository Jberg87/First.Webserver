package nl.sogyo.jesper.webserver;

import java.util.ArrayList;

/**
 * Created by jvdberg on 17/06/2014.
 */
public class Library implements WebApplication {
    private ArrayList<Book> bookCollection = new ArrayList<>();
    private Response response;
    private static ArrayList<Session> sessionList = new ArrayList<>();
    private CopyCookieWebApp ccwa = new CopyCookieWebApp();
    private int previousQueriesAdded = 0;

    public Library(){
        bookCollection.add(new Book("Jan", "Het Eerste Probeersel", "1111111111111"));
        bookCollection.add(new Book("Piet", "Het Tweede Probeersel", "2222222222222"));
        bookCollection.add(new Book("Jezus", "De Bijbel", "3333333333333"));
        bookCollection.add(new Book("Kees", "Het Oude Testament", "4444444444444"));
        bookCollection.add(new Book("Johan", "Het Nieuwe Testament", "5555555555555"));
        bookCollection.add(new Book("De Duivel", "Het Hellegat", "6666666666666"));
    }

    @Override
    public void process(Request request, Response response) {
        this.response = response;
        ccwa.process(request, response);
        sessionList.add(new Session(ccwa.getLastCookie()));
        response.setBody(request.giveStandardBody() + request.getSearchParametersString() + "<br>\r\n");
//        response.setBody(request.getSearchParametersString() + "<br>\r\n");
        searchForIsbnInLibrary(request, response);
        addLastFiveSearchesToResponse(request);
    }

    private void addLastFiveSearchesToResponse(Request request) {
        response.setBody(response.getBodyInProcess() + "<br><u>The following books have been searched for recently:</u><br>");
        sessionLoop: for (int i = sessionList.size()-1 ; i >= 0; i--){
            Session session = sessionList.get(i);
            for (Cookie c: request.getCookies()) {
                if ( previousQueriesAdded < 5 && (session.getCookie().getName()).equals(c.getName()) && (session.getCookie().getValue()).equals(c.getValue())) {
                    addBookToResponseBody(session.getIsbnList());
                    continue sessionLoop;
                } else if (previousQueriesAdded >= 5) break sessionLoop;
            }
        }
    }

    private void addBookToResponseBody(ArrayList<String> isbnList) {
        sessionLoop: for (String isbnOfSession: isbnList) {
            for (Book book: bookCollection){
                if (book.getIsbn().equals(isbnOfSession) && previousQueriesAdded < 5 ) {
                    response.setBody(response.getBodyInProcess() + "ISBN: " + book.getIsbn() + "; Author: " + book.getAuthor() + "; Title: " + book.getTitle() + "<br>");
                    previousQueriesAdded++;
                } else if (previousQueriesAdded >= 5) break sessionLoop;
            }
        }
    }

    private void searchForIsbnInLibrary(Request request, Response response) {
        boolean anyBookFound = false;
        parameter: for (SearchParameter searchParameter: request.getSearchParameters()) {
            if (isValidIsbnParameter(searchParameter)) {

                // in de session wordt dit gezochte ISBN adres opgeslagen
                sessionList.get(sessionList.size()-1).addIsbn(searchParameter.getSearchParameterValue());

                for (Book book: bookCollection) {
                    if (bookIsInLibrary(book, searchParameter)) {
                        if (!anyBookFound) {
                            response.setBody(response.getBodyInProcess() + "<u>The following books could be retrieved:</u><br>");
                            anyBookFound = true;
                        }
                        response.setBody(response.getBodyInProcess() + "ISBN: " + book.getIsbn() + "; Author: " + book.getAuthor() + "; Title: " + book.getTitle() + "<br>");
                        continue parameter;
                    }
                }
            } else {
                response.setStatus(Responsable.HTTPStatusCode.NotFound);
                break;
            }
            response.setBody(response.getBodyInProcess() + "ISBN: " + searchParameter.getSearchParameterValue() + "; No books found in this library for this ISBN<br>\r\n");
        }
    }

    private boolean isValidIsbnParameter(SearchParameter searchParameter) {
        return ((searchParameter.getSearchParameterName().equalsIgnoreCase("isbn") &&
                searchParameter.getSearchParameterValue().length() == 13));
    }

    private boolean bookIsInLibrary(Book book, SearchParameter searchParameter) {
        return ((book.getIsbn()).equals(searchParameter.getSearchParameterValue()));
    }

    public Response getResponse() {
        return this.response;
    }
}
