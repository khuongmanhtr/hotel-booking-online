var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = document.getElementById('modal-segment');
const viewBtns = $$('.management__service-promotion-btn');
const addNewBtn = $('.management__room-type-new');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const hiddenElement = $('.management__dashboard-search-hidden');
const csrfToken = $('input[data-csrf-token]').value;
let allServiceList = [];

const servicePromotionCode = {
	getAllServiceList() {
		fetch("/manager/service/getService", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"x-csrf-token": csrfToken
			}
		})
		.then(response => response.json())
		.then(services => allServiceList = services);
	},
	loadNewBtn() {
        addNewBtn.onclick = function() {
			manageServiceDetail.run();
		}
    },
    loadViewBtns() {
        const _this = this;
        viewBtns.forEach(btn => {
            btn.onclick = function() {
				manageServiceDetail.run(btn);
            }
        })
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
	run() {
		this.loadNewBtn();
		this.loadViewBtns();
		this.loadSelectElement();
        this.loadSelectOnchange();
		this.getAllServiceList();
	}
}
servicePromotionCode.run();

const manageServiceDetail = {
	clickedViewBtn: null,
	serviceList: [],
	activeServiceDetail: [],
	errorMessage: null,
	modalSection: null,
	isChanged: false,
    distanceY: 0,
	loadInitialization() {
		const _this = this;
		this.serviceList = [...allServiceList];

		if (this.clickedViewBtn != null) {
			const rowData = this.clickedViewBtn.closest('tr');
			const promoId = rowData.querySelector('input[name="servicePromoId"]').value.trim();
			const promoCode = rowData.querySelector('td:nth-child(1)').innerText.trim();
			const description = rowData.querySelector('td:nth-child(2)').innerText.trim();
			const currentStatus = rowData.querySelector('td:nth-child(4)').innerText.trim();

			const data = {
				id: promoId,
				code: promoCode,
				description: description,
				currentStatus: currentStatus,
			}

			fetch("/manager/promoCode/service/servicePromoDetailList", {
				method: 'POST',
				headers: {
					"Content-type" : "application/json; charset=utf-8",
					"x-csrf-token" : csrfToken,
				},
				body: data.id
			})
			.then(response => response.json())
			.then(responseData => {
				this.activeServiceDetail = responseData;
				this.loadDetailsModal(data);
			})
		} else {
			const data = {
				id: 0,
				code: null,
				description: null,
				currentStatus: "create",
			}
			this.loadDetailsModal(data);
		}

		
	},
    loadDetailsModal(data) {
        const _this = this;
        this.disableWindowScroll();

		const status = {
			"being used": "uneditable",
			"available" : "editable",
			"expired" : "deleteOnly",
			"create" : "create"
		}

        const modalSection = document.createElement('div');
        let duration = 300;
        modalSection.classList.add('modal-section');

        modalSection.style.animation = `fadeIn ${duration / 1000}s ease-in`;
        modalSection.innerHTML = this.renderHTML`
            <div class="modal-section-body">
                <!-- 
                    + add class "create" to change create mode
                    + add class "editable" to change editable mode
                    + add class "uneditable" to change uneditable mode
                -->
                <div class="modal__service-promotion ${status[data.currentStatus]}">
                    <h3 class="modal__service-promotion-heading">service promotion code detail</h3>
                    <div class="modal__service-promotion-infor">
                        <div class="grid-row">
							<input type="hidden" name="servicePromoId" value="${data.id}">
                            <div class="grid-col grid-col-12">
                                <div class="modal__service-promotion-detail">
                                    <span class="modal__service-promotion-title">Service Promotion Code:</span>
                                    <input type="text" class="modal__service-promotion-input" data-promo-code="1" value="${data.code}">
                                    <span class="modal__service-promotion-desc promoCode">${data.code}</span>
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__service-promotion-detail">
                                    <span class="modal__service-promotion-title">Description:</span>
                                    <input type="text" class="modal__service-promotion-input" data-description="1" value="${data.description}">
                                    <span class="modal__service-promotion-desc">${data.description}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h4 class="modal__applied-service-heading">Applied Service List</h4>
                    <div class="pill-btn blue-type-btn modal__applied-service-new-btn">
                        <i class="fas fa-plus"></i>
                        Add New Service
                    </div>
                    <div class="modal__service-list">
                        <table class="modal__service-list-table">
                            <thead>
                                <tr>
                                    <th>service name</th>
                                    <th>discount percent(%)</th>
                                    <th>action</th>
                                </tr>
                            </thead>
                            <tbody>
								${data.currentStatus == "create" ? "" : _this.renderServiceDetail()}
                            </tbody>
                        </table>
                    </div>

					<div class="modal__service-promotion-error"></div>

                    <div class="modal__service-promotion-btns">
						<!-- CREATE -->
                        <div class="modal__service-promotion-create">
                            <div class="pill-btn blue-type-btn modal__service-promotion-back-btn">back</div>
                            <div class="pill-btn green-type-btn modal__service-promotion-save-btn">save</div>
                        </div>

						<!-- EDITABLE -->
                        <div class="modal__service-promotion-edit">
                            <div class="pill-btn red-type-btn modal__service-promotion-delete-btn">delete code</div>
                            <div>
                                <div class="pill-btn yellow-type-btn modal__service-promotion-back-btn">cancel</div>
                                <div class="pill-btn green-type-btn modal__service-promotion-save-btn">save</div>
                            </div>
                        </div>

						<!-- READ-ONLY -->
                        <div class="modal__service-promotion-read-only">
                            <div class="pill-btn blue-type-btn modal__service-promotion-back-btn">back</div>
                        </div>

						<!-- DELETE-ONLY -->
						<div class="modal__service-promotion-delete-only">
                            <div class="pill-btn red-type-btn modal__service-promotion-delete-btn">delete code</div>
                            <div>
								<div class="pill-btn blue-type-btn modal__service-promotion-back-btn">back</div>
                            </div>
                        </div>
                    </div>

                    <div class="square-btn red-type-btn modal__service-close-btn">
                        <i class="fas fa-times"></i>
                    </div>
                </div>
            </div>
        `;
		this.modalSection = modalSection;
        modal.appendChild(modalSection);
        this.loadAllButton();
    },
	renderServiceDetail() {
		// Add tr with class "modal__service-list--editting" to turn on editing mode -->
		const _this = this;
		return this.activeServiceDetail.map(serviceDetail => `
			<tr>
				<td>
					<span class="modal__service-list-table-name" data-service-id="${serviceDetail.serviceId}">${_this.uppercaseFirstLetter(serviceDetail.serviceName)}</span>
					<select class="modal__service-list-table-select">
						${this.renderServiceList()}
					</select>
				</td>
				<td>
					<span class="modal__service-list-table-date" data-discount="${serviceDetail.discount}">${serviceDetail.discount}</span>
					<input type="text" class="modal__service-list-table-input" value="${serviceDetail.discount}">
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
					<div class="modal__service-read-only-btns">
						<span class="pill-btn modal__service-uneditable-btn">Uneditable</span>
					</div>
				</td>
			</tr>
		`).join('')
	},
	renderServiceList() {
		const _this = this;
		return this.serviceList.map(service => `
			<option value=${service.id}>${_this.uppercaseFirstLetter(service.name)}</option>
		`)
		.join('');
	},
    loadAllButton() {
        const _this = this;
		
		// Every select element onchange
		const selectElements = modal.querySelectorAll('.modal__service-list-table-select');
		if (selectElements.length > 0) {
			selectElements.forEach(element => {
				element.onchange = () => this.errorMessage.innerText = "";
			})
		}

		// Every discount input element onfocus
		const inputElements = modal.querySelectorAll('.modal__service-list-table-input');
		if (inputElements.length > 0) {
			inputElements.forEach(element => {
				element.onfocus = () => {
					this.errorMessage.innerText = "";
					element.classList.remove("error");
				}

			})
		}

		// Promo code input element onfocus
		const promoInfoElements = modal.querySelectorAll('.modal__service-promotion-input');
		if (promoInfoElements.length > 0) {
			promoInfoElements.forEach(element => {
				element.onfocus = () => {
					this.errorMessage.innerText = "";
					element.classList.remove("error");
				}

				element.oninput = () => this.isChanged = true;
			})
		}

		// Error Message
		const errorMessage = modal.querySelector('.modal__service-promotion-error');
		if (errorMessage) {
			this.errorMessage = errorMessage;
		}

		// Close Button 
		const closeBtn = modal.querySelector('.modal__service-close-btn');
		if (closeBtn) {
			closeBtn.onclick =() => this.closeModal();
		}

		// Back Button
		const backBtns = modal.querySelectorAll('.modal__service-promotion-back-btn');
		if (backBtns.length > 0) {
			backBtns.forEach(btn => btn.onclick = () => this.closeModal());
		}

        // Service Addition Button
        const addNewServiceBtn = modal.querySelector('.modal__applied-service-new-btn');
        if (addNewServiceBtn) {
            addNewServiceBtn.onclick = () => this.addNewService();
        }

        // Service Edition Button
        const serviceEditionBtns = modal.querySelectorAll('.modal__service-edit-btn');
        if (serviceEditionBtns.length > 0) {
            serviceEditionBtns.forEach(btn => {
                btn.onclick = function() {
                    _this.serviceEditionBtn(btn);
                }
            })
        }
        // Service Deletion Button
        const serviceDeletionBtns = modal.querySelectorAll('.modal__service-delete-btn');
        if (serviceDeletionBtns.length > 0) {
            serviceDeletionBtns.forEach(btn => {
                btn.onclick = function() {
                    _this.serviceDeletionBtn(btn);
                }
            })
        }

        // Service Save Button
        const serviceSaveBtns = modal.querySelectorAll('.modal__service-save-btn');
        if (serviceSaveBtns.length > 0) {
            serviceSaveBtns.forEach(btn => {
                btn.onclick = function() {
                    _this.serviceSaveBtn(btn);
                }
            })
        }

		// Save Button (sendata to update)
		const saveButtons = modal.querySelectorAll('.modal__service-promotion-save-btn');
		if (saveButtons.length > 0) {
			saveButtons.forEach(btn => 
				btn.onclick = () => this.sendDataToSave()
			);
		}

		// Delete button (sendata to delete)
		const deleteBtns = modal.querySelectorAll('.modal__service-promotion-delete-btn');
		if (deleteBtns.length > 0) {
			deleteBtns.forEach(btn => 
				btn.onclick = () => this.deleteData()
			);
		}
    },
    addNewService() {
        const tbodyTable = modal.querySelector('.modal__service-list-table tbody');
		const newRowData = document.createElement('tr');
		let newService;
		this.isChanged = true;

		if (this.serviceList.length > this.activeServiceDetail.length) {
			for (let i = 0; i < this.serviceList.length; i++) {
				newService = this.serviceList[i];
				const isExisted = this.activeServiceDetail.some(serviceDetail => serviceDetail.serviceId === newService.id);
				if (!isExisted) {
					this.activeServiceDetail.push({
						serviceId: newService.id,
						serviceName: newService.name,
						discount: 0,
					})
					break;
				}
			}
			
			// Turn on editting mode
			newRowData.classList.add('modal__service-list--editting');
			newRowData.innerHTML = `
				<td>
					<span class="modal__service-list-table-name" data-service-id="${newService.id}">${this.uppercaseFirstLetter(newService.name)}</span>
					<select class="modal__service-list-table-select">
						${this.renderServiceList()}
					</select>
				</td>
				<td>
					<span class="modal__service-list-table-date"></span>
					<input type="text" class="modal__service-list-table-input"/>
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
					<div class="modal__service-read-only-btns">
						<span class="pill-btn modal__service-uneditable-btn">Uneditable</span>
					</div>
				</td>
			`;
			const selectElement = newRowData.querySelector('.modal__service-list-table-select');
			selectElement.value = newService.id;
	
			if (tbodyTable) {
				tbodyTable.appendChild(newRowData);
				this.loadAllButton();
			}
		} else {
			this.errorMessage.innerText = 'You have reached the maximum of service list'
		}
		
    },
    serviceEditionBtn(btn) {
        const rowData = btn.closest('tr');

		// Save old Data
        const serviceName = rowData.querySelector('.modal__service-list-table-name');
		const serviceId = serviceName.dataset.serviceId;
		
		const discountPercent = rowData.querySelector('.modal__service-list-table-date').dataset.discount;

		// Display as editable data
        const selectElement = rowData.querySelector('.modal__service-list-table-select');
		selectElement.value = serviceId;
        
        const inputElement = rowData.querySelector('.modal__service-list-table-input');
        inputElement.value = discountPercent;

		// Turn on edit mode;
        rowData.classList.add('modal__service-list--editting');
		this.isChanged = true;
    },
    serviceDeletionBtn(btn) {
        const rowData = btn.closest('tr');
        const tbodyTable = $('.modal__service-list-table tbody');
        if (tbodyTable) {
			const serviceName = rowData.querySelector('.modal__service-list-table-name');
			const oldServiceId = parseInt(serviceName.dataset.serviceId);
			this.activeServiceDetail = this.activeServiceDetail.filter(serviceDetail => serviceDetail.serviceId !== oldServiceId);
            tbodyTable.removeChild(rowData);
        }
		this.errorMessage.innerText = '';
		this.isChanged = true;
    },
    serviceSaveBtn(btn) {
		const _this = this;
        const rowData = btn.closest('tr');
		let isValid = true;
		//Old Data
		const serviceName = rowData.querySelector('.modal__service-list-table-name');
		const oldServiceId = parseInt(serviceName.dataset.serviceId);
		const discountPercent = rowData.querySelector('.modal__service-list-table-date');

		const oldServiceDetail = this.activeServiceDetail.find(serviceDetail => serviceDetail.serviceId === oldServiceId);
		const oldIndex = this.activeServiceDetail.indexOf(oldServiceDetail);
        // New Data
        const selectElement = rowData.querySelector('.modal__service-list-table-select');
		const newServiceId = parseInt(selectElement.value);
		const inputElement = rowData.querySelector('.modal__service-list-table-input');
		const newDiscount = inputElement.value;

		if (inputElement.value.trim() == "") {
			isValid = false;
			inputElement.classList.add("error");
			this.errorMessage.innerText = "Enter discount percent before saving";
		}
		
		//Which service would you like to change
		if (newServiceId === oldServiceId) {
			discountPercent.innerText = newDiscount;
			discountPercent.dataset.discount = newDiscount;
			oldServiceDetail.discount = newDiscount;
		} else {
			let newServiceDetail = this.activeServiceDetail.find(serviceDetail => serviceDetail.serviceId === newServiceId);

			if (newServiceDetail) {
				this.errorMessage.innerText = "Duplicate applied service, please choose another service!";
				isValid = false;
			} else {
				newServiceDetail = {
					serviceId: newServiceId, 
					serviceName: _this.serviceList.find(service => service.id === newServiceId).name,
					discount: newDiscount
				}
				this.activeServiceDetail.splice(oldIndex, 1 , newServiceDetail);
				serviceName.innerText = _this.uppercaseFirstLetter(newServiceDetail.serviceName);
				serviceName.dataset.serviceId = newServiceDetail.serviceId;

				discountPercent.innerText = newDiscount;
				discountPercent.dataset.discount = newDiscount;
			}
		}
		
		// Turn off editting mod
		if (isValid) {
			this.errorMessage.innerText = "";
			rowData.classList.remove('modal__service-list--editting');
		}
    },
	sendDataToSave() {
		const promoId = modal.querySelector('input[name="servicePromoId"]').value;
		const promoIdInt = parseInt(promoId);

		const promoCodeEle = modal.querySelector('input[data-promo-code]');
		const promoDescEle = modal.querySelector('input[data-description]');

		// Validate empty input
		let isValid = this.validateInput(promoCodeEle) && this.validateInput(promoDescEle);


		// Check if have at least one row in editting mod
		const tbodyTable = modal.querySelector('.modal__service-list-table tbody');
		const allRow = tbodyTable.querySelectorAll('tr')
		if (allRow.length > 0) {
			const isExistedEditting = Array.from(allRow).some(row => row.classList.contains("modal__service-list--editting"));
			if (isExistedEditting) {
				isValid = false;
				this.errorMessage.innerText = "One of the applied service above still not save";
			}
		} else {
			isValid = false;
			this.errorMessage.innerText = "Add at least one new service";
		}

		if (isValid) {
			const data = {
				promoId: promoIdInt,
				promoCode: promoCodeEle.value.trim().toUpperCase(),
				promoDesc: promoDescEle.value.trim(),
				serviceDetails: this.activeServiceDetail,
			}
			
			if (this.isChanged) {
				fetch('/manager/promoCode/service/saveServicePromoCode',{
					method: 'POST',
					headers: {
						"Content-type": "application/json",
						"x-csrf-token": csrfToken,
					},
					body: JSON.stringify(data)
				})
				.then(() => {
					let duration = 300;
	
					this.modalSection.style.animation = `fadeOut ${duration / 1000}s forwards`;
					setTimeout(() => {
						modal.removeChild(this.modalSection);
						this.clickedViewBtn = null;
						this.serviceList = [];
						this.activeServiceDetail = [];
						this.errorMessage = null;
						this.modalSection = null;
	
						// location.href= "/manager/promoCode/service";
					}, duration);
				})
			} else {
				this.closeModal();
			}
			
		}
		
	},
	deleteData() {
		const promoId = modal.querySelector('input[name="servicePromoId"]').value;
		const promoIdInt = parseInt(promoId);

		const promoCodeSpan = modal.querySelector('.modal__service-promotion-desc.promoCode');
		this.closeModal();
		this.displayConfirmModal({
			promoId: promoIdInt,
			promoCode: promoCodeSpan.innerText.trim()
		})

	},
	displayConfirmModal: function({ promoId, promoCode }) {
		const _this = this;
        const modalSection = document.createElement('div');
        const duration = 300;
        this.disableWindowScroll();
        
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s linear`; 
        modalSection.innerHTML = this.renderHTML`
			<div class="modal-section-body">
				<div class="modal__confirm-delete warning-type">
					<div class="modal__confirm-delete-wrapper">
						<h3 class="modal__confirm-delete-heading">
							<i class="fas fa-exclamation-circle"></i>
						</h3>
						<div class="modal__confirm-delete-desc">Do you really want to delete promo code?</div>
						<div class="modal__confirm-delete-room">${promoCode}</div>
						<div class="modal__confirm-delete-btns">
							<div class="pill-btn green-type-btn modal__confirm-delete-yes-btn">Yes</div>
							<div class="pill-btn red-type-btn modal__confirm-delete-no-btn">No</div>
						</div>
					</div>
				</div>
			</div>
        `;
        modal.appendChild(modalSection);
        // Close modal
        function closeModal() {
            _this.enableWindowScroll();
            modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(modalSection), duration);
        }

        // No Button
        const noBtn = modal.querySelector('.modal__confirm-delete-no-btn');
        if (noBtn) {
            noBtn.onclick = closeModal;
        }

        const yesBtn = modal.querySelector('.modal__confirm-delete-yes-btn');
        if (yesBtn) {
            yesBtn.onclick = function() {
				fetch("/manager/promoCode/service/deleteServicePromoCode", {
					method: 'POST',
					headers: {
						"Content-type" : "application/json",
						"x-csrf-token": csrfToken
					},
					body: promoId
				})
				.then(() => {
					location.href = "/manager/promoCode/service"
				})
			}
        }
    },
    renderHTML: function([first,...strings], ...values) {
        return values.reduce(
            (acc, value) => [...acc, value, strings.shift()],
            [first]
        )
        .filter(x => x && x !== true || x === 0)
        .join('');
    },
	validateInput(input) {
		let isValid = true;

		if (input.value.trim() == "") {
			isValid = false;
			input.classList.add("error");
			this.errorMessage.innerText = "Please fulfill service promotion information"
		} else {
			isValid = true;
		}

		return isValid;

	},
	closeModal() {
        this.enableWindowScroll();
		let duration = 300;
		this.modalSection.style.animation = `fadeOut ${duration / 1000}s forwards`;
		setTimeout(() => {
			modal.removeChild(this.modalSection);

			this.clickedViewBtn = null;
			this.serviceList = [];
			this.activeServiceDetail = [];
			this.errorMessage = null;
			this.modalSection = null;
			
		}, duration)
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
    run(btn = null) {
		this.clickedViewBtn = btn;
        this.loadInitialization();
    }
}

