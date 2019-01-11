package com.islam.newsfeeder.util;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.other.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ActivityUtils {
    private ActivityUtils() {
    }

    public static void loadImage(ImageView imageView,
                                 String Url,
                                 RoundedCornersTransformation cornersTransformation) {
        if (Url != null && !Url.isEmpty()) {
            RequestCreator creator = initPicasso(Url, cornersTransformation);
            creator.into(imageView);
        }
    }


    public static void loadImage(Target target,
                                 String Url,
                                 RoundedCornersTransformation cornersTransformation) {
        if (Url != null && !Url.isEmpty()) {
            RequestCreator creator = initPicasso(Url, cornersTransformation);
            creator.into(target);
        }
    }

    private static RequestCreator initPicasso(String Url,
                                              RoundedCornersTransformation cornersTransformation) {

        RequestCreator creator = Picasso.get().load(Url);
        if (cornersTransformation != null)
            creator.transform(cornersTransformation);
        creator.placeholder(R.drawable.placeholder);
        creator.error(R.drawable.placeholder);
        return creator;
    }

    public static void setVisibility(int v, View... views) {
        for (View view : views) {
            view.setVisibility(v);
        }
    }

    public static void openCustomTab(Context context, int color, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        builder.setToolbarColor(color);
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }


    public static String calculateTimeDiff(String time) {
        try {
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
}
