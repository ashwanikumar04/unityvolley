using UnityEngine;
using System.Collections;
using System;
using System.Collections.Generic;

public class VolleyManager
{
    private AndroidJavaObject unityActivity;

    private VolleyManager()
    {
        AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        unityActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
    }

    public static VolleyManager Instance
    {
        get
        {
            return Inner.instance;
        }

    }

    private class Inner
    {
        static Inner() { }
        internal static readonly VolleyManager instance = new VolleyManager();
    }

    public void AddRequest(Request request)
    {
        GameObject gameObject = new GameObject(DateTime.Now.Ticks.ToString());
        VolleyBindings volleyBindings = gameObject.AddComponent<VolleyBindings>();
        volleyBindings.OnCompleteHandler = request.OnComplete;
        volleyBindings.OnErrorHandler = request.OnError;
        AndroidJavaObject httpRequest = new AndroidJavaObject("in.ashwanik.volleyandoid.HttpRequest", unityActivity, request.URL, gameObject.name);

        foreach (var param in request.Params)
        {
            httpRequest.Call("AddParam", param.Key, param.Value);
        }
        foreach (var header in request.Headers)
        {
            httpRequest.Call("AddHeader", header.Key, header.Value);
        }
        httpRequest.Call("execute");
    }
}

public class Request
{
    public string URL { get; set; }
    public Action<string> OnComplete { get; set; }
    public Action<string> OnError { get; set; }
    public Dictionary<string, string> Params { get; private set; }
    public Dictionary<string, string> Headers { get; private set; }

    public Request()
    {
        Params = new Dictionary<string, string>();
        Headers = new Dictionary<string, string>();
    }
}