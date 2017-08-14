package vpour.fastnet;

import org.apache.http.message.BasicHttpResponse;

/**
 * 响应类
 */

public class Response extends BasicHttpResponse{
    public int getStatusCode() {
        return 0;
    }


    public String getMessage() {
        return null;
    }

    public char[] getRawData() {
        return new char[0];
    }

}
