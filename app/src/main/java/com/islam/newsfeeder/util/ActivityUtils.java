package com.islam.newsfeeder.util;

import android.widget.ImageView;

import com.islam.newsfeeder.util.other.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public final class ActivityUtils {
    private ActivityUtils() {
    }

    public static void loadImage(ImageView imageView,
                                 String Url,
                                 RoundedCornersTransformation cornersTransformation) {
        if (Url != null && !Url.isEmpty()) {
            RequestCreator creator = Picasso.get().load(Url);
            if (cornersTransformation != null)
                creator.transform(cornersTransformation);
            creator.into(imageView);
        }
    }
}
