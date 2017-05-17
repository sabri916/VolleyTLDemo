package om.metamorph.volleytldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;

    private Button registerButton;

    private TextView responseTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText) findViewById(R.id.et_email);
        nameEditText = (EditText) findViewById(R.id.et_name);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        phoneEditText = (EditText) findViewById(R.id.et_phone);

        responseTextView = (TextView) findViewById(R.id.tv_response);

        registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                registerUser(email,name,password,phone);
            }
        });

    }

    private void registerUser(String email, String name, String password, String phone) {

        //Create Queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //url
        String url = "http://10.0.2.2:8080/rest/users";

        //body
        JSONObject body = new JSONObject();

        try {
            body.put("email", email);
            body.put("authentication_type", "email");
            body.put("password", password);
            body.put("name", name);
            body.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //create request
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseTextView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseTextView.setText(error.toString());
                    }
                }){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        Log.i(TAG, response.statusCode+" Authorization: " + response.headers.get("Authorization"));
                        return super.parseNetworkResponse(response);
                    }
                };
        requestQueue.add(jsonObjectRequest);
    }
}
