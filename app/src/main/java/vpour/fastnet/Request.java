package vpour.fastnet;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求类，GET，DELETE并不能传递请求参数，用户可以将参数
 * 构建到URL后传递进来并到Request中
 *
 * @param <T> T为请求返回的数据类型
 */

public abstract class Request<T> implements Comparable<Request<T>> {



    //Http请求方法枚举，这里我们只有GET,POST,PUT,DELETE四种
    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        // http request type
        private String mHttpMethod = "";

        private HttpMethod(String method) {
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    //优先级枚举
    public static enum Priority {
        Low,
        NORMAL,
        HIGH,
        IMMEDIATE
    }
}
