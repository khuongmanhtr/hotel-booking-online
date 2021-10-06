<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="management__category">
    <c:choose>
        <c:when test="${navigation == 'home'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-home"></i>
                    home
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/staff/home" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-home"></i>
                    home
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'checkIn'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-caret-square-right"></i>
                    check-in
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/staff/checkIn" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-caret-square-right"></i>
                    check-in
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'checkOut'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-caret-square-left"></i>
                    check-out
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/staff/checkOut" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-caret-square-left"></i>
                    check-out
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'report'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-chart-bar"></i>
                    report
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/staff/report" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-chart-bar"></i>
                    report
                </a>
            </li>
        </c:otherwise>
    </c:choose>
</ul>

