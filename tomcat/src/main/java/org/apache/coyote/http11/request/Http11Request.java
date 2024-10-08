package org.apache.coyote.http11.request;

import java.util.Map;
import java.util.Optional;
import org.apache.catalina.Session;
import org.apache.catalina.SessionManager;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.http11.MimeType;

public class Http11Request implements HttpRequest {

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
    public boolean isGet() {
        return requestLine.getMethod().isGet();
    }

    @Override
    public boolean isPost() {
        return requestLine.getMethod().isPost();
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
        return headers.existsSession();
    }

    @Override
    public String getHeader(String header) {
        return headers.getValue(header);
    }

    @Override
    public MimeType getAcceptMimeType() throws NoSuchFieldException {
        if (headers.existsAccept()) {
            return MimeType.from(headers.getFirstAcceptMimeType());
        }
        throw new NoSuchFieldException("요청에 Accept 헤더가 존재하지 않습니다.");
    }

    @Override
    public Map<String, String> getParsedBody() {
        return body.parseBody();
    }

    @Override
    public Session getSession() {
        Optional<Session> sessionOptional = SessionManager.getInstance().findSession(headers.getSession());
        return sessionOptional.orElseGet(Session::new);
    }

}
