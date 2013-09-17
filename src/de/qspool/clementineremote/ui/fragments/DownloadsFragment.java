/* This file is part of the Android Clementine Remote.
 * Copyright (C) 2013, Andreas Muttscheller <asfa194@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.qspool.clementineremote.ui.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.qspool.clementineremote.App;
import de.qspool.clementineremote.R;
import de.qspool.clementineremote.backend.pb.ClementineMessage;
import de.qspool.clementineremote.backend.player.MySong;
import de.qspool.clementineremote.ui.adapter.DownloadAdapter;

public class DownloadsFragment extends AbstractDrawerFragment {
	private ActionBar mActionBar; 
	private ListView mList;
	private DownloadAdapter mAdapter;
	private Timer mUpdateTimer;

	private View mEmptyDownloads;
	
	public TimerTask getTimerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				if (mAdapter != null) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdapter.notifyDataSetChanged();
							if (App.downloaders.isEmpty()) {
								mList.setEmptyView(mEmptyDownloads);
							}
						}
						
					});
				}
			}
			
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		// Check if we are still connected
		if (App.mClementineConnection == null
		 || App.mClementine           == null
		 || !App.mClementineConnection.isAlive()
		 || !App.mClementine.isConnected()) {
		} else {
			//RequestPlaylistSongs();
			setActionBarTitle();
			mUpdateTimer = new Timer();
			mUpdateTimer.scheduleAtFixedRate(getTimerTask(), 250, 250);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		mUpdateTimer.cancel();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloads_fragment,
				container, false);
		
		mList = (ListView) view.findViewById(R.id.downloads);
		mEmptyDownloads = view.findViewById(R.id.downloads_empty);
		
		// Create the adapter
		mAdapter = new DownloadAdapter(getActivity(), R.layout.download_row, App.downloaders);
		
		//mList.setOnItemClickListener(oiclSong);
		mList.setAdapter(mAdapter);
		
		mActionBar = getSherlockActivity().getSupportActionBar();
	    mActionBar.setTitle("");
	    mActionBar.setSubtitle("");
		
		setHasOptionsMenu(true);
		
		return view;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.playlist_menu, menu);
		
		super.onCreateOptionsMenu(menu,inflater);
	}
	
	private void setActionBarTitle() {
		MySong currentSong = App.mClementine.getCurrentSong();
		if (currentSong == null) {
			mActionBar.setTitle(getString(R.string.player_nosong));
			mActionBar.setSubtitle("");
		} else {
			mActionBar.setTitle(currentSong.getArtist());
			mActionBar.setSubtitle(currentSong.getTitle());
		}
	}
	
	@Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mList.setFastScrollEnabled(true);
        mList.setTextFilterEnabled(true);
        mList.setSelector(android.R.color.transparent);
         
        // Get the position of the current track if we have one

	}
	
	@Override
	public void MessageFromClementine(ClementineMessage clementineMessage) {
		switch (clementineMessage.getMessageType()) {

		default:
			break;
		}
	}

}