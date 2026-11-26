package com.backand.tambayanguild.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service public class CurrentUserService {
 private final HttpServletRequest request;
 public CurrentUserService(HttpServletRequest request){this.request=request;}
 public Long requireUserId(){Object id=request.getSession(false)==null?null:request.getSession(false).getAttribute("userId");if(!(id instanceof Long userId))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please log in");return userId;}
 public void requireUser(Long requested){if(requested!=null&&!requested.equals(requireUserId()))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You may only act as the logged-in user");}
}
