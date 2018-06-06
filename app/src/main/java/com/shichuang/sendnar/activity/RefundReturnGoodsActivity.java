package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.GridImageAdapter;
import com.shichuang.sendnar.adapter.OrderDetailsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.MsgCode;
import com.shichuang.sendnar.entify.OrderDetails;
import com.shichuang.sendnar.widget.FullyGridLayoutManager;
import com.shichuang.sendnar.widget.OptionDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 退款退货
 * Created by Administrator on 2018/6/6.
 */

public class RefundReturnGoodsActivity extends BaseActivity implements View.OnClickListener {
    // 商品列表
    private RecyclerView mRecyclerView;
    private OrderDetailsAdapter mAdapter;
    // 图片
    private RecyclerView mRvUploadDocuments;
    private GridImageAdapter mAdUploadDocuments;

    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private OrderDetails.OrderDetailsGoodsListModel orderInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_return_goods;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderInfo = (OrderDetails.OrderDetailsGoodsListModel) getIntent().getSerializableExtra("orderInfo");

        initRecyclerView();
        mRvUploadDocuments = view.findViewById(R.id.rv_upload_documents);
        mRvUploadDocuments.setLayoutManager(new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mAdUploadDocuments = new GridImageAdapter(mContext, listener);
        mRvUploadDocuments.setAdapter(mAdUploadDocuments);
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrderDetailsAdapter();
        mAdapter.isOrderDetailsPage(false);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        // 货物状态
        findViewById(R.id.ll_goods_status).setOnClickListener(this);
        // 退款原因
        findViewById(R.id.ll_refund_reason).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (orderInfo != null) {
            mAdapter.addData(orderInfo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goods_status:
                List<OptionDialog.Option> list = new ArrayList<>();
                list.add(new OptionDialog.Option(2, "未收到货"));
                list.add(new OptionDialog.Option(1, "已收到货"));
                OptionDialog mDialog = new OptionDialog(mContext);
                mDialog.setTitle("货物状态");
                mDialog.setData(list);
                mDialog.setOnOptionClickListener(new OptionDialog.OnOptionClickListener() {
                    @Override
                    public void onClick(OptionDialog.Option option, int position) {
                        showToast(option.getName());
                    }
                });
                mDialog.show();
                break;
            case R.id.ll_refund_reason:
                List<OptionDialog.Option> list2 = new ArrayList<>();
                list2.add(new OptionDialog.Option("未收到货"));
                list2.add(new OptionDialog.Option("已收到货"));
                OptionDialog mDialog2 = new OptionDialog(mContext);
                mDialog2.setTitle("退款原因");
                mDialog2.setData(list2);
                mDialog2.setOnOptionClickListener(new OptionDialog.OnOptionClickListener() {
                    @Override
                    public void onClick(OptionDialog.Option option, int position) {
                        showToast(option.getName());
                    }
                });
                mDialog2.show();
                break;
            case R.id.btn_commit:
                commit();
                break;
            default:
                break;
        }
    }

    public GridImageAdapter.onAddPicClickListener listener = new GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            PictureSelector.create(RefundReturnGoodsActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    mAdUploadDocuments.setList(selectList);
                    mAdUploadDocuments.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void getRufundReturnGoodsReason(){
        OkGo.<AMBaseDto<MsgCode>>post(Constants.refundReturnGoodsReasonUrl)
                .tag(mContext)
                .params("operate_type", 1)  // 1-退款 2-退款退货 3-换货
                .execute(new NewsCallback<AMBaseDto<MsgCode>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MsgCode>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MsgCode>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                           
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MsgCode>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void commit() {


    }


}
