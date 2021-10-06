<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="management__category">
    <c:choose>
        <c:when test="${navigation == 'home'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-home"></i>
                    <span class="management__category-module-title">home</span>
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/manager/home" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-home"></i>
                    <span class="management__category-module-title">home</span>
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'request'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-bell"></i>
                    <span class="management__category-module-title">customer-request</span>
                    <c:if test="${requestQuantity > 0}">
                        <span class="management__category-module-number">${requestQuantity}</span>
                    </c:if>
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/manager/request" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-bell"></i>
                    <span class="management__category-module-title">customer-request</span>
                    <c:if test="${requestQuantity > 0}">
                        <span class="management__category-module-number">${requestQuantity}</span>
                    </c:if>
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'room'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-hotel"></i>
                    <span class="management__category-module-title">room</span>
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/manager/room" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-hotel"></i>
                    <span class="management__category-module-title">room</span>
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'service'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-concierge-bell"></i>
                    <span class="management__category-module-title">service</span>
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/manager/service" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-concierge-bell"></i>
                    <span class="management__category-module-title">service</span>
                </a>
            </li>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${navigation == 'promotionCode'}">
            <li class="management__category-module">
                <a class="management__category-module-link active">
                    <i class="management__category-module-icon fas fa-ticket-alt"></i>
                    <span class="management__category-module-title">promotion-code</span>
                </a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="management__category-module">
                <a href="/manager/promoCode" class="management__category-module-link">
                    <i class="management__category-module-icon fas fa-ticket-alt"></i>
                    <span class="management__category-module-title">promotion-code</span>
                </a>
            </li>
        </c:otherwise>
    </c:choose>
</ul>
