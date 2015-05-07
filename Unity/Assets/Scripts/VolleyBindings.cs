using UnityEngine;
using System.Collections;
using System;

public class VolleyBindings : MonoBehaviour {

	public Action<string> OnCompleteHandler { get; set; }
	public Action<string> OnErrorHandler { get; set; }
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	public void OnComplete(String response)
	{
		OnCompleteHandler(response);
		DestroyObject(this.gameObject);
	}

	public void OnError(string error)
	{
		OnErrorHandler(error);
		DestroyObject(this.gameObject);
	}
}
