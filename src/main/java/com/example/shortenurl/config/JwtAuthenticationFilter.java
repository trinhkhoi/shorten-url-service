package com.example.shortenurl.config;

import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.common.util.JwtUtil;
import com.example.shortenurl.service.CustomerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter request before go to controller.
 * Date: 2021-04-18 11:07
 * @author khoitd
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private CustomerService customerService;

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			if (!"OPTIONS".equalsIgnoreCase(request.getMethod()) && !request.getRequestURI().startsWith("/public/")) {
				String jwt = getJwtFromRequest(request);
				if (JwtUtil.validateTokenLogin(jwt)) {
					Long idCustomer = JwtUtil.getIdCustomerFromToken(jwt);

					UserDetails userDetails = customerService.findById(idCustomer);
					if (userDetails != null) {
						UsernamePasswordAuthenticationToken
								authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
								userDetails
										.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} else {
					throw new BusinessException(HttpStatus.FORBIDDEN, "The session is invalid");
				}
			}
		} catch (Exception ex) {
			log.error("failed on set user authentication", ex);
			if (ex instanceof BusinessException) {
				response.setStatus(((BusinessException) ex).getStatus().value());
				response.getWriter().write(ex.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		// Check header Authorization contain jwt or not
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
