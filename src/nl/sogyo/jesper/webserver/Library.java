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
        response.setBody(response.getBodyInProcess() + "The following books have been searched for recently: <br>");
        System.out.println("Cookies worden gecheckt in Library.addlastfivesearchestoresponse");
        System.out.println(request.getCookies().size());
        cookieFor: for (Cookie c: request.getCookies()) {
//            System.out.println(c.getName() + "=" + c.getValue());
            System.out.println("library.addLastFiveSearchesToResponse: sessionlist size: " + sessionList.size());
            for (Session session: sessionList){
                System.out.println("Library.addLastFiveSearchesToResponse: SessionCookie name en value in de session" + session.getCookie().getName() + "=" + session.getCookie().getValue());
                System.out.println("Library.addLastFiveSearchesToResponse: Cookie name en value in de session" + c.getName() + "=" + c.getValue());


                System.out.println("Hallo doe is committen dan");



                if ((session.getCookie().getName()).equals(c.getName()) && (session.getCookie().getValue()).equals(c.getValue())) {
                    System.out.println("boek toegvoegd");
                    addBookToResponseBody(session.getIsbnList());
                    continue cookieFor;
                }
            }
        }
    }

    private void addBookToResponseBody(ArrayList<String> isbnList) {
        System.out.println("Library.addBookToResponseBody komt hij langs, dus cookie is gevonden");
        for (String isbn: isbnList) {
            for (Book book: bookCollection){
                if (book.getIsbn().equals(isbn)) {
                    System.out.println("boek aan body toegevoegd ");
                    response.setBody(response.getBodyInProcess() + "ISBN: " + book.getIsbn() + "; Author: " + book.getAuthor() + "; Title: " + book.getTitle() + "<br>");
                }
            }
        }
    }

    private void searchForIsbnInLibrary(Request request, Response response) {
        boolean anyBookFound = false;
        parameter: for (SearchParameter searchParameter: request.getSearchParameters()) {
            if (isValidIsbnParameter(searchParameter)) {
                book:  for (Book book: bookCollection) {
                    if (bookIsInLibrary(book, searchParameter)) {
                        if (!anyBookFound) {
                            response.setBody(response.getBodyInProcess() + "The following books could be retrieved:<br>");
                            anyBookFound = true;
                        }
                        response.setBody(response.getBodyInProcess() + "ISBN: " + book.getIsbn() + "; Author: " + book.getAuthor() + "; Title: " + book.getTitle() + "<br>");
                        continue parameter;
                    }
                }
            } else {
                response.setStatus(Responsable.HTTPStatusCode.NotFound);
                break parameter;
            }
            response.setBody(response.getBodyInProcess() + "ISBN: " + searchParameter.getSearchParameterValue() + "; No books found in this library for this ISBN<br>\r\n");
        }
    }

    private boolean isValidIsbnParameter(SearchParameter searchParameter) {
        return ( (searchParameter.getSearchParameterName().equalsIgnoreCase("isbn") &&
                searchParameter.getSearchParameterValue().length() == 13 ) ? true : false);
    }

    private boolean bookIsInLibrary(Book book, SearchParameter searchParameter) {
        return ((book.getIsbn()).equals(searchParameter.getSearchParameterValue()) ? true : false);
    }

    public Response getResponse() {
        return this.response;
    }
}
