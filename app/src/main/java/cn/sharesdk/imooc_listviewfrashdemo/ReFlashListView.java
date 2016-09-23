package cn.sharesdk.imooc_listviewfrashdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

/**
 * Created by suncj1 on 2016/9/23.
 */
public class ReFlashListView extends ListView {

    View headerView;
    public ReFlashListView(Context context) {
        this(context, null);
    }

    public ReFlashListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReFlashListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        headerView = inflater.inflate(R.layout.header_layout, null);
        this.addHeaderView(headerView);  //listview添加头布局
    }
}
