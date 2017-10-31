package com.mobcent.lowest.module.place.helper;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.lowest.module.place.api.constant.PlaceConstant;
import com.mobcent.lowest.module.place.delegate.PlaceFiledDelegate;
import com.mobcent.lowest.module.place.model.PlaceTypeModel;
import com.mobcent.lowest.module.place.service.PlaceAroundService;
import com.mobcent.lowest.module.place.service.impl.PlaceAroundServiceImpl;
import java.util.HashMap;
import java.util.Map;

public class PlaceFiledHelper {
    public static PlaceFiledHelper helper;
    private static Map<String, PlaceTypeModel> placeTypeMap;
    private String DEFAULT_QUERY_TYPE = "none_place";
    public String TAG = "PlaceFiledHelper";
    private String queryType = "";
    private TypeQueryTask task;

    class TypeQueryTask extends AsyncTask<Void, Void, PlaceTypeModel> {
        private PlaceAroundService aroundService = new PlaceAroundServiceImpl(this.context);
        private Context context;
        private PlaceFiledDelegate delegate;
        private String queryType;

        public TypeQueryTask(Context context, String queryType, PlaceFiledDelegate delegate) {
            this.queryType = queryType;
            this.context = context;
            this.delegate = delegate;
        }

        protected PlaceTypeModel doInBackground(Void... params) {
            if (PlaceFiledHelper.placeTypeMap.isEmpty()) {
                Map<String, PlaceTypeModel> placeTypeMap = this.aroundService.queryPlaceType(PlaceConstant.ASSETS_PLACE_FILED_FILENAME);
                if (placeTypeMap == null) {
                    return null;
                }
                PlaceFiledHelper.setPlaceTypeMap(placeTypeMap);
            }
            if (PlaceFiledHelper.getPlaceTypeMap().get(this.queryType) == null) {
                return (PlaceTypeModel) PlaceFiledHelper.getPlaceTypeMap().get(PlaceFiledHelper.this.DEFAULT_QUERY_TYPE);
            }
            return (PlaceTypeModel) PlaceFiledHelper.getPlaceTypeMap().get(this.queryType);
        }

        protected void onPostExecute(PlaceTypeModel result) {
            if (result != null) {
                PlaceFiledHelper.this.setQueryType(this.queryType);
                this.delegate.onPlaceFiledResult(result);
            }
        }
    }

    public static PlaceFiledHelper getInstance() {
        if (helper == null) {
            helper = new PlaceFiledHelper();
            placeTypeMap = new HashMap();
        }
        return helper;
    }

    public void queryFiledType(Context context, String queryType, PlaceFiledDelegate delegate) {
        if (!(this.task == null || this.task.isCancelled())) {
            this.task.cancel(true);
        }
        if (delegate == null) {
            delegate = new PlaceFiledDelegate() {
                public void onPlaceFiledResult(PlaceTypeModel placeTypeModel) {
                }
            };
        }
        this.task = new TypeQueryTask(context, queryType, delegate);
        this.task.execute(new Void[0]);
    }

    public static Map<String, PlaceTypeModel> getPlaceTypeMap() {
        return placeTypeMap;
    }

    public static void setPlaceTypeMap(Map<String, PlaceTypeModel> placeTypeMap) {
        placeTypeMap = placeTypeMap;
    }

    public String getQueryType() {
        return this.queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
