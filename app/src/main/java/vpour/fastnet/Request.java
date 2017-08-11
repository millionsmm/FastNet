package vpour.fastnet;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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


    //返回POST或PUT请求时body参数字节数组
    public byte[] getBody() {
        Map<String, String> params = getBodyParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getDefaultParamsEncoding());
        }
        return null;
    }


    //将参数转换为URL编码的参数串，格式为key1=value1&key2=value2
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams;
        try {
            encodedParams = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }

    }

    //用于对请求的排序处理，根据优先级和加入到队列的序号进行排序
    @Override
    public int compareTo(@NonNull Request<T> another) {
        Priority myPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        //如果优先级相等，那么按照添加到队列的序列号顺序来执行
        return myPriority.equals(anotherPriority) ? this.getSerialNum() - another.getSerialNum()
                : myPriority.ordinal() - anotherPriority.ordinal();
    }


    /**
     * 网络请求Listener，会被执行在UI线程
     *
     * @param <T> 请求的Response类型
     */
    public static interface RequestListener<T> {
        //请求完成的回调
        public void onComplete(int stCode, T response, String errMsg);
    }

    public static String getDefaultParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public void setSerialNum(int serialNum) {
        mSerialNum = serialNum;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isShouldCache() {
        return mShouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        mShouldCache = shouldCache;
    }

    public RequestListener<T> getRequestListener() {
        return mRequestListener;
    }

    public void setRequestListener(RequestListener<T> requestListener) {
        mRequestListener = requestListener;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        mHttpMethod = httpMethod;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public Map<String, String> getBodyParams() {
        return mBodyParams;
    }

    public void setBodyParams(Map<String, String> bodyParams) {
        mBodyParams = bodyParams;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getDefaultParamsEncoding();
    }



}
