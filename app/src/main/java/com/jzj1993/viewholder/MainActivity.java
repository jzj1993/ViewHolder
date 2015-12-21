package com.jzj1993.viewholder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;


public class MainActivity extends Activity {

    private ListView mListView;
    private ViewPager mViewPager;
    private Button mButton;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mButton = (Button) findViewById(R.id.btn_adapter);

        mListView.setAdapter(mAdapter1);
        mViewPager.setAdapter(mPagerAdapter1);
        mViewPager.setOffscreenPageLimit(1);

        mRadioGroup.check(R.id.radioButton1);
        refresh(R.id.radioButton1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAdapter();
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                refresh(checkedId);
            }
        });
    }

    private void refresh(int id) {
        boolean b = id == R.id.radioButton2;
        mListView.setVisibility(b ? View.GONE : View.VISIBLE);
        mViewPager.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private View createDifferentView(int d) {
        switch (d) {
            case 0:
                TextView t = new TextView(this);
                t.setText("text");
                return t;
            case 1:
                Button b = new Button(this);
                b.setText("button");
                b.setTextSize(30);
                return b;
            case 2:
            default:
                ImageView img = new ImageView(this);
                img.setImageResource(R.mipmap.ic_launcher);
                return img;
        }
    }

    private void log(String s) {
        Log.d("TAG", s);
    }

    public void selectAdapter() {
        if (mRadioGroup.getCheckedRadioButtonId() == R.id.radioButton1) {
            new AlertDialog.Builder(this)
                    .setItems(mItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mListView.setAdapter(mListAdapters[i]);
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setItems(mPagerItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mViewPager.setAdapter(mPagerAdapters[i]);
                        }
                    })
                    .show();
        }
    }

    abstract class Adapter extends BaseAdapter {

        public abstract String getTitle();

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    @SuppressLint("ViewHolder")
    private Adapter mAdapter1 = new Adapter() {
        @Override
        public String getTitle() {
            return "不使用缓存";
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 创建View
            View view = getLayoutInflater().inflate(R.layout.item, parent, false);
            // 获取对TextView的引用
            TextView textView = (TextView) view.findViewById(R.id.txt);
            // 刷新数据
            textView.setText(String.valueOf(position));
            return view;
        }
    };

    private Adapter mAdapter2 = new Adapter() {

        @Override
        public String getTitle() {
            return "所有项保存到内存";
        }

        HashMap<Integer, View> map = new HashMap<>();

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = map.get(position);
            if (view == null) {
                // 创建View
                view = getLayoutInflater().inflate(R.layout.item, parent, false);
                // 获取对TextView的引用
                TextView textView = (TextView) view.findViewById(R.id.txt);
                // 刷新数据
                textView.setText(String.valueOf(position));
                map.put(position, view);
            }
            return view;
        }
    };

    private Adapter mAdapter3 = new Adapter() {
        @Override
        public String getTitle() {
            return "ListView内建的缓存";
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // 创建View
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
            }
            // 获取对TextView的引用
            TextView textView = (TextView) convertView.findViewById(R.id.txt);
            // 刷新数据
            textView.setText(String.valueOf(position));
            return convertView;
        }
    };

    private Adapter mAdapter4 = new Adapter() {

        @Override
        public String getTitle() {
            return "ViewHolder";
        }

        class ViewHolder {
            TextView textView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // 创建View
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
                holder = new ViewHolder();
                // 获取对TextView的引用
                holder.textView = (TextView) convertView.findViewById(R.id.txt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置数据
            holder.textView.setText(String.valueOf(position));
            return convertView;
        }
    };

    private Adapter mAdapter5 = new Adapter() {

        @Override
        public String getTitle() {
            return "ViewHolder重构";
        }

        class ViewHolder {
            TextView textView;

            ViewHolder(View root) {
                // 获取对TextView的引用
                textView = (TextView) root.findViewById(R.id.txt);
            }

            void setData(final String s) {
                // 刷新数据
                textView.setText(s);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // 创建View
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.setData(String.valueOf(position));
            return convertView;
        }
    };

    private Adapter mAdapter6 = new Adapter() {

        @Override
        public String getTitle() {
            return "通用ViewHolder工具类";
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
            }
            Holder holder = Holder.getHolder(convertView);
            TextView textView = holder.getView(R.id.txt);
            textView.setText(String.valueOf(position));
            return convertView;
        }
    };

    public static class Holder {

        private final SparseArray<View> mViews;
        private View mConvertView;

        private Holder(View convertView) {
            mViews = new SparseArray<View>();
            mConvertView = convertView;
            mConvertView.setTag(this);
        }

        public static Holder getHolder(View convertView) {
            if (convertView == null) {
                throw new NullPointerException("mConvertView cannot be null!");
            }
            if (convertView.getTag() == null) {
                return new Holder(convertView);
            } else {
                return (Holder) convertView.getTag();
            }
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            //noinspection unchecked
            return (T) view;
        }
    }

    private Adapter mAdapter7 = new Adapter() {

        @Override
        public String getTitle() {
            return "加载数据错乱";
        }

        class ViewHolder {
            TextView textView;

            ViewHolder(View root) {
                // 获取对TextView的引用
                textView = (TextView) root.findViewById(R.id.txt);
            }

            void setData(final String s) {
                // 刷新数据
                textView.setText(s);
            }
        }

        private void setData(final ViewHolder holder, final int position) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.setData(String.valueOf(position));
                }
            }, (long) (Math.random() * 2000) + 1000);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // 创建View
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            setData(holder, position);
            return convertView;
        }
    };

    private Adapter mAdapter8 = new Adapter() {

        @Override
        public String getTitle() {
            return "加载数据错乱解决";
        }

        class ViewHolder {
            TextView textView;

            ViewHolder(View root) {
                // 获取对TextView的引用
                textView = (TextView) root.findViewById(R.id.txt);
            }

            void setData(final String s) {
                // 刷新数据
                textView.setText(s);
            }
        }

        private HashMap<Integer, String> mData = new HashMap<>();

        private void setData(ViewHolder holder, final int position) {
            String data = mData.get(position);
            holder.setData(data);
            if (data == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.put(position, String.valueOf(position));
                        notifyDataSetChanged();
                    }
                }, (long) (Math.random() * 2000) + 1000);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // 创建View
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            setData(holder, position);
            return convertView;
        }
    };

    private Adapter mAdapter9 = new Adapter() {

        @Override
        public String getTitle() {
            return "ListView每项结构不同";
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return createDifferentView(position % 3);
        }
    };

    private Adapter[] mListAdapters = {mAdapter1, mAdapter2, mAdapter3, mAdapter4, mAdapter5, mAdapter6, mAdapter7, mAdapter8, mAdapter9};
    private String[] mItems;

    {
        mItems = new String[mListAdapters.length];
        for (int i = 0; i < mListAdapters.length; ++i) {
            mItems[i] = mListAdapters[i].getTitle();
        }
    }

    private PagerAdapter mPagerAdapter1 = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView((View) object);
            log("destroy: " + position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = getLayoutInflater().inflate(R.layout.item, container, false);
            container.addView(v);
            TextView t = (TextView) v.findViewById(R.id.txt);
            t.setText(String.valueOf(position));
            log("instantiate: " + position);
            return v;
        }
    };

    private PagerAdapter mPagerAdapter2 = new PagerAdapter() {

        View[] mViews = new View[3];

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mViews[position] == null) {
                mViews[position] = createDifferentView(position);
            }
            container.addView(mViews[position]);
            return mViews[position];
        }
    };

    private PagerAdapter mPagerAdapter3 = new PagerAdapter() {

        LinkedList<View> mViewCache = new LinkedList<>();

        class ViewHolder {
            TextView textView;

            ViewHolder(View parent) {
                textView = (TextView) parent.findViewById(R.id.txt);
            }

            void setData(String s) {
                textView.setText(s);
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            View view = (View) object;
            container.removeView(view);
            log("remove view: " + position);
            mViewCache.add(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View v;
            final ViewHolder holder;
            if (mViewCache.isEmpty()) {
                log("create view: " + position);
                v = getLayoutInflater().inflate(R.layout.item, container, false);
                holder = new ViewHolder(v);
                v.setTag(holder);
            } else {
                log("get cached view: " + position);
                v = mViewCache.removeFirst();
                holder = (ViewHolder) v.getTag();
            }
            container.addView(v);
            holder.setData(String.valueOf(position));
            return v;
        }
    };

    private PagerAdapter mPagerAdapter4 = new PagerAdapter() {

        private static final int CACHE_COUNT = 3;
        LinkedList<View> mViewCache = new LinkedList<>();

        class ViewHolder {
            TextView textView;

            ViewHolder(View parent) {
                textView = (TextView) parent.findViewById(R.id.txt);
            }

            void setData(String s) {
                textView.setText(s);
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // create views
            if (mViewCache.isEmpty()) {
                for (int i = 0; i < CACHE_COUNT; ++i) {
                    log("create view: " + i);
                    View v = getLayoutInflater().inflate(R.layout.item, container, false);
                    ViewHolder holder = new ViewHolder(v);
                    v.setTag(holder);
                    mViewCache.add(v);
                }
            }

            log("get cached view: " + position);

            final int index = position >= CACHE_COUNT ? position % CACHE_COUNT : position;
            final View v = mViewCache.get(index);
            final ViewHolder holder = (ViewHolder) v.getTag();

            if (v.getParent() != null) {
                container.removeView(v);
            }
            container.addView(v);
            holder.setData(String.valueOf(position));
            return v;
        }
    };

    String[] mPagerItems = {"ViewPager", "内存中保存所有View", "使用ViewHolder缓存", "使用ViewHolder缓存2"};
    PagerAdapter[] mPagerAdapters = {mPagerAdapter1, mPagerAdapter2, mPagerAdapter3, mPagerAdapter4};
}
