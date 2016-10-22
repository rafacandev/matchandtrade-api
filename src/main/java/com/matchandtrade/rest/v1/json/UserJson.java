package com.matchandtrade.rest.v1.json;

public class UserJson {

	private String email;
	private String name;
	private Integer userId;
//	private Set<TradeListJson> tradeLists = new HashSet<>();

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}
	
//	@ApiModelProperty(hidden=true)
//	@JsonInclude(JsonInclude.Include.NON_EMPTY)
//	public Set<TradeListJson> getTradeLists() {
//		return tradeLists;
//	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
//	public void setTradeLists(Set<TradeListJson> tradeLists) {
//		this.tradeLists = tradeLists;
//	}

}
