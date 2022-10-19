package com.xiaobo.smartcalendar.activity.SearchLocationActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaobo.smartcalendar.R;

import java.util.List;

public class SearchLocationActivity extends AppCompatActivity implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener, View.OnClickListener {

    private AMap aMap;
    private String mKeyWords = "";              
    private ProgressDialog progDialog = null;   

    private PoiResult poiResult;                
    private int currentPage = 1;
    private PoiSearch.Query query;              
    private PoiSearch poiSearch;                
    private TextView mKeywordsTextView;
    private Marker mPoiMarker;
    private ImageView mCleanKeyWords;

    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int RESULT_CODE_KEYWORDS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        mCleanKeyWords = findViewById(R.id.clean_keywords);
        mCleanKeyWords.setOnClickListener(this);
        init();
        mKeyWords = "";
    }

    
    private void init() {
        if (aMap == null) {
            

        }
        mKeywordsTextView = findViewById(R.id.poi_keywords);
        mKeywordsTextView.setOnClickListener(this);
    }

    
    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);    
        aMap.setInfoWindowAdapter(this);        
        aMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    
    protected void doSearchQuery(String keywords) {
        showProgressDialog();
        currentPage = 1;
        
        query = new PoiSearch.Query(keywords, "", Constants.DEFAULT_CITY);
        
        query.setPageSize(10);
        query.setPageNum(currentPage);

        try {
            poiSearch = new PoiSearch(this, query);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.poi_keywords:
                Intent i = new Intent(this, InputTipsActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                break;
            case R.id.clean_keywords:
                mKeywordsTextView.setText("");
                aMap.clear();
                mCleanKeyWords.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                poiResult = result;
                
                List<PoiItem> poiItems = poiResult.getPois();   
                List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();   

                if (poiItems != null && poiItems.size() > 0) {
                    aMap.clear();   

                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + mKeyWords);
        progDialog.show();
    }
    
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
}