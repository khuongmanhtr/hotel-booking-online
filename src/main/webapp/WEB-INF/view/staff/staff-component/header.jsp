<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<header class="header">
    <div class="grid">
        <div class="navbar">
            <div class="navbar__part">
                <div class="navbar__part-logo">
                    <a href="/" class="navbar__part-logo-link">
                        <img src="/assets/image/logo/hotel.svg" alt="logo" class="navbar__part-logo-img">
                    </a>
                </div>
                <div class="navbar__part-title">HOTEL MANAGEMENT</div>
            </div>
            <div class="navbar__part">
                <div class="navbar__part-user">
                    <svg class="navbar__part-user-svg" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" width="45.532px" height="45.532px" viewBox="0 0 45.532 45.532" style="enable-background:new 0 0 45.532 45.532;" xml:space="preserve">
						<g>
							<path d="M22.766,0.001C10.194,0.001,0,10.193,0,22.766s10.193,22.765,22.766,22.765c12.574,0,22.766-10.192,22.766-22.765
								S35.34,0.001,22.766,0.001z M22.766,6.808c4.16,0,7.531,3.372,7.531,7.53c0,4.159-3.371,7.53-7.531,7.53
								c-4.158,0-7.529-3.371-7.529-7.53C15.237,10.18,18.608,6.808,22.766,6.808z M22.761,39.579c-4.149,0-7.949-1.511-10.88-4.012
								c-0.714-0.609-1.126-1.502-1.126-2.439c0-4.217,3.413-7.592,7.631-7.592h8.762c4.219,0,7.619,3.375,7.619,7.592
								c0,0.938-0.41,1.829-1.125,2.438C30.712,38.068,26.911,39.579,22.761,39.579z"/>
						</g>
					</svg>
                    <span class="navbar__part-user-name"><sec:authentication property="principal.username" /></span>
                    <ul class="navbar__user-menu">
                        <li class="navbar__user-menu-item">
                            <a href="/" class="navbar__user-hotel-link">Homepage</a>
                        </li>
                        <li class="navbar__user-menu-item">
                            <a href="/changePassword" class="navbar__user-hotel-link">Change Password</a>
                        </li>
                        <li class="navbar__user-menu-item">
                            <form action="/performLogout" method="post" class="navbar__user-hotel-form">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="navbar__user-log-out-button">
                                    Log out
                                    <i class="navbar__user-log-out-icon fas fa-sign-out-alt"></i>
                                </button>
                            </form>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</header>