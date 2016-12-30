package cn.carbs.android.testvp;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int FINGER_STATE_UNTOUCH = 0;
    private static final int FINGER_STATE_TOUCH = 1;
    HomeAdapter adapter;
    RecyclerView recyclerView;
    private int fingerState = FINGER_STATE_UNTOUCH;

    private Button button;

    List<String> urls = new ArrayList<>();
    List<String> urls1 = new ArrayList<>();
    List<String> urls2 = new ArrayList<>();
    private boolean flagData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflateListUrls();
        initRefreshHandler();
        button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeData();
            }
        });
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new ScrollSpeedLinearLayoutManger(getBaseContext()));
        urls.clear();
        urls.addAll(urls1);
        adapter = new HomeAdapter(urls);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        fingerState = FINGER_STATE_TOUCH;
                        mHandlerInNewThread.removeMessages(HANDLER_WHAT_REFRESH);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        fingerState = FINGER_STATE_TOUCH;
                        break;
                    case MotionEvent.ACTION_UP:
                        fingerState = FINGER_STATE_UNTOUCH;
                        mHandlerInNewThread.sendMessageDelayed(getMsg(HANDLER_WHAT_REFRESH), 0);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        fingerState = FINGER_STATE_UNTOUCH;
                        mHandlerInNewThread.sendMessageDelayed(getMsg(HANDLER_WHAT_REFRESH), 0);
                        break;
                }
                return false;
            }
        });
    }

    private static final int HANDLER_WHAT_REFRESH = 1;

    private HandlerThread mHandlerThread;
    private Handler mHandlerInNewThread;
    private static final int HANDLER_INTERVAL_REFRESH = 32;//millisecond

    private void initRefreshHandler(){
        mHandlerThread = new HandlerThread("HandlerThread-For-Refreshing");
        mHandlerThread.start();
        mHandlerInNewThread = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case HANDLER_WHAT_REFRESH:
                        int state = recyclerView.getScrollState();

                        if(state != RecyclerView.SCROLL_STATE_IDLE || fingerState == FINGER_STATE_TOUCH){
                            mHandlerInNewThread.sendMessageDelayed(getMsg(HANDLER_WHAT_REFRESH, 0, 0, msg.obj), HANDLER_INTERVAL_REFRESH);
                        }else{

                            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                            if (layoutManager instanceof LinearLayoutManager && recyclerView.getChildCount() > 0) {
                                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                                int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                                View firstView = recyclerView.getChildAt(0);
                                if (firstView.getTop() == 0){
                                    return;
                                }
                                if ( -firstView.getTop()  > recyclerView.getChildAt(0).getHeight() / 2){
                                    //移动到下一个
                                    recyclerView.smoothScrollToPosition(firstItemPosition + 1);
                                }else{
                                    //移动到上一个
                                    recyclerView.smoothScrollToPosition(firstItemPosition);
                                }
                            }
                        }
                        break;
                }
            }
        };
    }

    private void inflateListUrls(){
        urls1.clear();
        urls2.clear();

        urls2.add("http://img2.zol.com.cn/product/106_940x705/773/ceHAk5nVBlJ8g.jpg");
        urls2.add("http://pic11.nipic.com/20101214/213291_155243023914_2.jpg");
        urls2.add("http://img.tuku.cn/file_big/201504/772d5eb2a5ce45b59fa4998c41e51c99.jpg");
        urls2.add("http://www.pp3.cn/uploads/201509/2015090707.jpg");
        urls2.add("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1409/04/c3/38267794_1409842715151_800x600.jpg");
        urls2.add("http://tupian.enterdesk.com/2013/mxy/12/10/15/3.jpg");
        urls2.add("http://tupian.enterdesk.com/2013/mxy/12/11/4/3.jpg");
        urls2.add("http://pic17.nipic.com/20111122/6759425_152002413138_2.jpg");
        urls2.add("http://img4.duitang.com/uploads/item/201410/10/20141010171653_zWBNc.jpeg");
        urls2.add("http://www.pp3.cn/uploads/201504/2015041304.jpg");
        urls2.add("http://tupian.enterdesk.com/2013/lxy/12/27/2/9.jpg");
        urls2.add("http://pic76.nipic.com/file/20150831/10781037_235052985000_2.jpg");
        urls2.add("http://attach.bbs.miui.com/forum/201612/30/152136kieieeityymtal77.jpg");
        urls2.add("http://pic.58pic.com/58pic/13/72/07/55Z58PICKka_1024.jpg");

        urls1.add("http://pic250.quanjing.com/imageclk005/ic01306336.jpg");
        urls1.add("http://pic250.quanjing.com/mf015/mf700-00170409.jpg");
        urls1.add("http://pic250.quanjing.com/imageclk007/ic01706312.jpg");
        urls1.add("http://pic250.quanjing.com/ing021/ing_18941_03710.jpg");
        urls1.add("http://img.zcool.cn/community/01bf1655e514b16ac7251df840273f.jpg");
        urls1.add("http://pic78.huitu.com/res/20160604/1029007_20160604114552332126_1.jpg");
        urls1.add("http://m.tuniucdn.com/filebroker/cdn/snc/5d/5f/5d5fa49a9026507f48c289a342ba7e30_w700_h0_c0_t0.jpg");
        urls1.add("http://img.zcool.cn/community/0171095712079f6ac7251343f65d36.jpg@900w_1l_2o_100sh.jpg");
        urls1.add("http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg");
        urls1.add("http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg");
    }

    private void changeData(){
        urls.clear();
        urls.addAll(flagData ? urls1 : urls2);
        adapter.notifyDataSetChanged();
        flagData = !flagData;
    }

    private Message getMsg(int what){
        return getMsg(what, 0, 0, null);
    }

    private Message getMsg(int what, int arg1, int arg2, Object obj){
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        return msg;
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        List<String> urls;
        public HomeAdapter(List<String> urls){
            this.urls = urls;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getBaseContext()).inflate(R.layout.item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            Glide
                    .with(MainActivity.this)
                    .load(urls.get(position))
                    .centerCrop()
                    .crossFade()
                    .into(holder.iv);
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(),"click position : " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return urls.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            ImageView iv;
            View v;

            public MyViewHolder(View view)
            {
                super(view);
                v = view.findViewById(R.id.cover_view);
                iv = (ImageView) view.findViewById(R.id.image_view);
            }
        }
    }
}
