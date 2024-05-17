package com.sdm.login.oauth.provider;

import java.util.Map;

public class GithubUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;

    public GithubUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        /*
        id :
        login :
        node_id :
        avatar_url :
        gravatar_id :
        url : "https://api.github.com/users/{username}"
        html_url : "http://github.com/{username}"
        followers_url : "https://api.github.com/users/username/followers"
        following_url : "https://api.github.com/users/username/following{/other_user}"
        gists_url : "https://api.github.com/users/username/gists{/gist_id}"
        starred_url : "https://api.github.com/users/username/starred{/owner}{/repo}"
        subscriptions_url : "https://api.github.com/users/{username}/subscriptions"
        organizations_url : "https://api.github.com/users/{username}/orgs"
        repos_url : "https://api.github.com/users/{username}/repos"
        events_url : "https://api.github.com/users/{username}/events{/privacy}"
        received_events_url : "https://api.github.com/users/{username}/received_events"
        type : "User"
        site_admin : false
        name :
        company :
        blog :
        location :
        email :
        hireable :
        bio :
        twitter_username :
        public_repos : 0
        public_gists : 0
        followers : 0
        following : 0
        created_at :
        updated_at :
        private_gists : 0
        total_private_repos : 0
        owned_private_repos : 0
        disk_usage : 0
        collaborators : 0
        two_factor_authentication : false
        plan = {
            name : "free"
            space : 976562499
            collaborators : 0
            private_repos : 10000
        }
         */
    }
	
    @Override
    public String getProviderId() {
        return (String) attributes.get("id").toString();
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
		return "github";
	}
}
