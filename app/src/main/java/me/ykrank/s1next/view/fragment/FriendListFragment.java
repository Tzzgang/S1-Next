package me.ykrank.s1next.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.ykrank.androidtools.ui.vm.LoadingViewModel;
import com.github.ykrank.androidtools.util.L;

import io.reactivex.Single;
import me.ykrank.s1next.data.api.model.collection.Friends;
import me.ykrank.s1next.data.api.model.wrapper.BaseDataWrapper;
import me.ykrank.s1next.view.adapter.FriendRecyclerViewAdapter;

/**
 * Created by ykrank on 2017/1/16.
 */

public class FriendListFragment extends BaseRecyclerViewFragment<BaseDataWrapper<Friends>> {
    public static final String TAG = FriendListFragment.class.getName();
    private static final String ARG_UID = "uid";

    private String uid;
    private FriendRecyclerViewAdapter mRecyclerAdapter;

    public static FriendListFragment newInstance(String uid) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_UID, uid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = getArguments().getString(ARG_UID);
        L.leaveMsg("FriendListFragment##Uid:" + uid);

        RecyclerView recyclerView = getRecyclerView();
        Activity activity = getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerAdapter = new FriendRecyclerViewAdapter(activity);
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected Single<BaseDataWrapper<Friends>> getSourceObservable(@LoadingViewModel.LoadingDef int loading) {
        return mS1Service.getFriends(uid);
    }

    @Override
    protected void onNext(BaseDataWrapper<Friends> data) {
        Friends friends = data.getData();
        if (friends.getFriendList() == null || friends.getFriendList().isEmpty()) {
            //No data
        } else {
            super.onNext(data);
            mRecyclerAdapter.diffNewDataSet(friends.getFriendList(), true);
        }
    }
}
