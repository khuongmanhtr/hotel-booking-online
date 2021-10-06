<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;1,300;1,400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="icon" type="image/png" href="/assets/image/icon/hotel.png"/>
    <link rel="stylesheet" href="/assets/css/base.css"/>
    <link rel="stylesheet" href="/assets/css/header_footer.css"/>
    <link rel="stylesheet" href="/assets/css/booking-code.css"/>
</head>
<body>
    <!-- Header -->
    <c:import url="view-component/header.jsp"/>
    <!-- Booking Code -->
    <div class="bookingcode__section">
        <h2 class="bookingcode_heading">YOUR BOOKING INFORMATION</h2>
        <h3 class="bookingcode_code">
            BOOKING CODE:
            <span class="bookingcode_code-hightlight">${bookingCode}</span>
        </h3>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" class="bookingcode_code-csrf-token"/>
        <div class="grid">
            <c:choose>
                <c:when test="${bookingDetails != null}">
                    <div class="bookingcode__heading">
                        <div class="bookingcode__heading-part">
                            <span class="bookingcode__heading-title">Check-in:</span>
                            <span class="bookingcode__heading-date">${formattedDateFrom} from 14h.</span>
                        </div>

                        <div class="bookingcode__heading-part">
                            <span class="bookingcode__heading-title">Check-out:</span>
                            <span class="bookingcode__heading-date">${formattedToFrom} until 12h.</span>
                        </div>
                    </div>

                    <ul class="bookingcode__title">
                        <li class="bookingcode__title-item">Information</li>
                        <li class="bookingcode__title-item">Service</li>
                        <li class="bookingcode__title-rest">Action</li>
                    </ul>

                    <div class="bookingcode-orders">
                                <c:forEach items="${bookingDetails}" var="bookingDetail">
                                    <div class="bookingcode__info">
                                        <h3 class="bookingcode__info-title">${bookingDetail.roomType.name}</h3>
                                        <div class="bookingcode__info-wrapper">
                                            <ul class="bookingcode__detail">
                                                <li class="bookingcode__detail-item">
                                                    <div class="bookingcode__detail-item-caption">Room quantity:</div>
                                                    <div class="bookingcode__detail-item-desc">
                                                        <c:choose>
                                                            <c:when test="${bookingDetail.quantity > 1}">${bookingDetail.quantity} rooms</c:when>
                                                            <c:otherwise>${bookingDetail.quantity} room</c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </li>
                                                <li class="bookingcode__detail-item">
                                                    <div class="bookingcode__detail-item-caption">Number of people:</div>
                                                    <div class="bookingcode__detail-item-desc">${bookingDetail.roomType.numberOfPeople} peoples</div>
                                                </li>
                                                <li class="bookingcode__detail-item">
                                                    <div class="bookingcode__detail-item-caption">Stay period:</div>
                                                    <div class="bookingcode__detail-item-desc">
                                                        ${night + 1} days
                                                        <c:choose>
                                                            <c:when test="${night > 1}">${night} nights</c:when>
                                                            <c:otherwise>${night} night</c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </li>
                                            </ul>
                                            <div class="bookingcode__service">
                                                <ul class="bookingcode__service-list">
                                                    <li class="bookingcode__service-list-item">
                                                        <i class="bookingcode__service-list-icon fas fa-utensils"></i>
                                                        Breakfast included
                                                    </li>
                                                    <li class="bookingcode__service-list-item">
                                                        <i class="bookingcode__service-list-icon fas fa-wifi"></i>
                                                        Free Wifi
                                                    </li>
                                                    <li class="bookingcode__service-list-item">
                                                        <i class="bookingcode__service-list-icon fas fa-thermometer-half"></i>
                                                        Air Conditioning
                                                    </li>
                                                    <li class="bookingcode__service-list-item">
                                                        <i class="bookingcode__service-list-icon fas fa-heart"></i>
                                                        Romantic Atmosphere
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="bookingcode__action">
                                                <c:choose>
                                                    <c:when test="${bookingDetail.status.id > 1}">
                                                        <!-- Processing order -->
                                                        <input type="hidden" name="status-id" value="${bookingDetail.status.id}"/>
                                                        <div class="pill-btn bookingcode__action-non-cancellable">
                                                            <i class="bookingcode__action-icon"></i>
                                                            ${bookingDetail.status.description}
                                                        </div>
                                                        <p class="bookingcode__action-note">${bookingDetail.status.message}</p>
                                                    </c:when>

                                                    <c:when test="${notAllowCancellable}">
                                                        <!-- Non-cancellable order -->
                                                        <input type="hidden" name="status-id" value="-1"/>
                                                        <div class="pill-btn bookingcode__action-non-cancellable">
                                                            <i class="bookingcode__action-icon"></i>
                                                            Non-cancellable
                                                        </div>
                                                        <p class="bookingcode__action-note">This room order is no more cancellable</p>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <!-- Cancel order -->
                                                        <input type="hidden" name="status-id" value="${bookingDetail.status.id}"/>
                                                        <div class="pill-btn black-type-btn bookingcode__action-cancel">
                                                            <i class="bookingcode__action-icon"></i>
                                                            Cancel Order
                                                        </div>
                                                        <p class="bookingcode__action-note">${bookingDetail.status.message}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                </c:when>
                <c:when test="${bookingDetails == null}">
                    <div class="bookingcode-notfound">
                        <div class="bookingcode__notfound-block">
                            <p class="bookingcode__notfound-content">this booking code is not found</p>
                            <a href="/" class="square-btn black-type-btn bookingcode__notfound-btn">Back to homepage</a>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </div>

    </div>
    <div id="modal-segment"></div>
    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/header.js"></script>
    <script src="/assets/js/booking-code.js"></script>
</body>
</html>
