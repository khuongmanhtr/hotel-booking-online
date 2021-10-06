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
    <title>Home | Active Room - Hotel Management</title>
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
                            <ul class="management__dashboard-heading-list">
                                <li class="management__dashboard-heading-item">
                                    <a class="square-btn management__dashboard-heading-link active">All Active Room</a>
                                </li>
                                <li class="management__dashboard-heading-item">
                                    <a href="/staff/home/bookingCode" class="square-btn management__dashboard-heading-link">All Booking Code</a>
                                </li>
                                <li class="management__dashboard-heading-item">
                                    <a href="/staff/home/service" class="square-btn management__dashboard-heading-link">Services</a>
                                </li>
                            </ul>
                            <div class="management__dashboard-monitor">
                                <form action="/staff/home/active" method="get" class="management__dashboard-search">
                                    <div class="management__dashboard-search-form">
                                        <label class="management__dashboard-search-title">Filter by:</label>
                                        <select name="filterBy" class="management__dashboard-search-option">
                                            <c:forEach items="${options}" var="option">
                                                <c:choose>
                                                    <c:when test="${option.key == filter}">
                                                        <option value="${option.key}" selected>${option.value}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${option.key}">${option.value}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="management__dashboard-search-form input-row">
                                        <label class="management__dashboard-search-title">Search value:</label>
                                        <input type="text" name="searchValue" class="management__dashboard-search-input" value="${searchValue}">
                                    </div>
                                    <div class="management__dashboard-search-form">
                                        <button class="pill-btn blue-type-btn management__dashboard-search-btn">Search</button>
                                    </div>
                                </form>
                                <div class="table-background-layer">
                                    <table class="table-foreground-layer management__dashboard-table">
                                        <thead>
                                        <tr>
                                            <th>room</th>
                                            <th>people in staying</th>
                                            <th>booking code</th>
                                            <th>book from</th>
                                            <th>book to</th>
                                            <th>check-in date</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test="${checkInDetailList.size() > 0}">
                                                <c:forEach items="${checkInDetailList}" var="checkInDetail">
                                                    <tr>
                                                        <td>${checkInDetail.room.name}</td>
                                                        <td>${checkInDetail.actualPeople}</td>
                                                        <td>${checkInDetail.bookingCode.bookingCode}</td>
                                                        <td>
                                                            <fmt:formatDate value='${checkInDetail.bookingCode.dateFrom}' pattern='dd-MM-yyyy'/>
                                                        </td>
                                                        <td>
                                                            <fmt:formatDate value='${checkInDetail.bookingCode.dateTo}' pattern='dd-MM-yyyy'/>
                                                        </td>
                                                        <td>
                                                            <fmt:formatDate value='${checkInDetail.bookingCode.checkInDate}' pattern='dd-MM-yyyy'/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                    <c:if test="${checkInDetailList.size() == 0 || checkInDetailList == null}">
    									<div class="table-background-layer-empty">No results were found</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="management__dashboard-pagination">
                            <div class="pagination-section">
                                <c:if test="${checkInDetailList.size() > 0}">
                                    <div class="pagination__container">
                                        <c:choose>
                                            <c:when test="${paginationList.size() > 0}">
                                                <div class="pagination__prev">
                                                    <!-- add class "display-none to hide" -->
                                                    <c:choose>
                                                        <c:when test="${minPage == pageIndex}">
                                                            <a class="pagination__prev-link display-none">
                                                                <i class="fas fa-chevron-left"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="/staff/home/active?page=${pageIndex - 1}&searchValue=${searchValue}&filter=${filter}" class="pagination__prev-link">
                                                                <i class="fas fa-chevron-left"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <ul class="pagination__page">
                                                    <c:forEach items="${paginationList}" var="pagination" varStatus="status">
                                                        <li class="pagination__page-number">
                                                            <!-- add class "active" to show current active -->
                                                            <c:if test="${indexOfPageIndex == status.index}">
                                                                <a class="pagination__page-link active">${pagination}</a>
                                                            </c:if>
                                                            <c:if test="${indexOfPageIndex != status.index}">
                                                                <a href="/staff/home/active?page=${pagination}&searchValue=${searchValue}&filter=${filter}" class="pagination__page-link">${pagination}</a>
                                                            </c:if>
                                                        </li>
                                                    </c:forEach>
                                                </ul>

                                                <div class="pagination__next">
                                                    <c:choose>
                                                        <c:when test="${maxPage == pageIndex}">
                                                            <!-- add class "display-none to hide" -->
                                                            <a class="pagination__next-link display-none">
                                                                <i class="fas fa-chevron-right"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="/staff/home/active?page=${pageIndex + 1}&searchValue=${searchValue}&filter=${filter}" class="pagination__next-link">
                                                                <i class="fas fa-chevron-right"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <c:import url="staff-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
	<script src="/assets/js/staff/receptionist-home.js"></script>
</body>
</html>