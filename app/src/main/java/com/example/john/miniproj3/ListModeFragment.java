package com.example.john.miniproj3;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by John on 25/4/2016.
 */
public class ListModeFragment extends BaseFragment{
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initData() {
        switch (getClass().getSimpleName()) {
            case "CardModeFragment":
                getActivity().setTitle("Card Mode");
                break;
            case "ListModeFragment":
                getActivity().setTitle("List Mode");
                break;
        }
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.receiverviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyAdapterList());
    }
}
