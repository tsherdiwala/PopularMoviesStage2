package com.knoxpo.popularmoviesstage1.dailogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.knoxpo.popularmoviesstage1.R;

import java.util.ArrayList;

/**
 * Created by asus on 4/18/2016.
 */
public class VideoDialog extends ListDialog {

    @Override
    public void onBindItem(TextView v, String item, int position) {
        String text = getString(R.string.trailer_list,position+1);
        v.setText(text);
    }

    public static VideoDialog newInstance(ArrayList<String> trailers) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARGS_ITEMS,trailers);
        VideoDialog fragment = new VideoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onItemClicked(String item) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+item)));
    }
}
