package com.easyhome.common.modules.plugin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Track的序列化对象,用与AIDL远程通信
 * 
 * 
 */
public class PluginTrack implements Parcelable {

	String ID = "";

	String Name = "";

	String Artist = "";

	String Url = "";

	String json = "";

	public String getID() {
		return this.ID;
	}

	public void setID(String id) {
		this.ID = id;
	}

	public String getName() {
		return this.Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getArtist() {
		return this.Artist;
	}

	public void setArtist(String artist) {
		this.Artist = artist;
	}

	public String getUrl() {
		return this.Url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public PluginTrack() {

	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.ID);
		dest.writeString(this.Name);
		dest.writeString(this.Artist);
		dest.writeString(this.Url);
		dest.writeString(this.json);
	}

	public static final Creator<PluginTrack> CREATOR = new Creator<PluginTrack>() {

		@Override
		public PluginTrack createFromParcel(Parcel source) {
			PluginTrack track = new PluginTrack();
			track.setID(source.readString());
			track.setName(source.readString());
			track.setArtist(source.readString());
			track.setUrl(source.readString());
			track.setJson(source.readString());

			return track;
		}

		@Override
		public PluginTrack[] newArray(int size) {

			return new PluginTrack[size];
		}

	};

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:" + this.ID + "-name:" + this.Name + "-artist:" + this.Artist;
	}
}
