package com.islam.newsfeeder.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.other.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ActivityUtils {
    private ActivityUtils() {
    }

    @BindingAdapter({"target", "height", "width", "imageUrl"})
    public static void loadImageBinding(ImageView imageView, Target target, int height, int width, String Url) {
        loadImage(target, height, width, Url, null);

    }

    public static void loadImage(ImageView imageView, int height, int width,
                                 String Url,
                                 RoundedCornersTransformation cornersTransformation) {
        if (Url != null && !Url.isEmpty()) {
            RequestCreator creator = initPicasso(Url, cornersTransformation);
            creator.resize(width, height);
            creator.into(imageView);
        } else imageView.setImageResource(R.drawable.no_image_placeholder);

    }


    public static void loadImage(Target target, int height, int width,
                                 String Url,
                                 RoundedCornersTransformation cornersTransformation) {
        if (Url != null && !Url.isEmpty()) {
            RequestCreator creator = initPicasso(Url, cornersTransformation);
            creator.resize(width, height);
            creator.into(target);
        }
    }

    private static RequestCreator initPicasso(String Url,
                                              RoundedCornersTransformation cornersTransformation) {

        RequestCreator creator = Picasso.get().load(Url);
        if (cornersTransformation != null)
            creator.transform(cornersTransformation);
        creator.placeholder(R.drawable.placeholder);
        creator.error(R.drawable.no_image_placeholder);
        return creator;
    }

    public static void setVisibility(int v, View... views) {
        for (View view : views) {
            view.setVisibility(v);
        }
    }
    public static void showToast(Context context, int stringId) {
        showToast(context,context.getString(stringId));
    }

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Open the browser with given url
     *
     * @param context
     * @param color   The color of browser toolbar
     * @param url     The url of the article
     */
    public static void openCustomTab(Context context, int color, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        builder.setToolbarColor(color);
        //set start and exit animations
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }


    /**
     * convert the string time to different format like ( 1 hour ago ,  yesterday ...etc)
     *
     * @param time The string time fetched from the api
     * @return
     */
    public static String calculateTimeDiff(String time) {
        try {
            //get the first 11 chars ti match the format yyyy-MM-dd
            time = time.substring(0, 10);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(time);

            String diff = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), 0);
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String convertProvidersToString(List<Provider> providers) {
        List<String> resources = new ArrayList<>();
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            if (provider.isChecked()) {
                resources.add(provider.getSourceId());
            }
        }
        return TextUtils.join(",", resources);
    }
}
