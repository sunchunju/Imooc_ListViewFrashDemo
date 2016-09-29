package cn.sharesdk.imooc_listviewfrashdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity implements ReFlashListView.ReflashDataListener{

    private ArrayList<String> mDatas;
    private ReFlashListView mList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ReFlashListView) findViewById(R.id.id_list);
        initData();
        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, mDatas);
        mList.setAdapter(adapter);
        mList.setmReflashDataListener(this);
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i='A';i<'Z';i++){
            mDatas.add((char)i+"");
        }
    }

    @Override
    public void onRelashData() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 'a'; i < 'c'; i++) {
                    mDatas.add(0, (char) i + "");
                }
                adapter.notifyDataSetChanged();
                mList.reflashViewByState();
                //刷新完成
                mList.reflashComplete();
            }
        },2000);

    }
}
