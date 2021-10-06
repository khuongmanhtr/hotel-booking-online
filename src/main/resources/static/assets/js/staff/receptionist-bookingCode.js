var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const viewDetailBtns = $$('.management__bookingcode-table-view-detail');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const hiddenElement = $('.management__dashboard-search-hidden');
const csrfToken = $('input[data-csrf-token]').value;

const bookingCode = {
	loadViewBtns: function () {
        const _this = this;
        viewDetailBtns.forEach(btn => {
            btn.onclick = function() {
				const rowData = btn.closest('tr');
				const bookingCode = rowData.querySelector('td:nth-child(1)').innerHTML.trim();
				_this.getCodeInformation(bookingCode);
            }
        })
    },
	getCodeInformation(code) {
		const _this = this;
		const xhr = new XMLHttpRequest();
		xhr.open("POST", '/staff/bookingCode/getCode');
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.setRequestHeader("x-csrf-token", csrfToken);
		xhr.send(code);
	
		xhr.onreadystatechange = function () {
			if(this.readyState == 4 && this.status == 200) {
				const json = this.responseText;
				const data = JSON.parse(json);
				if (data) {
					_this.loadModalWithCode(data);
					
				}
			}
		}
	},
	loadModalWithCode: function(data) {
        const modalSection = document.createElement('div');
        let duration = 300;
        modalSection.classList.add("modal-section");
        modalSection.style.animation = `fadeIn ${duration / 1000}s ease-in`;
        modalSection.innerHTML = `
			<div class="modal-section-body">
				<div class="modal__bookingcode">
					<h4 class="modal__bookingcode-heading">BOOKING CODE INFORMATION</h3>
					<div class="modal__bookingcode-infor">
						<div class="grid-row">
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title booking-code">Booking Code:</span>
									<span class="modal__bookingcode-infor-desc">${data.bookingCode}</span>
								</div>
							</div>
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title">Customer:</span>
									<span class="modal__bookingcode-infor-desc">${data.customer}</span>
								</div>
							</div>
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title">Book From:</span>
									<span class="modal__bookingcode-infor-desc">
										${this.formatDateTypeTwo(data.bookFrom)}
									</span>
								</div>
							</div>
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title">Check-In Date:</span>
									<span class="modal__bookingcode-infor-desc">
										${data.checkIn ? this.formatDateTypeTwo(data.checkIn) : "Not Yet"}
									</span>
								</div>
							</div>
							
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title">Book To:</span>
									<span class="modal__bookingcode-infor-desc">
										${this.formatDateTypeTwo(data.bookTo)}
									</span>
								</div>
							</div>
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title">Check-Out Date:</span>
									<span class="modal__bookingcode-infor-desc">
										${data.checkOut ? this.formatDateTypeTwo(data.checkOut) : "Not Yet"}
									</span>
								</div>
							</div>
							<div class="grid-col grid-col-6">
								<div class="modal__bookingcode-infor-detail">
									<span class="modal__bookingcode-infor-title">Total Room:</span>
									<span class="modal__bookingcode-infor-desc">
										${data.totalRoom}
									</span>
								</div>
							</div>
						</div>
					</div>
					<h4 class="modal__bookingcode-room-heading">Room List</h4>
					<div class="modal__roomType-list">
						<table class="modal__roomType-list-table">
							<thead>
								<tr>
									<th>Room Type</th>
									<th>Quantity</th>
								</tr>
							</thead>
							<tbody>
								<!-- Add tr with class "modal__roomType-list--editting" to turn on editing mode -->
								${this.renderRoomsTypes(data)}
							</tbody>
						</table>
					</div>
					<!-- <div class="modal__bookingcode-back-btn-block"> -->
					<div class="pill-btn blue-type-btn modal__bookingcode-back-btn">BACK</div>
					<!-- </div> -->
					<div class="square-btn red-type-btn modal__bookingcode-close-btn">
						<i class="fas fa-times"></i>
					</div>
				</div>
			</div>
        `;
        modal.appendChild(modalSection);

        const closeBtn = modal.querySelector('.modal__bookingcode-close-btn');
        const backBtn = modal.querySelector('.modal__bookingcode-back-btn');

        if (closeBtn) {
            closeBtn.onclick = function() {
                modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
                setTimeout(function() {
                    modal.removeChild(modalSection);
                },duration);
            }
        }

        if (backBtn) {
            backBtn.onclick = function() {
                modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
                setTimeout(function() {
                    modal.removeChild(modalSection);
                },duration);
            }
        }
    },
	renderRoomsTypes(data) {
		const _this = this;
		if(data.roomTypes != null) {
			return data.roomTypes.reduce((acc, roomType) => {
				return acc.concat(
						`<tr>
							<td>${_this.uppercaseFirstLetter(roomType.roomType)}</td>
							<td>${roomType.quantity}</td>
						</tr>`
					)
				}, [])
				.join("");
		} else {
			return "";
		}
	},
	formatDateTypeTwo(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [day, month, year].join('-');
    },
    uppercaseFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
	loadSelectElement() {
		const option = selectElement.value;		
		// Remove old input form
		const oldInput = inputRow.querySelector('input[class="management__dashboard-search-input"]');
		if (oldInput) {
			inputRow.removeChild(oldInput);
		}
		
		// Add new input form
		if (option.toUpperCase() == "all".toUpperCase()) {
			inputRow.style.visibility = "hidden";
		} else if (option.toUpperCase() == "bookFrom".toUpperCase() || option.toUpperCase() == "bookTo".toUpperCase()) {
			inputRow.appendChild(this.createInputForm("date"));	
			inputRow.style.visibility = "visible";
		} else {
			inputRow.appendChild(this.createInputForm("text"));
			inputRow.style.visibility = "visible";
		}
	},
	createInputForm(type) {
		const input = document.createElement('input');
		input.type = type;
		input.name = "searchValue"
		input.classList.add("management__dashboard-search-input");
		input.value = hiddenElement.value;
		return input;
	},
	loadSelectOnchange() {
		const _this = this;
		if (selectElement) {
			selectElement.onchange = function() {
				hiddenElement.value = "";
				_this.loadSelectElement();
			}
		}
	},
    run: function() {
		this.loadSelectElement();
		this.loadSelectOnchange();
        this.loadViewBtns();
		
    }
}
bookingCode.run();

