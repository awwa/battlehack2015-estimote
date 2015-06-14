package com.awwa.estimoteexample;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by awwa on 15/06/14.
 */
public class PostMessageTask extends AsyncTask<String, Integer, Integer> {
    @Override
    protected Integer doInBackground(String... contents) {
        Integer status = -1;
        try {

            String url="https://aqueous-dawn-6184.herokuapp.com/send";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

//            ArrayList<NameValuePair> params = new ArrayList <NameValuePair>();
//            //params.add( new BasicNameValuePair("content", contents[0]));
//            params.add( new BasicNameValuePair("mac", "jajajajaja"));
//            params.add( new BasicNameValuePair("acc", "999.000"));
            StringEntity body = new StringEntity(contents[0]);

            HttpResponse res = null;

            try {
                //post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                post.setEntity(body);
                res = httpClient.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }

            status = Integer.valueOf(res.getStatusLine().getStatusCode());
            String result = EntityUtils.toString(res.getEntity());
            Log.i("TAG", "doInBackground: result" + result);
            Log.i("TAG", "doInBackground: status" + status);
        }
        catch (IOException e) {
            Log.e("TAG", "doInBackground: post error: ", e);
        }

        return status;
    }

}
