package net.qyq.graduation_project.palmtop_campus.navigation_map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.PoiInputItemWidget;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;

import net.qyq.graduation_project.common.app.Activity;
import net.qyq.graduation_project.palmtop_campus.R;
import net.qyq.graduation_project.palmtop_campus.navigation_map.adapter.BusResultListAdapter;
import net.qyq.graduation_project.palmtop_campus.navigation_map.adapter.RouteLineAdapter;
import net.qyq.graduation_project.palmtop_campus.navigation_map.adapter.SearchResultAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Author：钱
 * Date: 2019/3/14 0014 14:40
 * Des:地图导航主页，主要实现以下功能：
 * 1、定位
 * 2、搜索终点并得到终点的坐标
 * 3、进行线路规划，将路线显示在地图上，供用户选择路线
 * 4、开始导航
 */
public class MapActivity extends Activity implements AMapLocationListener,
        CompoundButton.OnCheckedChangeListener, TextWatcher,
        View.OnFocusChangeListener, Inputtips.InputtipsListener,
        View.OnClickListener, AMapNaviListener, RouteSearch.OnRouteSearchListener,
        AdapterView.OnItemClickListener, View.OnTouchListener, PoiSearch.OnPoiSearchListener {
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.start_point)
    EditText startPoint;
    @BindView(R.id.end_point)
    AutoCompleteTextView endPoint;
    @BindView(R.id.drive_strategy)
    LinearLayout driveStrategy; //驾车规划条件
    @BindView(R.id.congestion)
    CheckBox noCongestion; //避免拥堵
    @BindView(R.id.no_highway)
    CheckBox noHighWay; //不走高速
    @BindView(R.id.cost)
    CheckBox noCost; //避免收费
    @BindView(R.id.highway)
    CheckBox highWay; //高速优先
    @BindView(R.id.walk)
    ImageButton walkWay;
    @BindView(R.id.bus)
    ImageButton busWay;
    @BindView(R.id.drive_car)
    ImageButton driveWay;
    @BindView(R.id.result_list)
    ListView resultList;
    @BindView(R.id.bus_result)
    ListView busResult;//公交线路查询
    @BindView(R.id.route_navigation)
    LinearLayout routeAndNavi;
    @BindView(R.id.route_line_way)
    GridView routeLineWay;
    @BindView(R.id.navigation_start)
    Button startNavigation;

    //地图有关对象声明
    private AMap aMap;
    MyLocationStyle locationStyle;
    private AMapNavi mNavigation;
    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption = new AMapLocationClientOption();

    private int pointType;
    private RouteSearch mRouteSearch;
    //条目点击状态
    private boolean endItemBoolean = false;

    //坐标相关
    private NaviLatLng startLatLng;
    private NaviLatLng endLatLng;
    private String city; //中心城市名称

    //驾车条件选择状态
    private boolean avoidCongestion, avoidHighWay, avoidCost, priorHighWay;

    private List<Tip> mTipList;
    //起点坐标集合
    private List<NaviLatLng> startList = new ArrayList<>();
    //终点集合
    private List<NaviLatLng> endList = new ArrayList<>();
    //途径点集合
    private List<NaviLatLng> wayList = new ArrayList<>();
    //保存当前算好的路线
    private SparseArray<RouteOverLay> routeOverLays = new SparseArray<>();

    //出行方式按钮自动触发点击事件
    private final int CODE_WALK_WAY = 0, CODE_BUS_WAY = 1, CODE_DRIVE_WAY = 2;
    private int wayCode = CODE_WALK_WAY;
    private int zindex = 1;
    private RouteLineAdapter routeLineAdapter;


    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        initAMap();
        initLocation();
        mNavigation = AMapNavi.getInstance(getApplicationContext());
        walkWay.setSelected(true);
        mRouteSearch = new RouteSearch(this);
        initListener();
    }

    private void initLocation() {
        mClient = new AMapLocationClient(getApplicationContext());

        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setHttpTimeOut(30000);
        mOption.setInterval(2000);
        mOption.setOnceLocation(false);

        mClient.setLocationOption(mOption);
        mClient.setLocationListener(this);
        //开启定位
        mClient.startLocation();
    }


    private void initAMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        //设置指南针是否可见
        aMap.getUiSettings().setCompassEnabled(true);
        //设置是否以地图中心店缩放
        aMap.getUiSettings().setGestureScaleByMapCenter(true);
        //设置比例尺控件是否可见
        aMap.getUiSettings().setScaleControlsEnabled(true);

        locationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        locationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(locationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        locationStyle.showMyLocation(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        locationStyle.anchor(0, 1);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mRouteSearch.setRouteSearchListener(this);
        mNavigation.addAMapNaviListener(this);
        //列表点击监听
        resultList.setOnItemClickListener(this);
        resultList.setOnTouchListener(this);
        //输入监听
        startPoint.addTextChangedListener(this);
        startPoint.setOnFocusChangeListener(this);
        endPoint.addTextChangedListener(this);
        endPoint.setOnFocusChangeListener(this);
        endPoint.requestFocus();
        //驾车条件监听
        noCongestion.setOnCheckedChangeListener(this);
        noHighWay.setOnCheckedChangeListener(this);
        noCost.setOnCheckedChangeListener(this);
        highWay.setOnCheckedChangeListener(this);
        //出行方式监听
        walkWay.setOnClickListener(this);
        busWay.setOnClickListener(this);
        driveWay.setOnClickListener(this);

        //出行方案
        routeLineWay.setOnItemClickListener(this);
        startNavigation.setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_navi_map;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mClient != null) {
            mClient.onDestroy();
            mClient = null;
            mOption = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                startLatLng = new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                city = aMapLocation.getCity();
                //                myPositionLatLng = startLatLng;
                startList.clear();
                startList.add(startLatLng);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_start:
                Intent intent;
                if (wayCode == CODE_WALK_WAY) {
                    intent = new Intent(getApplicationContext(), NavigationWalkActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("endPoint", endLatLng);
                    intent.putExtra("walkPoint", bundle);
                } else {
                    intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("gps", true);
                }
                startActivity(intent);
                break;
            case R.id.walk:
                wayCode = CODE_WALK_WAY;
                walkWay.setSelected(true);
                busWay.setSelected(false);
                driveWay.setSelected(false);
                driveStrategy.setVisibility(View.GONE);
                busResult.setVisibility(View.GONE);
                clearRoute();
                mNavigation.calculateWalkRoute(startLatLng, endLatLng);
                break;
            case R.id.bus:
                wayCode = CODE_BUS_WAY;
                walkWay.setSelected(false);
                busWay.setSelected(true);
                driveWay.setSelected(false);
                busResult.setVisibility(View.VISIBLE);
                driveStrategy.setVisibility(View.GONE);
                clearRoute();
                if (endLatLng != null) {
                    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(startLatLng.getLatitude(), startLatLng.getLongitude()), new LatLonPoint(endLatLng.getLatitude(), endLatLng.getLongitude()));
                    RouteSearch.BusRouteQuery busQuery =
                            new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BUS_DEFAULT, city, 0);
                    mRouteSearch.calculateBusRouteAsyn(busQuery);
                } else {
                    Toast.makeText(this, "请输入终点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.drive_car:
                wayCode = CODE_DRIVE_WAY;
                walkWay.setSelected(false);
                busWay.setSelected(false);
                driveWay.setSelected(true);
                clearRoute();
                busResult.setVisibility(View.GONE);
                driveStrategy.setVisibility(View.VISIBLE);
                int strategyFlag = 0;
                try {
                    strategyFlag = mNavigation.strategyConvert(avoidCongestion, avoidHighWay, avoidCost, priorHighWay, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strategyFlag >= 0) {
                    mNavigation.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverLays.size(); i++) {
            RouteOverLay route = routeOverLays.valueAt(i);
            route.removeFromMap();
        }
        routeOverLays.clear();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.congestion:
                avoidCongestion = isChecked;
                driveWay.performClick();
                break;
            case R.id.no_highway:
                if (priorHighWay) {
                    Toast.makeText(this, "不走高速与高速优先不能同时选择", Toast.LENGTH_SHORT).show();
                    noHighWay.setChecked(false);
                    avoidHighWay = false;
                } else {
                    avoidHighWay = isChecked;
                    driveWay.performClick();
                }
                break;
            case R.id.cost:
                if (priorHighWay) {
                    Toast.makeText(this, "高速优先与避免收费不能同时选择", Toast.LENGTH_SHORT).show();
                    noCost.setChecked(false);
                    avoidCost = false;
                } else {
                    avoidCost = isChecked;
                    driveWay.performClick();
                }
                break;
            case R.id.highway:
                if (avoidCost || avoidHighWay) {
                    Toast.makeText(this, "高速优先不能与避免收费或不走高速同时选择。", Toast.LENGTH_SHORT).show();
                    highWay.setChecked(false);
                    priorHighWay = false;
                } else {
                    priorHighWay = isChecked;
                    driveWay.performClick();
                }
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before == 0) {
            endItemBoolean = false;
            resultList.setVisibility(View.GONE);
        }
        if (count == 0) {
            clearRoute();
        }
        String newText = s.toString().trim();
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery query = new InputtipsQuery(newText, city);
            Inputtips inputtips = new Inputtips(getApplicationContext(), query);
            inputtips.setInputtipsListener(this);
            //异步查询
            inputtips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        try {
            if (i == 1000) {
                mTipList = new ArrayList<>();
                for (Tip tip :
                        list) {
                    if (tip.getPoint() == null) {
                        continue;
                    }
                    mTipList.add(tip);
                }
                if (mTipList == null || mTipList.isEmpty()) {
                    Toast.makeText(this, "抱歉，没有搜索到结果，请换个关键词试试", Toast.LENGTH_SHORT).show();
                    resultList.setVisibility(View.GONE);
                } else {
                    if (endItemBoolean) {
                        return;
                    } else {
                        resultList.setVisibility(View.VISIBLE);
                        SearchResultAdapter resultAdapter = new SearchResultAdapter(getApplicationContext(), mTipList);
                        resultList.setAdapter(resultAdapter);
                        resultAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                Toast.makeText(this, "出错了，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Throwable e) {
            Toast.makeText(this, "出错了，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int errorCode) {
        try {
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (poiItem == null) {
                    return;
                }
                LatLonPoint exitP = poiItem.getExit();
                LatLonPoint enterP = poiItem.getEnter();
                if (pointType == PoiInputItemWidget.TYPE_START) {
                    if (exitP != null) {
                        startLatLng = new NaviLatLng(exitP.getLatitude(), exitP.getLongitude());
                    } else {
                        if (enterP != null) {
                            startLatLng = new NaviLatLng(enterP.getLatitude(), enterP.getLongitude());
                        }
                    }
                    startList.clear();
                    startList.add(startLatLng);
                }
                if (pointType == PoiInputItemWidget.TYPE_DEST) {
                    if (enterP != null) {
                        endLatLng = new NaviLatLng(enterP.getLatitude(), enterP.getLongitude());
                    } else {
                        if (exitP != null) {
                            endLatLng = new NaviLatLng(exitP.getLatitude(), exitP.getLongitude());
                        }
                    }
                    endList.clear();
                    endList.add(endLatLng);
                }
                switch (wayCode) {
                    case CODE_WALK_WAY:
                        walkWay.performClick();
                        break;
                    case CODE_BUS_WAY:
                        busWay.performClick();
                        break;
                    case CODE_DRIVE_WAY:
                        driveWay.performClick();
                        break;
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.start_point) {
            pointType = PoiInputItemWidget.TYPE_START;
        } else if (v.getId() == R.id.end_point) {
            pointType = PoiInputItemWidget.TYPE_DEST;
        } else {
            pointType = -1;
        }
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        routeOverLays.clear();
        List<AMapNaviPath> pathList = new ArrayList<>();
        HashMap<Integer, AMapNaviPath> paths = mNavigation.getNaviPaths();
        for (int anInt : ints) {
            AMapNaviPath path = paths.get(anInt);
            if (path != null) {
                drawRoutes(anInt, path);
                pathList.add(path);
            }
        }
        routeAndNavi.setVisibility(View.VISIBLE);
        routeLineAdapter = new RouteLineAdapter(getApplicationContext(), pathList);
        routeLineWay.setAdapter(routeLineAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.result_list:
                //点击提示后再次进行搜索，获取POI出入口信息
                if (mTipList != null) {
                    Tip tip = (Tip) parent.getItemAtPosition(position);
                    endPoint.setText(tip.getName());
                    endItemBoolean = true;
                    Poi selectedPoi = new Poi(tip.getName(), new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude()), tip.getPoiID());
                    if (!TextUtils.isEmpty(selectedPoi.getPoiId())) {
                        PoiSearch.Query query = new PoiSearch.Query(selectedPoi.getName(), "", city);
                        query.setDistanceSort(false);
                        query.requireSubPois(true);
                        PoiSearch poiSearch = new PoiSearch(getApplicationContext(), query);
                        poiSearch.setOnPoiSearchListener(this);
                        poiSearch.searchPOIIdAsyn(selectedPoi.getPoiId());
                        resultList.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.route_line_way:
                if (routeOverLays.size() == 1) {
                    mNavigation.selectRouteId(routeOverLays.keyAt(0));
                    routeLineAdapter.setSelectedPosition(position);
                    routeLineAdapter.notifyDataSetChanged();
                } else {
                    int routeId = routeOverLays.keyAt(position);
                    //突出选择的那条路
                    for (int i = 0; i < routeOverLays.size(); i++) {
                        int key = routeOverLays.keyAt(i);
                        routeOverLays.get(key).setTransparency(0.4f);
                    }
                    routeLineAdapter.setSelectedPosition(position);
                    routeLineAdapter.notifyDataSetChanged();
                    RouteOverLay routeOverlay = routeOverLays.get(routeId);
                    if (routeOverlay != null) {
                        routeOverlay.setTransparency(1);
                        //把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
                        routeOverlay.setZindex(zindex++);
                    }
                    mNavigation.selectRouteId(routeId);
                    break;
                }
        }

    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay route = new RouteOverLay(aMap, path, this);
        route.setTrafficLine(false);
        route.addToMap();
        routeOverLays.put(routeId, route);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (busRouteResult != null && busRouteResult.getPaths() != null) {
                if (busRouteResult.getPaths().size() > 0) {
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(getApplicationContext(), busRouteResult);
                    busResult.setAdapter(mBusResultListAdapter);
                } else if (busRouteResult.getPaths() == null) {
                    Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, i, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Toast.makeText(getApplicationContext(), "计算路线失败，errorcode＝" + i, Toast.LENGTH_SHORT).show();
    }


    /**
     * ==========================================================================
     * 此后的方法不需要修改或编辑
     * ===========================================================================
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }
}
