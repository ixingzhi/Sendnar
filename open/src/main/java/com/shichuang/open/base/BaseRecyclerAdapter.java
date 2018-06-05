package com.shichuang.open.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by xiedd on 2017/8/14.
 */

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater inflater;

    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mDatas = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        if (datas == null) datas = new ArrayList<>();
        this.mContext = context;
        this.mDatas = datas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseRecyclerAdapter(Context context, T[] datas) {
        this.mContext = context;
        this.mDatas = new ArrayList<T>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.addAll(mDatas, datas);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 更新数据，替换原有数据
     */
    public void update(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据
     */
    public void addData(T data) {
        mDatas.add(0, data);
        notifyItemInserted(0);
    }

    /**
     * 在列表尾添加一串数据
     */
    public void addData(List<T> datas) {
        int start = mDatas.size();
        mDatas.addAll(datas);
        notifyItemRangeChanged(start, datas.size());
    }

    /**
     * 移除一条数据
     */
    public void remove(int position) {
        if (position > mDatas.size() - 1) {
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除一条数据
     */
    public void remove(T item) {
        int position = 0;
        ListIterator<T> iterator = mDatas.listIterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next == item) {
                iterator.remove();
                notifyItemRemoved(position);
            }
            position++;
        }
    }

    /**
     * 获取所有数据
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 清除所有数据
     */
    public void remove() {
        mDatas.clear();
        notifyDataSetChanged();
    }
}