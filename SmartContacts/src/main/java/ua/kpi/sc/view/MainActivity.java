package ua.kpi.sc.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.kpi.sc.control.BackupGroups;
import ua.kpi.sc.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button backup = (Button)findViewById(R.id.backup_button);
        final BackupGroups backupGroups = new BackupGroups(this);
        final MainActivity activity = this;

        String userName = getIntent().getStringExtra("user_name");
        TextView welcome = (TextView)findViewById(R.id.main_welcome);
        welcome.setText("Welcome, " + userName + "!");

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pairs = backupGroups.makeBackup();

                AlertDialog dlgAlert  = new AlertDialog.Builder(activity).create();
                dlgAlert.setMessage("Saved " + pairs + " contacts");
                dlgAlert.setTitle("Backup");
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
