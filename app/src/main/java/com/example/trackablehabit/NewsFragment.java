package com.example.trackablehabit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    TextView likeBtn;

    private ArrayList<FeedModel> feedModelArrayList = new ArrayList<>();
    private FeedAdapter adapterFeed;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.newsRecyclerView);

        adapterFeed = new FeedAdapter(getActivity(), feedModelArrayList);
        recyclerView.setAdapter(adapterFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        populateRecyclerView();

        return rootView;
    }

    private void populateRecyclerView() {

        FeedModel feedModel = new FeedModel(1, 9, 2,
                "Chengxin received the 1 Week reward for keeping up with the Exercise habit! ", "2 hrs");
        feedModelArrayList.add(feedModel);
        feedModel = new FeedModel(2, 26, 6,
                "Edeline kept up with her Drink Water habit for the past 10 days!", "9 hrs");
        feedModelArrayList.add(feedModel);
        feedModel = new FeedModel(3, 17, 5,
                "Hola", "13 hrs");
        feedModelArrayList.add(feedModel);

        adapterFeed.notifyDataSetChanged();
    }
}
