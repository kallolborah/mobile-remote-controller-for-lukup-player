package com.player.service;

public class ProgramInfo {
	private String eventId;
	private String serviceId;
	private String eventCategory;
	private String channelPrice;
	private String priceModel;
	private String serviceType;
	private String chlSubscribe;
	private String eventSubscribe;
	private String lock;
	private String isevent;
	
	private String eventImage;
	private String eventName;
	private String eventPrice;
	private String like;
	private String timeStamp;
	
	private String eventDate;
	private String eventDur;
	private String eventStartTime;
	
	private String description;
	
	public ProgramInfo(){
		
	}

	public ProgramInfo(String eventId, String serviceId,String eventCategory,String channelPrice,String priceModel,
		String serviceType, String subscribe, String eventsubscribe, String lock,String isevent,String eventImage,
		String eventName, String eventPrice, String like,String timeStamp, String description){
		this.eventId = eventId;
		this.serviceId = serviceId;
		this.eventCategory = eventCategory;
		this.channelPrice = channelPrice;
		this.priceModel = priceModel;
		this.serviceType = serviceType;
		this.chlSubscribe = subscribe;
		this.eventSubscribe = eventsubscribe;
		this.lock = lock;
		this.isevent = isevent;
		this.eventImage = eventImage;
		this.eventName = eventName;
		this.eventPrice = eventPrice;
		this.like = like;
		this.timeStamp = timeStamp;
		this.description = description;
	}
	
	
	public ProgramInfo(String eventId, String serviceId,String eventCategory,String channelPrice,String priceModel,
			String serviceType, String subscribe, String eventsubscribe, String lock,String isevent,String eventImage,
			String eventName, String eventPrice, String like,String timeStamp, String date, String starttime,String dur, String description){
			this.eventId = eventId;
			this.serviceId = serviceId;
			this.eventCategory = eventCategory;
			this.channelPrice = channelPrice;
			this.priceModel = priceModel;
			this.serviceType = serviceType;
			this.chlSubscribe = subscribe;
			this.eventSubscribe = eventsubscribe;
			this.lock = lock;
			this.isevent = isevent;
			this.eventImage = eventImage;
			this.eventName = eventName;
			this.eventPrice = eventPrice;
			this.like = like;
			this.timeStamp = timeStamp;
			this.timeStamp = date;
			this.timeStamp = starttime;
			this.timeStamp = dur;
			this.description = description;
		}
	

	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the eventCategory
	 */
	public String getEventCategory() {
		return eventCategory;
	}

	/**
	 * @param eventCategory the eventCategory to set
	 */
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	/**
	 * @return the channelPrice
	 */
	public String getChannelPrice() {
		return channelPrice;
	}

	/**
	 * @param channelPrice the channelPrice to set
	 */
	public void setChannelPrice(String channelPrice) {
		this.channelPrice = channelPrice;
	}

	/**
	 * @return the priceModel
	 */
	public String getPriceModel() {
		return priceModel;
	}

	/**
	 * @param priceModel the priceModel to set
	 */
	public void setPriceModel(String priceModel) {
		this.priceModel = priceModel;
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the subscribe
	 */
	public String getChlSubscribe() {
		return chlSubscribe;
	}

	/**
	 * @param subscribe the subscribe to set
	 */
	public void setChlSubscribe(String subscribe) {
		this.chlSubscribe = subscribe;
	}
	
	
	public String getEventSubscribe() {
		return eventSubscribe;
	}

	/**
	 * @param subscribe the subscribe to set
	 */
	public void setEventSubscribe(String subscribe) {
		this.eventSubscribe = subscribe;
	}

	/**
	 * @return the lock
	 */
	public String getLock() {
		return lock;
	}

	/**
	 * @param lock the lock to set
	 */
	public void setLock(String lock) {
		this.lock = lock;
	}

	/**
	 * @return the isevent
	 */
	public String getIsevent() {
		return isevent;
	}

	/**
	 * @param isevent the isevent to set
	 */
	public void setIsevent(String isevent) {
		this.isevent = isevent;
	}

	/**
	 * @return the eventImage
	 */
	public String getEventImage() {
		return eventImage;
	}

	/**
	 * @param eventImage the eventImage to set
	 */
	public void setEventImage(String eventImage) {
		this.eventImage = eventImage;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the eventPrice
	 */
	public String getEventPrice() {
		return eventPrice;
	}

	/**
	 * @param eventPrice the eventPrice to set
	 */
	public void setEventPrice(String eventPrice) {
		this.eventPrice = eventPrice;
	}

	/**
	 * @return the like
	 */
	public String getLike() {
		return like;
	}

	/**
	 * @param like the like to set
	 */
	public void setLike(String like) {
		this.like = like;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * @return the eventDate
	 */
	public String getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the eventDur
	 */
	public String getEventDur() {
		return eventDur;
	}

	/**
	 * @param eventDur the eventDur to set
	 */
	public void setEventDur(String eventDur) {
		this.eventDur = eventDur;
	}

	/**
	 * @return the eventStartTime
	 */
	public String getEventStartTime() {
		return eventStartTime;
	}

	/**
	 * @param eventStartTime the eventStartTime to set
	 */
	public void setEventStartTime(String eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String desc) {
		this.description= desc;
	}
	
}
