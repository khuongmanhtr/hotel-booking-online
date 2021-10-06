var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const roomTable = $('.management__service-list-table');
const allRowRoom = roomTable.querySelectorAll('tbody tr');
const serviceTable = $('.management__service-payment-table');
const allRowService = serviceTable.querySelectorAll('tbody tr');
const totalServiceEle = $('.management__service-payment-desc[data-total-service]');
const subtotalEle = $('.management__service-payment-desc[data-subtotal]');
const totalDiscountEle = $('.management__service-payment-desc[data-total-discount]');
const totalPaymentEle = $('.management__service-payment-desc[data-total-payment]');
const confirmBtn = $('.management__service-payment-ok');
const csrfToken = $('.management__crsfToken').value;


const checkOutPayment = {
	modalSection: null,
	bookingCode: $('.management__checkin-code-desc[data-code]').innerText.trim(),
	serviceList: [],
    distanceY: 0,
	loadServiceList() {
		const _this = this;
		if (allRowService.length > 0) {
			allRowService.forEach(row => {
				const serviceName = row.querySelector('td:nth-child(1)').innerText.trim();
				const totalQuantity = row.querySelector('td:nth-child(2)').innerText.trim();
				const totalInt = parseInt(totalQuantity);
				const unitPrice = row.querySelector('td:nth-child(3)').innerText.trim();
				const unitPriceInt = parseInt(unitPrice) * 1000;
				const discount = row.querySelector('td:nth-child(5)').innerText.trim();
				const discountDouble = parseFloat(discount);
				_this.serviceList.push({
					serviceName: serviceName,
					totalQuantity: totalInt,
					unitPrice: unitPriceInt,
					discountPercent: discountDouble,
				})
			})
		}
	},
	loadPaymentInfor() {
		if (this.serviceList.length > 0) {
			let totalService = 0;
			let subtotal = 0;
			let totalDiscount = 0;
			this.serviceList.forEach(service => {
				const totalPrice = service.totalQuantity * service.unitPrice;
				const discount = totalPrice * service.discountPercent / 100;
				totalDiscount += discount
				subtotal += totalPrice;
				totalService += service.totalQuantity;
			})
			const totalPayment = subtotal - totalDiscount;

			totalServiceEle.innerText = totalService;
			subtotalEle.innerText = new Intl.NumberFormat().format(subtotal);
			
			totalDiscountEle.innerText = new Intl.NumberFormat().format(totalDiscount);
			totalPaymentEle.innerText = new Intl.NumberFormat().format(totalPayment);
		}
	},
	loadConfirmBtn() {
		const _this = this;
		if (confirmBtn) {
			confirmBtn.onclick = function() {
				_this.displayConfirmModal({
					content: 'Do you confirm all information before checkout ?',
					type: "exclamation"
				});
			}
		}
	},
	displayConfirmModal({content, type}) {
        const _this = this;
        this.disableWindowScroll();
		const modalSection = document.createElement('div');
		modalSection.classList.add('modal-section');
		let duration = 300;
		let modalBody;
		const typeModal = {
			exclamation: "modal__type--exclamation",
			success: "modal__type--done",
		}

		modalSection.style.animation = `
            fadeIn ${ duration / 1000 }s linear
        `;

		if (type == Object.keys(typeModal)[0]) {
			modalBody = `
				<div class="modal-section-body">
					<div class="modal__notification ${ typeModal[type] }">
						<div class="modal__notification-heading ${ typeModal[type] }">
							<i class="fas fa-exclamation-circle"></i>
						</div>
						<h4 class="modal__notification-title">${ content }</h4>
						<div class="modal__notification-buttons">
								<div class="pill-btn green-type-btn modal__notification-confirm">Yes</div>
							<div class="pill-btn red-type-btn modal__notification-cancel">No</div>
						</div>
					</div>
				</div>
			`;
		} else if (type == Object.keys(typeModal)[1]) {
			modalBody = `
				<div class="modal-section-body">
					<div class="modal__notification ${ typeModal[type] }">
						<div class="modal__notification-heading ${ typeModal[type] }">
							<i class="fas fa-check-circle"></i>                            
						</div>
						<h4 class="modal__notification-title">${ content }</h4>
						<div class="modal__notification-buttons">
							<div class="pill-btn green-type-btn modal__notification-okay">OK</div>
						</div>
					</div>
				</div>
			`;
		}

		this.modalSection = modalSection;
		modalSection.innerHTML = modalBody;
		modal.appendChild(modalSection);

		function closeModal() {
            _this.enableWindowScroll();
			modalSection.style.animation = `
				fadeOut ${ duration / 1000 }s linear forwards
			`
			setTimeout(() => modal.removeChild(modalSection), duration);
		}

		const noBtn = $('.modal__notification-cancel');
		const yesBtn = $('.modal__notification-confirm');
		const okayBtn = $('.modal__notification-okay');

		// No Button
		if (noBtn) {
			noBtn.onclick = function () {
				closeModal();
			}
		}
		// Yes button
		if (yesBtn) {
			yesBtn.onclick = () => this.sendData(this.bookingCode);
		}

		// Okay Button
		if (okayBtn) {
			okayBtn.onclick = function () {
				modalSection.style.animation = `
					fadeOut ${ duration / 1000 }s linear forwards
				`
				setTimeout(() => {
					modal.removeChild(modalSection);
					location.href = "/staff/checkOut";
				}, duration);
			}
		}
	},
	sendData(data) {
		const _this = this;
		const xhr = new XMLHttpRequest();
		xhr.open('POST','/staff/checkOut/updateData')
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.setRequestHeader('x-csrf-token', csrfToken);
		xhr.send(data);

		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status === 200) {
				const json = this.responseText;
				const responseData = JSON.parse(json);

				if (responseData) {
					let duration = 300;
					_this.modalSection.style.animation = `
						fadeOut ${ duration / 1000 }s linear forwards
					`
					setTimeout(() => {
						modal.removeChild(_this.modalSection);
						_this.displayConfirmModal ({
							type: 'success',
							content: 'You have been checked out this booking code successfully'
						})
					}, duration)
				}
			}
		}
	},
	uppercaseAllRoomType() {
		const _this = this;
		allRowRoom.forEach(row => {
			const roomTypeRow = row.querySelector('td:nth-child(2)');
			const storedName = roomTypeRow.innerText.trim();
			roomTypeRow.innerText = _this.uppercaseFirstLetter(storedName);
		})
	},
	uppercaseAllService() {
		const _this = this;
		if (allRowRoom.length > 0) {
			allRowService.forEach(row => {
				const serviceNameRow = row.querySelector('td:nth-child(1)');
				const storedName = serviceNameRow.innerText.trim();
				serviceNameRow.innerText = _this.uppercaseFirstLetter(storedName);
			})
		}
	},
	uppercaseFirstLetter(string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
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
	run() {
		this.loadServiceList();
		this.loadPaymentInfor();
		this.loadConfirmBtn();
		this.uppercaseAllRoomType();
		this.uppercaseAllService();
	},
}
checkOutPayment.run();