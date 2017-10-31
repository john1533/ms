package com.mobcent.discuz.module.person.activity.delegate;

public interface TaskExecuteDelegate {
    void executeFail();

    void executeSuccess(String str);
}
