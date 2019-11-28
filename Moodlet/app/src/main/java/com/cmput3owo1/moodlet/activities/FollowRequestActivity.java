package com.cmput3owo1.moodlet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.adapters.RequestListAdapter;
import com.cmput3owo1.moodlet.models.FollowRequest;
import com.cmput3owo1.moodlet.models.User;
import com.cmput3owo1.moodlet.services.IMoodEventServiceProvider;
import com.cmput3owo1.moodlet.services.IUserServiceProvider;
import com.cmput3owo1.moodlet.services.MoodEventService;
import com.cmput3owo1.moodlet.services.UserService;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class FollowRequestActivity extends AppCompatActivity implements
        IUserServiceProvider.OnAcceptRequestsListener, RequestListAdapter.OnAcceptClickListener,
        RequestListAdapter.OnDeclineClickListener, IUserServiceProvider.OnAcceptRequestListener {

    Toolbar toolbar;
    private ListView requestsListView;
    private RequestListAdapter requestsAdapter;
    private ArrayList<FollowRequest> requestDataList;
    private IUserServiceProvider userService;
    private IMoodEventServiceProvider moodEventService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //      Manually replace navigation icon with custom icon.
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              TODO: Create intent to go to the follow requests fragment.
                Toast.makeText(FollowRequestActivity.this, "Go back", Toast.LENGTH_SHORT).show();

                finish();

            }
        });

        requestsListView = findViewById(R.id.requests_list_view);
        requestDataList = new ArrayList<>();

        //requestDataList.add(new FollowRequest("bitch", "whore"));
        requestsAdapter = new RequestListAdapter(this, requestDataList, this, this); // not sure how the listener works

        requestsListView.setAdapter(requestsAdapter);

        userService = new UserService();
        userService.getFollowRequests(this);

        moodEventService = new MoodEventService();
    }

    @Override
    public void onAcceptRequestsUpdate(ArrayList<FollowRequest> newRequests) {
        requestDataList.clear();
        requestDataList.addAll(newRequests);
        requestsAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnAcceptClick(FollowRequest requestFrom) {
        userService.acceptFollowRequest(requestFrom, this);
    }

    @Override
    public void OnDeclineClick(FollowRequest request) {
        userService.deleteFollowRequest(request);
    }

    @Override
    public void onAcceptRequestSuccess(String newFollowerUsername) {
        moodEventService.updateFollowerWithMostRecentMood(newFollowerUsername);
    }
}
