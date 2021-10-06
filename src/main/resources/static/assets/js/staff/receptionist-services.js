var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const viewDetailBtns = $$('.management__services-table-view-detail');
const hiddenElement = $('.management__dashboard-search-hidden');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const csrfToken = $('input[data-csrf-token]').value;

const searchInput = {
	//Main Screen Event
	inputValue: $('.abcdef'),
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
	loadViewBtns() {
		const _this = this;
		viewDetailBtns.forEach(btn => {
            btn.onclick = function(){
				serviceApp.run(btn);
			}
		})
	},
	run() {
		this.loadSelectElement();
		this.loadSelectOnchange();
		this.loadViewBtns();
	}
}
searchInput.run();


const serviceApp = {
	clickedViewBtn: null,
    tbodyTable: null,
	data: null,
	errorMessage: null,
	totalService: null,
	isServiceChange: false,
    distanceY: 0,
    loadInitialization() {
		const rowData = this.clickedViewBtn.closest("tr");
		const room = rowData.querySelector("td:nth-child(1)").innerText.trim();
		const bookingCode = rowData.querySelector("td:nth-child(2)").innerText.trim();
		const data = {
			room: room,
			code: bookingCode,
		}
		this.getRoomByNameAndCode(data);
    },
	getRoomByNameAndCode(data) {
		const _this = this;
		
		const xhr = new XMLHttpRequest();
		xhr.open("POST", '/staff/service/getRoom');
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.setRequestHeader("x-csrf-token", csrfToken);
		xhr.send(JSON.stringify(data));
	
		xhr.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
				const json = this.responseText;
				const responseData = JSON.parse(json);
				_this.data = responseData;
				_this.loadModalWithData(responseData);
			}
		}
	},
    loadModalWithData(data) {
        const _this = this;
        const modalSection = document.createElement('div');
        let duration = 300;
        this.disableWindowScroll();

        modalSection.classList.add("modal-section");
        modalSection.style.animation = `fadeIn ${duration / 1000}s ease-in`;
        modalSection.innerHTML = `
            <div class="modal-section-body">
                <div class="modal__service">
                    <h3 class="modal__service-heading">ROOM SERVICE DETAIL</h3>
                    <div class="modal__service-infor">
                        <div class="grid-row">
                            <div class="grid-col grid-col-6">
                                <div class="modal__service-infor-detail">
                                    <span class="modal__service-infor-title booking-code">Room:</span>
                                    <span class="modal__service-infor-desc">${data.room}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-6">
                                <div class="modal__service-infor-detail">
                                    <span class="modal__service-infor-title">Booking Code:</span>
                                    <span class="modal__service-infor-desc">${data.code}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-6">
                                <div class="modal__service-infor-detail">
                                    <span class="modal__service-infor-title">Maximum People:</span>
                                    <span class="modal__service-infor-desc">${data.maxPeople}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-6">
                                <div class="modal__service-infor-detail">
                                    <span class="modal__service-infor-title">Check-in Date:</span>
                                    <span class="modal__service-infor-desc">
										${_this.formatDateTypeTwo(data.checkIn)}
									</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-6">
                                <div class="modal__service-infor-detail">
                                    <span class="modal__service-infor-title">People In Staying:</span>
                                    <span class="modal__service-infor-desc">${data.peopleInStaying}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-6">
                                <div class="modal__service-infor-detail">
                                    <span class="modal__service-infor-title">Total Service:</span>
                                    <span class="modal__service-infor-desc" data-total-service="1">${data.totalService}</span>
                                </div>
                            </div>
                        </div>
                    </div>
					<div class="modal__service-room-upper">
						<h4 class="modal__service-room-heading">Service List</h4>
						<div class="modal__service-new">
							<div class="pill-btn blue-type-btn modal__service-new-btn">
								<i class="fas fa-plus"></i>
								Add New Service
							</div>
						</div>
					</div>
                    <div class="modal__service-list">
                        <table class="modal__service-list-table">
                            <thead>
                                <tr>
                                    <th>service</th>
                                    <th>registry date</th>
                                    <th>action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Add tr with class "modal__service-list--editting" to turn on editing mode -->
                                ${_this.loadServiceList(data.serviceInRoom, data.serviceList, data.checkIn)}
                            </tbody>
                        </table>
                    </div>
					<div class="modal__service-list-error"></div>
                    <div class="modal__service-back-btn-block">
                        <div class="pill-btn red-type-btn modal__service-cancel-btn">cancel</div>
                        <div class="pill-btn green-type-btn modal__service-ok-btn">ok</div>
                    </div>
                    <div class="square-btn red-type-btn modal__service-close-btn">
                        <i class="fas fa-times"></i>
                    </div>
                </div>
            </div>
        `;

        modal.appendChild(modalSection);
        // Close button 
        const cancelBtn = modal.querySelector('.modal__service-cancel-btn');
        const closeBtn = modal.querySelector('.modal__service-close-btn');    

        if (cancelBtn) {
            cancelBtn.onclick = function () {
                _this.closeModal(duration, modalSection);
            }
        }

        if (closeBtn) {
            closeBtn.onclick = function () {
                _this.closeModal(duration, modalSection);
            }
        }

        // Add new service
        const addNewBtn = modal.querySelector('.modal__service-new-btn');
        if (addNewBtn) {
            addNewBtn.onclick = function () {
				_this.transformOKButton();
                _this.addNewService();
				_this.rewriteServiceInRoom();
            }
        }
		// Update button
		const updateBtn = modal.querySelector('.modal__service-ok-btn');
		if (updateBtn) {
			updateBtn.onclick = function () {
				const rowDatas = $$('.modal__service-list-table tbody tr');
				const isSavedAll = Array.from(rowDatas).every(row => !row.classList.contains("modal__service-list--editting"));
				
				if (_this.isServiceChange) {
					if (isSavedAll) {
						_this.sendServiceToUpdate(_this.data);
						_this.updateTotalService();
						_this.closeModal(duration, modalSection);
					} else {
						_this.errorMessage.innerText = "One of the services above still not save";
					}
				} else {
					_this.closeModal(duration, modalSection);
				}
			}
		}
		
        //  Load delete loadBtns
        this.tbodyTable = modal.querySelector('.modal__service-list-table tbody');
        this.loadDeleteBtns();
        this.loadEditBtns();

		// Load error errorMessage
		this.errorMessage = modal.querySelector('.modal__service-list-error');
		this.totalService = modal.querySelector('span[data-total-service]');
    },
	loadServiceList(services, serviceOptions, checkIn) {
		const _this = this;
		return services.map(service => {
			return `
				<tr>
					<td>
						<span class="modal__service-list-table-name">${_this.uppercaseFirstLetter(service.serviceName)}</span>
						<select class="modal__service-list-table-select">
							${_this.loadServiceOptions(serviceOptions , service.serviceName)}
						</select>
					</td>
					<td>
						<span class="modal__service-list-table-date">
							${_this.formatDateTypeTwo(service.dateRegistry)}
						</span>
						<input class="modal__service-list-table-input" 
						       type="date" 
							   min="${_this.formatDateTypeOne(checkIn)}" 
							   value="${_this.formatDateTypeOne(service.dateRegistry)}"
						/>
					</td>
					<td>
						<div class="modal__service-edit-btns">
							<span class="pill-btn blue-type-btn modal__service-edit-btn">Edit</span>
							<span class="pill-btn red-type-btn modal__service-delete-btn">Delete</span>
						</div>
						<div class="modal__service-save-btns">
							<span class="pill-btn green-type-btn modal__service-save-btn">Save</span>
							<span class="pill-btn red-type-btn modal__service-delete-btn">Delete</span>
						</div>
					</td>
				</tr>
			`;
		}).join("");
	},
	loadServiceOptions (serviceOptions, serviceName) {
		const _this = this;
		return serviceOptions.map(option => {
			if (serviceName == option) {
				return `
					<option value="${option}" selected>${_this.uppercaseFirstLetter(option)}</option>
				`;
			} else {
				return `
					<option value="${option}">${_this.uppercaseFirstLetter(option)}</option>
				`;
			}
		}).join("");
	},
    closeModal(duration, modalSection) {
        this.enableWindowScroll();

        modalSection.style.animation = `fadeOut ${duration / 1000}s ease-in forwards`;

		this.tbodyTable = null;
		this.data = null;
		this.errorMessage = null;
		this.totalService = null;
		this.isServiceChange = false;
		
        setTimeout(function () {
            modal.removeChild(modalSection);
        },duration);
		
    },
    addNewService: function() {
		const _this = this;
        if (this.tbodyTable) {
            let newServiceRow = document.createElement("tr");

            newServiceRow.classList.add("modal__service-list--editting");
            newServiceRow.innerHTML = `
                <td>
                    <span class="modal__service-list-table-name">Laundry</span>
                    <select class="modal__service-list-table-select">
                        ${_this.loadServiceOptions(_this.data.serviceList)}
                    </select>
                </td>
                <td>
                    <span class="modal__service-list-table-date">
						${_this.formatDateTypeTwo(_this.data.today)}
					</span>
                    <input class="modal__service-list-table-input" 
						   type="date" 
						   min="${_this.formatDateTypeOne(_this.data.checkIn)}"
						   value="${_this.formatDateTypeOne(_this.data.today)}"
					/>
                </td>
                <td>
                    <div class="modal__service-edit-btns">
                        <span class="pill-btn blue-type-btn modal__service-edit-btn">Edit</span>
                        <span class="pill-btn red-type-btn modal__service-delete-btn">Delete</span>
                    </div>
                    <div class="modal__service-save-btns">
                        <span class="pill-btn green-type-btn modal__service-save-btn">Save</span>
                        <span class="pill-btn red-type-btn modal__service-delete-btn">Delete</span>
                    </div>
                </td>
            `;
            this.tbodyTable.appendChild(newServiceRow);
            this.loadSaveBtns();
            this.loadDeleteBtns();
        }
    },
    loadDeleteBtns: function () {
        const _this = this;
        const deleteBtns = modal.querySelectorAll('.modal__service-delete-btn');
        if (deleteBtns.length > 0) {
            deleteBtns.forEach(btn => {
                btn.onclick = function () {
                    const rowData = this.closest("tr");
					
					_this.transformOKButton();
                    _this.tbodyTable.removeChild(rowData);
					_this.rewriteServiceInRoom();
                }
            })
        }
    },
    loadEditBtns: function() {
        const _this = this;
        const editBtns = modal.querySelectorAll('.modal__service-edit-btn');
        editBtns.forEach(btn => {
            btn.onclick = function () {
                const rowData = this.closest("tr");  
                // Get old data before editting
                const serviceName = rowData.querySelector(".modal__service-list-table-name").innerText;
                const dateString = rowData.querySelector(".modal__service-list-table-date").innerText;

                const registryDate = new Date(_this.splitDate(dateString));
                const registryDateString = _this.formatDateTypeOne(registryDate);

                // Start editting 
                rowData.classList.add("modal__service-list--editting");

                // Display editting form with old data
                const selectElement = rowData.querySelector('.modal__service-list-table-select');
                const options = Array.from(selectElement.options);
                if (options.length > 0) {
                    const optionResult = options.find(option => option.innerText == serviceName);
                    selectElement.selectedIndex = optionResult.index;
                }

                const inputElement = rowData.querySelector('.modal__service-list-table-input');
                inputElement.value = registryDateString;

				_this.transformOKButton();
                _this.loadSaveBtns();
                _this.loadDeleteBtns();
            }
        })
    },
	loadSaveBtns: function() {
        const _this = this;
        const saveBtns = modal.querySelectorAll('.modal__service-save-btn');
        saveBtns.forEach(btn => {
            btn.onclick = function() {
                const rowData = this.closest("tr");
                // Get new data
                const selectElement = rowData.querySelector('.modal__service-list-table-select');
                const options = selectElement.options;                
                const serviceName = options[options.selectedIndex].innerText;

                const dateString = rowData.querySelector('input[type="date"]').value;
                const registryDate = new Date(dateString);
                const registryDateString = _this.formatDateTypeTwo(registryDate);

                // Save new data
                rowData.querySelector(".modal__service-list-table-name").innerText = serviceName;
                rowData.querySelector(".modal__service-list-table-date").innerText = registryDateString;
                
                // Turn off editting mod
                rowData.classList.remove("modal__service-list--editting");

				_this.transformOKButton();
				_this.rewriteServiceInRoom();
                _this.loadEditBtns();
                _this.loadDeleteBtns();
            }
        })
    },
	rewriteServiceInRoom() {
		const rowDatas = $$('.modal__service-list-table tbody tr');
		this.data.serviceInRoom = [];
		rowDatas.forEach((row) => {
			const serviceName = row.querySelector('.modal__service-list-table-select').value;
			const dateRegistry = row.querySelector('.modal__service-list-table-input').value;
			this.data.serviceInRoom.push({
				serviceName: serviceName,
				dateRegistry: dateRegistry
			})
		})
		this.totalService.innerText = this.data.serviceInRoom.length;
		
	},
	transformOKButton() {
		const updateBtn = modal.querySelector('.modal__service-ok-btn');
		if (this.errorMessage) {
			this.errorMessage.innerText = "";
		}

		if (updateBtn) {
			updateBtn.innerText = "update";
		}

		this.isServiceChange = true;
	},
	sendServiceToUpdate(data) {
		const xhr = new XMLHttpRequest();
		xhr.open("POST", '/staff/service/updateService');
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.setRequestHeader("x-csrf-token", csrfToken);
		xhr.send(JSON.stringify(data));
	
		xhr.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
				const json = this.responseText;
				const responseData = JSON.parse(json);
			}
		}
	},
	updateTotalService() {
		const rowData = this.clickedViewBtn.closest("tr");
		const totalServiceElement = rowData.querySelector("td:nth-child(4)");
		totalServiceElement.innerText = this.data.serviceInRoom.length;
	},
    splitDate: function(dateString) {
        return dateString.replace(/^(\w+)-(\d+)/, '$2-$1');// convert dd-MM-yyyy to MM-dd-yyyy
    },
    formatDateTypeOne: function(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    },
    formatDateTypeTwo: function(date) {
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
    run(btn) {
		this.clickedViewBtn = btn;
        this.loadInitialization();
    }
}


