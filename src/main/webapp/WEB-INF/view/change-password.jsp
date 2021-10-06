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
    <title>Home | Change Password - Hotel Management</title>
</head>
<body>
    <!-- Header -->
    <c:import url="./staff/staff-component/header.jsp"/>

    <div class="management">
        <div class="grid" style="display:flex;">
            <div class="management__change-password">
                <h3 class="management__change-password-heading">change password</h3>
                <div class="management__change-password-forms">
                    <input type="hidden" data-csrf-token="1" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="management__change-password-form">
                        <span class="management__change-password-title">username:</span>
                        <input type="text" name="username" class="management__change-password-input" data-username="1" value="${principal}" readonly>
                    </div>
                    <div class="management__change-password-form">
                        <span class="management__change-password-title">new password:</span>
                        <input type="password" name="password" class="management__change-password-input" data-password-type="password">
                    </div>
                    <div class="management__change-password-form">
                        <span class="management__change-password-title">retype new password:</span>
                        <input type="password" name="retype-password" class="management__change-password-input" data-password-type="retype">
                    </div>
                    <div class="management__change-password-wrapper">
                        <p class="management__change-password-error"></p>
                        <div class="management__change-password-btns">
                            <a href="/" class="pill-btn red-type-btn management__change-password-back-btn">back</a>
                            <button class="pill-btn green-type-btn management__change-password-confirm-btn">confirm</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="modal-segment"></div>

    <c:import url="./staff/staff-component/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/admin/account-changePassword.js"></script>
</body>
</html>
