package cn.sharesdk.imooc_listviewfrashdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suncj1 on 2016/9/23.
 */
public class ReFlashListView extends ListView implements AbsListView.OnScrollListener{

    public static final String TAG = "ReFlashListView";
    View headerView;
    int headerHeight;
    int firstVisibleItem;//当前可见的第一个item位置
    boolean isTopDown = false;//是否从ListView最顶端时按下
    int startY;

    int state; //当前状态
    final int NORMAL = 0; //正常状态
    final int PULL = 1; //下拉刷新状态
    final int RELEASE = 2; //松开刷新状态
    final int REFLASHING = 3; //正在刷新状态

    int scrollState;//listview当前的滚动状态

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
        headerHeight = getHeaderViewHeight();
        Log.i("suncj",TAG+" headerHeight = "+headerHeight);
        setTopPadding(-headerHeight);
        this.addHeaderView(headerView);  //listview添加头布局
        this.setOnScrollListener(this);
    }

    private int getHeaderViewHeight() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        headerView.measure(w, h);
        return headerView.getMeasuredHeight();
    }

    private void setTopPadding(int tpoPadding) {
        headerView.setPadding(headerView.getPaddingLeft(), tpoPadding, headerView.getPaddingRight(), headerView.getPaddingBottom());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("suncj", TAG + " onMeasure ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("suncj", TAG + " onDraw ");
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN: //按下
                if (firstVisibleItem == 0){
                    isTopDown = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE: //移动
                onMove(ev);
                reflashViewByState();
                invalidate();
                break;
            case MotionEvent.ACTION_UP: //抬起
                //判断手指抬起时的状态
                if (state == RELEASE){
                    //如果是“松开可以刷新”状态，转变状态，并且刷新数据
                    state = REFLASHING;
                }else if (state == PULL){
                    //在提示“下拉可以刷新”状态抬起，恢复当前状态，并且将标记为重置
                    state = NORMAL;
                    isTopDown = false;
                }else if (state == REFLASHING){
                    state = NORMAL;
                    isTopDown = true;
                }
                reflashViewByState();
                invalidate();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onMove(MotionEvent ev) {
        if (!isTopDown ){
            return; //不是从ListView顶端位置按下的，直接return
        }
        int tempY = (int) ev.getY();
        int space = tempY - startY; //移动的距离
        int topPadding = space - headerHeight;
        switch (state){
            case NORMAL:
                if (space > 0){
                    state = PULL;
                }
                break;
            case PULL:
                setTopPadding(topPadding); //设置上边距
                if (space > headerHeight+30 && scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    state = RELEASE;
                }
                break;
            case RELEASE:
                setTopPadding(topPadding);
                if (space < headerHeight+30){
                    state = PULL;
                }else if (space <= 0){  //向下滑动listview，则设置state为normal
                    state = NORMAL;
                    isTopDown = false;
                }
                break;
            case REFLASHING:
                setTopPadding(topPadding);
                break;

        }

    }

    /**
     * 根据当前状态改变界面显示*/
    private void reflashViewByState(){
        TextView tip = (TextView) headerView.findViewById(R.id.tv_tip);
        ImageView arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        ProgressBar progressBar = (ProgressBar) headerView.findViewById(R.id.progress);
        //箭头添加动画
        RotateAnimation anim = new RotateAnimation(0,180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        RotateAnimation anim1 = new RotateAnimation(180,0,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        switch (state){
            case NORMAL:
                arrow.clearAnimation();
                setTopPadding(-headerHeight);
                break;
            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("下拉可以刷新");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case RELEASE:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("松开可以刷新");
                anim.setDuration(500);
                anim.setFillAfter(true);
                break;
            case REFLASHING:
                setTopPadding(headerHeight);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tip.setText("正在刷新");
                break;
        }
    }

    public void reflashComplete(){
        state = NORMAL;
        isTopDown = false;
        reflashViewByState();
        TextView lastupdateTime = (TextView) headerView.findViewById(R.id.tv_lastupdatetime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastupdateTime.setText(time);
        setTopPadding(-headerHeight);
        invalidate();
    }
}
