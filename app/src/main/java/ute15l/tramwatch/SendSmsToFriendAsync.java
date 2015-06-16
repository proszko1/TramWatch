package ute15l.tramwatch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by PAWEL on 2015-06-16.
 */
public class SendSmsToFriendAsync extends AsyncTask<Void,Void,Void>{

    private FragmentActivity activity;
    private String message;
    private ProgressDialog progressDialog;
    private boolean errorFlag;
    private ToSendSmsData toSendSmsData;

    public SendSmsToFriendAsync(FragmentActivity activity, ToSendSmsData toSendSmsData) {
        this.activity = activity;
        this.toSendSmsData = toSendSmsData;
        prepareMessage();
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void prepareMessage() {
        message = "Witaj "+ toSendSmsData.getFriendName() + "! Tu "+toSendSmsData.getYourName()+". Pisze z aplikacji TramWatch. Za "+toSendSmsData.getMinutesToGo()+
                " minut z przystanku "+toSendSmsData.getStopName()+" odjezdza "+toSendSmsData.getVehicleType()+" nr. "+toSendSmsData.getVehicleNumber()+ " w kierunku "+toSendSmsData.getDestination()+
                ". Czekam na ciebie. Zdazysz?";
    }

    @Override
    protected void onPreExecute() {
        errorFlag = false;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.send_sms_to_friend_sending));
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String address = null;
        try {
            address = "https://api.bihapi.pl/orange/oracle/sendsms?to="+ toSendSmsData.getFriendNumber()+"&from=48789102195&msg="+ URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpHost targetHost = new HttpHost("api.bihapi.pl", 443, "https");
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                    new UsernamePasswordCredentials("pw", "testpw4$"));

            sslClient(client);
            HttpGet httpget = new HttpGet(address);
            HttpResponse response = client.execute(httpget);
            Log.i("SMS", response.getStatusLine().toString());

        } catch (IllegalStateException e) {
            errorFlag = true;
            Log.e("LOCATION", "IllegalStateException: " + e);
        } catch (ClientProtocolException e) {
            errorFlag = true;
            Log.e("LOCATION", "ClientProtocolException: " + e);
        } catch (IOException e) {
            errorFlag = true;
            Log.e("LOCATION", "IOException: " + e);
        }
        return null;
    }

    protected void onPostExecute(Void unused) {
        progressDialog.dismiss();
        if(!errorFlag){
            Toast.makeText(activity, activity.getResources().getText(R.string.send_sms_to_friend_sending_success), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, activity.getResources().getText(R.string.send_sms_to_friend_sending_fail), Toast.LENGTH_LONG).show();
        }
    }

    public void sendSMS(String number, String msg) {


    }

    private void sslClient(HttpClient client) {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { tm }, null);
            MySSLSocketFactory ssf = new MySSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));

        } catch (Exception ex) {
        }
    }

}
