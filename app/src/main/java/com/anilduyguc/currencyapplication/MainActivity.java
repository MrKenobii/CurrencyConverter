package com.anilduyguc.currencyapplication;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Spinner dropdown;
    private Spinner dropdown2;
    private TextView internetText;
    private Button button;
    private ArrayList<String> rates;
    private ArrayList<String> codes;
    private HashMap<String, Float> ratesCodesMap;
    private float gbpTo, usdTo, tryTo,eurTo,chfTo = 0.f;
    private float gbpFrom, usdFrom, tryFrom,eurFrom,chfFrom = 0.f;
    private boolean gbpToFlag = false, usdToFlag = false, tryToFlag = false,eurToFlag = false ,chfToFlag = false;
    private boolean gbpFromFlag = false, usdFromFlag = false, tryFromFlag = false,eurFromFlag = false ,chfFromFlag = false;
    private boolean hasInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        internetText = findViewById(R.id.internetTV);
        editText = findViewById(R.id.editText);
        button= findViewById(R.id.button_id);
        dropdown = findViewById(R.id.spinner1);
        dropdown2 = findViewById(R.id.spinner2);
        rates = new ArrayList<>();
        codes = new ArrayList<>();
        ratesCodesMap = new HashMap<>();
        // AUD, USD, CAD, GBP, EUR, TRY,UAH
        String [] items = new String[]{"GBP", "USD","TRY","EUR","CHF"};
        String [] items1 = new String[]{"GBP", "USD","TRY","EUR","CHF"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);

        dropdown.setAdapter(adapter);
        dropdown2.setAdapter(adapter2);

        if( checkNetworkConnection()){
            hasInternet = true;
            new HTTPAsyncTask().execute("https://api.exchangerate.host/latest?format=xml");
        } else hasInternet = false;

    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if(networkInfo!=null &&(isConnected=networkInfo.isConnected())){
            internetText.setText("Connected: " + networkInfo.getTypeName());
            internetText.setBackgroundColor(0xFF7CCC26);
        }
        else {
            internetText.setText("Not Connected");
            internetText.setBackgroundColor(0xFFFF0000);
        }
        return isConnected;
    }
    public void convertCurrency(View view){
        if(hasInternet){
            String val = editText.getText().toString();
            Log.d("Value", val);
            if(!Double.isNaN(Double.parseDouble(val)) && (!val.equals("") || !val.equals(".") || !val.equals(","))){
                float value = Float.parseFloat(val);
                dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                        switch (position){
                            case 0:
                                Log.v("item",(String) adapterView.getItemAtPosition(position));
                                gbpFrom = ratesCodesMap.get("GBP");
                                gbpFromFlag = true;
                                usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; chfFromFlag = false;
                                break;

                            case 1:
                                usdFrom = ratesCodesMap.get("USD");
                                usdFromFlag = true;
                                gbpFromFlag = false; tryFromFlag = false; eurFromFlag = false; chfFromFlag = false;
                                break;
                            case 2:
                                tryFrom = ratesCodesMap.get("TRY");
                                tryFromFlag = true;
                                usdFromFlag = false; gbpFromFlag = false; eurFromFlag = false; chfFromFlag = false;
                                break;
                            case 3:
                                eurFrom = ratesCodesMap.get("EUR");
                                eurFromFlag = true;
                                usdFromFlag = false; tryFromFlag = false; gbpFromFlag = false; chfFromFlag = false;
                                break;
                            case 4:
                                chfFrom = ratesCodesMap.get("CHF");
                                chfFromFlag = true;
                                usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; gbpFromFlag = false;
                                break;

                        }

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        switch (position){
                            case 0:
                                Log.v("item",(String) adapterView.getItemAtPosition(position));
                                gbpTo = ratesCodesMap.get("GBP");
                                gbpToFlag = true;
                                usdToFlag = false; tryToFlag = false; eurToFlag = false; chfToFlag = false;
                                break;

                            case 1:
                                usdTo = ratesCodesMap.get("USD");
                                usdToFlag = true;
                                gbpToFlag = false; tryToFlag = false; eurToFlag = false; chfToFlag = false;
                                break;
                            case 2:
                                tryTo = ratesCodesMap.get("TRY");
                                tryToFlag = true;
                                gbpToFlag = false; usdToFlag = false; eurToFlag = false; chfToFlag = false;
                                break;
                            case 3:
                                eurTo = ratesCodesMap.get("EUR");
                                eurToFlag = true;
                                gbpToFlag = false; tryToFlag = false; usdToFlag = false; chfToFlag = false;
                                break;
                            case 4:
                                chfTo = ratesCodesMap.get("CHF");
                                chfToFlag = true;
                                gbpToFlag = false; tryToFlag = false; eurToFlag = false; usdToFlag = false;
                                break;

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                if(chfToFlag && chfFromFlag){
                    textView.setText(String.valueOf(((value * chfFrom) / chfTo)));
                } else if(chfToFlag && usdFromFlag){
                    textView.setText(String.valueOf(((value * usdFrom) / chfTo)));
                } else if(chfToFlag && eurFromFlag){
                    textView.setText(String.valueOf(((value * eurFrom) / chfTo)));
                } else if(chfToFlag && tryFromFlag){
                    textView.setText(String.valueOf(((value * tryFrom) / chfTo)));
                } else if(chfToFlag && gbpFromFlag){
                    textView.setText(String.valueOf(((value * gbpFrom) / chfTo)));
                } else if(usdToFlag && usdFromFlag){
                    textView.setText(String.valueOf(((value * usdFrom) / usdTo)));
                } else if(usdToFlag && chfFromFlag){
                    textView.setText(String.valueOf(((value * chfFrom) / usdTo)));
                } else if(usdToFlag && eurFromFlag){
                    textView.setText(String.valueOf(((value * eurFrom) / usdTo)));
                } else if(usdToFlag && tryFromFlag){
                    textView.setText(String.valueOf(((value * tryFrom) / usdTo)));
                } else if(usdToFlag && gbpFromFlag){
                    textView.setText(String.valueOf(((value * gbpFrom) / usdTo)));
                } else if(eurToFlag && eurFromFlag){
                    textView.setText(String.valueOf(((value * eurFrom) / eurTo)));
                } else if(eurToFlag && chfFromFlag){
                    textView.setText(String.valueOf(((value * chfFrom) / eurTo)));
                } else if(eurToFlag && usdFromFlag){
                    textView.setText(String.valueOf(((value * usdFrom) / eurTo)));
                } else if(eurToFlag && tryFromFlag){
                    textView.setText(String.valueOf(((value * tryFrom) / eurTo)));
                } else if(eurToFlag && gbpFromFlag){
                    textView.setText(String.valueOf(((value * gbpFrom) / eurTo)));
                } else if(tryToFlag && tryFromFlag){
                    textView.setText(String.valueOf(((value * tryFrom) / tryTo)));
                } else if(tryToFlag && chfFromFlag){
                    textView.setText(String.valueOf(((value * chfFrom) / tryTo)));
                } else if(tryToFlag && usdFromFlag){
                    textView.setText(String.valueOf(((value * usdFrom) / tryTo)));
                } else if(tryToFlag && eurFromFlag){
                    textView.setText(String.valueOf(((value * eurFrom) / tryTo)));
                } else if(tryToFlag && gbpFromFlag){
                    textView.setText(String.valueOf(((value * gbpFrom) / tryTo)));
                } else if(gbpToFlag && gbpFromFlag){
                    textView.setText(String.valueOf(((value * gbpFrom) / gbpTo)));
                } else if(gbpToFlag && chfFromFlag){
                    textView.setText(String.valueOf(((value * chfFrom) / gbpTo)));
                } else if(gbpToFlag && usdFromFlag){
                    textView.setText(String.valueOf(((value * usdFrom) / gbpTo)));
                } else if(gbpToFlag && tryFromFlag){
                    textView.setText(String.valueOf(((value * tryFrom) / gbpTo)));
                } else if(gbpToFlag && eurFromFlag){
                    textView.setText(String.valueOf(((value * eurFrom) / gbpTo)));
                }
            } else Toast.makeText(MainActivity.this, "Please enter a valid value!", Toast.LENGTH_SHORT).show();
        } else textView.setText("No internet access");
    }
    private class HTTPAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                return HttpGet(strings[0]);
            }catch (IOException exception){
                return "Something went wrong. Please check the url and internet connection.";
            }
        }

        private String HttpGet(String string) throws IOException {
            InputStream inputStream = null;
            String result = "";
            URL url = new URL(string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            inputStream = conn.getInputStream();
            if(inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Something went wrong. Please check the url";
            Log.d("Result in HttpGet method", result);
            //textView.setText(result);
            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line  = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null){
                result += line;
            }
            inputStream.close();
            return result;
        }
        public void XMLParser(String result) throws XmlPullParserException, IOException {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(new StringReader(result));
            int eventType = xmlPullParser.getEventType();
            String newResult = "";
            String tag = "";
            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_DOCUMENT){
                    newResult = "This IP Belongs to";
                } else if(eventType == XmlPullParser.START_TAG){
                    tag = xmlPullParser.getName();
                } else if(eventType == XmlPullParser.END_TAG){

                } else if(eventType==XmlPullParser.TEXT){
                    if(tag.equals("rate")){
                        rates.add(xmlPullParser.getText());
                        newResult = newResult + System.getProperty("line.separator") + tag + ": " + xmlPullParser.getText();
                        tag = "";
                    }
                    else if(tag.equals("code")){
                        codes.add(xmlPullParser.getText());
                        tag = "";
                    }
                }
                eventType = xmlPullParser.next();
            }
            this.listToMap();
        }
        private void listToMap(){
            for(int i=0; i<rates.size(); i++){
                ratesCodesMap.put(codes.get(i), Float.valueOf(rates.get(i)));
            }
            for(Map.Entry<String, Float> entry: ratesCodesMap.entrySet()){
                Log.d("HASHMAP", entry.getKey() + ": " + entry.getValue());
            }
        }
        protected void onPostExecute(String s){
            try {
                XMLParser(s);
            }catch (XmlPullParserException | IOException e){
                e.printStackTrace();
            }
        }
    }
}