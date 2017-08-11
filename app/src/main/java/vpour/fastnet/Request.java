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
    //默认的编码方式
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    //请求序列号
    protected int mSerialNum = 0;
    //优先级默认设置为Normal
    protected Priority mPriority = Priority.NORMAL;
    //是否取消该请求
    protected boolean isCancel = false;
    //该请求是否缓存
    private boolean mShouldCache = true;
    //请求Listener
    protected RequestListener<T> mRequestListener;
    //请求的URL
    private String mUrl = "";
    //请求的方法
    HttpMethod mHttpMethod = HttpMethod.GET;
    //请求的header
    private Map<String, String> mHeaders = new HashMap<>();
    //请求参数
    private Map<String, String> mBodyParams = new HashMap<>();

    /**
     * @param requestListener 请求回调，将请求回调给客户
     * @param url             请求的目标url
     * @param httpMethod      请求方式
     */
    public Request(RequestListener<T> requestListener, String url, HttpMethod httpMethod) {
        mRequestListener = requestListener;
        mUrl = url;
        mHttpMethod = httpMethod;
    }

    //从原生的网络请求中解析结果，子类必须覆写
    public abstract T parseResponse(Response response);

    //处理Response，该方法需要运行在UI线程
    public final void deliveryResponse(Response response) {
        //解析得到请求结果
        T result = parseResponse(response);
        if (mRequestListener != null) {
            int stCode = response != null ? response.getStatusCode() : -1;
            String msg = response != null ? response.getMessage() : "unknown error";
            mRequestListener.onComplete(stCode, result, msg);
        }
    }

    public static interface RequestListener<T> {
        //请求完成的回调
        public void onComplete(int stCode, T response, String errMsg);
    }


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
