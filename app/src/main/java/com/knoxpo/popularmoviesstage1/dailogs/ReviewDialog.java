package com.knoxpo.popularmoviesstage1.dailogs;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asus on 4/18/2016.
 */
public class ReviewDialog extends ListDialog {

    public static ReviewDialog newInstance(ArrayList<String> reviews) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARGS_ITEMS,reviews);
        ReviewDialog fragment = new ReviewDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBindItem(TextView v, String item, int position) {
        v.setText(item);
    }
}
