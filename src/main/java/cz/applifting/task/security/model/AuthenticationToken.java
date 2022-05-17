package cz.applifting.task.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {

    private final String userDetails;


    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, String details) {
        super(authorities);
        this.userDetails = details;
        super.setAuthenticated(true);
        super.setDetails(userDetails);
    }


    @Override
    public String getCredentials() {
        return userDetails;
    }

    @Override
    public String getPrincipal() {
        return userDetails;
    }
}
