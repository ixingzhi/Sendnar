package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.MyFragmentPagerAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Invoice;
import com.shichuang.sendnar.entify.Page;
import com.shichuang.sendnar.fragment.CommercialElectronicInvoiceFragment;
import com.shichuang.sendnar.fragment.CommercialInvoiceFragment;
import com.shichuang.sendnar.widget.RxTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 填写发票信息
 * Created by Administrator on 2018/4/19.
 */

public class FillInTheInvoiceInformationActivity extends BaseActivity {
    private Button mBtnCommercialInvoice;
    private Button mBtnCommercialElectronicInvoice;
    private ViewPager mViewPager;

    private Invoice invoice;

    // 普通发票
    private CommercialInvoiceFragment mCommercialInvoiceFragment;
    // 电子发票
    private CommercialElectronicInvoiceFragment mCommercialElectronicInvoiceFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fill_in_the_invoice_information;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        invoice = (Invoice) getIntent().getSerializableExtra("invoice");
        mBtnCommercialInvoice = (Button) findViewById(R.id.btn_commercial_invoice);
        mBtnCommercialElectronicInvoice = (Button) findViewById(R.id.btn_commercial_electronic_invoice);
        initViewPager();
        selectTab(0);
    }

    private void initViewPager() {
        mCommercialInvoiceFragment = CommercialInvoiceFragment.newInstance();
        mCommercialElectronicInvoiceFragment = CommercialElectronicInvoiceFragment.newInstance();

        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mCommercialInvoiceFragment);
        mFragmentList.add(mCommercialElectronicInvoiceFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void initEvent() {
        mBtnCommercialInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mBtnCommercialElectronicInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                //invoiceInstructions();
                SinglePage.getInstance().toPage(mContext, "发票须知", SinglePage.INVOICE_INFORMATION,"");
            }
        });
    }

    @Override
    public void initData() {
        if (null != invoice) {
            // 创建Fragment 异步，需要延迟处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleData(invoice);
                }
            }, 300);
        } else {
            getInvoiceHistoryInformation();
        }
    }

    private void selectTab(int i) {
        mBtnCommercialInvoice.setSelected(i == 0 ? true : false);
        mBtnCommercialElectronicInvoice.setSelected(i == 0 ? false : true);
    }

    private void invoiceInstructions() {
        OkGo.<AMBaseDto<Page>>get(Constants.invoiceInstructionsUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<Page>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Page>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Page>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            WebPageActivity.newInstance(mContext, "发票须知", Constants.MAIN_ENGINE_PIC + response.body().data.getH5Url());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Page>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast(response.getException().getMessage());

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 发票历史信息
     */
    private void getInvoiceHistoryInformation() {
        OkGo.<AMBaseDto<List<Invoice>>>get(Constants.invoiceHistoryInformationUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<List<Invoice>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<Invoice>>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<Invoice>>> response) {
                        if (response.body().code == 0) {
                            List<Invoice> invoiceList = response.body().data;
                            if (invoiceList != null && invoiceList.size() > 0) {
                                handleData(invoiceList.get(0));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<Invoice>>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    /**
     * 取历史发票第一条信息，填充到页面
     *
     * @param invoice
     */
    private void handleData(Invoice invoice) {
        if (invoice.getType() == 1) {  // 普通发票
            selectTab(0);
            mViewPager.setCurrentItem(0);
            if (null != mCommercialInvoiceFragment) {
                mCommercialInvoiceFragment.handleData(invoice);
            }
        } else {  // 电子发票
            selectTab(1);
            mViewPager.setCurrentItem(1);
            if (null != mCommercialElectronicInvoiceFragment) {
                mCommercialElectronicInvoiceFragment.handleData(invoice);
            }
        }
    }

}

