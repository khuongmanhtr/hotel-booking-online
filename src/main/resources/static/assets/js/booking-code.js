var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);


const main = document.getElementById('modal-segment');
const cancelBtns = $$('.bookingcode__action-cancel');
const bookingCode = $('.bookingcode_code-hightlight').innerText.trim();
const roomTypeNames = $$('.bookingcode__info-title');
const csrfToken = $('.bookingcode_code-csrf-token').value;
const actionBtns = $$('.bookingcode__action');

const searchBookingCode = {
    distanceY: 0,
	loadIcons: function () {
		const statusIcon = {
			"-1": "fab fa-creative-commons-nc",
			"1": "fas fa-times-circle",
			"2": "fas fa-money-bill-wave",
			"3": "fas fa-money-bill-wave",
			"4": "fab fa-creative-commons-nc",
		}
		// -1: non cancellable, 1: allow cancel, 2: processing request
		// 3: refunded, 4: refused refund
		if (actionBtns.length > 0) {
			actionBtns.forEach(action => {
				const hiddenInput = action.querySelector('input[name="status-id"]');
				const statusId = hiddenInput.value;
				const iconElement = action.querySelector('.bookingcode__action-icon');

				iconElement.setAttribute("class", `bookingcode__action-icon ${ statusIcon[statusId] }`);
			})
		}

	},
	loadCancelBtns: function () {
		const _this = this;
		cancelBtns.forEach((btn) => {
			btn.onclick = function () {
				_this.displayConfirmModal({
					order: btn.closest('.bookingcode__info'),
					type: "exclamation",
					content: "Do you really want to cancel this room order ?"
				});
			}
		});
	},
	displayConfirmModal: function ({order, type, content}) {
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

		modalSection.innerHTML = modalBody;
		main.appendChild(modalSection);

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
			yesBtn.onclick = function () {
				closeModal();
				_this.sendCancelRequest(order);
			}
		}
		function closeModal() {
            _this.enableWindowScroll();
			modalSection.style.animation = `
                fadeOut ${ duration / 1000 }s linear forwards
            `
			setTimeout(() => main.removeChild(modalSection), duration);
		}
		// Okay button
		if (okayBtn) {
			okayBtn.onclick = function () {
				closeModal();
			}
		}
	},
	cancelOrder: function (order) {
		if (order) {
			const actions = order.querySelector('.bookingcode__action');
			actions.innerHTML = `
                <div class="pill-btn bookingcode__action-non-cancellable">
                    <i class="bookingcode__action-icon fas fa-money-bill-wave"></i>
                    processing request
                </div>
                <p class="bookingcode__action-note">This order is being processed</p>
            `;

		}
	},
	sendCancelRequest: function (order) {
		const _this = this;
		if (order) {
			let roomType = order.querySelector('.bookingcode__info-title').innerText.trim();
			roomType = this.lowercaseFirstLetter(roomType);
			const data = {
				roomType: roomType,
				bookingCode: bookingCode
			}

			const xhr = new XMLHttpRequest();
			xhr.open("POST", "/cancelRoom");
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.setRequestHeader("x-csrf-token", csrfToken);
			xhr.send(JSON.stringify(data));
			xhr.onreadystatechange = function () {
				if (this.readyState == 4 && this.status == 200) {
					const json = this.responseText;
					isSuccess = JSON.parse(json);
					if (isSuccess) {
						_this.displayConfirmModal({
							type: "success",
							content: "Your request to cancel order is in consideration. Thank you!"
						})
						_this.cancelOrder(order);
					}
				}
			}
		}
	},
	uppercaseAllRoomType: function () {
		const _this = this;
		roomTypeNames.forEach(roomType => {
			const storedName = roomType.innerText;
			roomType.innerText = _this.uppercaseFirstLetter(storedName);
		})
	},
	uppercaseFirstLetter: function (string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	},
	lowercaseFirstLetter: function (string) {
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
	run: function () {
		this.loadCancelBtns();
		this.loadIcons();
		this.uppercaseAllRoomType();
	}
}
searchBookingCode.run();

