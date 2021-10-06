var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const paymentBlock = $('.cart__total-container');
const emptyCartBlock = $('.cart__empty');
const modal = document.getElementById('modal-segment');
const roomTypeNames = $$('.cart__room-type');
const bookNowBtn = $('.cart__total-btn');
const csrfToken = $('.cart__total-btn-csrf').value;
const dateFrom = $('input[name="dateFrom"]').value;
const dateTo = $('input[name="dateTo"]').value;

const manageCart = {
    roomTypes: [],
    distanceY: 0,
    loadOrders: function() {
        const orders = $$('.cart__order-info');
        const _this = this;
        if (orders.length !== 0) {
            orders.forEach((order, index) => {
                const roomType = {};
                //Name
                const roomTypeName = order.querySelector('.cart__room-type').innerText;
                roomType.name = roomTypeName;
                //Price
                const price = order.querySelector('.cart__price-number').dataset.price;
                const priceInt = Number(price);
                roomType.price = priceInt;
                //Quantity
                const quantityInput = order.querySelector('.cart__room-quantity-number');
                let quantity = quantityInput.value;
                roomType.quantity = quantity;
                //Save data into array
                this.roomTypes.push(roomType);
                
                const maxEmpty = order.querySelector('.cart__room-quantity-empty').innerText;
                const maxEmptyInt = Number(maxEmpty);
                
                const clostBtn = order.querySelector('.cart__close');

                quantityInput.oninput = function(e) {
                    var validKey = "1234567890";
                    // Check backspace key
                    if (e.data !== null) {
                        // In the case of all input not include in array
                        if (!validKey.includes(e.data)) {
                            // this.value = validQuantityArr[index];
                            this.value = quantity;
                        } else {
                        // In the case of all input include in array and check if the value equal 0
                            if (Number(this.value) == 0) {
                                this.value = quantity;
                            } else if (Number(this.value) > maxEmptyInt) {
                                quantity = maxEmpty;
                                this.value = quantity;
                            } else {
                                quantity = this.value;
                            }
                        }
                    } else {
                        if (this.value !== "") {
                            quantity = this.value;
                        }
                    }
                    _this.roomTypes[index].quantity = quantity;
                    _this.updatePaymentValue();
                    _this.loadPayment();
                }
            
                quantityInput.onblur = function() {
                    this.value = quantity;
                }
            
                // Quantity Subtraction and Addition 
                const minusBtn = order.querySelector('.cart__room-quantity-decrease');
                const plusBtn = order.querySelector('.cart__room-quantity-increase');
            
                minusBtn.onclick = function() {
                    let quantityInt = Number(quantity);
                    if (quantityInt == 1) {
                        _this.displayConfirm({
                            order: order,
                            content: "Do you really want to delete this room ?"
                        });
                    } else {
                        quantityInput.value = --quantityInt;
                        quantity = quantityInput.value;

                        _this.roomTypes[index].quantity = quantity;
                        _this.loadPayment();
                        _this.updatePaymentValue();
                    }
                }
            
                plusBtn.onclick = function() {
                    let quantityInt = Number(quantity);
                    if (quantityInt >= maxEmptyInt) {
                        // console.log("Sr you cant exceed over maximum number");
                    } else {
                        quantityInput.value = ++quantityInt;
                        quantity = quantityInput.value;
                        _this.roomTypes[index].quantity = quantity;
                        _this.loadPayment();
                        _this.updatePaymentValue();
                    }
                }
                
                clostBtn.onclick = function() {
                    _this.displayConfirm({
                        order: order,
                        content: "Do you really want to delete this room?"
                    });
                }
            });
        }
    },
    loadPayment: function() {
        const totalPayment = $('.cart__total');
        const oldRoomList = totalPayment.querySelector('.cart__total-list')

        if (oldRoomList) {
            totalPayment.removeChild(oldRoomList);
        }

        const ulElement = document.createElement('ul');
        ulElement.classList.add('cart__total-list');
        if (this.roomTypes.length > 0) {
            this.roomTypes.forEach(roomType => {
                let totalValue = roomType.quantity * roomType.price;

                totalValue = new Intl.NumberFormat().format(totalValue);
                
                let priceFormat = new Intl.NumberFormat().format(roomType.price);

                let roomTypeName = this.uppercaseFirstLetter(roomType.name);

                ulElement.innerHTML += `
                    <li class="cart__total-list-item">
                        <div class="cart__total-room-type">${roomTypeName}:</div>
                        <div class="cart__total-room-detail">
                            <span class="cart__total-room-qtt">${roomType.quantity}</span>
                            <span class="cart__total-room-mutiply">x</span>
                            <span class="cart__total-room-price">${priceFormat}</span>
                            <span class="cart__total-room-equal">=</span>
                            <span class="cart__total-room-value">${totalValue}</span>
                        </div>
                    </li>
                `;
            })
        }

        totalPayment.insertBefore(ulElement,totalPayment.firstChild);
    },
    loadBookNowBtn: function () {
        const _this = this;
        bookNowBtn.onclick = function() {
            if (_this.roomTypes.length > 0) {
                _this.sendData({
                    url : "/book/confirm", 
                    method: "post",
                    csrfToken: csrfToken,
                    datas : {
                        dateFrom: dateFrom,
                        dateTo: dateTo,
                        roomTypes: _this.roomTypes
                    }
                });
            }
        }
    },
    sendData: function({url, method , csrfToken , datas}) {
        // The rest of this code assumes you are not using a library.
        // It can be made less verbose if you use one.
        const form = document.createElement('form');
        form.method = method;
        form.action = url;

        const csrfField = document.createElement('input');
        csrfField.type = "hidden";
        csrfField.name = "_csrf";
        csrfField.value = csrfToken;
        form.appendChild(csrfField);

        const dateFromField = document.createElement('input');
        dateFromField.type = "hidden";
        dateFromField.name = "dateFrom";
        dateFromField.value = datas.dateFrom;
        form.appendChild(dateFromField);

        const dateToField = document.createElement('input');
        dateToField.type = "hidden";
        dateToField.name = "dateTo";
        dateToField.value =  datas.dateTo;
        form.appendChild(dateToField);
    
        for (const data of datas.roomTypes) {
            for (const key in data) {
                if (data.hasOwnProperty(key)) {
                    const hiddenField = document.createElement('input');
                    hiddenField.type = 'hidden';
                    hiddenField.name = key;
                    hiddenField.value = data[key];

                    form.appendChild(hiddenField);
                }
            }
        }
        document.body.appendChild(form);
        form.submit();
    },
    updatePaymentValue: function() {
        const totalRoomElement = $('.cart__total-info-room');
        const totalValueElement = $('.cart__total-info-number');
        const totalNightElement = $('.cart__total-info-night');
        const totalPriceElement = $('.cart__total-info-price');

        let totalQuantity = 0;
        let totalPrice = 0;
        
        this.roomTypes.forEach(roomType => {
            //Total room quantity
            let quantityInt = Number(roomType.quantity);
            totalQuantity += quantityInt;
            //Total price
            totalPrice += roomType.price * quantityInt;
        });

        if (totalRoomElement) {
            totalRoomElement.innerText = totalQuantity;
        }

        if (totalValueElement) {
            totalValueElement.innerText = new Intl.NumberFormat().format(totalPrice);
        }

        if (totalPriceElement) {
            let nightInt = Number(totalNightElement.innerText);
            totalPriceElement.innerText = new Intl.NumberFormat().format(totalPrice * nightInt);
        }
    },
    displayConfirm: function ({order, content}) {
        const modalSection = document.createElement('div');
        const _this = this;
        this.disableWindowScroll();

        modalSection.classList.add('modal-section');
        let duration = 300;

        modalSection.style.animation = `
            fadeIn ${duration / 1000}s linear
        `;

        const modalBody = `
            <div class="modal-section-body">
                <div class="modal__notification modal__type--exclamation">
                    <div class="modal__notification-heading modal__type--exclamation">
                        <i class="fas fa-exclamation-circle"></i>
                    </div>
                    <h4 class="modal__notification-title">${content}</h4>
                    <div class="modal__notification-buttons">
                        <div class="pill-btn green-type-btn modal__notification-confirm">Yes</div>
                        <div class="pill-btn red-type-btn modal__notification-cancel">No</div>
                    </div>
                </div>
            </div>
        `;

        modalSection.innerHTML = modalBody;
        modal.appendChild(modalSection);

        const noBtn = $('.modal__notification-cancel');
        const yesBtn = $('.modal__notification-confirm');

        function closeModal() {
            _this.enableWindowScroll();
            modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`
            setTimeout(() => modal.removeChild(modalSection), duration)
        }
        
        // No Button action
        if (noBtn) {
            noBtn.onclick = closeModal;
        }

        // Yes Action button
        yesBtn.onclick = function() {
            closeModal();
            let roomTypeName = order.querySelector('.cart__room-type').innerText;
            roomTypeName = _this.lowercaseFirstLetter(roomTypeName);

            _this.roomTypes = _this.roomTypes.filter(roomType => roomType.name !== roomTypeName);

            const ordersElement = $('.cart__orders');
            ordersElement.removeChild(order);

            _this.checkRoomTypesIsEmpty();
            _this.loadPayment();
            _this.updatePaymentValue();
        }
    },
    checkRoomTypesIsEmpty: function() {
        if (this.roomTypes.length === 0) {
            paymentBlock.classList.remove('display-block');
            paymentBlock.classList.add('display-none');
    
            emptyCartBlock.classList.remove('display-none');
            emptyCartBlock.classList.add('display-block');
        }
    },
    uppercaseAllRoomType: function() {
        const _this = this;
        if (roomTypeNames.length > 0) {
            roomTypeNames.forEach(roomType => {
                const storedName = roomType.innerText;
                roomType.innerText = _this.uppercaseFirstLetter(storedName);
            })
        }
    },
    uppercaseFirstLetter: function(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    lowercaseFirstLetter: function(string) {
        return string.charAt(0).toLowerCase() + string.slice(1);
    },
    enableWindowScroll() {
        document.body.style.top = '';
        document.body.classList.remove('modal-open');
        window.scrollTo({
            top: this.distanceY,
            left: 0,
            behavior: 'instant'
          });
    },
    disableWindowScroll() {
        this.distanceY = window.scrollY;
        document.body.style.top = `-${window.scrollY}px`;
        document.body.classList.add('modal-open');
    },
    run: function() {
        this.loadOrders();
        this.loadPayment();
        this.loadBookNowBtn();
        this.updatePaymentValue();
        this.uppercaseAllRoomType();
    }
}
manageCart.run();
