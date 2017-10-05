package fabiohideki.com.read_sms_mms_messages;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ColumnSMSMMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_smsmms);

        ContentResolver contentResolver = getContentResolver();
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        StringBuilder textBuilder = new StringBuilder();

        for (int i = 0; i < cursor.getColumnCount(); i++) {

            textBuilder.append(cursor.getColumnName(i) + " \n");

        }

        TextView textView = (TextView) findViewById(R.id.tv_column);
        textView.setText(textBuilder.toString());

        cursor.close();

    }
}
