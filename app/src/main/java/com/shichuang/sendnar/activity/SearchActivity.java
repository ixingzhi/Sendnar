package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.HistorySearchAdapter;
import com.shichuang.sendnar.common.OrderOperation;
import com.shichuang.sendnar.common.SearchCache;
import com.shichuang.sendnar.widget.ConfirmDialog;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class SearchActivity extends BaseActivity {
    private EditText mEtSearch;
    private HistorySearchAdapter mAdHistorySearch;

    private List<String> historySearchList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtSearch = (EditText) findViewById(R.id.et_search);
        historySearchList = SearchCache.get(mContext);

        RecyclerView mRvHistorySearch = (RecyclerView) findViewById(R.id.recycler_view_history_search);
        FlexboxLayoutManager lmHistorySearch = new FlexboxLayoutManager(mContext);
        lmHistorySearch.setFlexWrap(FlexWrap.WRAP);
        lmHistorySearch.setAlignItems(AlignItems.STRETCH);
        mRvHistorySearch.setLayoutManager(lmHistorySearch);
        mAdHistorySearch = new HistorySearchAdapter();
        mRvHistorySearch.setAdapter(mAdHistorySearch);
        mAdHistorySearch.addData(historySearchList);

        // 暂无搜索接口
        RecyclerView mRvHotSearch = (RecyclerView) findViewById(R.id.recycler_view_hot_search);
        FlexboxLayoutManager lmHotSearch = new FlexboxLayoutManager(mContext);
        lmHotSearch.setFlexWrap(FlexWrap.WRAP);
        lmHotSearch.setAlignItems(AlignItems.STRETCH);
        mRvHotSearch.setLayoutManager(lmHotSearch);
        HistorySearchAdapter mAdHotSearch = new HistorySearchAdapter();
        mRvHotSearch.setAdapter(mAdHotSearch);
        //mAdHotSearch.addData(historySearchList);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
        findViewById(R.id.iv_delete_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog mDialog = new ConfirmDialog(mContext);
                mDialog.setMessage("确认删除历史搜索记录？");
                mDialog.setNegativeButton("取消", null);
                mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        historySearchList.clear();
                        SearchCache.update(mContext, historySearchList);
                        mAdHistorySearch.replaceData(historySearchList);
                    }
                });
                mDialog.show();
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchContent = mEtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(searchContent)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("typeId", 13);
                        bundle.putString("content", searchContent);
                        RxActivityTool.skipActivity(mContext, SearchResultActivity.class, bundle);

                        // 如果有相同的搜索文字，则删除
                        if (historySearchList.contains(searchContent)) {
                            historySearchList.remove(searchContent);
                        }
                        historySearchList.add(0, searchContent);
                        SearchCache.update(mContext, historySearchList);
                        mAdHistorySearch.replaceData(historySearchList);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
    }
}
