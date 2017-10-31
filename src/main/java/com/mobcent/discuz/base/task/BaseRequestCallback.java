package com.mobcent.discuz.base.task;

public interface BaseRequestCallback<Result> {
    void onPostExecute(Result result);

    void onPreExecute();
}
