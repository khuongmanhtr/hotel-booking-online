var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const roomTypeNames = $$('.cart__room-heading');
const orders = $$('.cart__order-info');
const csrfToken = $('.payment__confirmation-csrf').value;
const codeInput = $('.payment__promotion-input');
const notiElement = $('.payment__promotion-noti');
const codeDetailInfo = $('.payment__promotion-info');
const emptyElement = $('.payment__promotion-empty');
const applyBtn = $('.payment__promotion-apply-btn');
const undoBtn = $('.payment__promotion-undo-btn');
const customerNameElement = $('.payment__guest-input.name');
const customerAccountElement = $('.payment__guest-input.account');
const transferMessageElement = $('.payment__guest-input.desc')
const cusErrMessage = $('.payment__guest-error');
const confirmBtn = $('.payment__confirmation-btn');
const dateFrom = $('input[name="dateFrom"]');
const dateTo = $('input[name="dateTo"]');

const confirmBooking = {
    data: {
        dateFrom: "",
        dateTo: "",
        promoCode: "",
        promoType: "",
        customerName: "",
        customerAccount: "",
        transferMessage: "",
        roomTypes: []
    },
    loadRoomTypes: function() {
        const _this = this;
        _this.data.dateFrom = dateFrom.value;
        _this.data.dateTo = dateTo.value;
        if (orders.length > 0) {
            orders.forEach(order => {
                const roomType = {};
                const roomTypeName = order.querySelector('.cart__room-heading');
                roomType.name = roomTypeName.innerText;

                const roomTypePrice = order.querySelector('.cart__price-number');
                roomType.totalprice = roomTypePrice.dataset.totalPrice;

                const roomTypeQtt = order.querySelector('.cart__room-detail-desc[data-quantity]');
                roomType.quantity = roomTypeQtt.dataset.quantity;
                _this.data.roomTypes.push(roomType);
            })
        }
    },
    loadPayment: function() {
        const totalPriceElement = $('.payment__information-number[data-total]');
        const discountElement = $('.payment__information-number[data-discount]');
        const totalPaymentElement = $('.payment__information-number[data-payment]');

        const totalPrice = this.data.roomTypes.reduce((acc,roomType) => {
            return acc += Number(roomType.totalprice);
        },0)
        totalPriceElement.dataset.total = totalPrice;
        totalPriceElement.innerText = new Intl.NumberFormat().format(totalPrice);

        const discount = parseFloat(discountElement.dataset.discount);
        const totalPayment = totalPrice * (100 - discount) /100;
        totalPaymentElement.dataset.payment = totalPayment;
        totalPaymentElement.innerText = new Intl.NumberFormat().format(totalPayment);
    },
    loadPromoCodeBtns: function() {
        const _this = this;

        codeInput.onfocus = function() {
            this.classList.remove("invalid");
        }

        applyBtn.onclick = function() {
            _this.checkPromotionCode();
            _this.loadPayment();
        }

        undoBtn.onclick = function() {
            _this.clearPromoInfo();

            codeInput.disabled = false;
            codeInput.focus();

            applyBtn.classList.add("display-block");
            applyBtn.classList.remove("display-none");

            this.classList.add("display-none");
            this.classList.remove("display-block");

            notiElement.classList.remove("invalid");
            notiElement.classList.remove("valid");
            notiElement.innerText = "Do your have another promotion code?";

            _this.data.promoCode = "";
            _this.data.promoType = "";

            const discountElement = $('.payment__information-number[data-discount]');
            discountElement.dataset.discount = 0;
            discountElement.innerText = 0;
            _this.loadPayment();
        }
    },
    loadConfirmBtn: function() {
        const _this = this;

        customerNameElement.onfocus = () => customerNameElement.classList.remove("invalid");
        customerAccountElement.onfocus = () => customerAccountElement.classList.remove("invalid");
        transferMessageElement.onfocus = () => transferMessageElement.classList.remove("invalid");
        
        if (confirmBtn) {
            confirmBtn.onclick = function() {

                const customerName = customerNameElement.value.trim();
                const customerAccount = customerAccountElement.value.trim();
                const transferMessage = transferMessageElement.value.trim();

                if(_this.validateCustomer(customerName, customerAccount, transferMessage)) {
                    _this.data.customerAccount = customerAccount;
                    _this.data.customerName = customerName;
                    _this.data.transferMessage = transferMessage;
                    _this.sendData({
                        url:"/book/payment",
                        method: "post",
                        csrfToken: csrfToken,
                        data: _this.data
                    });
                }
            }
        }
    },
    checkPromotionCode: function() {
        const _this = this;
        const code = codeInput.value.trim().toUpperCase();

        if (code !== "") {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", '/getCode');
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader("x-csrf-token", csrfToken);
            xhr.send(code);
        
            xhr.onreadystatechange = function () {
                if(this.readyState == 4 && this.status == 200) {
                    const json = this.responseText;
                    const codeInfo = JSON.parse(json);
                    _this.renderPromotionInfo(codeInfo, code);
                    _this.updateDiscount(codeInfo);
                }
            }
        } else {
            codeInput.classList.add("invalid");
            notiElement.classList.remove("valid");
            notiElement.classList.add("invalid");
            notiElement.innerText = "Please enter your code to check !";
        }
    },
    renderPromotionInfo: function(codeInfo, code) {
        this.clearPromoInfo();

        const details = document.createElement("ul");
        details.classList.add("payment__promotion-info-list");

        const noteInfo = document.createElement("p");
        noteInfo.classList.add('payment__promotion-info-noti');

        if (codeInfo.isExisted) {
            for(var key in codeInfo.contents) {
                if (codeInfo.contents.hasOwnProperty(key)) {
                    details.innerHTML +=`
                        <li class="payment__promotion-info-item">
                            <span class="payment__promotion-info-title">${this.uppercaseFirstLetter(key)}:</span>
                            <span class="payment__promotion-info-desc">-${codeInfo.contents[key]}%</span>
                        </li>
                    `;
                }
                
            }
            codeDetailInfo.insertBefore(details, emptyElement);

            noteInfo.innerText = "Note: Promotion code will be applied after you pay for the reservation";
            codeDetailInfo.insertBefore(noteInfo, emptyElement);

            codeDetailInfo.classList.remove("display-flex");
            emptyElement.classList.add("display-none");

            notiElement.classList.remove("invalid");
            notiElement.classList.add("valid");
            notiElement.innerText = "Success! This code is valid";

            codeInput.disabled = true;
            applyBtn.classList.remove("display-block");
            applyBtn.classList.add("display-none");

            undoBtn.classList.remove("display-none");
            undoBtn.classList.add("display-block");

            this.data.promoCode = code;
            this.data.promoType = codeInfo.type;

        } else {
            codeDetailInfo.appendChild(emptyElement);
            codeDetailInfo.classList.add("display-flex");

            emptyElement.classList.remove("display-none");
            codeInput.classList.add("invalid");

            notiElement.classList.remove("valid");
            notiElement.classList.add("invalid");
            notiElement.innerText = "Failed! This code is not valid, Please try again!";
        }
    },
    clearPromoInfo: function() {
        codeDetailInfo.innerHTML = "";
        codeDetailInfo.appendChild(emptyElement);
        codeDetailInfo.classList.add("display-flex");
        emptyElement.classList.remove("display-none");
    },
    updateDiscount: function(codeInfo) {
        if (codeInfo.isExisted) {
            if (codeInfo.type == "roomCode") {
                const discount = parseFloat(...Object.values(codeInfo.contents));
                const discountElement = $('.payment__information-number[data-discount]');
                discountElement.dataset.discount = discount;
                discountElement.innerText = discount;

                this.loadPayment();
            }
        }
    },
    validateCustomer: function(name,account, desc) {
        let isValid = true;

        if (this.checkNotEmpty(name)) {
            isValid = true;
        } else {
            cusErrMessage.innerText = "Please fulfill your account information!";
            customerNameElement.classList.add("invalid");
            return !isValid;
        }

        if (this.checkNotEmpty(account)) {
            isValid = true;
        } else {
            cusErrMessage.innerText = "Please fulfill your account information!";
            customerAccountElement.classList.add("invalid");
            return !isValid;
        }

        if (this.checkNotEmpty(desc)) {
            isValid = true;
        } else {
            cusErrMessage.innerText = "Please fulfill your account information!";
            customerAccountElement.classList.add("invalid");
            return !isValid;
        }
        
        if (this.checkValidName(name)) {
            isValid = true;
        } else {
            cusErrMessage.innerText = "Your name is invalid, please try again!";
            customerNameElement.classList.add("invalid");
            return !isValid;
        }

        if (this.checkValidBankAccount(account)) {
            isValid = true;
        } else {
            cusErrMessage.innerText = "This your account is invalid, please try again!";
            customerAccountElement.classList.add("invalid");
            return !isValid;
        }

        if (isValid == true) {cusErrMessage.innerText = ""};

        return isValid;
    },
    sendData: function({ url , method, csrfToken, data }) {
        const form = document.createElement('form');
        form.method = method;
        form.action = url;

        const csrfElement = document.createElement("input");
        csrfElement.type = "hidden";
        csrfElement.name = "_csrf";
        csrfElement.value = csrfToken;
        form.appendChild(csrfElement);

        createForm(data);

        function createForm (data) {
            for (const key in data) {
                if (Array.isArray(data[key])) {
                    data[key].forEach(data => {
                        createForm(data);
                    })
                } else {
                    const input = document.createElement("input");
                    input.type = "hidden";
                    input.name = key;
                    input.value = data[key];
                    form.appendChild(input);
                }
            }
        }
        console.log(this.data);
        document.body.appendChild(form);
        form.submit();
    },
    checkNotEmpty: function(string) {
        const isNotEmpty = string !== "" ? true : false;
        return isNotEmpty;
    },
    checkValidName: function(name) {
        const regExp = /^(?!.*[@$!%*#?&])[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\s\W|_]+$/;
        const isValid = name.match(regExp) ?  true : false;
        return isValid;
    },
    checkValidBankAccount: function(account) {
        const regExp = /^[0-9]{1,}$/
        const isValid = account.match(regExp) ?  true : false;
        return isValid;
    },
    uppercaseAllRoomType: function (){
        const _this = this;
        roomTypeNames.forEach(roomType => {
            const storedName = roomType.innerText;
            roomType.innerText = _this.uppercaseFirstLetter(storedName);
        })
    },
    uppercaseFirstLetter: function(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    run: function () {
        this.loadRoomTypes();
        this.loadPayment();
        this.loadPromoCodeBtns();
        this.loadConfirmBtn();
        this.uppercaseAllRoomType();
    }
}
confirmBooking.run();