<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri ="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <link rel="stylesheet" href="/assets/css/single-room.css"/>
</head>
<body>
    <!-- Header -->
    <c:import url="view-component/header.jsp"/>

    <div class="room-section">
        <div class="grid">
            <div class="grid-row">
                <div class="grid-col grid-col-6">
                    <div class="room__image">
                        <img src="/assets/image/room/${roomType.avatar}" class="room__image-img" alt=""></img>
                    </div>
                </div>
                <div class="grid-col grid-col-6">
                    <div class="room__information">
                        <div class="room__info-upper">
                            <h3 class="room__info-heading">${roomType.name}</h3>
                            <ul class="room__info-body">
                                <li class="room__info-body-row">
                                    <div class="room__info-body-field">Number of peoples: </div>
                                    <div class="room__info-body-desc">${roomType.numberOfPeople}</div>
                                </li>
                                <li class="room__info-body-row">
                                    <div class="room__info-body-field">Description: </div>
                                    <div class="room__info-body-desc">${roomType.description}</div>
                                </li>
                                <li class="room__info-body-row">
                                    <div class="room__info-body-field">Price: </div>
                                    <div class="room__info-body-desc">
                                        <span class="room__info-body-price">
                                            <fmt:formatNumber type="number" value="${roomType.price}" pattern="###,###"/>
                                        </span>
                                        <span class="room__info-body-currency">₫</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="room__info-lower">
                            <a href="/book/chooseRoom" class="room__info-btn">CHECK AVAILABLE ROOM</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Swiper -->
            <div class="swiper-section">
                <div class="swiper-container mySwiper">
                    <div class="swiper-wrapper">
                        <c:forEach items="${roomTypeImageList}" var="images">
                            <div class="swiper-slide">
                                <img src="/assets/image/room/${images.roomType.name}/${images.image}" alt="image" class="swiper-img">
                            </div>
                        </c:forEach>
                    </div>
                    <!-- <div class="swiper-pagination"></div> -->
                </div>
                <div class="swiper-btn-next">
                    <i class="fas fa-chevron-right"></i>
                </div>
                <div class="swiper-btn-prev">
                    <i class="fas fa-chevron-left"></i>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>

    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="/assets/js/header.js"></script>
    <script src="/assets/js/single-room.js"></script>

    <!-- Initialize Swiper -->
    <script>
        var swiper = new Swiper(".mySwiper", {
            slidesPerView: 4,
            spaceBetween: 16,
            freeMode: true,
            // pagination: {
            //     el: ".swiper-pagination",
            //     clickable: true
            // },
            navigation: {
                nextEl: ".swiper-btn-next",
                // prevEl: ".swiper-button-prev",
                prevEl: ".swiper-btn-prev",
            }
        });
    </script>
</body>
</html>
