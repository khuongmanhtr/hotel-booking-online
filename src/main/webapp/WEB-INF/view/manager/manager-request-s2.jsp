<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;1,300;1,400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="icon" type="image/png" href="/assets/image/icon/hotel.png"/>
    <link rel="stylesheet" href="/assets/css/base.css"/>
    <link rel="stylesheet" href="/assets/css/staff/header_footer.css"/>
    <link rel="stylesheet" href="/assets/css/staff/staff-base.css"/>
    <title>Customer Request | Confirm Request - Hotel Management</title>
</head>
<body>
    <!-- Header -->
    <c:import url="../staff/staff-component/header.jsp"/>

    <div class="management">
        <div class="grid">
            <div class="grid-row">
                <div class="grid-col grid-col-2 ">
                    <c:import url="manager-component/navigation-box.jsp"/>
                </div>
                <div class="grid-col grid-col-10 ">
                    <div class="management__dashboard">
                        <div class="management__dashboard-upper">
                            <ul class="management__checkin-steps">
                                <li class="management__checkin-step">
                                    <span class="management__checkin-step-number active">1</span>
                                    <span class="management__checkin-step-title">Handle Request</span>
                                </li>
                                <li class="management__checkin-step active">
                                    <span class="management__checkin-step-number">2</span>
                                    <span class="management__checkin-step-title">Confirm request</span>
                                </li>
                                <li class="management__checkin-step">
                                    <span class="management__checkin-step-number">3</span>
                                    <span class="management__checkin-step-title">Finish</span>
                                </li>
                            </ul>

                            <div class="management__dashboard-monitor">
                                <div class="management__checkin-code">
                                    <h3 class="management__checkin-code-heading">Booking code information</h3>
                                    <div class="management__checkin-code-body">
                                        <div class="grid-row">
                                            <div class="grid-col grid-col-6">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Booking Code:</span>
                                                    <span class="management__checkin-code-desc">${bookingCode.bookingCode}</span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Customer:</span>
                                                    <span class="management__checkin-code-desc">${bookingCode.customer.name}</span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Booked Date:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <fmt:formatDate value='${bookingCode.bookingDate}' pattern='dd-MM-yyyy'/>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Total Room:</span>
                                                    <span class="management__checkin-code-desc">${totalRoom}</span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Promotion Code:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <c:if test="${promoCode != null}">${promoCode.code}</c:if>
                                                        <c:if test="${promoCode == null}">Not Have</c:if>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Book From:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <fmt:formatDate value='${bookingCode.dateFrom}' pattern='dd-MM-yyyy'/>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Promotion Type:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <c:if test="${promoType != null}">${promoType}</c:if>
                                                        <c:if test="${promoType == null}">Not Have</c:if>
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="grid-col grid-col-6 ">
                                                <div class="management__checkin-code-detail">
                                                    <span class="management__checkin-code-title">Book To:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <fmt:formatDate value='${bookingCode.dateTo}' pattern='dd-MM-yyyy'/>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="management__booking-payment">
                                    <h3 class="management__booking-payment-heading">Booking Payment Information</h3>
                                    <table class="table-minimal-type management__booking-payment-table">
                                        <thead>
                                            <tr>
                                                <th>room type</th>
                                                <th>status</th>
                                                <th>quantity</th>
                                                <th>unit price</th>
                                                <th>total price</th>
                                                <th>discount (%)</th>
                                                <th>after discount</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="totalPerNight" value="${0}"/>
                                            <c:forEach items="${bookingDetailList}" var="bd">
                                                <c:set var="totalPrice" value="${bd.quantity * bd.roomType.price}"/>
                                                <tr>
                                                    <td>${bd.roomType.name}</td>
                                                    <c:choose>
                                                        <c:when test="${bd.status.id == 1}">
                                                            <td style="color: var(--done-button-color);">normal</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td style="color: var(--cancel-button-color);">${bd.status.description}</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td>${bd.quantity}</td>
                                                    <td>
                                                        <fmt:formatNumber type="number" value="${bd.roomType.price}" pattern="###,###"/>
                                                    </td>
                                                    <td>
                                                        <fmt:formatNumber type="number" value="${totalPrice}" pattern="###,###"/>
                                                    </td>
                                                    <c:choose>
                                                        <c:when test="${promoType == 'Room Discount'}">
                                                            <c:set var="discount" value="${promoCode.discountPercent}"/>
                                                            <td>${promoCode.discountPercent}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="discount" value="0"/>
                                                            <td>0</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td>
                                                        <c:set var="afterDiscountBP" value="${totalPrice * (100 - discount) / 100}"/>
                                                        <c:set var="totalPerNight" value="${totalPerNight + afterDiscountBP}"/>
                                                        <fmt:formatNumber type="number" value="${afterDiscountBP}" pattern="###,###"/>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <tr class="management__booking-payment-table-total">
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td colspan="2" style="font-weight: 500;">Total Per Night:</td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${totalPerNight}" pattern="###,###"/>
                                                </td>
                                            </tr>
                                            <tr class="management__booking-payment-table-total">
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td colspan="2" style="font-weight: 500;">Night In Staying:</td>
                                                <td>
                                                    <c:set var="numberOfNight" value="${numberOfNight}"/>
                                                    <c:out value="${numberOfNight}"/>
                                                </td>
                                            </tr>
                                            <tr class="management__booking-payment-table-total">
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td colspan="2" style="font-weight: 500;">Total Advance Payment:</td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${totalPerNight * numberOfNight}" pattern="###,###"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="management__cancel-request">
                                    <h3 class="management__cancel-request-heading">requested cancel room</h3>
                                    <table class="table-minimal-type management__cancel-request-table">
                                        <thead>
                                            <tr>
                                                <th>room type</th>
                                                <th>quantity</th>
                                                <th>unit price</th>
                                                <th>total price</th>
                                                <th>discount</th>
                                                <th>after discount</th>
                                                <th>refund (80%)</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="totalPriceBD" value="${bookingDetail.quantity * bookingDetail.roomType.price}"/>
                                            <tr>
                                                <td>${bookingDetail.roomType.name}</td>
                                                <td>${bookingDetail.quantity}</td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${bookingDetail.roomType.price}" pattern="###,###"/>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${totalPriceBD}" pattern="###,###"/>
                                                </td>
                                                <c:choose>
                                                    <c:when test="${promoType == 'Room Discount'}">
                                                        <c:set var="discountBD" value="${promoCode.discountPercent}"/>
                                                        <td>${promoCode.discountPercent}</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="discountBD" value="0"/>
                                                        <td>0</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td>
                                                    <c:set var="afterDiscountBD" value="${totalPriceBD * (100 - discountBD) / 100}"/>
                                                    <fmt:formatNumber type="number" value="${afterDiscountBD}" pattern="###,###"/>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${afterDiscountBD * 0.8}" pattern="###,###"/>
                                                </td>
                                            </tr>
                                            <tr class="management__cancel-request-table-total">
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td colspan="2" style="font-weight: 500;">Total Refund Per Night:</td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${afterDiscountBD * 0.8}" pattern="###,###"/>
                                                </td>
                                            </tr>
                                            <tr class="management__cancel-request-table-total">
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td colspan="2" style="font-weight: 500;">Night In Staying:</td>
                                                <td>
                                                    <c:out value="${numberOfNight}"/>
                                                </td>
                                            </tr>
                                            <tr class="management__cancel-request-table-total">
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td colspan="2" style="font-weight: 500;">Total Refund:</td>
                                                <td>
                                                    <fmt:formatNumber type="number" value="${afterDiscountBD * 0.8 * numberOfNight}" pattern="###,###"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="management__payment-infos">
                                    <div class="grid-row">
                                        <div class="grid-col grid-col-6">
                                            <div class="management__payment-customer">
                                                <h3 class="management__payment-customer-heading">customer account</h3>
                                                <div class="management__payment-customer-details">
                                                    <div class="management__payment-customer-detail">
                                                        <span class="management__payment-customer-title">Customer:</span>
                                                        <span class="management__payment-customer-desc">${bookingCode.customer.name}</span>
                                                    </div>
                                                    <div class="management__payment-customer-detail">
                                                        <span class="management__payment-customer-title">Bank account:</span>
                                                        <span class="management__payment-customer-desc">${bookingCode.customer.bankAccount}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="grid-col grid-col-6">
                                            <div class="management__payment-info">
                                                <h3 class="management__payment-info-heading">payment information</h3>
                                                <div class="management__payment-info-details">
                                                    <div class="management__payment-info-detail">
                                                        <span class="management__payment-info-title">Advance Payment (VND):</span>
                                                        <span class="management__payment-info-number">
                                                            <fmt:formatNumber type="number" value="${totalPerNight * numberOfNight}" pattern="###,###"/>
                                                        </span>
                                                    </div>
                                                    <div class="management__payment-info-detail">
                                                        <span class="management__payment-info-title">Total refund (VND):</span>
                                                        <span class="management__payment-info-number">
                                                            <fmt:formatNumber type="number" value="${afterDiscountBD * 0.8 * numberOfNight}" pattern="###,###"/>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="grid-col grid-col-6">
                                            <div class="management__payment-hotel">
                                                <h3 class="management__payment-hotel-heading">your account</h3>
                                                <div class="management__payment-hotel-details">
                                                    <div class="management__payment-hotel-detail">
                                                        <label class="management__payment-hotel-label">Your bank account:</label>
                                                        <input type="text" readonly class="management__payment-hotel-input" value="${hotelBankAccount.bankAccount}">
                                                    </div>
<%--                                                    <span class="management__payment-hotel-err">Please enter your bank account</span>--%>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="grid-col grid-col-6 management__payment-btns">
                                            <a href="/manager/request" class="pill-btn red-type-btn management__payment-back-btn">Back</a>
                                            <form action="/manager/request/decline" method="post">
                                                <input type="hidden" name="bookingCode" value="${bookingCode.bookingCode}"/>
                                                <input type="hidden" name="roomType" value="${bookingDetail.roomType.name}"/>
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <button class="pill-btn yellow-type-btn management__payment-decline-btn">Decline</button>
                                            </form>
                                            <form action="/manager/request/accept" method="post" >
                                                <input type="hidden" name="bookingCode" value="${bookingCode.bookingCode}"/>
                                                <input type="hidden" name="roomType" value="${bookingDetail.roomType.name}"/>
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <button class="pill-btn green-type-btn management__payment-confirm-btn">Accept</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div id="modal-segment"></div>
    <c:import url="../staff/staff-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/staff/manager-request-s2.js"></script>
</body>
</html>
