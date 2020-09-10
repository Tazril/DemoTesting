package com.crossover;

import com.crossover.services.ExternalRatingApprovalService;
import com.crossover.services.NotificationService;

public class ModerateClass {

    private int lastRating;
    private NotificationService notificationService;
    private ExternalRatingApprovalService externalRatingApprovalService;

    public ModerateClass() {

    }

    public String createRatingString(int rating, int ratingCeiling) {
        StringBuilder ratingStr = new StringBuilder();

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
        if (rating == lastRating) {
            ratingStr.append("-CACHED");
        }

        this.notificationService.notify(rating);

        return ratingStr.toString();
    }
}
