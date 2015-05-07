package in.ashwanik.volleyandoid;

public class WebResponse {

    private String response;

    public WebResponse(int responseCode, String response) {
        this.response = response;

    }

    public String getResponse() {
        return response;
    }
}
