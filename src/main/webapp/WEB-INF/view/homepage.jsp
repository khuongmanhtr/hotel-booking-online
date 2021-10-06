<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="icon" type="image/png" href="./assets/image/icon/hotel.png"/>
    <link rel="stylesheet" href="./assets/css/base.css"/>
    <link rel="stylesheet" href="./assets/css/header_footer.css"/>
    <link rel="stylesheet" href="./assets/css/homepage.css"/>
</head>
<body>
    <!-- Header -->

    <c:import url="view-component/header.jsp"/>
    <!-- Carosel -->
    <div class="slider-section">
        <div class="grid">
            <div id="carousel-homepage" class="slider__carousel carousel slide carousel-fade" data-bs-ride="carousel">
                <div class="carousel-indicators">
                    <button type="button" data-bs-target="#carousel-homepage" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                    <button type="button" data-bs-target="#carousel-homepage" data-bs-slide-to="1" aria-label="Slide 2"></button>
                    <button type="button" data-bs-target="#carousel-homepage" data-bs-slide-to="2" aria-label="Slide 3"></button>
                    <button type="button" data-bs-target="#carousel-homepage" data-bs-slide-to="3" aria-label="Slide 4"></button>
                    <button type="button" data-bs-target="#carousel-homepage" data-bs-slide-to="4" aria-label="Slide 5"></button>
                </div>
                <div class="carousel-inner">
                    <div class="carousel-item active" data-bs-interval="3000">
                        <img src="./assets/image/carosel/4.jpg" class="d-block w-100" alt="img1">
                    </div>
                    <div class="carousel-item" data-bs-interval="3000">
                        <img src="./assets/image/carosel/5.jpg" class="d-block w-100" alt="img2">
                    </div>
                    <div class="carousel-item" data-bs-interval="3000">
                        <img src="./assets/image/carosel/2.jpg" class="d-block w-100" alt="img3">
                    </div>
                    <div class="carousel-item" data-bs-interval="3000">
                        <img src="./assets/image/carosel/6.jpg" class="d-block w-100" alt="img4">
                    </div>
                    <div class="carousel-item" data-bs-interval="3000">
                        <img src="./assets/image/carosel/7.jpg" class="d-block w-100" alt="img5">
                    </div>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#carousel-homepage" data-bs-slide="prev">
                    <span class="slider__carousel-btn carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carousel-homepage" data-bs-slide="next">
                    <span class="slider__carousel-btn carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>
        </div>
    </div>
    <div class="content__checking">
        <div class="grid">
            <h2 class="content__checking-heading">CHECK AVAILABLE ROOM</h2>
            <form action="/book/chooseRoom" method="post" class="content__checking-date-form">
                <div class="content__checking-from-date">
                    <label for="check-date-from" class="content__checking-date-title">From Date</label>
                    <!-- Đưa min value ra -->
                    <input type="date"
                           id="check-date-from"
                           name="dateFrom"
                           min="<fmt:formatDate value='${today}' pattern='yyyy-MM-dd'/>"
                           class="content__checking-date-input"
                           required/>
                </div>
                <div class="content__checking-to-date">
                    <label for="check-date-to" class="content__checking-date-title">To Date</label>
                    <!-- Đưa min value ra -->
                    <input type="date"
                           id="check-date-to"
                           name="dateTo"
                           min="<fmt:formatDate value='${today}' pattern='yyyy-MM-dd'/>"
                           class="content__checking-date-input"
                           required/>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn content__checking-date-btn">Check Now</button>
            </form>
        </div>
    </div>
    <!-- Content -->
    <div class="content-section">
        <div class="grid">
            <div class="content-body">
                <div class="content__intro">
                    <div class="content__intro-desc">
                        <h2 class="content__intro-title">WELCOME TO OUR HOTEL</h2>
                        <p class="content__intro-para">
                            Known as an important venue on the Central heritage road, Da Nang city is surrounded by 3 world cultural heritages: Hue, Hoi An, My Son. This place is blessed with pure white sand beaches pristine by the endless sea route, mountains, clouds, passes, seemingly simple but also creates a poetic Da Nang with charming scenery.
                            My hotel is proud to be the stay, dedicated support for your trips. Located opposite My Khe beach, one of the most beautiful beaches in the world, only takes 5 minutes to go to the City Center, 10 minutes to reach the International Airport, my hotel quickly becomes an attractive place and is the optimal choice for all businessmen and tourists when coming to Da Nang city.
                            With our professional staff trained according to international standards, my hotel is looking forward to welcoming and hope to be the perfect choice for the vacation or business trip of every traveler.
                        </p>
                    </div>
                    <div class="content__intro-image">
                        <div class="content__intro-img"></div>
                        <div class="content__intro-image-block">
                            <div class="content__intro-image-heading">Let's Go Travelling</div>
                            <p class="content__intro-image-desc">Amazing Journey With The Best Experience</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Rooms -->
    <div class="room-section">
        <div class="grid">
            <div class="room-body">
                <div class="room__intro">
                    <h2 class="room__intro-heading">HOTEL ROOMS & QUALITY SERVICES</h2>
                    <p class="room__intro-description">
                        My hotel has many rooms and suites with modern equipment and first-class amenities. By combining true Vietnamese traditional values with bold modern design, we have created a luxurious 3-star hotel that captures the charming Vietnamese spirit.
                    </p>
                </div>
                <div class="grid-row room__details">
                    <c:forEach items="${roomTypeList}" var="roomType" begin="0" end="3">
                        <div class="grid-col grid-col-6">
                            <div class="room__details-item">
                                <img src="./assets/image/room/${roomType.avatar}" alt="" class="room__details-item-img">
                                <div class="room__details-item-block">
                                    <div class="room__details-item-upper">
                                        <h3 class="room__details-item-title">${roomType.name}</h3>
                                        <p class="room__details-item-quantity">Number of people: ${roomType.numberOfPeople}</p>
                                        <p class="room__details-item-desc">${roomType.description}</p>
                                    </div>
                                    <div class="room__details-item-lower">
                                        <a href="/room/${roomType.name}" class="btn square-btn room__details-item-btn">View Detail</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <!-- Footer -->
    <c:import url="view-component/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js" integrity="sha384-cn7l7gDp0eyniUwwAZgrzD06kc/tftFf19TOAs2zVinnD/C7E91j9yyk5//jjpt/" crossorigin="anonymous"></script>
    <script src="./assets/js/header.js"></script>
    <script src="./assets/js/homepage.js"></script>
</body>
</html>
