package com.player.service;


public class PackageInfo {
	private String packageId;
	private String image;
	private String packageName;
	private String packagePrice;
	private String description;
	
	public PackageInfo(){
		
	}
	
	public PackageInfo(String id,String image,String name,String price,String desc){
		this.packageId = id;
		this.image = image;
		this.packageName = name;
		this.packagePrice = price;
		this.description = desc;
	}
	
	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the packagePrice
	 */
	public String getPackagePrice() {
		return packagePrice;
	}
	/**
	 * @param packagePrice the packagePrice to set
	 */
	public void setPackagePrice(String packagePrice) {
		this.packagePrice = packagePrice;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
