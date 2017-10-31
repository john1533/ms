package com.mobcent.lowest.module.place.helper;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.lowest.module.place.delegate.PlaceHotwordsListener;
import com.mobcent.lowest.module.place.model.PlaceHotNavModel;
import com.mobcent.lowest.module.place.service.impl.helper.PlaceAroundServiceImplHelper;
import java.util.List;

public class PlaceHotwordsHelper {
    public String fileName = "PlaceAroundHotwords.dat";
    private PlaceHotwordsTask task;

    class PlaceHotwordsTask extends AsyncTask<Void, Void, List<PlaceHotNavModel>> {
        private Context context;
        private PlaceHotwordsListener delegate;

        public PlaceHotwordsTask(Context context, PlaceHotwordsListener delegate) {
            this.context = context;
            this.delegate = delegate;
        }

        protected List<PlaceHotNavModel> doInBackground(Void... arg0) {
            return PlaceAroundServiceImplHelper.queryHotWordList(this.context, PlaceHotwordsHelper.this.fileName);
        }

        protected void onPostExecute(List<PlaceHotNavModel> result) {
            if (this.delegate != null) {
                this.delegate.onResult(result);
            }
        }
    }

    public void queryHotwordsList(Context context, PlaceHotwordsListener delegate) {
        if (!(this.task == null || this.task.isCancelled())) {
            this.task.cancel(true);
        }
        this.task = new PlaceHotwordsTask(context, delegate);
        this.task.execute(new Void[0]);
    }
}
