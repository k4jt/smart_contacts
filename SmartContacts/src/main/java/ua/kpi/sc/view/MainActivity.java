package ua.kpi.sc.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import ua.kpi.sc.control.BackupGroups;
import ua.kpi.sc.R;
import ua.kpi.sc.control.CallStatistics;
import ua.kpi.sc.model.Statistics;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button backup = (Button)findViewById(R.id.backup_button);
        final BackupGroups backupGroups = new BackupGroups(this);
        final MainActivity activity = this;

        String userName = getIntent().getStringExtra("user_name");
        final int userId = getIntent().getIntExtra("user_id", 0);
        TextView welcome = (TextView)findViewById(R.id.main_welcome);
        welcome.setText("Welcome, " + userName + "!");


        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CallStatistics callStatistics = new CallStatistics(activity);
                Map<String, Statistics> statistics = callStatistics.getStatistics();

                String report = "Collect statistics for " + statistics.size() + " contacts.";

                AlertDialog dlgAlert  = new AlertDialog.Builder(activity).create();
                dlgAlert.setTitle("Backup");
                if (backupGroups.isUserDidBackup(userId)) {
                    dlgAlert.setMessage("You had already saved your contacts. " + report);
                } else {
                    int pairs = backupGroups.makeBackup(userId);
                    dlgAlert.setMessage("Saved " + pairs + " contacts");
                }
                dlgAlert.setButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dlgAlert.show();
            }
        });





    }



}
