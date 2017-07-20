package demofilereadwriting.android.myapplicationdev.com.p09_gettingmylocations;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CheckRecordActivity extends AppCompatActivity {

    Button refresh;
    TextView records;
    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);


        lv = (ListView) findViewById(R.id.lv);
        refresh = (Button)findViewById(R.id.refresh);
        records = (TextView)findViewById(R.id.recordNumber);
        al = new ArrayList<>();
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);

        File targetFile = new File( Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Location", "location.txt");

        if (targetFile.exists() == true){
            String data ="";
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null){
                    data += line + "\n";
                    line = br.readLine();


                }
                br.close();
                reader.close();
                String[] dataArray = data.split("\n");
                for (int i = 0; i < dataArray.length; i++){
                    al.add(dataArray[i]);
                }
                lv.setAdapter(aa);
                aa.notifyDataSetChanged();

                records.setText("Number of Records: " + dataArray.length);


            } catch (Exception e) {
                Toast.makeText(CheckRecordActivity.this, "Failed to read!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                al.clear();


                File targetFile = new File( Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/Location", "location.txt");

                if (targetFile.exists() == true){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null){
                            data += line + "\n";
                            line = br.readLine();


                        }
                        br.close();
                        reader.close();
                        String[] dataArray = data.split("\n");
                        for (int i = 0; i < dataArray.length; i++){
                            al.add(dataArray[i]);
                        }
                        lv.setAdapter(aa);
                        aa.notifyDataSetChanged();

                        records.setText("Number of Records: " + dataArray.length);


                    } catch (Exception e) {
                        Toast.makeText(CheckRecordActivity.this, "Failed to read!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


            }
        });

    }
}
