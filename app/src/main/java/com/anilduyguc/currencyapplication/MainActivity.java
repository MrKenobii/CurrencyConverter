package com.anilduyguc.currencyapplication;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private boolean gbpToFlag, usdToFlag, tryToFlag, eurToFlag, chfToFlag, audToFlag, cadToFlag, uahToFlag, rubToFlag;
    private boolean gbpFromFlag, usdFromFlag, tryFromFlag, eurFromFlag, chfFromFlag, audFromFlag, cadFromFlag, uahFromFlag, rubFromFlag;
    private boolean hasInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gbpToFlag = true; usdToFlag = false; tryToFlag = false; eurToFlag = false;chfToFlag = false; audToFlag = false; cadToFlag = false; uahToFlag = false; rubToFlag=false;
        gbpFromFlag=true; usdFromFlag=false; tryFromFlag=false; eurFromFlag=false; chfFromFlag=false; cadFromFlag=false; audFromFlag=false; uahFromFlag=false; rubFromFlag=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        internetText = findViewById(R.id.internetTV);
        editText = findViewById(R.id.editText);
        button= findViewById(R.id.button_id);
        dropdown = findViewById(R.id.spinner1); //Left
        dropdown2 = findViewById(R.id.spinner2); //Right
        rates = new ArrayList<>();
        codes = new ArrayList<>();
        ratesCodesMap = new HashMap<>();
        // AUD, USD, CAD, GBP, EUR, TRY,UAH
        final List<String> items = Arrays.asList("GBP", "USD","TRY","EUR","CHF", "AUD", "CAD", "UAH", "RUB");
        final List<Integer> flags = Arrays.asList(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i);

        CustomAdapter adapterLeft = new CustomAdapter(getApplicationContext(), flags, items);
        CustomAdapter adapterRight = new CustomAdapter(getApplicationContext(), flags, items);


        dropdown.setAdapter(adapterLeft);
        dropdown2.setAdapter(adapterRight);

        if(checkNetworkConnection()){
            hasInternet = true;
            new HTTPAsyncTask().execute("https://api.exchangerate.host/latest?format=xml");
        } else hasInternet = false;
        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                switch (position){
                    case 0:
                        gbpFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; chfFromFlag = false; audFromFlag=false; cadFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;

                    case 1:
                        usdFromFlag = true;
                        gbpFromFlag = false; tryFromFlag = false; eurFromFlag = false; chfFromFlag = false;  audFromFlag=false; cadFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;
                    case 2:
                        tryFromFlag = true;
                        usdFromFlag = false; gbpFromFlag = false; eurFromFlag = false; chfFromFlag = false; audFromFlag=false; cadFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;
                    case 3:
                        eurFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; gbpFromFlag = false; chfFromFlag = false; audFromFlag=false; cadFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;
                    case 4:
                        chfFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; gbpFromFlag = false; audFromFlag=false; cadFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;
                    case 5:
                        //"AUD", "CAD", "UAH", "RUB"
                        audFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; gbpFromFlag = false; chfFromFlag=false; cadFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;
                    case 6:
                        //"AUD", "CAD", "UAH", "RUB"
                        cadFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; gbpFromFlag = false; chfFromFlag=false; audFromFlag = false; uahFromFlag=false; rubFromFlag=false;
                        break;
                    case 7:
                        //"AUD", "CAD", "UAH", "RUB"
                        uahFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; gbpFromFlag = false; chfFromFlag=false; cadFromFlag = false; audFromFlag=false; rubFromFlag=false;
                        break;
                    case 8:
                        //"AUD", "CAD", "UAH", "RUB"
                        rubFromFlag = true;
                        usdFromFlag = false; tryFromFlag = false; eurFromFlag = false; gbpFromFlag = false; chfFromFlag=false; cadFromFlag = false; uahFromFlag=false; audFromFlag=false;
                        break;
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0:
                        gbpToFlag = true;
                        usdToFlag = false; tryToFlag = false; eurToFlag = false; chfToFlag = false; audToFlag=false; cadToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;

                    case 1:
                        usdToFlag = true;
                        gbpToFlag = false; tryToFlag = false; eurToFlag = false; chfToFlag = false; audToFlag=false; cadToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;
                    case 2:
                        tryToFlag = true;
                        gbpToFlag = false; usdToFlag = false; eurToFlag = false; chfToFlag = false; audToFlag=false; cadToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;
                    case 3:
                        eurToFlag = true;
                        gbpToFlag = false; tryToFlag = false; usdToFlag = false; chfToFlag = false; audToFlag=false; cadToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;
                    case 4:
                        chfToFlag = true;
                        gbpToFlag = false; tryToFlag = false; eurToFlag = false; usdToFlag = false; audToFlag=false; cadToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;
                    case 5:
                        //"AUD", "CAD", "UAH", "RUB"
                        audToFlag = true;
                        gbpToFlag = false; usdToFlag = false; eurToFlag = false; chfToFlag = false; tryToFlag=false; cadToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;
                    case 6:
                        cadToFlag = true;
                        gbpToFlag = false; usdToFlag = false; eurToFlag = false; chfToFlag = false; audToFlag=false; tryToFlag = false; uahToFlag=false; rubToFlag=false;
                        break;
                    case 7:
                        uahToFlag = true;
                        gbpToFlag = false; usdToFlag = false; eurToFlag = false; chfToFlag = false; audToFlag=false; cadToFlag = false; tryToFlag=false; rubToFlag=false;
                        break;
                    case 8:
                        //"AUD", "CAD", "UAH", "RUB"
                        rubToFlag = true;
                        gbpToFlag = false; usdToFlag = false; eurToFlag = false; chfToFlag = false; audToFlag=false; cadToFlag = false; uahToFlag=false; tryToFlag=false;
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
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
        Log.d("IsConnected", String.valueOf(isConnected));
        return isConnected;
    }
    public void convertCurrency(View view){
        try {
            if(hasInternet){
                String val = editText.getText().toString();
                Log.d("Value", val);
                if(!Float.isNaN(Float.parseFloat(val)) && (!val.equals("") || !val.equals(".") || !val.equals(","))){
                try {
                    float value = Float.parseFloat(val);
                    //"AUD", "CAD", "UAH", "RUB"
                    if(chfToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value * ratesCodesMap.get("CHF")) / ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value * ratesCodesMap.get("USD")) / ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("CHF"))));
                    } else if(chfToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("CHF"))));
                    } else if(usdToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value * ratesCodesMap.get("TRY")) / ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("USD"))));
                    } else if(usdToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("USD"))));
                    } else if(eurToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("EUR"))));
                    } else if(eurToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) /  ratesCodesMap.get("EUR"))));
                    } else if(eurToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("EUR"))));
                    } else if(eurToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("EUR"))));
                    } else if(eurToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("EUR"))));
                    }  else if(eurToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("EUR"))));
                    }  else if(eurToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("EUR"))));
                    }  else if(eurToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("EUR"))));
                    }  else if(eurToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("EUR"))));
                    } else if(tryToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("TRY"))));
                    } else if(tryToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("TRY"))));
                    } else if(gbpToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) / ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("GBP"))));
                    } else if(gbpToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("GBP"))));
                    } else if(audToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) / ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("AUD"))));
                    } else if(audToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("AUD"))));
                    } else if(cadToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) / ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("CAD"))));
                    } else if(cadToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("CAD"))));
                    } else if(uahToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) / ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("UAH"))));
                    } else if(uahToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("UAH"))));
                    } else if(rubToFlag && gbpFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("GBP")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && chfFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CHF")) / ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && usdFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("USD")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && tryFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("TRY")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && eurFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("EUR")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && audFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("AUD")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && cadFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("CAD")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && uahFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("UAH")) /  ratesCodesMap.get("RUB"))));
                    } else if(rubToFlag && rubFromFlag){
                        textView.setText(String.valueOf(((value *  ratesCodesMap.get("RUB")) /  ratesCodesMap.get("RUB"))));
                    }
                } catch (NumberFormatException | IllegalStateException ex){
                    Toast.makeText(MainActivity.this, "Please enter a valid value!", Toast.LENGTH_SHORT).show();
                }
            } else Toast.makeText(MainActivity.this, "Please enter a valid value!", Toast.LENGTH_SHORT).show();
            } else textView.setText("No internet access");
        } catch (IllegalStateException | NumberFormatException e){
            Toast.makeText(MainActivity.this, "Please enter a valid value!", Toast.LENGTH_SHORT).show();
        }
    }
    private class HTTPAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d("Income strings in doInBackground", Arrays.toString(strings));
                String s = HttpGet(strings[0]);
                Log.d("Value of HttpGet return", s);
                return s;
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

            Log.v("Result in HttpGet method", result);
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
            Log.d("Result in convertInputStreamToString", result);
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
                        newResult = newResult + (System.getProperty("line.separator")) + tag +": " +xmlPullParser.getText();
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
                Log.v("onPosExecute method", s);
                XMLParser(s);
            }catch (XmlPullParserException | IOException e){
                e.printStackTrace();
            }
        }
    }
}