package com.webservice.rest;

public class Response {
	 private String status;
	 private Integer code;
	 private String id;
	 private String Name;
	public Response(String status, Integer code, String id, String name) {
		super();
		this.status = status;
		this.code = code;
		this.id = id;
		Name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Response() {
	
	}
	 	
}
