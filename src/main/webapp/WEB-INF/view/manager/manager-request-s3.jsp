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
    <title>Customer Request | Finish - Hotel Management</title>
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
                                <li class="management__checkin-step">
                                    <span class="management__checkin-step-number">2</span>
                                    <span class="management__checkin-step-title">Confirm request</span>
                                </li>
                                <li class="management__checkin-step active">
                                    <span class="management__checkin-step-number">3</span>
                                    <span class="management__checkin-step-title">Finish</span>
                                </li>
                            </ul>

                            <div class="management__dashboard-monitor management__customer-request-finish">
                                <div class="management__notification ${boxType}">
                                    <div class="management__notification-heading" data-success-type="1">
                                        <i class="management__notification-icon fas fa-check-circle"></i>
                                        <div class="management__notification-heading-desc">Congratulation! You've refunded successfully!</div>
                                        <div class="management__notification-infos">
                                            <div class="management__notification-info">
                                                <span class="management__notification-title">Customer:</span>
                                                <span class="management__notification-desc">${bookingCode.bookingCode}</span>
                                            </div>
                                            <div class="management__notification-info">
                                                <span class="management__notification-title">Bank account:</span>
                                                <span class="management__notification-desc">${bookingCode.customer.bankAccount}</span>
                                            </div>
                                            <div class="management__notification-info">
                                                <span class="management__notification-title">Total refund (VND):</span>
                                                <span class="management__notification-desc">
                                                    <fmt:formatNumber type="number" value="${totalRefund}" pattern="###,###"/>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="management__notification-heading" data-failure-type="1">
                                        <i class="management__notification-icon fas fa-times-circle"></i>
                                        <div class="management__notification-heading-desc">Sorry! Something wrong happened!</div>
                                    </div>
                                    <div class="management__notification-heading" data-decline-type="1">
                                        <i class="management__notification-icon fas fa-check-circle"></i>
                                        <div class="management__notification-heading-desc">You've declined refunding successfully</div>
                                    </div>
                                    <div class="management__notification-back">
                                        <a href="/manager/request/" class="square-btn blue-type-btn management__notification-back-btn">Go Back</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <c:import url="../staff/staff-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
</body>
</html>