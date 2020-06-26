package com.meritamerica.main.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetail implements UserDetails {
	private String username;
	private String password;
	private List<String> roles;
	private List<GrantedAuthority> authorities;
	
	public MyUserDetail(Users user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.roles = user.getRoleList();
		this.authorities = user.getAuthorityList();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}