package fabiohideki.com.read_sms_mms_messages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hidek on 04/10/2017.
 */

public class ListSMSAdapter extends ArrayAdapter<SmsModel> {

    public ListSMSAdapter(Context context, ArrayList values) {
        super(context, 0, values);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SmsModel smsModel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        TextView textViewId = (TextView) convertView.findViewById(R.id.tv_id);
        TextView textViewFrom = (TextView) convertView.findViewById(R.id.tv_from_number);
        TextView textViewData = (TextView) convertView.findViewById(R.id.tv_data);
        TextView textViewType = (TextView) convertView.findViewById(R.id.tv_type);
        TextView textViewThreadId = (TextView) convertView.findViewById(R.id.tv_thread_id);

        textViewId.setText(String.valueOf(smsModel.id));
        textViewFrom.setText(smsModel.number);
        textViewData.setText(millisToDate(smsModel.data));
        textViewType.setText(smsModel.type);
        textViewThreadId.setText(String.valueOf(smsModel.threadId));


        return convertView;
    }

    public static String millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        Date date = calendar.getTime();
        finalDate = date.toString();
        return finalDate;
    }
}
