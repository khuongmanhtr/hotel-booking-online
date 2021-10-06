var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = document.getElementById('modal-segment');
const rooms = $$('.management__room-all-detail');
const allRoomType = $$('.management__room-statistics-table tbody td:nth-child(1)')
const csrfToken = $('input[data-csrf-token]').value;

var manageRoom = {
	roomTypes: null,
	allRoom: null,
	errorMessage: null,
	modalSection: null,
    distanceY: 0,
	loadInitialData() {
		const _this = this;
		
		// All Room Type
		const roomTypeData = fetch("/manager/room/getRoomTypes", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"x-csrf-token": csrfToken
			}
		})
		.then(response => response.json());

		// All Room
		const allRoomData = fetch("/manager/room/getAllRoom", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"x-csrf-token": csrfToken
			}
		})
		.then(response => response.json());
		
		Promise.all([roomTypeData, allRoomData])
			.then(data => {
				_this.roomTypes = data[0];
				_this.allRoom = data[1];
			});
		
	},
    loadNewButton: function() {
        const addNewBtn = $('.management__room-rooms-new');
        addNewBtn.onclick = () => this.loadCreateModeModal();
    },
    loadAllRoomClick: function() {
        const _this = this;
        rooms.forEach(room => {
            room.onclick = () => {
				const roomName = room.querySelector('.management__room-all-name').innerText.trim();

				fetch('/manager/room/getRoom', {
					method: 'POST',
					credentials: 'include',
					headers: {
						"Accept": "application/json",
						'Content-Type': 'application/json',
						'x-csrf-token': csrfToken,
					},
					body: roomName,
				})
				.then(response => response.json())
				.then((data) => {
					if (room.classList.contains('management__room-view--being-used')) {
						_this.loadUneditableModeModal(data);
					} else {
						_this.loadViewModeModal(data);
					}
				});
            }
        })
    },
    loadCreateModeModal() {
		const _this = this;
        this.loadViewModeModal();
        const roomForm = modal.querySelector('.modal__rooms-form');
        roomForm.classList.add('create');

		const select = $('.modal__rooms-form-type-select');
		const roomType = select.value;
		this.renderRoomTypeInformation(roomType);
    },
    loadUneditableModeModal(data) {
        this.loadViewModeModal(data);
        const roomForm = modal.querySelector('.modal__rooms-form');
        roomForm.classList.add('uneditable');
    },
    loadViewModeModal(data = null) {
        this.disableWindowScroll();
        const _this = this;
        const modalSection = document.createElement('div');
        const duration = 300;

        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s linear`;

        modalSection.innerHTML = `
            <div class="modal-section-body">
                <!-- + add class "create" to change create form mode
                     + add class "uneditable" to change read only mode --> 
                <div class="modal__rooms-form">
                    <div class="modal__rooms-form-inputs">
                        <h3 class="modal__rooms-form-heading">room information</h3>
                        <div class="grid-row modal__rooms-form-details">
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__rooms-form-title">Room Name:</span>
                                    <input type="text" class="modal__rooms-form-input">
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__rooms-form-title">Room Type:</span>
                                    <select class="modal__rooms-form-type-select">
                                        ${_this.renderAllRoomType()}
                                    </select>
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__rooms-form-title">Status:</span>
                                    <select class="modal__rooms-form-status-select">
                                        <option value="1">Active</option>
                                        <option value="0">Deactive</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
					<input type="hidden" name="roomId" value="${data ? data.roomId : ''}"/>
                    <div class="modal__rooms-form-view">
                        <h3 class="modal__rooms-form-heading">room information</h3>
                        <div class="grid-row modal__rooms-form-details">
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__rooms-form-title">Room Name:</span>
                                    <span class="modal__rooms-form-desc" data-room-name="${data ? data.roomName : ''}">${data ? data.roomName : ''}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__rooms-form-title">Room Type:</span>
                                    <span class="modal__rooms-form-desc" data-room-type="${data ? data.roomType.name : ''}">${data ? _this.uppercaseFirstLetter(data.roomType.name) : ''}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__rooms-form-title">Status:</span>
                                    <span class="modal__rooms-form-desc" data-room-status="${data ? (data.isActive ? 1 : 0) : ''}">${data ? (data.isActive ? "Active": "Deactive") : ''}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal__rooms-form-infos">
                        <h3 class="modal__rooms-form-heading">room type information</h3>
                        <div class="modal__room-type-info">
                            <div class="modal__room-type-detail">
                                <span class="modal__room-type-title">Number of people:</span>
                                <span class="modal__room-type-desc">${data ? data.roomType.numberOfPeople : ''}</span>
                            </div>
                            <div class="modal__room-type-detail">
                                <span class="modal__room-type-title">Price:</span>
                                <span class="modal__room-type-desc">${data ? new Intl.NumberFormat().format(data.roomType.price) : ''}</span>
                            </div>
                            <div class="modal__room-type-detail">
                                <span class="modal__room-type-title">Description:</span>
                                <span class="modal__room-type-desc modal__room-type-desc--height">${data ? data.roomType.description : ''}</span>
                            </div>
                        </div>
                    </div>

					<div class="modal__rooms-form-error"></div>

                    <div class="modal__rooms-form-create-btns">
                        <div class="modal__rooms-form-btn-left"></div>
                        <div class="modal__rooms-form-btn-right">
                            <div class="pill-btn red-type-btn modal__rooms-form-back-btn">back</div>
                            <div class="pill-btn green-type-btn modal__rooms-form-save-btn">save</div>
                        </div>
                    </div>

                    <div class="modal__rooms-form-edit-btns">
                        <div class="modal__rooms-form-btn-left"></div>
                        <div class="modal__rooms-form-btn-right">
                            <div class="pill-btn red-type-btn modal__rooms-form-cancel-btn">cancel</div>
                            <div class="pill-btn green-type-btn modal__rooms-form-save-btn">save</div>
                        </div>
                    </div>

                    <div class="modal__rooms-form-view-btns">
                        <div class="modal__rooms-form-btn-left">
                            <div class="pill-btn red-type-btn modal__rooms-form-delete-btn">delete</div>
                        </div>
                        <div class="modal__rooms-form-btn-right">
                            <div class="pill-btn blue-type-btn modal__rooms-form-back-btn">back</div>
                            <div class="pill-btn yellow-type-btn modal__rooms-form-edit-btn">edit</div>
                        </div>
                    </div>

                    <div class="modal__rooms-form-uneditale-btns">
                        <div class="modal__rooms-form-btn-left">
                        </div>
                        <div class="modal__rooms-form-btn-right">
                            <div class="pill-btn blue-type-btn modal__rooms-form-back-btn">back</div>
                        </div>
                    </div>

                    <div class="square-btn red-type-btn modal__rooms-close-btn">
                        <i class="fas fa-times"></i>
                    </div>
                </div>
            </div>
        `;
        modal.appendChild(modalSection);
        
		const closeBtn = modal.querySelector('.modal__rooms-close-btn');
		const editBtn = modal.querySelector('.modal__rooms-form-edit-btn');
		const cancelBtn = modal.querySelector('.modal__rooms-form-cancel-btn');
		const deleteBtn = modal.querySelector('.modal__rooms-form-delete-btn');
		const backBtns = modal.querySelectorAll('.modal__rooms-form-back-btn');
		const saveBtns = modal.querySelectorAll('.modal__rooms-form-save-btn');

		const roomIdInput = modal.querySelector('input[name="roomId"]');
        const roomNameInput = modal.querySelector('.modal__rooms-form-input');
		const roomTypeSelection = modal.querySelector('.modal__rooms-form-type-select');
		const roomStatusSelection = modal.querySelector('.modal__rooms-form-status-select');

		const roomForm = modal.querySelector('.modal__rooms-form');
        const roomNameSpan = modal.querySelector('.modal__rooms-form-view span[data-room-name]');
		const roomTypeSpan = modal.querySelector('.modal__rooms-form-view span[data-room-type]');
		const roomStatusSpan = modal.querySelector('.modal__rooms-form-view span[data-room-status]');

		// Close Modal
        function closeModal() {
            _this.enableWindowScroll();
            modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(modalSection), duration);
        }

		// Store variable
		this.errorMessage = modal.querySelector('.modal__rooms-form-error');
		this.modalSection = modalSection;

		//Close Button 
        if (closeBtn) {
            closeBtn.onclick = closeModal;
        }

        //Back button 
        if (backBtns.length > 0) {
            backBtns.forEach(backBtn => {
                backBtn.onclick = closeModal;
            })
        }

		// Save Buttons
		if (saveBtns.length > 0) {
			saveBtns.forEach(saveBtn => {
				saveBtn.onclick = () => {
					const roomId = roomIdInput.value;
					const roomIdInt = roomId == "" ? 0 : parseInt(roomId);

					const isValidName = _this.validateName(roomNameInput);
					const roomName = roomNameInput.value;
					const roomType = roomTypeSelection.value;
					const roomStatus = parseInt(roomStatusSelection.value) ? true : false;
					const data = {
						roomId: roomIdInt,
						roomName: roomName,
						roomType: roomType,
						roomStatus: roomStatus
					};

					if (isValidName) {
						if (roomIdInt == 0) {
							// create
							// check room name có duplicate hay không
							const isDuplicateRoomName = _this.allRoom.find(room => room.roomName === roomName);
							if (isDuplicateRoomName) {
								_this.errorMessage.innerText = "Your room name is existed";
								roomNameInput.classList.add('error');
							} else {
								_this.saveData(data);
							}
						} else {
							// update
							// kiểm tra cả cặp id và room name có index trên đó không
							const isExistedIdNamePair = _this.allRoom.find(room => room.roomId === roomIdInt && room.roomName === roomName)
							if (isExistedIdNamePair) {
								_this.saveData(data);
							} else {
								// kiêm tra room name có trùng không
								const isDuplicateRoomName = _this.allRoom.find(room => room.roomName === roomName);
								if (isDuplicateRoomName) {
									_this.errorMessage.innerText = "Your room name is existed";
									roomNameInput.classList.add('error');
								} else {
									_this.saveData(data);
								}
							}
						}
					}
				};
			})
		}
        // Edit button
        if (editBtn) {
            editBtn.onclick = function() {
                roomForm.classList.add('edit');

                const roomName = roomNameSpan.dataset.roomName;
                const roomType = roomTypeSpan.dataset.roomType;
                const roomStatus = roomStatusSpan.dataset.roomStatus;
                
                roomNameInput.value = roomName;
				roomTypeSelection.value = roomType;
				roomStatusSelection.value = roomStatus;
            }
        }

        // Cancel button
        if (cancelBtn) {
            cancelBtn.onclick = function() {
                roomForm.classList.remove('edit');
				const roomType = roomTypeSpan.dataset.roomType;
				if (roomType) {
					_this.renderRoomTypeInformation(roomType);
				}
            }
        }

        // Delete buttons
        if (deleteBtn) {
            deleteBtn.onclick = function() {
                const roomId = roomIdInput.value;
                const roomName = roomNameSpan.dataset.roomName;
                _this.loadConfirmModal({
					roomId: roomId,
					roomName: roomName,
					content: "Do you really want to delete this room ?"
				});
            }
        }

		// Room Type Options onchange
		roomTypeSelection.onchange = function () {
			const roomType = this.value
			_this.renderRoomTypeInformation(roomType);
		}

		roomNameInput.onfocus = function () {
			_this.resetErrorMessage();
		}

    },
	saveData(data) {
		const _this = this;
		fetch("/manager/room/save", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"x-csrf-token": csrfToken,
			},
			body: JSON.stringify(data)
		})
		.then(response => response.json())
		.then((responseData) => {
			let duration = 300;
			_this.modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(_this.modalSection), duration);

			if (responseData) {
				setTimeout(() => location.href="/manager/room/", duration);
			}
		});
	},
	deleteData(roomId) {
		const _this = this;
		fetch("/manager/room/delete", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"x-csrf-token": csrfToken,
			},
			body: roomId
		})
		.then(response => response.json())
		.then((responseData) => {
			let duration = 300;
			_this.modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(_this.modalSection), duration);

			if (responseData) {
				setTimeout(() => location.href="/manager/room/", duration);
			}
		});
	},
	renderRoomTypeInformation(roomType) {
		const roomTypeInfor = $('.modal__room-type-info');

		const roomTypeResult = this.roomTypes.find(type => type.roomType === roomType);

		if (roomTypeInfor) {
			roomTypeInfor.innerHTML = `
				<div class="modal__room-type-detail">
					<span class="modal__room-type-title">Number of people:</span>
					<span class="modal__room-type-desc">${roomTypeResult.numberOfPeople}</span>
				</div>
				<div class="modal__room-type-detail">
					<span class="modal__room-type-title">Price:</span>
					<span class="modal__room-type-desc">${new Intl.NumberFormat().format(roomTypeResult.price)}</span>
				</div>
				<div class="modal__room-type-detail">
					<span class="modal__room-type-title">Description:</span>
					<span class="modal__room-type-desc modal__room-type-desc--height">${roomTypeResult.description}</span>
				</div>
			`
		}
	},
	renderAllRoomType() {
		return this.roomTypes.map(type => 
			`<option value="${type.roomType}">${this.uppercaseFirstLetter(type.roomType)}</option>`)
		.join('')
	},
    loadConfirmModal({roomId, roomName, content}) {
        const modalSection = document.createElement('div');
        const duration = 300;

        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s linear`; 
        
		const modalBody = `
			<div class="modal-section-body">
				<div class="modal__notification modal__type--exclamation">
					<div class="modal__notification-heading modal__type--exclamation">
						<i class="fas fa-exclamation-circle"></i>
					</div>
					<h4 class="modal__notification-title">${content}</h4>
					<div class="modal__notification-room">${roomName}</div>
					<div class="modal__notification-buttons">
						<div class="pill-btn green-type-btn modal__notification-confirm">Yes</div>
						<div class="pill-btn red-type-btn modal__notification-cancel">No</div>
					</div>
				</div>
			</div>
		`;
		modalSection.innerHTML = modalBody;

        modal.appendChild(modalSection);
        // Close modal
        function closeModal() {
            modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(modalSection), duration);
        }

        // No Button
        const noBtn = modal.querySelector('.modal__notification-cancel');
        if (noBtn) {
            noBtn.onclick = closeModal;
        }

        const yesBtn = modal.querySelector('.modal__notification-confirm');
        if (yesBtn) {
			yesBtn.onclick = () => {
				closeModal();
				this.deleteData(roomId)
			};
        }
    },
	validateName(input) {
		let isValid = true;
		if (!this.checkEmpty(input.value.trim())) {
			this.errorMessage.innerText = "Room name is empty"
			input.classList.add('error');
			return !isValid;
		}

		if (!this.checkValidName(input.value.trim())) {
			this.errorMessage.innerText = "Room name is not valid"
			input.classList.add('error');
			return !isValid;
		}

		return isValid;
	},
	resetErrorMessage() {
		const nameInput = modal.querySelector('.modal__rooms-form-input');
		if (nameInput) {
			nameInput.classList.remove('error');
		}
		this.errorMessage.innerText = "";
	},
	checkEmpty(string) {
		return string !== "" ? true : false;
	},
	checkValidName(string) {
		const regExp = /^[a-zA-Z0-9]{1,}$/
		return string.match(regExp) ? true : false;
	},
	uppercaseAllRoomType() {
		const _this = this;
		if (allRoomType.length > 0) {
			allRoomType.forEach(roomType =>{
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
		this.loadInitialData();
        this.loadNewButton();
        this.loadAllRoomClick();
		this.uppercaseAllRoomType();
    }
}
manageRoom.run();
