package com.sdm.login.oauth.provider;

import java.util.HashMap;
import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
      this.attributes = new HashMap<>();
      this.attributes.putAll(attributes);
      Map<String, Object> kakaoProperties = (Map<String, Object>) this.attributes.get("properties");
      Map<String, Object> kakaoAccount = (Map<String, Object>) this.attributes.get("kakao_account");
      /*
      id :
      connected_at :
      properties = {
          nickname : 데이터 받아오도록 설정 했을때만 존재
          profile_image : 데이터 받아오도록 설정 했을때만 존재
          thumbnail_image : 데이터 받아오도록 설정 했을때만 존재
        }
       kakao_account = {
        email : 데이터 받아오도록 설정 했을때만 존재
        email_verified : 데이터 받아오도록 설정 했을때만 존재
        is_email_valid :
        has_email :
        is_email_verified :
        email_needs_agreement :
       }
       */

      // merge
      this.attributes.putAll(kakaoProperties);
      this.attributes.putAll(kakaoAccount);
    }
	
    @Override
    public String getProviderId() {
        return (String) attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

	@Override
	public String getProvider() {
		return "kakao";
	}

}
