package ua.kpi.sc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button backup = (Button)findViewById(R.id.backup_button);
        final BackupGroups backupGroups = new BackupGroups(this);
        final MainActivity activity = this;


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
