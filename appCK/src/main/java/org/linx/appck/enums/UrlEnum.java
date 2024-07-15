package org.linx.appck.enums;

public enum UrlEnum {

    AUTHENTICATE_URL("http://localhost:8080/v1/authenticate"),
    GET_ITEMS_URL("http://localhost:8080/v1/api/item/all"),
    FIND_CLIENT_BY_EMAIL_URL("http://localhost:8080/v1/api/client/email");

    private final String url;

    UrlEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
