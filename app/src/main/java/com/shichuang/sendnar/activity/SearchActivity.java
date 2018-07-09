package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.HistorySearchAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SearchCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.KeyWord;
import com.shichuang.sendnar.widget.ConfirmDialog;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class SearchActivity extends BaseActivity {
    private EditText mEtSearch;
    private HistorySearchAdapter mAdHistorySearch;
    private HistorySearchAdapter mAdHotSearch;

    private List<KeyWord> historySearchList;

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
        mAdHotSearch = new HistorySearchAdapter();
        mRvHotSearch.setAdapter(mAdHotSearch);
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
                        if (historySearchList != null && historySearchList.size() > 0) {
                            historySearchList.clear();
                            SearchCache.update(mContext, historySearchList);
                            mAdHistorySearch.replaceData(historySearchList);
                        }
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
                    search(searchContent);
                }
                return false;
            }
        });
        mAdHistorySearch.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                search(mAdHistorySearch.getItem(position).getWord());
            }
        });
        mAdHotSearch.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                search(mAdHotSearch.getItem(position).getWord());
            }
        });
    }

    @Override
    public void initData() {
        getHotWordData();
    }

    private void getHotWordData() {
        OkGo.<AMBaseDto<List<KeyWord>>>get(Constants.hotWordUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<List<KeyWord>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<KeyWord>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<KeyWord>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            mAdHotSearch.replaceData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<KeyWord>>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void search(final String content) {
        if (!TextUtils.isEmpty(content)) {
            // 如果有相同的搜索文字，则删除
            Iterator<KeyWord> iterable = historySearchList.iterator();
            while (iterable.hasNext()) {
                if (content.equals(iterable.next().getWord())) {
                    iterable.remove();
                }
            }
//            for (KeyWord word : historySearchList) {
//                if (word.getWord().equals(content)) {
//                    //historySearchList.remove(word);
//                }
//            }
            historySearchList.add(0, new KeyWord(content));
            SearchCache.update(mContext, historySearchList);
            mAdHistorySearch.replaceData(historySearchList);

            Bundle bundle = new Bundle();
            bundle.putInt("typeId", 13);
            bundle.putString("content", content);
            RxActivityTool.skipActivity(mContext, SearchResultActivity.class, bundle);
        }

    }
}
