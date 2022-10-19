package com.xiaobo.smartcalendar.activity.PoiSearchWithoutMap;

import static com.xiaobo.smartcalendar.Public.PublicVaribale.POISEARCHACTIVITY_RESULT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaobo.smartcalendar.Public.CurrentLocation;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.SearchLocationActivity.Constants;
import com.amap.api.services.core.SuggestionCity;
import com.xiaobo.smartcalendar.activity.SearchLocationActivity.PoiOverlay;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class PoiSearchWithoutMapActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, PoiSearch.OnPoiSearchListener, AdapterView.OnItemClickListener {

    private SearchView mSearchView;
    private ImageView mBack;
    private ListView mInputListView;
    private PoiListAdapter mAdapter;
    private ArrayList<PoiInfoModel> mData;

    private String mKeyWords = "";  
    private PoiResult poiResult;    
    private int currentPage = 1;
    private PoiSearch.Query query;  
    private PoiSearch poiSearch;    

    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int RESULT_CODE_KEYWORDS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search_without_map);

        Log.d("POISEARCH", "打开了搜索页面");

        receivedInfo();

        initData();

        initSearchView();
    }

    
    private void receivedInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {

            }
            catch (Exception e) {
                Log.e("PoiSearchActivity", "获取上级信息错误");
            }
        }
    }
    
    private void initData() {
        this.mData = new ArrayList<PoiInfoModel>();
        mInputListView = findViewById(R.id.inputtip_list);
        mAdapter = new PoiListAdapter(this, mData);
        mInputListView.setAdapter(mAdapter);
        mInputListView.setOnItemClickListener(this);
    }

    private void initSearchView() {
        mSearchView = findViewById(R.id.keyWord);
        mSearchView.setOnQueryTextListener(this);
        
        mSearchView.setIconified(false);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    
    protected void doSearchQuery(String keywords) {
        currentPage = 1;
        
        query = new PoiSearch.Query(keywords, "", CurrentLocation.getInstance().getDistrict());
        
        query.setPageSize(30);
        
        query.setPageNum(currentPage);

        try {
            poiSearch = new PoiSearch(this, query);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }
    
    private void showSuggestCity(List<SuggestionCity> cities) {
        Log.d("PoiSearch", "poi没有搜索到数据");
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }

        Log.d("PoiSearchWithoutMapActivity", infomation);
    }

    @Override
    
    public boolean onQueryTextSubmit(String query) {
        Log.d("PoiSearch", "提交查询:" + query);
        doSearchQuery(query);
        return false;
    }

    @Override
    
    public boolean onQueryTextChange(String newText) {
        Log.d("PoiSearch", "输入框信息改变:" + newText);
        doSearchQuery(newText);
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        Log.d("PoiSearch", "POI搜索回调");
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {
                    poiResult = result;
                    
                    List<PoiItem> poiItems = poiResult.getPois();
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {
                        Log.d("PoiSearch", "搜索到了相关数据");
                        
                        for (PoiItem item : poiItems) {
                            LatLonPoint llp = item.getLatLonPoint();
                            double lon = llp.getLongitude();
                            double lat = llp.getLatitude();
                            
                            String title = item.getTitle();
                            
                            String address = item.getSnippet();
                            mData.add(new PoiInfoModel(String.valueOf(lon), String.valueOf(lat), title, address));
                            refresh();
                        }

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {

                        Log.d("PoiSearchWithoutMapActivity", "没有搜索到相关数据");
                    }
                }
            } else {

                Log.d("PoiSearchWithoutMapActivity", "没有搜索到相关数据");
            }
        } else {

            Log.d("PoiSearchWithoutMapActivity", String.valueOf(rCode));
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
        PoiInfoModel model = mData.get(position);
        
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("lon", model.getLon());
        bundle.putString("lat", model.getLat());
        bundle.putString("title", model.getTitle());
        bundle.putString("address", model.getAddress());
        intent.putExtras(bundle);
        setResult(POISEARCHACTIVITY_RESULT, intent);
        finish();
    }

    
    private void refresh() {
        mAdapter.notifyDataSetChanged();
    }

}