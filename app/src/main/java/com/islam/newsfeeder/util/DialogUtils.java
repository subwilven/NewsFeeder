package com.islam.newsfeeder.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.islam.newsfeeder.POJO.Provider;

import java.util.List;

public final class DialogUtils {
    private DialogUtils() {
    }

    public static MaterialDialog createLoadingDialog(Context context, int title, int content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public static MaterialDialog createListDialog(Context context, int title, List<Provider> content,
                                                  MaterialDialog.ListCallback callback) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .items(content)
                .itemsCallback(callback)
                .build();
    }

}
