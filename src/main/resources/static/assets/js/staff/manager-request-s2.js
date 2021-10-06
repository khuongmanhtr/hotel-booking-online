var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const bookingPaymentTable = $('.management__booking-payment-table');
const allRowPaymentData = bookingPaymentTable.querySelectorAll('tbody tr');
const cancelRequestTable = $('.management__cancel-request-table');
const allRowRequestData = cancelRequestTable.querySelectorAll('tbody tr');
const declineBtn = $('.management__payment-decline-btn');
const confirmBtn = $('.management__payment-confirm-btn');

const servicePayment = {
	form: null,
    distanceY: 0,
	loadBtn() {
		const _this = this;
		declineBtn.onclick = function(e) {
			e.preventDefault();
			_this.form = this.closest('form');
			_this.displayConfirmModal({
				type: "exclamation",
				content: "Do you really want to decline to refund this room order ?"
			})
		}

		confirmBtn.onclick = function(e) {
			e.preventDefault();
			_this.form = this.closest('form');
			_this.displayConfirmModal({
				type: "exclamation",
				content: "Do you really want to accept to refund this room order ?"
			})
		}
	},
	displayConfirmModal({content, type}) {
        this.disableWindowScroll();
        const _this = this;
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
			const _this = this;
			noBtn.onclick = function () {
				closeModal();
				_this.form = null;
			}
		}
		// Yes button
		if (yesBtn) {
			yesBtn.onclick = () => this.form.submit();
		}
	},
	uppercasePaymentTable() {
		const _this = this;
		if (allRowPaymentData.length > 0) {
			allRowPaymentData.forEach(row => {
				const roomType = row.querySelector('td:nth-child(1)');
				const status = row.querySelector('td:nth-child(2)');
				roomType.innerText = _this.uppercaseFirstLetter(roomType.innerText.trim());
				status.innerText = _this.uppercaseFirstLetter(status.innerText.trim());
			})
		}
	},
	uppercaseRequestTable() {
		const _this = this;
		if (allRowRequestData.length > 0) {
			allRowRequestData.forEach(row => {
				const roomType = row.querySelector('td:nth-child(1)');
				roomType.innerText = _this.uppercaseFirstLetter(roomType.innerText.trim());
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
		this.loadBtn();
		this.uppercasePaymentTable();
		this.uppercaseRequestTable();
	}
}
servicePayment.run();