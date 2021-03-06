package com.mgilangjanuar.dev.goscele.modules.forum.detail.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.adapter.CommentRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumCommentListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.presenter.ForumDetailPresenter;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */
public class ForumCommentFragment extends BaseFragment implements ForumCommentListener {

    @BindView(R.id.recycler_view_forum_detail)
    RecyclerView recyclerView;
    @BindView(R.id.text_status_forum_comments)
    TextView status;
    @BindView(R.id.swipe_refresh_forum_detail)
    SwipeRefreshLayout swipeRefreshLayout;
    private ForumDetailPresenter presenter;

    public static BaseFragment newInstance(ForumDetailPresenter presenter) {
        Bundle args = new Bundle();
        ForumCommentFragment fragment = new ForumCommentFragment();
        fragment.presenter = presenter;
        fragment.presenter.setCommentListener(fragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_forum_comment;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        presenter.runProvider(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.runProvider(ForumCommentFragment.this, true);
            }
        });
    }

    @Override
    public void onRetrieveComments(CommentRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            status.setVisibility(TextView.GONE);
        } else {
            status.setText(R.string.empty);
        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String error) {
        showSnackbar(error);
    }
}
