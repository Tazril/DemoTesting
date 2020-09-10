package com.crossover;

import com.crossover.services.ExternalRatingApprovalService;
import com.crossover.services.NotificationService;
import com.crossover.utils.Utils;
import java.util.concurrent.TimeUnit;

public class HardClass {

    private static final int HARD_CACHE;
    private static final Utils UTILS;

    static {
        HARD_CACHE = 22;
        UTILS = new Utils();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private NotificationService notificationService;
    private ExternalRatingApprovalService externalRatingApprovalService;

    public HardClass() {

    }

    public String createRatingString(int rating, int ratingCeiling) {
        StringBuffer ratingStr = new StringBuffer();

        if (rating > ratingCeiling) {
            throw new IllegalArgumentException("Cannot be over the hard ceiling");
        }

        if (!externalRatingApprovalService.isApproved(rating)) {
            return "NOT-APP";
        }

        if (rating == ratingCeiling) {
            ratingStr.append("TOP+");
        } else {
            final int midCeiling = (int) Math.floor((ratingCeiling / 2.0));
            if (rating >= midCeiling) {
                ratingStr.append("HIGH=");
            } else if (rating < midCeiling) {
                ratingStr.append("LOW-");
            }
        }
        ratingStr.append(rating);
        if (rating == HARD_CACHE) {
            ratingStr.append("-CACHED");
        }

        ratingStr.append(UTILS.getRatingDecoration());

        this.notificationService.notify(rating);

        return ratingStr.toString();
    }
}
