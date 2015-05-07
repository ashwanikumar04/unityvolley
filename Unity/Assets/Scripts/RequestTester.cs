using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class RequestTester : MonoBehaviour
{
    public Text logger;
    // Use this for initialization
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    public void Request1()
    {
        Request request = new Request();
        request.URL = "http://www.ashwanik.in/blog/blogpost";
        request.Params.Add("data", "test data");
        request.OnComplete = (data) =>
        {
            logger.text += "data: " + data + "\n";
            Debug.Log("data: " + data);
        };
        request.OnError = (error) =>
        {
            Debug.Log("error: " + error);
        };

        VolleyManager.Instance.AddRequest(request);
    }
}
