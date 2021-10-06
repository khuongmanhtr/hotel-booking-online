var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const table = $('.management__customer-request-table');
const allRowData = table.querySelectorAll('tbody tr');
const detailBtns = $$('.management__customer-request-btn');
const csrfToken = $('input[data-csrf-token]').value;

const managerRequest = {
	bookingDetails: [],
	loadAllRowData() {
		const _this = this;
		if(allRowData.length > 0) {
			allRowData.forEach(row => {
				const bookingCode = row.querySelector('td:nth-child(1)').innerText.trim();
				const roomType = row.querySelector('td:nth-child(3)').innerText.trim();
				const button = row.querySelector('td:nth-child(5) .management__customer-request-btn');
				_this.bookingDetails.push({
					bookingCode: bookingCode,
					roomType: roomType,
					btn: button,
				})
			})
		}
	},
	loadDetailBtns() {
		const _this = this;
		if (detailBtns.length > 0) {
			detailBtns.forEach(btn => {
				btn.onclick = function() {
					const result = _this.bookingDetails.find(bookingDetail => bookingDetail.btn === btn);
					_this.createFormToSubmit(result);
				}
			})
		}
	},
	createFormToSubmit(result) {
		const form = document.createElement('form');
		form.action = '/manager/request/confirmRequest'
		form.method = 'POST';
		form.style.display = "none";

		const csrfField = document.createElement('input');
		csrfField.type = 'hidden';
		csrfField.name = '_csrf';
		csrfField.value = csrfToken;
		form.appendChild(csrfField);

		const codeInput = document.createElement('input');
		codeInput.type = 'hidden';
		codeInput.name = 'bookingCode';
		codeInput.value = result.bookingCode;
		form.appendChild(codeInput);

		const roomTypeInput = document.createElement('input');
		roomTypeInput.type = 'hidden';
		roomTypeInput.name = 'roomType';
		roomTypeInput.value = result.roomType;
		form.appendChild(roomTypeInput);

		document.body.appendChild(form);
		form.submit();
	},
	uppercaseAllRoomType() {
		const _this = this;
		if(allRowData.length > 0) {
			allRowData.forEach(row => {
				const roomType = row.querySelector('td:nth-child(3)');
				const storedName = roomType.innerText.trim();
				roomType.innerText = _this.uppercaseFirstLetter(storedName);
			})
		}
	},
	uppercaseFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
	run() {
		this.loadAllRowData();
		this.loadDetailBtns();
		this.uppercaseAllRoomType();
	}
}
managerRequest.run();
