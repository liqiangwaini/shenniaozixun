/*
 * Copyright (C) 2012 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xingbobase.extra.chooser;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.xingbobase.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * List adapter for Files.
 * 
 * @version 2013-12-11
 * @author paulburke (ipaulpro)
 */
public class FileListAdapter extends BaseAdapter {

    private final static int ICON_FOLDER = R.drawable.chooser_file_ic_folder;
    private final static int ICON_FILE = R.drawable.chooser_file_ic_file;

    private final LayoutInflater mInflater;

    private List<File> mData = new ArrayList<File>();
    private Context context;

    public FileListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void add(File file) {
        mData.add(file);
        notifyDataSetChanged();
    }

    public void remove(File file) {
        mData.remove(file);
        notifyDataSetChanged();
    }

    public void insert(File file, int index) {
        mData.add(index, file);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public File getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public List<File> getListItems() {
        return mData;
    }

    /**
     * Set the list items without notifying on the clear. This prevents loss of
     * scroll position.
     *
     * @param data
     */
    public void setListItems(List<File> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);

        // Get the file at the current position
        final File file = getItem(position);

        // Set the TextView as the file name
        title.setText(file.getName());

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        // If the item is not a directory, use the file icon
        //int icon = file.isDirectory() ? ICON_FOLDER : ICON_FILE;
        //view.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        if (file.isDirectory()) {
            icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            icon.setImageResource(R.drawable.chooser_file_ic_chooser);
        } else {
            Bitmap bitmap=getBitmapFromVideo(file);
            if(bitmap==null){
              icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
              icon.setImageResource(R.drawable.chooser_file_ic_file);
            }else{
                icon.setScaleType(ImageView.ScaleType.FIT_XY);
                icon.setImageBitmap(bitmap);
            }

        }
        return convertView;
    }

    public Bitmap getBitmapFromVideo(File file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context,Uri.fromFile(file));
        }catch (RuntimeException e){
            return null;
        }
        return retriever.getFrameAtTime(1*1000*1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }
}