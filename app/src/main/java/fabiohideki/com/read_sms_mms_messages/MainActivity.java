package fabiohideki.com.read_sms_mms_messages;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.bt_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ColumnSMSMMSActivity.class);
                startActivity(intent);
            }
        });


        int GET_MY_PERMISSION = 1;

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("fabio-mms", "if1");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
                Log.d("fabio", "if2");
            /* do nothing*/
            } else {
                Log.d("fabio", "else2");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, GET_MY_PERMISSION);
            }
        } else {

            ContentResolver contentResolver = getContentResolver();
            final String[] projection = new String[]{"*"};
            Uri uri = Uri.parse("content://mms-sms/conversations/");
            Cursor cursor = contentResolver.query(uri, projection, null, null, null);

            ArrayList<SmsModel> array = new ArrayList();

            while (cursor.moveToNext()) {

                SmsModel smsModel = new SmsModel();

                smsModel.id = cursor.getInt(cursor.getColumnIndex("_id"));
                Log.d("fabio-mms-while", String.valueOf(smsModel.id));
                smsModel.number = cursor.getString(cursor.getColumnIndex("address"));
                smsModel.data = cursor.getLong(cursor.getColumnIndex("date"));
                smsModel.threadId = cursor.getInt(cursor.getColumnIndex("thread_id"));

                String string = cursor.getString(cursor.getColumnIndex("ct_t"));

                if ("application/vnd.wap.multipart.related".equals(string)) {
                    // it's MMS
                    smsModel.type = "mms";
                } else {
                    // it's SMS
                    smsModel.type = "sms";
                }
                array.add(smsModel);
            }

            ListSMSAdapter adapter = new ListSMSAdapter(this, array);

            ListView listView = (ListView) findViewById(R.id.listview_main);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(MainActivity.this, SMSMMSDetailActivity.class);
                    String threadId = ((TextView) view.findViewById(R.id.tv_thread_id)).getText().toString();
                    String type = ((TextView) view.findViewById(R.id.tv_type)).getText().toString();
                    intent.putExtra("thread_id", threadId);
                    intent.putExtra("type", type);
                    startActivity(intent);

                }
            });

            cursor.close();

        }

    } //onCreate


}

    /*  Column: tid
        Column: normalized_date
        Column: sc_timestamp
        Column: kt_tm_type
        Column: extra_data
        Column: group_id
        Column: text_only
        Column: ct_t
        Column: reserve_time
        Column: msg_box
        Column: lgeSec
        Column: v
        Column: ct_cls
        Column: retr_txt_cs
        Column: type
        Column: address
        Column: st
        Column: save_call_type
        Column: person
        Column: tag
        Column: tr_id
        Column: read
        Column: m_id
        Column: body
        Column: lgeCallbackNumber
        Column: lgeExpires
        Column: lgePinRemainCnt
        Column: lgeMac
        Column: m_type
        Column: doInstalled
        Column: lgeMsgType
        Column: locked
        Column: name
        Column: priority
        Column: reply_option
        Column: ui_duplicate
        Column: resp_txt
        Column: retr_st
        Column: lgeCreated
        Column: lgeSiid
        Column: error_code
        Column: sms_imsi_data
        Column: dcs
        Column: index_on_icc
        Column: sms_format
        Column: reply_path_present
        Column: lgeAction
        Column: sub
        Column: original_address
        Column: rr
        Column: status
        Column: ct_l
        Column: message_class
        Column: subject
        Column: _id
        Column: reply_address
        Column: m_size
        Column: exp
        Column: c0_iei
        Column: sub_cs
        Column: kt_is_tm
        Column: sub_id
        Column: date
        Column: resp_st
        Column: imsi_data
        Column: service_msg_sender_address
        Column: simcopy
        Column: date_sent
        Column: pri
        Column: lgeReceived
        Column: protocol
        Column: msg_boxtype
        Column: textlink
        Column: insert_time
        Column: thread_id
        Column: d_rpt
        Column: read_status
        Column: typeex
        Column: lgeDoc
        Column: spam_report
        Column: rpt_a
        Column: m_cls
        Column: service_center
        Column: tag_eng*/