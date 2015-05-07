package in.ashwanik.volleyandoid;

public interface IAsyncCallback {
     void onComplete(WebResponse responseContent);
     void onError(ErrorDetail errorData);
}