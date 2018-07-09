package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.GridImageAdapter;
import com.shichuang.sendnar.adapter.OrderDetailsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.Convert;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.OrderDetails;
import com.shichuang.sendnar.entify.RefundReturnGoodsReason;
import com.shichuang.sendnar.entify.UploadFile;
import com.shichuang.sendnar.event.UpdateOrderEvent;
import com.shichuang.sendnar.widget.FullyGridLayoutManager;
import com.shichuang.sendnar.widget.OptionDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 退款退货
 * Created by Administrator on 2018/6/6.
 */

public class RefundReturnGoodsActivity extends BaseActivity implements View.OnClickListener {
    // 商品列表
    private RecyclerView mRecyclerView;
    private OrderDetailsAdapter mAdapter;
    private TextView mTvGoodsStatus;
    private TextView mTvRefundReason;
    private TextView mTvRefundAmount;
    private EditText mEtRefundInstructions;
    private TextView mTvRefundInstructionsCount;
    // 图片
    private RecyclerView mRvUploadDocuments;
    private GridImageAdapter mAdUploadDocuments;

    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private OrderDetails.OrderDetailsGoodsListModel orderInfo;
    // 操作类型
    private int operateType;
    private List<RefundReturnGoodsReason.Reason> reasonList;
    // 上传成功之后的图片地址
    private List<String> uploadList = new ArrayList<>();

    // 货物状态
    private int goodsStatus;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_return_goods;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        operateType = getIntent().getIntExtra("operateType", 0);
        orderInfo = (OrderDetails.OrderDetailsGoodsListModel) getIntent().getSerializableExtra("orderInfo");

        initRecyclerView();
        mTvGoodsStatus = view.findViewById(R.id.tv_goods_status);
        mTvRefundReason = view.findViewById(R.id.tv_refund_reason);
        mTvRefundAmount = view.findViewById(R.id.tv_refund_amount);
        mEtRefundInstructions = view.findViewById(R.id.et_refund_instructions);
        mTvRefundInstructionsCount = view.findViewById(R.id.tv_refund_instructions_count);

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
        mEtRefundInstructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvRefundInstructionsCount.setText(String.format("%d/100", s.length()));
            }
        });
    }

    @Override
    public void initData() {
        if (orderInfo != null) {
            mAdapter.addData(orderInfo);
            mTvRefundAmount.setText("¥" + RxBigDecimalTool.toDecimal(orderInfo.getSalePrice(), 2));
        }
        getRefundReturnGoodsReason();
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
                        goodsStatus = option.getId();
                        mTvGoodsStatus.setText(option.getName());
                    }
                });
                mDialog.show();
                break;
            case R.id.ll_refund_reason:
                List<OptionDialog.Option> list2 = new ArrayList<>();
                if (reasonList != null) {
                    for (RefundReturnGoodsReason.Reason model : reasonList) {
                        list2.add(new OptionDialog.Option(model.getId(), model.getCauseName()));
                    }
                }
                OptionDialog mDialog2 = new OptionDialog(mContext);
                mDialog2.setTitle("退款原因");
                mDialog2.setData(list2);
                mDialog2.setOnOptionClickListener(new OptionDialog.OnOptionClickListener() {
                    @Override
                    public void onClick(OptionDialog.Option option, int position) {
                        mTvRefundReason.setText(option.getName());
                    }
                });
                mDialog2.show();
                break;
            case R.id.btn_commit:
                checkInfo();
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

    private void getRefundReturnGoodsReason() {
        OkGo.<AMBaseDto<RefundReturnGoodsReason>>get(Constants.refundReturnGoodsReasonUrl)
                .tag(mContext)
                .params("operate_type", operateType)  // 1-退款 2-退款退货 3-换货
                .execute(new NewsCallback<AMBaseDto<RefundReturnGoodsReason>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<RefundReturnGoodsReason>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<RefundReturnGoodsReason>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            reasonList = response.body().data.getReasonList();
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<RefundReturnGoodsReason>> response) {
                        super.onError(response);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }


    private void checkInfo() {
        String refundReason = mTvRefundReason.getText().toString().trim();
        String refundInstructions = mTvRefundInstructionsCount.getText().toString().trim();

        if (goodsStatus == 0) {
            showToast("请选择货物状态");
        } else if (TextUtils.isEmpty(refundReason)) {
            showToast("请选择退款原因");
        }
//        else if (TextUtils.isEmpty(refundInstructions)) {
//            showToast("请填写退款说明");
//        }
//        else if (refundInstructions.trim().length() < 5) {
//            showToast("退款说明不能小于5个字符");
//        }
        else {
            // 如果选择了图片，先上传图片
            showLoading();
            if (selectList.size() > 0) {
                uploadPicture(refundReason, refundInstructions);
            } else {
                commit(refundReason, refundInstructions, "");
            }
        }
    }

    private void uploadPicture(final String refundReason, final String refundInstructions) {
        uploadList.clear();
        List<File> list = new ArrayList<>();
        for (LocalMedia model : selectList) {
            list.clear();
            list.add(new File(model.getCompressPath()));
            OkGo.<String>post(Constants.uploadFile)//
                    .tag(this)//
                    .addFileParams("file", list)
                    .converter(new StringConvert())//
                    .adapt(new ObservableResponse<String>())//
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                        }
                    })//
                    .observeOn(AndroidSchedulers.mainThread())//
                    .subscribe(new Observer<Response<String>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            showLoading();
                        }

                        @Override
                        public void onNext(@NonNull Response<String> response) {
                            final UploadFile uploadFile = Convert.fromJson(response.body(), UploadFile.class);
                            if (uploadFile.getStatus() == 1) {
                                uploadList.add(uploadFile.getPath());
                                if (uploadList.size() == selectList.size()) {
                                    // 去掉最后一个逗号
                                    StringBuffer sbUploadPic = new StringBuffer();
                                    for (int i = 0; i < uploadList.size(); i++) {
                                        sbUploadPic.append(uploadList.get(i) + ",");
                                    }
                                    sbUploadPic.deleteCharAt(sbUploadPic.length() - 1);
                                    commit(refundReason, refundInstructions, sbUploadPic.toString());
                                }
                            } else {
                                dismissLoading();
                                showToast(uploadFile.getMsg());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                            dismissLoading();
                            showToast("网络异常，稍后再试");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    private void commit(String refundReason, String refundInstructions, String picUrl) {
        OkGo.<AMBaseDto<Empty>>post(Constants.refundReturnGoodsCommitUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("refund_type", operateType)  // 退款类型1=退款 2=退货
                .params("refund_reason", refundReason)
                .params("refund_amount", orderInfo.getSalePrice())
                .params("refund_desc", refundInstructions)
                .params("refund_pics", picUrl)
                .params("order_detail_id", orderInfo.getOrderDetailId())  // 订单详情id
                .params("is_receive", goodsStatus)  // 1-已收到货 2-未收到货
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    RxActivityTool.finish(mContext);
                                    EventBus.getDefault().post(new UpdateOrderEvent());
                                }
                            }, 200);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }


}
