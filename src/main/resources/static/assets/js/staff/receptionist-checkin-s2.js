var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const remainRoomList = $('.management__room-remain');
const checkInTable = $('.management__checkin-room-table');
const roomTypeList = checkInTable.querySelectorAll('.management__checkin-room-table tr td:nth-child(1)');
const quantityList = checkInTable.querySelectorAll('.management__checkin-room-table tr td:nth-child(3)');
const allRoom = $$('.management__room-all-detail');
const bookingCodeElement = $('.management__checkin-code-desc[data-code]');
const errorMessage = $('.management__room-error-message');
const nextBtn = $('.management__room-selection-next-btn');
const csrfToken = $('.management__crsfToken').value;

var checkInApp = {
	data: {
		bookingCode: bookingCodeElement.innerText.trim(),
		rooms: []
	},
    roomTypes: [],
    loadRoomTypes: function() {
        // Push all room type into object to handle
        for (let i = 0; i < roomTypeList.length; i++) {
            this.roomTypes.push({
                roomType: roomTypeList[i].innerText, 
                quantity: parseInt(quantityList[i].innerText)
            })
        }
        this.renderRemainRoomList();
    },
    renderRemainRoomList: function() {
		const _this = this;
        let roomTypeRemain;

        if (this.roomTypes.length > 0) {
            roomTypeRemain = this.roomTypes.reduce((html, roomType) => {
                return html += `
                    <div class="management__room-remain-detail">
                        <span class="management__room-remain-type">${_this.uppercaseFirstLetter(roomType.roomType)}</span>
                        <div class="management__room-remain-qtt">
                            <span class="management__room-remain-number">${roomType.quantity}</span>
                            <span class="management__room-remain-unit">${roomType.quantity <= 1 ? 'room left' : 'rooms left'}</span>
                        </div>
                    </div>
                `;
            },'')
        }
        remainRoomList.innerHTML = roomTypeRemain;
    },
    loadAllRoom: function() {
        const _this = this;
        allRoom.forEach(room => {
            room.onclick = function(e) {
                const checkbox = this.querySelector('input[type="checkbox"]');
                if (checkbox) {
					errorMessage.innerText = "";
                    if (e.target !== checkbox) {
                        checkbox.checked = !checkbox.checked;
                    }
                    _this.updateRemainRoom(room, checkbox);
                    checkbox.checked ? this.classList.add('active') : this.classList.remove('active')
                }
            }
        })
    },
    updateRemainRoom: function(room, checkbox) {
        const roomTypeOnClick = room.querySelector('.management__room-all-type').innerText.toUpperCase().trim();
        const result = this.roomTypes.find(room => room.roomType.toUpperCase().trim() === roomTypeOnClick);
		
        if (result) {
            if (checkbox.checked) {
                result.quantity <= 0 ? checkbox.checked = false : result.quantity--;
            } else {
                result.quantity++;
            }
        } else {
            checkbox.checked = !checkbox.checked;
        }
		this.updateData(room,checkbox);
        this.renderRemainRoomList();
    },
	updateData(room, checkbox) {
		const roomName = checkbox.value;
		// const roomType = room.querySelector('.management__room-all-type').innerText.trim().toLowerCase();
		if (checkbox.checked) {
			this.data.rooms.push({
				roomName: roomName,
			})
		} else {
			const matchedResult = this.data.rooms.find(room => room.roomName == roomName);
			if (matchedResult) {
				const index = this.data.rooms.indexOf(matchedResult);
				this.data.rooms.splice(index, 1);
			}
		}
	},
	loadNextBtn() {
		const _this = this;
		nextBtn.onclick = function () {
			const isFillAll = _this.roomTypes.every(roomType => roomType.quantity === 0);
			
			if (isFillAll) {
				_this.sendData(_this.data);
			} else {
				errorMessage.innerText = "Please choose all room before check in";
			}
		}
	},
	sendData(data) {
		const form = document.createElement('form');
        form.method = "POST";
        form.action = "/staff/checkIn/finish";

        const csrfElement = document.createElement("input");
        csrfElement.type = "hidden";
        csrfElement.name = "_csrf";
        csrfElement.value = csrfToken;
        form.appendChild(csrfElement);

		createForm(data);
		function createForm(data) {
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
		
        document.body.appendChild(form);
        form.submit();
	},
	uppercaseAllRoomTypes() {
		roomTypeList.forEach(roomType => {
			const storedName = roomType.innerText;
			roomType.innerText = this.uppercaseFirstLetter(storedName)
		});
	},
    uppercaseFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    run: function() {
        this.loadRoomTypes();
		this.loadNextBtn();
        this.loadAllRoom();
		this.uppercaseAllRoomTypes();
    }
}
checkInApp.run();