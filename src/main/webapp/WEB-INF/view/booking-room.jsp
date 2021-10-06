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
    <link rel="stylesheet" href="/assets/css/booking-room.css"/>
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
                        <div class="cart__step-item-number ">1</div>
                        <div class="cart__step-item-desc">Choose room</div>
                    </li>
                    <li class="cart__step-item">
                        <div class="cart__step-item-number active">2</div>
                        <div class="cart__step-item-desc">Book room</div>
                    </li>
                    <li class="cart__step-item">
                        <div class="cart__step-item-number">3</div>
                        <div class="cart__step-item-desc">Confirm your booking</div>
                    </li>
                    <li class="cart__step-item">
                        <div class="cart__step-item-number">4</div>
                        <div class="cart__step-item-desc">Payment</div>
                    </li>
                </ul>
            </div>
            <div class="cart__date">
                <div class="cart__date-part">
                    <span class="cart__date-title">FROM DATE:</span>
                    <span class="cart__date-date">
                        <fmt:formatDate value='${dateFromCheck}' pattern='dd-MM-yyyy'/>
                        <input type="hidden" name="dateFrom" value="${dateFromCheck}"/>
                    </span>
                </div>
                <div class="cart__date-part">
                    <span class="cart__date-title">TO DATE:</span>
                    <span class="cart__date-date">
                        <fmt:formatDate value='${dateToCheck}' pattern='dd-MM-yyyy'/>
                        <input type="hidden" name="dateTo" value="${dateToCheck}">
                    </span>
                </div>
            </div>
            <div class="cart__heading">
                <div class="grid-row">
                    <div class="grid-col grid-col-4">Information</div>
                    <div class="grid-col grid-col-2 text-al-center">Number of people</div>
                    <div class="grid-col grid-col-3 text-al-center">Price</div>
                    <div class="grid-col grid-col-3 text-al-center">Room Quantity</div>
                </div>
            </div>
            <!-- Cart List -->
            <div class="cart__orders">
                <c:forEach items="${roomTypeMap}" var="roomTypeMap">
                    <div class="cart__order-info">
                        <div class="cart__room">
                            <div class="grid-row">
                                <div class="grid-col grid-col-4">
                                    <div class="cart__room-wrapper">
                                        <img src="/assets/image/room/${roomTypeMap.key.avatar}" alt="" class="cart__room-img">
                                        <div class="cart__room-desc">
                                            <div class="cart__room-type">${roomTypeMap.key.name}</div>
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
                                    </div>
                                </div>
                                <div class="grid-col grid-col-2 cart__people-block">
                                    <div class="cart__people-quantity">
                                        <i class="cart__people-quantity-icon fas fa-users"></i>
                                        <p class="cart__people-quantity-number">${roomTypeMap.key.numberOfPeople} Peoples</p>
                                    </div>
                                </div>
                                <div class="grid-col grid-col-3 cart__price-block">
                                    <div class="cart__price">
                                        <span class="cart__price-number" 
                                            data-price="<fmt:formatNumber type="number" value="${roomTypeMap.key.price}" pattern="###"/>"
                                        >
                                            <fmt:formatNumber type="number" value="${roomTypeMap.key.price}" pattern="###,###"/>
                                        </span> ₫
                                        <p class="cart__price-unit">/ night</p>
                                    </div>
                                </div>
                                <div class="grid-col grid-col-3 cart__room-quantity-block">
                                    <div class="cart__room-quantity">
                                        <div class="cart__room-quantity-wrapper">
                                            <div class="pill-btn black-type-btn cart__room-quantity-decrease">
                                                <i class="fas fa-minus"></i>
                                            </div>
                                            <input type="text" maxlength="3" class="cart__room-quantity-number" value="1">
                                            <div class="pill-btn black-type-btn cart__room-quantity-increase">
                                                <i class="fas fa-plus"></i>
                                            </div>
                                        </div>
                                        <div class="cart__room-quantity-rest">
                                            Available:
                                            <span class="cart__room-quantity-empty">${roomTypeMap.value}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="square-btn red-type-btn cart__close">
                                    <i class="cart__close-icon fas fa-times"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
            <!-- Add class "display-none" to hide-->
            <div class="cart__total-container">
                <div class="cart__total">
                    <!-- <ul class="cart__total-list">
                        <li class="cart__total-list-item">
                            <div class="cart__total-room-type">Room Type A:</div>
                            <div class="cart__total-room-detail">
                                <span class="cart__total-room-qtt">2</span>
                                <span class="cart__total-room-mutiply">x</span>
                                <span class="cart__total-room-price">720.000</span>
                                <span class="cart__total-room-equal">=</span>
                                <span class="cart__total-room-value">1.440.000</span>
                            </div>
                        </li>
                    </ul> -->
                    <div class="cart__total-wrapper">
                        <div class="cart__total-info">
                            <div class="cart__total-info-detail">
                                <span class="cart__total-info-title">Total room: </span>
                                <div class="cart__total-info-value">
                                    <span class="cart__total-info-room">0</span>
                                </div>
                            </div>
                            <div class="cart__total-info-detail">
                                <span class="cart__total-info-title">Total price per night:</span>
                                <div class="cart__total-info-value">
                                    <span class="cart__total-info-currency">₫</span>
                                    <span class="cart__total-info-number">0</span>
                                </div>
                            </div>
                            <div class="cart__total-info-detail">
                                <span class="cart__total-info-title">Night:</span>
                                <div class="cart__total-info-value">
                                    <span class="cart__total-info-night">${night}</span>
                                </div>
                            </div>
                            <div class="cart__total-info-detail">
                                <span class="cart__total-info-title">Total payment:</span>
                                <div class="cart__total-info-value">
                                    <span class="cart__total-info-currency">₫</span>
                                    <span class="cart__total-info-price">0</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="cart__total-book">
                    <div class="square-btn black-type-btn cart__total-btn">Book Now</div>
                    <input class="cart__total-btn-csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
<%--                    <div class="cart__error">Đơn hàng của hàng của bạn không hợp lệ xin vui lòng kiểm tra</div>--%>
                </div>
            </div>
            <div class="cart__empty display-none">
                <img src="/assets/image/icon/empty-cart.svg" class="cart__empty-img" alt="empty-cart">
                <h3 class="cart__empty-content">You don't have any booking</h3>
                <a href="/" class="square-btn black-type-btn cart__empty-btn">Back to homepage</a>
            </div>
        </div>
    </div>

    <div id="modal-segment"></div>
    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/header.js"></script>
    <script src="/assets/js/booking-room.js"></script>
    
</body>
</html>

