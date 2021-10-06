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
    <link rel="stylesheet" href="/assets/css/confirm-booking.css"/>
</head>
<body>
    <!-- Header -->
    <c:import url="view-component/header.jsp"/>

    <div class="cart">
        <div class="grid">
            <div class="cart__step">
                <ul class="cart__step-list">
                    <li class="cart__step-item">
                        <!-- add class "active" to hightlight -->
                        <div class="cart__step-item-number">1</div>
                        <div class="cart__step-item-desc">Choose room</div>
                    </li>
                    <li class="cart__step-item">
                        <div class="cart__step-item-number">2</div>
                        <div class="cart__step-item-desc">Book room</div>
                    </li>
                    <li class="cart__step-item">
                        <div class="cart__step-item-number active">3</div>
                        <div class="cart__step-item-desc">Confirm your booking</div>
                    </li>
                    <li class="cart__step-item">
                        <div class="cart__step-item-number">4</div>
                        <div class="cart__step-item-desc">Payment</div>
                    </li>
                </ul>
            </div>
            <div class="cart__date-detail">
                <h3 class="cart__date-heading">Your schedule</h3>
                <ul class="cart__date-schedule">
                    <li class="cart__date-check">
                        <div class="cart__date-check-title">Check-in:</div>
                        <div class="cart__date-check-desc">${formattedDateFrom} from 14h. </div>
                        <input type="hidden" name="dateFrom" value="${dateFromCheck}"/>
                    </li>
                    <li class="cart__date-check">
                        <div class="cart__date-check-title">Check-out:</div>
                        <div class="cart__date-check-desc">${formattedDateTo} until 12h.</div>
                        <input type="hidden" name="dateTo" value="${dateToCheck}">
                    </li>
                </ul>
            </div>
            <div class="cart__accomodation">Accommodation Booking</div>
            <div class="cart__heading">
                <div class="grid-row">
                    <div class="grid-col grid-col-4">Information</div>
                    <div class="grid-col grid-col-3">Services</div>
                    <div class="grid-col grid-col-2 text-al-center">Number of People</div>
                    <div class="grid-col grid-col-3 text-al-center">Total price</div>
                </div>
            </div>
            <!-- Cart List -->
            <div class="cart__orders">
                <c:forEach items="${roomTypeMap}" var="rt">
                    <div class="cart__order-info">
                        <h3 class="cart__room-heading">${rt.key.name}</h3>
                        <div class="cart__room">
                            <div class="grid-row">
                                <div class="grid-col grid-col-4">
                                    <ul class="cart__room-wrapper">
                                        <li class="cart__room-detail-item">
                                            <span class="cart__room-detail-title">Room quantity:</span>
                                            <span class="cart__room-detail-desc" data-quantity=${rt.value}>${rt.value}</span>
                                        </li>
                                        <li class="cart__room-detail-item">
                                            <span class="cart__room-detail-title">Stay period:</span>
                                            <span class="cart__room-detail-desc">
                                                    ${night + 1} days
                                                    <c:choose>
                                                        <c:when test="${night > 1}">${night} nights</c:when>
                                                        <c:otherwise>${night} night</c:otherwise>
                                                    </c:choose>
                                            </span>
                                        </li>
                                        <li class="cart__room-detail-item">
                                            <span class="cart__room-detail-title">Unit price (per night):</span>
                                            <span class="cart__room-detail-desc">
                                                 <fmt:formatNumber type="number" value="${rt.key.price}" pattern="###,###"/>
                                                <span>₫</span>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                                <div class="grid-col grid-col-3">
                                    <ul class="cart__room-info">
                                        <li class="cart__room-service">
                                            <i class="cart__room-service-icon fas fa-utensils"></i>
                                            Breakfast included
                                        </li>
                                        <li class="cart__room-service">
                                            <i class="cart__room-service-icon fas fa-wifi"></i>
                                            Free Wifi
                                        </li>
                                        <li class="cart__room-service">
                                            <i class="cart__room-service-icon fas fa-thermometer-half"></i>
                                            Air Conditioning
                                        </li>
                                        <li class="cart__room-service">
                                            <i class="cart__room-service-icon fas fa-heart"></i>
                                            Romantic Atmosphere
                                        </li>
                                    </ul>
                                </div>
                                <div class="grid-col grid-col-2 cart__people-block">
                                    <div class="cart__people-quantity">
                                        <i class="cart__people-quantity-icon fas fa-users"></i>
                                        <p class="cart__people-quantity-number">${rt.key.numberOfPeople} Peoples / Room</p>
                                    </div>
                                </div>
                                <div class="grid-col grid-col-3 cart__price-block">
                                    <div class="cart__price">
                                        <span class="cart__price-number" data-total-price="${rt.key.price * night * rt.value}">
                                             <fmt:formatNumber type="number" value="${rt.key.price * night * rt.value}" pattern="###,###"/>
                                        </span>
                                        <span class="cart__spanrice-unit">₫</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
            <!-- Payment -->
            <div class="payment">
                <div class="grid">
                    <div class="grid-row">
                        <div class="grid-col grid-col-6">
                            <h3 class="payment__promotion-heading">Promotion code</h3>
                            <div class="payment__promotion">
                                <div class="payment__promotion-code">
                                    <label class="payment__promotion-label">Enter promotion code (Optional):</label>
                                    <input type="text" class="payment__promotion-input">
                                </div>
                                <!-- Add class "valid" or "invalid" to dislay type of noti -->
                                <p class="payment__promotion-noti">Do your have any promotion code?</p>
                                <div class="payment__promotion-group-btns">
                                    <div class="square-btn black-type-btn payment__promotion-apply-btn">Apply</div>
                                    <div class="square-btn black-type-btn payment__promotion-undo-btn display-none">Undo</div>
                                </div>
                            </div>
                        </div>
                        <div class="grid-col grid-col-6">
                            <h3 class="payment__promotion-info-heading">Your promotion code information</h3>
                            <!-- Add class "display-flex" when have no valid promotion code  -->
                            <div class="payment__promotion-info display-flex">
                                <!-- <ul class="payment__promotion-info-list">
                                    <li class="payment__promotion-info-item">
                                        <span class="payment__promotion-info-title">Laundry:</span>
                                        <span class="payment__promotion-info-desc">-5%</span>
                                    </li>
                                </ul>
                                <p class="payment__promotion-info-noti">Note: Promotion code will be applied after you pay for the reservation</p> -->
                                <p class="payment__promotion-empty">Enter your promotion code to experience our outstanding services </p>
                            </div>
                        </div>
                        <div class="grid-col grid-col-6">
                            <h3 class="payment__guest-heading">Guest details</h3>
                            <div class="payment__guest">
                                <div class="payment__guest-info">
                                    <label class="payment__guest-title">Name: (*)</label>
                                    <input type="text" class="payment__guest-input name">
                                </div>
                                <div class="payment__guest-info">
                                    <label class="payment__guest-title">Bank account: (*)</label>
                                    <input type="text" class="payment__guest-input account">
                                </div>
                                <div class="payment__guest-info">
                                    <label class="payment__guest-title">Transfer Description: (*)</label>
                                    <input type="text" class="payment__guest-input desc">
                                </div>
                                <p class="payment__guest-error"></p>
                            </div>
                        </div>
                        <div class="grid-col grid-col-6">
                            <h3 class="payment__information-heading">Payment information</h3>
                            <div class="payment__information">
                                <ul class="payment__information-field">
                                    <li class="payment__information-content">
                                        <span class="payment__information-caption">Total payment:</span>
                                        <span class="payment__information-value">
                                            <span class="payment__information-number" data-total="0">0</span>
                                            <span class="payment__information-currency">₫</span>
                                        </span>
                                    </li>
                                    <li class="payment__information-content">
                                        <span class="payment__information-caption">Discount:</span>
                                        <span class="payment__information-value">
                                            <span class="payment__information-number" data-discount="0">0</span>
                                            <span class="payment__information-currency">%</span>
                                        </span>
                                    </li>
                                    <li class="payment__information-content">
                                        <span class="payment__information-caption">After discount (VAT included):</span>
                                        <span class="payment__information-value">
                                            <span class="payment__information-number" data-payment="0">0</span>
                                            <span class="payment__information-currency">₫</span>
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="grid-col grid-col-6">
                            <h3 class="payment__information-heading">HOTEL POLICY</h3>
                            <div class="payment__information">
                                <div class="payment__policy">
                                    <ul style="padding-left: 0;">
                                        <li>- If you would like to cancel single order, please cancel 3 days before check-in.</li>
                                        <li>- After your cancel request is accepted, you will be refunded 80% of total payment of single order.</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="grid-col grid-col-6 payment__confirmation">
                            <button class="square-btn black-type-btn payment__confirmation-btn">confirm the booking</button>
                            <input class="payment__confirmation-csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/header.js"></script>
    <script src="/assets/js/confirm-booking.js"></script>
</body>
</html>

