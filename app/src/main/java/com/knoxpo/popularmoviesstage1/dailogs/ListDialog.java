package com.knoxpo.popularmoviesstage1.dailogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.knoxpo.popularmoviesstage1.R;

import java.util.ArrayList;

/**
 * Created by asus on 4/18/2016.
 */
public abstract class ListDialog extends DialogFragment {

    protected static final String
            ARGS_ITEMS = "items",
            ARGS_ID = "id";

    private ArrayList<String> mItems;
    private RecyclerView mListRV;

    public abstract void onBindItem(TextView v, String item, int position);

    protected void onItemClicked(String item) {
        //overload this to listen
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = getArguments().getStringArrayList(ARGS_ITEMS);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        mListRV = (RecyclerView) inflater.inflate(R.layout.dialog_list, null);

        mListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        mListRV.setAdapter(new Adapter());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Panel);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setView(mListRV, 0, 0, 0, 0);

        return dialog;
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private LayoutInflater mInflater;

        public Adapter() {
            mInflater = LayoutInflater.from(getActivity());
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindItem(mItems.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }

            public void bindItem(final String item, int position) {

                onBindItem((TextView) itemView, item, position);
                /*((TextView)itemView).setText(item.toString());*/
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClicked(item);
                    }
                });
            }
        }
    }
}