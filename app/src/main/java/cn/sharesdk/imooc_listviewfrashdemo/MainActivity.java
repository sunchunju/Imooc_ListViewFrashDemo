package cn.sharesdk.imooc_listviewfrashdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<String> mDatas;
    private ReFlashListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ReFlashListView) findViewById(R.id.id_list);
        initData();
        mList.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, mDatas));
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i='A';i<'Z';i++){
            mDatas.add((char)i+"");
        }
    }
}
