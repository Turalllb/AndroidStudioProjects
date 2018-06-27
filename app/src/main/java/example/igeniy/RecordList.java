package example.igeniy;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static example.igeniy.RecordAdapter.TimeRecord;
import static example.igeniy.RecordAdapter.TimeRecordStr;
import static example.igeniy.StartActivity.typeface;

/**
 * Created by Турал on 08.05.2017.
 */

public class RecordList extends ListActivity {

    private ArrayAdapter<Integer> mAdapter;
    private ArrayAdapter<String>  mAdapterStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.records);




        // создаем адаптер
        mAdapterStr = new ArrayAdapter<String>(this,
                R.layout.list_text, TimeRecordStr) {

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                 convertView = super.getView(position, convertView, parent);
                //if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_text, null);

                //TextView textView = (TextView) view.findViewById(R.id.text1); //так TextView в разметке один, для экономии ресурсов (каких ресурсов?) можно не искать его, а использовать прямое приведение как в строке внизу и сделано.
                ((TextView)convertView).setTypeface(typeface);
                return convertView;
            }
        };


        //подключаем адаптер
        setListAdapter(mAdapterStr);
    }
}
