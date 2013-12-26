package com.jerome.photoselect.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * ClassName:CategoryInfo <br>
 * Function: <br>
 * 
 * @author Administrator
 * @version
 * @Date 2013 2013-12-5 上午11:25:27
 * 
 * @see
 */
public class CategoryInfo implements Serializable {
	private static final long serialVersionUID = -7835215676193811600L;
	private int id;
	private String path;
	private String name;
	private String thumbnailPath;
	private int photoNum;
	private ArrayList<PhotoInfo> photoPaths;

	public CategoryInfo(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.photoPaths = new ArrayList<PhotoInfo>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public int getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}

	public void addPhotoNum() {
		this.photoNum++;
	}

	public void addPhoto(String path) {
		PhotoInfo item = new PhotoInfo(path);
		if (!photoPaths.contains(item)) {
			photoPaths.add(item);
		}
	}

	public ArrayList<PhotoInfo> getPhotoPaths() {
		return photoPaths;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryInfo other = (CategoryInfo) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
