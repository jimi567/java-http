package org.apache.coyote.http11.request;

import com.techcourse.exception.UncheckedServletException;
import java.util.Map;
import java.util.Optional;
import org.apache.catalina.Session;
import org.apache.catalina.SessionManager;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.MimeType;

public class Http11Request implements HttpRequest {

    private static final String JSESSIONID = "JSESSIONID";
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_HEADER_DLELIMITER = ",";

    private final Http11RequestLine requestLine;
    private final Http11RequestHeaders headers;
    private final Http11RequestBody body;

    public Http11Request(
            Http11RequestLine requestLine,
            Http11RequestHeaders headers,
            Http11RequestBody body
    ) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    @Override
    public String getRequestURI() {
        return requestLine.getURI();
    }

    @Override
    public String getPath() {
        return requestLine.getPath();
    }

    @Override
    public boolean existsSession() {
        return headers.existsCookie(JSESSIONID);
    }

    @Override
    public boolean existsAccept() {
        return headers.getValue(ACCEPT) != null;
    }

    @Override
    public String getHeader(String header) {
        return headers.getValue(header);
    }

    @Override
    public MimeType getAcceptMimeType() {
        if (existsAccept()) {
            return MimeType.from(getHeader(ACCEPT).split(ACCEPT_HEADER_DLELIMITER)[0]);
        }
        throw new UncheckedServletException(new IllegalArgumentException("요청에 Accept 헤더가 존재하지 않습니다."));
    }

    @Override
    public String getCookie(String cookieName) {
        return headers.getCookie(cookieName);
    }

    @Override
    public Map<String, String> getParsedBody() {
        return body.parseBody();
    }

    @Override
    public Session getSession() {
        Optional<Session> sessionOptional = SessionManager.getInstance().findSession(getCookie(JSESSIONID));
        return sessionOptional.orElseGet(Session::new);
    }

}
