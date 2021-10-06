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
    <title>Home | Room Type- Hotel Management</title>
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
                            <ul class="management__dashboard-heading-list">
                                <li class="management__dashboard-heading-item">
                                    <a href="/manager/room/allRoom" class="square-btn management__dashboard-heading-link">All Room</a>
                                </li>
                                <li class="management__dashboard-heading-item">
                                    <a class="square-btn management__dashboard-heading-link active">All Room Type</a>
                                </li>
                            </ul>
                            <div class="management__dashboard-monitor">
                                <form action="/manager/room/createRoomType/save"
                                      method="post"
                                      class="management__room-type-form"
                                      enctype="multipart/form-data"
                                >
                                    <ul class="management__room-type-details">
                                        <h3 class="management__room-type-heading">${title}</h3>
                                        <input type="hidden" name="typeId" value="${roomType.id}"/>
                                        <li class="management__room-type-detail">
                                            <label class="management__room-type-title">Room Type:</label>
                                            <input type="text" name="typeName" class="management__room-type-input" value="${roomType.name}">
                                        </li>
                                        <li class="management__room-type-detail">
                                            <label class="management__room-type-title">Number of People:</label>
                                            <input type="text" name="numberOfPeople" class="management__room-type-input" value="${roomType.numberOfPeople}">
                                        </li>
                                        <li class="management__room-type-detail">
                                            <label class="management__room-type-title">Price per night (VND):</label>
                                            <input type="text"
                                                   class="management__room-type-input"
                                                   name="typePrice"
                                                   value="<fmt:formatNumber type="number" value="${roomType.price}" pattern="###"/>">
                                        </li>
                                        <li class="management__room-type-detail">
                                            <label class="management__room-type-title">Description:</label>
                                            <textarea class="management__room-type-input" name="typeDesc" cols="30" rows="3">${roomType.description}</textarea>
                                        </li>
                                        <li class="management__room-type-detail">
                                            <label class="management__room-type-title">Image:</label>
											<div class="management__room-type-wrapper">
                                                <c:if test="${roomType != null}">
                                                    <div style="margin-bottom: 1.6rem" class="management__room-type-image">
                                                        <img src="/assets/image/room/${roomType.avatar}" class="management__room-type-img" />
                                                    </div>
                                                </c:if>
												<input type="file" name="typeImage" class="management__room-type-file">
											</div>
                                        </li>
                                        <li class="management__room-type-btns">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <div class="management__room-type-error"></div>
                                            <a href="/manager/room/allRoomType" class="pill-btn red-type-btn management__room-type-cancel-btn">cancel</a>
                                            <button class="pill-btn green-type-btn management__room-type-save-btn">save</button>
                                        </li>
                                    </ul>
                                </form>
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
    <script src="/assets/js/staff/manager-createRoomType.js"></script>
</body>
</html>

