/*
 * Copyright 2015. Pureum Choe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.massivcode.androidmusicplayer.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.massivcode.androidmusicplayer.R;
import com.massivcode.androidmusicplayer.adapters.PlaylistAdapter;
import com.massivcode.androidmusicplayer.database.DbHelper;
import com.massivcode.androidmusicplayer.database.MyPlaylistContract;
import com.massivcode.androidmusicplayer.database.MyPlaylistFacade;
import com.massivcode.androidmusicplayer.events.Event;
import com.massivcode.androidmusicplayer.events.MusicEvent;
import com.massivcode.androidmusicplayer.events.PlayBack;
import com.massivcode.androidmusicplayer.events.ReloadPlaylist;

import de.greenrobot.event.EventBus;


public class PlaylistFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();
    private FloatingActionButton mFab;
    private TextView mNotifyNoDataTextView;
    private ExpandableListView mListView;

    private MyPlaylistFacade mFacade;
    private PlaylistAdapter mAdapter;

    public PlaylistFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFacade = new MyPlaylistFacade(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener((View.OnClickListener) getActivity());

        mNotifyNoDataTextView = (TextView) view.findViewById(R.id.notify_noData_tv);
        mListView = (ExpandableListView) view.findViewById(R.id.playlist_listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (Build.VERSION.SDK_INT < 23) {
            mAdapter = new PlaylistAdapter(mFacade.getAllUserPlaylist(), getActivity(), true);
            mListView.setAdapter(mAdapter);

        } else {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (mAdapter == null) {
                    if (mFacade.isAlreadyExist()) {
                        mNotifyNoDataTextView.setVisibility(View.GONE);
                        EventBus.getDefault().post(new ReloadPlaylist());
                    } else {
                        mNotifyNoDataTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
//        for(int i=0; i<mAdapter.getGroupCount(); i++) {
//            mListView.expandGroup(i);
//        }

        //플레이리스트 하위 음악데이터 이벤트리스너
        mListView.setOnChildClickListener((ExpandableListView.OnChildClickListener) getActivity());
        //플레이리스트 이벤트리스너
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    // EventBus 용 이벤트 수신
    @SuppressWarnings("unused")
    public void onEvent(Event event) {

        if (event instanceof MusicEvent) {
//            Log.d(TAG, "플레이리스트에서 뮤직이벤트를 받았습니다.");
            if(mAdapter != null) {
                mAdapter.swapMusicEvent((MusicEvent) event);
                mAdapter.notifyDataSetChanged();
            }
        } else if (event instanceof PlayBack) {
//            Log.d(TAG, "플레이리스트에서 플레이백이벤트를 받았습니다.");
            PlayBack playback = (PlayBack) event;
            if(mAdapter != null) {
                if (mAdapter.getPlayback() == null || mAdapter.getPlayback().isPlaying() != playback.isPlaying()) {
                    mAdapter.swapPlayback(playback);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else if (event instanceof ReloadPlaylist) {
            if (mAdapter == null) {
                Log.d(TAG, "어댑터 널");
                mAdapter = new PlaylistAdapter(mFacade.getAllUserPlaylist(), getActivity(), true);
                mListView.setAdapter(mAdapter);
            } else {
                Log.d(TAG, "어댑터 널이 아님");
                mAdapter.changeCursor(mFacade.getAllUserPlaylist());
            }
            mNotifyNoDataTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int itemType = mListView.getPackedPositionType(id);    //클릭한 요소 Type(ex. 그룹뷰, 자식뷰)
        Cursor itemData = (Cursor)parent.getAdapter().getItem(position); //클릭한 요소에 대한 데이터 Load.
        if (itemType == 0) {
            Log.d(TAG, "그룹뷰가 롱클릭되었습니다.");
            String name = itemData.getString(itemData.getColumnIndexOrThrow(MyPlaylistContract.MyPlaylistEntry.COLUMN_NAME_PLAYLIST));
            showConfirmDialog(name, itemType);
        } else if(itemType ==1){
            Log.d(TAG, "자식뷰가 롱클릭 되었습니다.");
            String _id = itemData.getString(itemData.getColumnIndexOrThrow(MyPlaylistContract.MyPlaylistEntry._ID));
            showConfirmDialog(_id, itemType);
        }else {
            Log.d(TAG, "그냥 이유없이 롱클릭되었습니다. 왜지....");
            Log.d(TAG, "포지션 : " + position);
        }
        return true;
    }

    private void showConfirmDialog(final String param, final int itemType) {
        Log.d(TAG, "삭제 눌림");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_dialog_title)
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(itemType == 0) { //그룹 삭제
                                    mFacade.deleteUserPlaylist(param);
                                }else if(itemType ==1) { //단일 삭제
                                    mFacade.deletePlayListMusic(param);
                                }
                                mAdapter.changeCursor(mFacade.getAllUserPlaylist());

                                if (mFacade.isAlreadyExist()) {
                                    Log.d(TAG, "데이터가 있습니다.");
                                    mNotifyNoDataTextView.setVisibility(View.GONE);
                                } else {
                                    Log.d(TAG, "데이터가 없습니다.");
                                    mNotifyNoDataTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }
}
