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
    <title>Check In | Arrange Room - Hotel Management</title>
</head>
<body>
    <!-- Header -->
    <c:import url="staff-component/header.jsp"/>

    <div class="management">
        <div class="grid">
            <div class="grid-row">
                <div class="grid-col grid-col-2 ">
                    <c:import url="staff-component/navigation-box.jsp"/>
                </div>
                <div class="grid-col grid-col-10 ">
                    <div class="management__dashboard">
                        <div class="management__dashboard-upper">
                            <ul class="management__checkin-steps">
                                <li class="management__checkin-step">
                                    <span class="management__checkin-step-number">1</span>
                                    <span class="management__checkin-step-title">Choose booking code</span>
                                </li>
                                <li class="management__checkin-step active">
                                    <span class="management__checkin-step-number">2</span>
                                    <span class="management__checkin-step-title">Arrange Room</span>
                                </li>
                                <li class="management__checkin-step">
                                    <span class="management__checkin-step-number">3</span>
                                    <span class="management__checkin-step-title">Finish Check-in</span>
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
                                                    <span class="management__checkin-code-desc" data-code="1">${bookingCode.bookingCode}</span>
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
                                                    <span class="management__checkin-code-title">Book From:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <fmt:formatDate value='${bookingCode.dateFrom}' pattern='dd-MM-yyyy'/>
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
                                                    <span class="management__checkin-code-title">Book To:</span>
                                                    <span class="management__checkin-code-desc">
                                                        <fmt:formatDate value='${bookingCode.dateTo}' pattern='dd-MM-yyyy'/>
                                                    </span>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                                <div class="management__checkin-room">
                                    <h3 class="management__checkin-room-heading">Room List</h3>
                                    <table class="table-minimal-type management__checkin-room-table">
                                        <thead>
                                        <tr>
                                            <th>Room Type</th>
                                            <th>Number of people</th>
                                            <th>Room quantity</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${bookingDetailList}" var="bd">
                                                <tr>
                                                    <td>${bd.roomType.name}</td>
                                                    <td>${bd.roomType.numberOfPeople}</td>
                                                    <td>${bd.quantity}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="management__room-selection">
                                    <h3 class="management__room-selection-heading">Arrange Room</h3>
                                    <div class="management__room-selection-frame">
                                        <div class="management__room-annotations">
                                            <div class="management__room-annotations-status">
                                                <div class="grid-row">
                                                    <div class="grid-col grid-col-6">
                                                        <div class="management__room-annotations-item">
                                                            <span class="management__room-annotations-color available"></span>
                                                            <span class="management__room-annotations-desc">Available</span>
                                                        </div>
                                                    </div>
                                                    <div class="grid-col grid-col-6">
                                                        <div class="management__room-annotations-item">
                                                            <span class="management__room-annotations-color being-used"></span>
                                                            <span class="management__room-annotations-desc">Being Used</span>
                                                        </div>
                                                    </div>
                                                    <div class="grid-col grid-col-6">
                                                        <div class="management__room-annotations-item">
                                                            <span class="management__room-annotations-color not-available "></span>
                                                            <span class="management__room-annotations-desc">Not Available</span>
                                                        </div>
                                                    </div>
                                                    <div class="grid-col grid-col-6">
                                                        <div class="management__room-annotations-item">
                                                            <span class="management__room-annotations-color different-type"></span>
                                                            <span class="management__room-annotations-desc">Different Room Type</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="management__room-remain">
                                            </div>
                                        </div>

                                        <div class="management__room-all">
                                            <div class="grid-row">
												<input type="hidden" class="management__crsfToken" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <c:forEach items="${allRoom}" var="room">
                                                    <c:choose>
                                                        <c:when test="${room.currentStatus == 'same type'}">
                                                            <div class="grid-col grid-col-1-2 ">
                                                                <div class="management__room-all-detail management__room-all--available">
                                                                    <div class="management__room-all-name">${room.roomName}</div>
                                                                    <div class="management__room-all-type">${room.typeName}</div>
                                                                    <input type="checkbox" class="management__room-all-checkbox" value="${room.roomName}">
                                                                </div>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${room.currentStatus == 'being used'}">
                                                            <div class="grid-col grid-col-1-2 ">
                                                                <div class="management__room-all-detail management__room-all--being-used">
                                                                    <div class="management__room-all-name">${room.roomName}</div>
                                                                    <div class="management__room-all-type">${room.typeName}</div>
                                                                </div>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${room.currentStatus == 'not available'}">
                                                            <div class="grid-col grid-col-1-2 ">
                                                                <div class="management__room-all-detail management__room-all--not-available">
                                                                    <div class="management__room-all-name">${room.roomName}</div>
                                                                    <div class="management__room-all-type">${room.typeName}</div>
                                                                </div>
                                                            </div>
                                                        </c:when>
                                                        <c:when test="${room.currentStatus == 'diff type'}">
                                                            <div class="grid-col grid-col-1-2 ">
                                                                <div class="management__room-all-detail management__room-all--different-type">
                                                                    <div class="management__room-all-name">${room.roomName}</div>
                                                                    <div class="management__room-all-type">${room.typeName}</div>
                                                                </div>
                                                            </div>
                                                        </c:when>
                                                    </c:choose>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="management__room-error-message"></div>
                                <div class="management__room-selection-action">
                                    <a href="/staff/checkIn/" class="pill-btn red-type-btn management__room-selection-prev-btn">back</a>
                                    <div class="pill-btn green-type-btn management__room-selection-next-btn">next</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="modal-segment"></div>
    <c:import url="staff-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/staff/receptionist-checkin-s2.js"></script>
    </body>
</html>

