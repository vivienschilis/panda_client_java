package com.pandastream;
import java.util.*;
import net.sf.json.JSONObject;


public class AudioVideoModel extends ApiModel
{	
	public AudioVideoModel(JSONObject json) {
		super(json);
	}
	
	public String getExtName(){
		return get("extname");
	}
	
	public String getPath(){
		return get("path");
	}
	
	public String getStatus(){
		return get("status");
	}
	
	public String getVideoCodec(){
		return get("video_codec");
	}
	
	public String getAudioCodec(){
		return get("audio_codec");
	}
	
	public String getHeight(){
		return get("height");
	}
	
	public String getWidth(){
		return get("width");
	}
	
	public String getFps(){
		return get("fps");
	}
	
	public String getDuration(){
		return get("duration");
	}
	
	public String getFileSize(){
		return get("file_size");
	}
	
	public String getCreatedAt(){
		return get("created_at");
	}
	
	public String getUpdatedAt(){
		return get("updated_at");
	}
	
	public String getErrorClass(){
		return get("error_class");
	}
	
	public String getErrorMessage(){
		return get("error_message");
	}
	
	public String getUrl() {
		return get("path") + get("extname");
	}
	
}