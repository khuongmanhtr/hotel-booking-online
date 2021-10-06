<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Home | All Account - Hotel Management</title>
</head>
<body>
    <!-- Header -->
    <c:import url="../staff/staff-component/header.jsp"/>

    <div class="management">
        <div class="grid">
            <div class="grid-row">
                <div class="grid-col grid-col-2 ">
                    <ul class="management__category">
                        <li class="management__category-module">
                            <a href="#" class="management__category-module-link active">
                                <i class="management__category-module-icon fas fa-home"></i>
                                home
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="grid-col grid-col-10 ">
                    <div class="management__dashboard">
                        <div class="management__dashboard-upper">
                            <ul class="management__dashboard-heading-list">
                                <li class="management__dashboard-heading-item">
                                    <a class="square-btn management__dashboard-heading-link active">All Account</a>
                                </li>
                            </ul>
                            <div class="management__dashboard-monitor">
								<input type="hidden" data-csrf-token="1" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <form action="/admin" method="get" class="management__dashboard-search">
                                    <div class="management__dashboard-search-form">
                                        <label class="management__dashboard-search-title">Filter By:</label>
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
                                        <input type="hidden" class="management__dashboard-search-hidden" value="${searchValue}">
                                    </div>
                                    <div class="management__dashboard-search-form">
                                        <button class="pill-btn blue-type-btn management__dashboard-search-btn">Search</button>
                                    </div>
                                </form>
                                <div class="pill-btn blue-type-btn management__room-type-new">
                                    <i class="fas fa-plus"></i>
                                    Add New Account
                                </div>
                                <div class="table-background-layer">
                                    <table class="table-foreground-layer management__all-account-table">
                                        <thead>
											<tr>
												<th>username</th>
												<th>password</th>
												<th>enabled</th>
												<th>action</th>
											</tr>
                                        </thead>
                                        <tbody>
                                            <c:if test="${userList.size() > 0}">
                                                <c:forEach items="${userList}" var="user">
                                                    <tr>
                                                        <td>${user.username}</td>
                                                        <td>${user.password}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${user.enabled == true}">
                                                                    <span class="management__account-status--active">active</span>
                                                                </c:when>
                                                                <c:when test="${user.enabled == false}">
                                                                    <span class="management__account-status--inactive">inactive</span>
                                                                </c:when>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <div class="management__room-type-table-btns">
                                                                <span class="pill-btn blue-type-btn management__room-type-edit-btn">Edit</span>
                                                                <span class="pill-btn red-type-btn management__room-type-delete-btn">Delete</span>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                    <c:if test="${userList.size() == 0 || userList == null}">
                                        <div class="table-background-layer-empty">No results were found</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="management__dashboard-pagination">
                            <div class="pagination-section">
                                <c:if test="${userList.size() > 0}">
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
                                                            <a href="/admin?page=${pageIndex - 1}&searchValue=${searchValue}&filter=${filter}" class="pagination__prev-link">
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
                                                                <a href="/admin?page=${pagination}&searchValue=${searchValue}&filter=${filter}" class="pagination__page-link">${pagination}</a>
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
                                                            <a href="/admin?page=${pageIndex + 1}&searchValue=${searchValue}&filter=${filter}" class="pagination__next-link">
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
    <div id="modal-segment">
	</div>
    <c:import url="../staff/staff-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/admin/admin-homepage.js"></script>
</body>
</html>
