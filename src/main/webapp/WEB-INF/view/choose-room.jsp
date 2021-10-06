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
    <link rel="stylesheet" href="/assets/css/choose-room.css"/>
</head>
<body>
    <!-- Header -->
    <c:import url="view-component/header.jsp"/>
    <div class="booking-step">
        <div class="grid">
            <div class="booking__step">
                <ul class="booking__step-list">
                    <li class="booking__step-item">
                        <!-- add class "active" to hightlight -->
                        <div class="booking__step-item-number active">1</div>
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
                        <div class="booking__step-item-number">4</div>
                        <div class="booking__step-item-desc">Payment</div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- Date checking form -->
    <div class="date__form">
        <div class="grid">
            <form action="/book/chooseRoom" method="post" class="date__form-list">
                <div class="date__form-item">
                    <label class="date__form-item-title">FROM DATE</label>
                    <input type="date"
                           min="<fmt:formatDate value='${today}' pattern='yyyy-MM-dd'/>"
                           name="dateFrom"
                           class="date__form-item-from"
                           required
                           value="${dateFromCheck}"
                    />
                </div>
                <div class="date__form-item">
                    <label class="date__form-item-title">TO DATE</label>
                    <input type="date"
                           min="<fmt:formatDate value='${today}' pattern='yyyy-MM-dd'/>"
                           name="dateTo"
                           class="date__form-item-to"
                           required
                           value="${dateToCheck}"
                    />
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="date__form-item date__form-item-button">
                    <button class="square-btn black-type-btn date__form-btn">CHECK</button>
                </div>
            </form>
        </div>
    </div>
    <!-- Search result -->
    <c:if test="${roomTypes.size() > 0}">
        <div class="search-result">
            <form action="/book/bookRoom" method="post">
                <div class="grid">
                    <!-- Date information -->
                    <div class="result__heading">
                        <div class="result__heading-part">
                            <span class="result__heading-title">FROM DATE:</span>
                            <span class="result__heading-date">
                                <fmt:formatDate value='${dateFromCheck}' pattern='dd-MM-yyyy'/>
                                <input type="hidden" name="dateFrom" value="${dateFromCheck}"/>
                            </span>
                        </div>
                        <div class="result__heading-part">
                            <span class="result__heading-title">TO DATE:</span>
                            <span class="result__heading-date">
                                <fmt:formatDate value='${dateToCheck}' pattern='dd-MM-yyyy'/>
                                <input type="hidden" name="dateTo" value="${dateToCheck}">
                            </span>
                        </div>
                    </div>
                    <!-- Title -->
                    <div class="result__information-caption">
                        <div class="grid-row">
                            <div class="grid-col grid-col-5">Information</div>
                            <div class="grid-col grid-col-3 text-al-center">Number of people</div>
                            <div class="grid-col grid-col-3 text-al-center">Price</div>
                            <div class="grid-col grid-col-1 text-al-center"></div>
                        </div>
                    </div>
                    <!-- Room list -->
                    <div class="result__list">
                        <c:forEach items="${roomTypes}" var="roomType">
                            <div class="result__list-item">
                                <div class="grid-row">
                                    <div class="grid-col grid-col-5">
                                        <div class="result__item-info">
                                            <img src="/assets/image/room/${roomType.avatar}" alt="" class="result__item-info-img">
                                            <div class="result__item-info-detail">
                                                <h3 class="result__item-info-title">${roomType.name}</h3>
                                                <ul class="result__item-info-body">
                                                    <li class="result__item-info-service">
                                                        <i class="result__item-info-icon fas fa-utensils"></i>
                                                        Breakfast included
                                                    </li>
                                                    <li class="result__item-info-service">
                                                        <i class="result__item-info-icon fas fa-wifi"></i>
                                                        Free Wifi
                                                    </li>
                                                    <li class="result__item-info-service">
                                                        <i class="result__item-info-icon fas fa-thermometer-half"></i>
                                                        Air Conditioning
                                                    </li>
                                                    <li class="result__item-info-service">
                                                        <i class="result__item-info-icon fas fa-heart"></i>
                                                        Romantic Atmosphere
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="grid-col grid-col-3 result__item-qtt">
                                        <i class="result__item-qtt-icon fas fa-users"></i>
                                        <p class="result__item-qtt-people">${roomType.numberOfPeople} Peoples</p>
                                    </div>
                                    <div class="grid-col grid-col-3 result__item-price">
                                        <div class="result__item-price-wrapper">
                                            <span class="result__item-number">
                                                <fmt:formatNumber type="number" value="${roomType.price}" pattern="###,###"/>
                                            </span>
                                            <span>₫</span>
                                            <span class="result__item-currency">/ 1 night</span>
                                        </div>
                                    </div>
                                    <div class="grid-col grid-col-1 result__item-checkbox">
                                        <div class="squaredFour">
                                            <input class="result__item-checkbox-input" type="checkbox" value="${roomType.name}" name="roomType"/>
                                            <label></label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <!-- Add class "selected" to change color -->
                    </div>
                    <p class="search__warning"></p>
                    <div class="search__next-button">
                        <button class="square-btn black-type-btn search__next-btn">Next</button>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </c:if>
    <c:if test="${roomTypes.size() <= 0}">
        <div class="search-result">
            <div class="grid">
                <h2 class="search__result-heading">${message}</h2>
            </div>
        </div>
    </c:if>
    <c:if test="${notification != null}">
        <div class="search-result">
            <div class="grid">
                <h2 class="search__result-heading">${notification}</h2>
            </div>
        </div>
    </c:if>

    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>
    <!-- Modal -->
    <div id="modal-segment"></div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/header.js"></script>
    <script src="/assets/js/choose-room.js"></script>
</body>
</html>
