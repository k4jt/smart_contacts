package ua.kpi.sc.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ua.kpi.sc.R;
import ua.kpi.sc.clustering.ClusteringUtils;
import ua.kpi.sc.control.BackupGroups;
import ua.kpi.sc.control.StatisticsCollector;
import ua.kpi.sc.dao.Group;
import ua.kpi.sc.model.ContactGroupPair;
import ua.kpi.sc.model.PersonStatistics;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button start = (Button)findViewById(R.id.start_button);
        Button backup = (Button)findViewById(R.id.backup_button);
        final BackupGroups backupGroups = new BackupGroups(this);
        final MainActivity activity = this;

        String userName = getIntent().getStringExtra("user_name");
        final int userId = getIntent().getIntExtra("user_id", 0);
        TextView welcome = (TextView)findViewById(R.id.main_welcome);
        welcome.setText("Welcome, " + userName + "!");

        List<ContactGroupPair> pairs = backupGroups.getGroups();
        if (!backupGroups.isUserDidBackup(userId)) {
            backupGroups.makeBackup(userId, pairs);
            AlertDialog dlgAlert  = new AlertDialog.Builder(activity).create();
            dlgAlert.setTitle("Backup");
            dlgAlert.setMessage("Backup successfully completed. Saved " + pairs.size() + " contacts");
            dlgAlert.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dlgAlert.show();
        }

        final Group group = new Group(activity);
        for (ContactGroupPair pair : pairs) {
            group.deleteContactFromAllGroups(pair.contactId);
        }
        for (ContactGroupPair pair : pairs) {
            group.hideGroup(pair.groupId);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: +1. delete contacts from current groups
                //TODO: +2. hide current group
                //TODO: +3. make clusterization
                //TODO: +4. create new groups for clusters
                //TODO: +5. assign contacts to new group

                HashMap<String, String> contactIdToGroupName = null;
                try {
                    StatisticsCollector collector = StatisticsCollector.getInstance(activity);
                    while (!collector.isFinishedStatistics()) try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                    contactIdToGroupName = ClusteringUtils.clustereIt(collector.getStatistics());
                } catch (IOException e) {}

                Map<String, Integer> groupNameToGroupId = new HashMap<String, Integer>();

                for (Map.Entry entry : contactIdToGroupName.entrySet()) {
                    int contactId = Integer.parseInt((String)entry.getKey());
                    String groupName = (String)entry.getValue();

                    Integer groupId = groupNameToGroupId.get(groupName);
                    if (groupId == null) {
                        groupId = group.createGroup(groupName);
                    }

                    if (groupId != null && groupId != 0) {
                        group.addToGroup(contactId, groupId);
                    }
                }

                AlertDialog dlgAlert  = new AlertDialog.Builder(activity).create();
                dlgAlert.setTitle("Grouping");
                dlgAlert.setMessage("Grouping successfully completed.");
                dlgAlert.setButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dlgAlert.show();

            }
        });


        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: +1. delete contacts from current groups
                //TODO: +2. hide current groups
                //TODO: +3. read backup
                //TODO: +4. assign contacts to particular groups
                //TODO: +5. make groups visible

                List<ContactGroupPair> currentPairs = backupGroups.getGroups();
                for (ContactGroupPair pair : currentPairs) {
                    group.deleteContactFromAllGroups(pair.contactId);
                }
                for (ContactGroupPair pair : currentPairs) {
                    group.hideGroup(pair.groupId);
                }

                List<ContactGroupPair> savedPairs = backupGroups.getBackup(userId);
                Set<Integer> uniqueGroups = new HashSet<Integer>();
                for (ContactGroupPair pair : savedPairs) {
                    group.addToGroup(pair.contactId, pair.groupId);
                    uniqueGroups.add(pair.groupId);
                }

                for (Integer groupId : uniqueGroups) {
                    group.showGroup(groupId);
                }

                AlertDialog dlgAlert  = new AlertDialog.Builder(activity).create();
                dlgAlert.setTitle("Backup");
                dlgAlert.setMessage("Backup successfully completed.");
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
