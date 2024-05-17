package com.sdm.login.oauth.provider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;
	
    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        /*
        sub : "109494303050835000000"
        name : "홍길동"
        given_name : "길동"
        family_name : "홍"
        picture : "https://lh3.googleusercontent.com/a-/AOh14Gj8J9
        email :
        email_verified : true
        locale : "ko"
         */
    }
	
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

	@Override
	public String getProvider() {
		return "google";
	}

}
