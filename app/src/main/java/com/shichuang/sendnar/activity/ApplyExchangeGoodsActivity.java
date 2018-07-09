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
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.OrderDetails;
import com.shichuang.sendnar.entify.OrderReceiveAddress;
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
 * Created by Administrator on 2018/6/5.
 */

public class ApplyExchangeGoodsActivity extends BaseActivity implements View.OnClickListener {
    // 商品列表
    private RecyclerView mRecyclerView;
    private OrderDetailsAdapter mAdapter;
    private TextView mTvExchangeGoodsReason;
    private TextView mTvGoodsName;
    private TextView mTvConsignee;
    private TextView mTvPhone;
    private TextView mTvDetailsAddress;
    private EditText mEtExchangeGoodsInstructions;
    private TextView mTvExchangeGoodsInstructionsCount;
    // 图片
    private RecyclerView mRvUploadDocuments;
    private GridImageAdapter mAdUploadDocuments;

    // 操作类型
    private int operateType;

    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private OrderDetails.OrderDetailsGoodsListModel orderInfo;
    private List<RefundReturnGoodsReason.Reason> reasonList;
    // 上传成功之后的图片地址
    private List<String> uploadList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_exchange_goods;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        operateType = getIntent().getIntExtra("operateType", 0);
        orderInfo = (OrderDetails.OrderDetailsGoodsListModel) getIntent().getSerializableExtra("orderInfo");
        initRecyclerView();
        mTvExchangeGoodsReason = view.findViewById(R.id.tv_exchange_goods_reason);
        mTvGoodsName = view.findViewById(R.id.tv_goods_name);
        mTvConsignee = (TextView) findViewById(R.id.tv_consignee);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvDetailsAddress = (TextView) findViewById(R.id.tv_details_address);
        mEtExchangeGoodsInstructions = view.findViewById(R.id.et_exchange_goods_instructions);
        mTvExchangeGoodsInstructionsCount = view.findViewById(R.id.tv_exchange_goods_instructions_count);
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
        findViewById(R.id.ll_exchange_goods_reason).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        mEtExchangeGoodsInstructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvExchangeGoodsInstructionsCount.setText(String.format("%d/100", s.length()));
            }
        });
    }

    @Override
    public void initData() {
        if (orderInfo != null) {
            mAdapter.addData(orderInfo);
            mTvGoodsName.setText(orderInfo.getSortName());
        }
        getRefundReturnGoodsReason();
        getAddress();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_exchange_goods_reason:
                List<OptionDialog.Option> list2 = new ArrayList<>();
                if (reasonList != null) {
                    for (RefundReturnGoodsReason.Reason model : reasonList) {
                        list2.add(new OptionDialog.Option(model.getId(), model.getCauseName()));
                    }
                }
                OptionDialog mDialog2 = new OptionDialog(mContext);
                mDialog2.setTitle("换货原因");
                mDialog2.setData(list2);
                mDialog2.setOnOptionClickListener(new OptionDialog.OnOptionClickListener() {
                    @Override
                    public void onClick(OptionDialog.Option option, int position) {
                        mTvExchangeGoodsReason.setText(option.getName());
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
            PictureSelector.create(ApplyExchangeGoodsActivity.this)
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
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
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

    private void getAddress() {
        OkGo.<AMBaseDto<OrderReceiveAddress>>get(Constants.getOrderReceiveAddressUrl)
                .tag(mContext)
                .params("order_detail_id", orderInfo.getOrderDetailId())
                .execute(new NewsCallback<AMBaseDto<OrderReceiveAddress>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<OrderReceiveAddress>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<OrderReceiveAddress>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            OrderReceiveAddress address = response.body().data;
                            mTvConsignee.setText(address.getNickname());
                            mTvPhone.setText(address.getPhone());
                            mTvDetailsAddress.setText(address.getReceiveAddress());
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<OrderReceiveAddress>> response) {
                        super.onError(response);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void checkInfo() {
        String exchangeGoodsReason = mTvExchangeGoodsReason.getText().toString().trim();
        String exchangeGoodsInstructions = mTvExchangeGoodsInstructionsCount.getText().toString().trim();

        if (TextUtils.isEmpty(exchangeGoodsReason)) {
            showToast("请选择换货原因");
        } else if (TextUtils.isEmpty(exchangeGoodsInstructions)) {
            showToast("请填写换货说明");
        } else if (exchangeGoodsInstructions.trim().length() < 5) {
            showToast("换货说明不能小于5个字符");
        } else {
            // 如果选择了图片，先上传图片
            showLoading();
            if (selectList.size() > 0) {
                uploadPicture(exchangeGoodsReason, exchangeGoodsInstructions);
            } else {
                commit(exchangeGoodsReason, exchangeGoodsReason, "");
            }
        }
    }

    private void uploadPicture(final String exchangeGoodsReason, final String exchangeGoodsInstructions) {
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
                                    commit(exchangeGoodsReason, exchangeGoodsInstructions, sbUploadPic.toString());
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

    private void commit(String exchangeGoodsReason, String exchangeGoodsInstructions, String picUrl) {
        OkGo.<AMBaseDto<Empty>>post(Constants.refundReturnGoodsCommitUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("refund_type", operateType)  // 退款类型1=退款 2=退货
                .params("refund_reason", exchangeGoodsReason)
                .params("refund_amount", orderInfo.getSalePrice())
                .params("refund_desc", exchangeGoodsInstructions)
                .params("refund_pics", picUrl)
                .params("order_detail_id", orderInfo.getOrderDetailId())  // 订单详情id
                //.params("is_receive", goodsStatus)  // 1-已收到货 2-未收到货
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
