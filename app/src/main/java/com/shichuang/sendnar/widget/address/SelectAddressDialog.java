package com.shichuang.sendnar.widget.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.R;
import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/24.
 */

public class SelectAddressDialog extends BaseDialog {
    private int firstPosition = 0;
    private CityInterface province;
    private CityInterface city;
    private CityInterface area;

    private OnSelectListener mOnSelectListener = null;
    private AddressSelector addressSelector;
    //private static ArrayList<City> mCityList;
    private ArrayList<City2> provinceList = new ArrayList<>();
    private ArrayList<City2> cityList = new ArrayList<>();
    private ArrayList<City2> areaList = new ArrayList<>();

    public SelectAddressDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        setFullScreenWidth();
        initView();
        initData();
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_address, null);
        setContentView(dialogView);
        addressSelector = dialogView.findViewById(R.id.address);
        addressSelector.setTabAmount(3);
        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface cityInterface, int position, int tabPosition) {
                switch (tabPosition) {
                    case 0:  // 点击省，进入市
                        firstPosition = position;
                        province = cityInterface;
                        //addressSelector.setCities((ArrayList) mCityList.get(position).citys);
                        getCityData();
                        break;
                    case 1:  // 点击市，进入区
                        city = cityInterface;
                        //addressSelector.setCities((ArrayList) mCityList.get(firstPosition).citys.get(position).areas);
                        getAreaData();
                        break;
                    case 2:  // 点击区，选择完毕
                        area = cityInterface;
                        if (mOnSelectListener != null) {
                            mOnSelectListener.onSelected(province, city, area);
                            dismiss();
                        }
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()) {
                    case 0:  // 省
                        //addressSelector.setCities((ArrayList) mCityList);
                        addressSelector.setCities(provinceList);
                        addressSelector.show(AddressSelector.HIDE);
                        break;
                    case 1:  // 市
                        //addressSelector.setCities((ArrayList) mCityList.get(firstPosition).citys);
                        addressSelector.setCities(cityList);
                        addressSelector.show(AddressSelector.HIDE);
                        break;
                    case 2:  // 区
                        //addressSelector.setCities((ArrayList) mList.get(position).citys);
                        //addressSelector.setCities(cities3);
                        addressSelector.setCities(areaList);
                        addressSelector.show(AddressSelector.HIDE);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {
            }
        });
    }

    private void initData() {
        getProvinceData();
    }

    public interface OnSelectListener {
        void onSelected(CityInterface province, CityInterface city, CityInterface area);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

//    public static void init() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                handleData();
//            }
//        }).start();
//    }

//    private static void handleData() {
//        //将json数据变成字符串
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            InputStream inputStream = Open.getInstance().getContext().getClass().getClassLoader().getResourceAsStream("assets/" + "city.json");
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 解析数据
//        JsonParser parser = new JsonParser();
//        JsonArray jsonArray1 = parser.parse(stringBuilder.toString()).getAsJsonArray();
//        mCityList = new ArrayList<>();
//        Gson gson = new Gson();
//        for (JsonElement city : jsonArray1) {
//            City city2 = gson.fromJson(city, City.class);
//            mCityList.add(city2);
//        }
//    }


    /**
     * 获取省
     */
    private void getProvinceData() {
        OkGo.<AMBaseDto<ArrayList<City2>>>get(Constants.getProvinceUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<ArrayList<City2>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ArrayList<City2>>, ? extends Request> request) {
                        super.onStart(request);
                        addressSelector.show(AddressSelector.LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ArrayList<City2>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            addressSelector.show(AddressSelector.SUCCESS);
                            provinceList = response.body().data;
                            addressSelector.setCities(provinceList);
                        } else {
                            addressSelector.show(AddressSelector.FAIL);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ArrayList<City2>>> response) {
                        super.onError(response);
                        addressSelector.show(AddressSelector.FAIL);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 获取市
     */
    private void getCityData() {
        OkGo.<AMBaseDto<ArrayList<City2>>>get(Constants.getCityUrl)
                .tag(mContext)
                .params("province_id", province.getCityCode())
                .execute(new NewsCallback<AMBaseDto<ArrayList<City2>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ArrayList<City2>>, ? extends Request> request) {
                        super.onStart(request);
                        addressSelector.show(AddressSelector.LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ArrayList<City2>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            addressSelector.show(AddressSelector.SUCCESS);
                            cityList = response.body().data;
                            addressSelector.setCities(cityList);
                        } else {
                            addressSelector.show(AddressSelector.FAIL);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ArrayList<City2>>> response) {
                        super.onError(response);
                        addressSelector.show(AddressSelector.FAIL);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 获取区
     */
    private void getAreaData() {
        OkGo.<AMBaseDto<ArrayList<City2>>>get(Constants.getAreaUrl)
                .tag(mContext)
                .params("city_id", city.getCityCode())
                .execute(new NewsCallback<AMBaseDto<ArrayList<City2>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ArrayList<City2>>, ? extends Request> request) {
                        super.onStart(request);
                        addressSelector.show(AddressSelector.LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ArrayList<City2>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            addressSelector.show(AddressSelector.SUCCESS);
                            areaList = response.body().data;
                            addressSelector.setCities(areaList);
                        } else {
                            addressSelector.show(AddressSelector.FAIL);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ArrayList<City2>>> response) {
                        super.onError(response);
                        addressSelector.show(AddressSelector.FAIL);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}