package fabiohideki.com.read_sms_mms_messages;

import android.content.ContentResolver;
import android.database.Cursor;
import android.icu.text.MessageFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class SMSMMSDetailActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsmmsdetail);
        String threadID;
        String type;

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            threadID = null;
            type = null;
        } else {
            threadID = extras.getString("thread_id");
            type = extras.getString("type");
        }


        TextView textView = (TextView) findViewById(R.id.tv_detail_id);
        textView.setText(threadID);

        TextView textViewType = (TextView) findViewById(R.id.tv_detail_type);
        textViewType.setText(type);


        TextView textViewDetail = (TextView) findViewById(R.id.tv_detail_column);

        if (("mms").equals(type)) {

            Uri uri = Uri.parse("content://mms/");
            String selection = "thread_id = " + threadID;
            Cursor cursor = getContentResolver().query(uri, null, selection, null, null);

            StringBuilder textBuilder = new StringBuilder();


            while (cursor.moveToNext()) {

                textBuilder.append("\n\n++++MESSAGE++++++ \n");
                textBuilder.append("Address: " + getAddressNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")))) + " \n");

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    textBuilder.append(cursor.getColumnName(i) + " = " + cursor.getString(i) + " \n");

                }

                textBuilder.append("++++PARTS++++++ \n");

                String selectionPart = "mid=" + cursor.getString(cursor.getColumnIndex("_id"));
                Uri uri2 = Uri.parse("content://mms/part");
                Cursor cPart = getContentResolver().query(uri2, null, selectionPart, null, null);

                while (cPart.moveToNext()) {
                    textBuilder.append("+++++++++++++++++++++++++++++++++++++++++++" + "\n");
                    for (int i = 0; i < cPart.getColumnCount(); i++) {
                        textBuilder.append(cPart.getColumnName(i) + " = " + cPart.getString(i) + " \n");
                    }

                }


                cPart.close();
            }
            cursor.close();

            textViewDetail.setText(textBuilder.toString());
            Log.d("fabio-log", textBuilder.toString());

        } else {

            ContentResolver contentResolver = getContentResolver();
            String selection = "thread_id = " + threadID;
            Uri uri = Uri.parse("content://sms");
            Cursor cursor = contentResolver.query(uri, null, selection, null, null);

            StringBuilder builder = new StringBuilder();

            if (cursor.moveToFirst()) {

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    builder.append(cursor.getColumnName(i) + " = " + cursor.getString(i) + " \n");

                }

                textViewDetail.setText(builder.toString());
            }

            cursor.close();

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getAddressNumber(int id) {
        String selectionAdd = new String("msg_id=" + id);
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        Cursor cAdd = getContentResolver().query(uriAddress, null,
                selectionAdd, null, null);
        String name = null;
        if (cAdd.moveToFirst()) {
            do {
                String number = cAdd.getString(cAdd.getColumnIndex("address"));
                if (number != null) {
                    try {
                        Long.parseLong(number.replace("-", ""));
                        name = number;
                    } catch (NumberFormatException nfe) {
                        if (name == null) {
                            name = number;
                        }
                    }
                }
            } while (cAdd.moveToNext());
        }
        if (cAdd != null) {
            cAdd.close();
        }
        return name;
    }

}
