package com.crossover;

public class SimpleClass {

    public String createRatingString(int rating, int ratingCeiling) {
        StringBuilder ratingStr = new StringBuilder();

        if (rating > ratingCeiling) {
            throw new IllegalArgumentException("Cannot be over the hard ceiling");
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

        return ratingStr.toString();
    }
}
