package com.islam.newsfeeder.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.islam.newsfeeder.pojo.Provider;

import java.util.List;

public final class DialogUtils {
    private DialogUtils() {
    }

    /**
     * create loading dialog
     *
     * @param context
     * @param title   The title of the dialog
     * @param content The Message of the dialog
     * @return
     */
    public static MaterialDialog createLoadingDialog(Context context, int title, int content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    /**
     * create dilaog that has list of items and single choice
     * @param context
     * @param title The title of the dialog
     * @param content The Message of the dialog
     * @param callback Callback for on Item clicked in the list
     * @return
     */
    public static MaterialDialog createListDialog(Context context, int title, List<Provider> content,
                                                  MaterialDialog.ListCallback callback) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .items(content)
                .itemsCallback(callback)
                .build();
    }

}
