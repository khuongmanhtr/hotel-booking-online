var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const allRowData = $$('.management__checkin-finish-table tbody tr');
const bookingCodeElement = $('.management__checkin-code-desc[data-code]');
const finishBtn = $('.management__room-selection-next-btn');
const csrfToken = $('.management__crsfToken').value;

const checkInCompletion = {
	modalSection: null,
	data: {
		bookingCode: bookingCodeElement.innerText.trim(),
		rooms: []
	},
    distanceY: 0,
	loadRowData() {
		const _this = this;
		allRowData.forEach(row => {
			// Add Select Element
			const quantity = parseInt(row.querySelector('td:nth-child(3)').innerText);
			const lastColumn = row.querySelector('td:nth-last-child(1)');

			const roomTypeE = row.querySelector('td:nth-child(2)')
			const storedName = roomTypeE.innerText.trim();
			roomTypeE.innerText = _this.uppercaseFirstLetter(storedName);
		
			const select = document.createElement('select');
			select.classList.add('management__checkin-finish-table-select');
			let options;
		
			for (let i = 1; i <= quantity; i++) {
				options += `<option value="${i}">${i <= 1 ? i + " people" : i + " peoples"}</option>`;
			}
		
			select.innerHTML = options;
			lastColumn.appendChild(select);

			//Add Data
			const roomName = row.querySelector('td:nth-child(1)').innerText.trim()
			_this.data.rooms.push({
				room: roomName,
				actualPeople: select.value
			})

			select.onchange = function() {
				const resultRoom = _this.data.rooms.find(room => room.room == roomName);
				if (resultRoom) {
					resultRoom.actualPeople = this.value;
				}
			}
		})
	},
	loadFinishBtn() {
		finishBtn.onclick = () => 
			this.displayConfirmModal({
				content: "Do you confirm all information to check in?",
				type: "exclamation"
			});
	},
	displayConfirmModal({content, type}) {
        const _this = this;
		const modalSection = document.createElement('div');
		modalSection.classList.add('modal-section');
		let duration = 300;
		let modalBody;
        this.disableWindowScroll();
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
			yesBtn.onclick = () => this.sendData(this.data);
		}

		// Okay Button
		if (okayBtn) {
			okayBtn.onclick = function () {
				modalSection.style.animation = `
					fadeOut ${ duration / 1000 }s linear forwards
				`
				setTimeout(() => {
					modal.removeChild(modalSection);
					location.href = "/staff/checkIn";
				}, duration);
			}
		}
	},
	sendData(data) {
		const _this = this;
		const xhr = new XMLHttpRequest();
		xhr.open('POST','/staff/checkIn/updateData')
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.setRequestHeader('x-csrf-token', csrfToken);
		xhr.send(JSON.stringify(data));

		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status === 200) {
				const json = this.responseText;
				const responseData = JSON.parse(json)
				if (responseData) {
					let duration = 300;
					_this.modalSection.style.animation = `
						fadeOut ${ duration / 1000 }s linear forwards
					`
					setTimeout(() => {
						modal.removeChild(_this.modalSection);
						_this.displayConfirmModal ({
							type: 'success',
							content: 'You have been checked in this booking code successfully'
						})
					}, duration)
				}
			}
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
		this.loadRowData();
		this.loadFinishBtn();
	}
}
checkInCompletion.run();

