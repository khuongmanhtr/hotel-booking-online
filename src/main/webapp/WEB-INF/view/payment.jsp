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
    <link rel="stylesheet" href="/assets/css/payment.css"/>
</head>
<body>
    <!-- Header -->
    <c:import url="view-component/header.jsp"/>

    <div class="booking">
        <div class="grid">
            <div class="booking__step">
                <ul class="booking__step-list">
                    <li class="booking__step-item">
                        <!-- add class "active" to hightlight -->
                        <div class="booking__step-item-number ">1</div>
                        <div class="booking__step-item-desc">Choose room</div>
                    </li>
                    <li class="booking__step-item">
                        <div class="booking__step-item-number">2</div>
                        <div class="booking__step-item-desc">Book room</div>
                    </li>
                    <li class="booking__step-item">
                        <div class="booking__step-item-number">3</div>
                        <div class="booking__step-item-desc">Confirm your booking</div>
                    </li>
                    <li class="booking__step-item">
                        <div class="booking__step-item-number active">4</div>
                        <div class="booking__step-item-desc">Payment</div>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="payment">
        <div class="grid">
            <c:choose>
                <c:when test="${status == true}">
                    <div class="payment-notification">
                        <div class="payment__noti">
                            <div class="payment__noti-top payment__type--success">
                                <i class="payment__noti-icon fas fa-check-circle"></i>
                            </div>
                            <h2 class="payment__noti-heading">You have successfully booked your room</h2>
                            <p class="payment__noti-desc">Please keep this booking code and present at counter when checking in hotel</p>
                        </div>
                        <h3 class="payment__noti-info-heading">Your information</h3>
                        <ul class="payment__noti-info">
                            <li class="payment__noti-detail">
                                <span class="payment__noti-title">Full name:</span>
                                <span class="payment__noti-name">${customerName}</span>
                            </li>
                            <li class="payment__noti-detail">
                                <span class="payment__noti-title">Booking code:</span>
                                <span class="payment__noti-booking">
                            <span class="payment__noti-code">${bookingCode}</span>
                            <span class="pill-btn black-type-btn payment__noti-copy">Copy</span>
                        </span>
                            </li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="payment-notification">
                        <div class="payment__noti">
                            <div class="payment__noti-top payment__type--failure">
                                <i class="payment__noti-icon fas fa-times-circle"></i>
                            </div>
                            <h2 class="payment__noti-heading">Sorry, something went wrong</h2>
                            <p class="payment__noti-desc">Please try again!</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="payment__homepage">
                <a href="/" class="square-btn black-type-btn payment__homepage-link">Homepage</a>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/header.js"></script>
    <script src="/assets/js/payment.js">
    </script>
</body>
</html>

