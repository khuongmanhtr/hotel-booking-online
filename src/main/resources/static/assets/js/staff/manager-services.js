var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = document.getElementById('modal-segment');
const editBtns = $$('.management__room-type-edit-btn');
const deleteBtns = $$('.management__room-type-delete-btn');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const hiddenElement = $('.management__dashboard-search-hidden');
const csrfToken = $('input[data-csrf-token]').value;

var manageService = {
    distanceY: 0,
    loadAddNewBtn: function() {
        const _this = this;
        const addNewBtn = $('.management__room-type-new');
        if (addNewBtn) {
            addNewBtn.onclick = function() {
                _this.loadFormModal({
					serviceId: 0,
                    serviceName: null, 
                    price: null, 
                    description: null,
                });
            }
        }
    },
    loadEditBtns: function () {
        const _this = this;
        editBtns.forEach(btn => {
            btn.onclick = function() {
				const rowData = btn.closest('tr');
				const serviceId = rowData.querySelector('input[name="serviceId"]').value.trim();
				const serviceName = rowData.querySelector('td:nth-child(1)').innerText.trim();
				const price = rowData.querySelector('td:nth-child(2)').innerText.trim();
				const description = rowData.querySelector('td:nth-child(3)').innerText.trim();

                _this.loadFormModal({
					serviceId: serviceId,
                    serviceName: serviceName, 
                    price: price, 
                    description: description,
                });
            } 
        })
    },
    loadDeleteBtns: function() {
        const _this = this;
        deleteBtns.forEach(btn => {
            btn.onclick = function() {
                const rowData = btn.closest('tr');
                const serviceId = rowData.querySelector('input[name="serviceId"]').value;
				const serviceName = rowData.querySelector('td:nth-child(1)').innerText.trim();

                _this.loadConfirmModal({
					serviceId: serviceId,
					serviceName: serviceName
				});
            }
        })
    },
    loadFormModal: function({serviceId, serviceName , price, description }) {
		const _this = this;
        this.disableWindowScroll();
        const modalSection = document.createElement('div');
        const duration = 300;
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s ease-in`;

        modalSection.innerHTML = `
            <div class="modal-section-body">
                <div class="modal__service-form">
                    <div class="modal__service-form-inputs">
                        <h3 class="modal__rooms-form-heading">service information</h3>
                        <div class="grid-row modal__service-form-details">
							<input type="hidden" name="serviceId" data-service-id="1" value="${serviceId}">
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__service-form-title">Service name:</span>
                                    <input type="text" class="modal__service-form-input" data-service-name="1" value="${serviceName == null ? "" : serviceName}">
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__service-form-title">Price:</span>
                                    <input type="text" class="modal__service-form-input" data-service-price="1" value="${price == null ? "" : parseFloat(price)}">
                                </div>
                            </div>
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__service-form-title">Description:</span>
                                    <input type="text" class="modal__service-form-input" data-service-description="1" value="${description == null ? "" : description}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal__service-form-edit-btns">
                        <div class="pill-btn red-type-btn modal__rooms-form-cancel-btn">cancel</div>
                        <div class="pill-btn green-type-btn modal__rooms-form-save-btn">save</div>
                    </div>
                    <div class="square-btn red-type-btn modal__rooms-close-btn">
                        <i class="fas fa-times"></i>
                    </div>
                </div>
            </div>
        `;

        modal.appendChild(modalSection);

        //close modal
        function closeModal() {
            _this.enableWindowScroll();
            modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(modalSection), duration);
        }
        // close button
        const closeBtn = modal.querySelector('.modal__rooms-close-btn');
        if (closeBtn) {
            closeBtn.onclick = closeModal;
        }

        // cancel button
        const cancelBtn = modal.querySelector('.modal__rooms-form-cancel-btn');
        if (cancelBtn) {
            cancelBtn.onclick = closeModal;
        }

		const inputs = modal.querySelectorAll('.modal__service-form-input');
		if (inputs.length > 0) {
			inputs.forEach(input => {
				input.onfocus = () => input.classList.remove("error");
			})
		}

		// save button
		const saveBtn = modal.querySelector('.modal__rooms-form-save-btn');
		if (saveBtn) {
			saveBtn.onclick = function () {
				const serviceIdEle = modal.querySelector('input[data-service-id]');
				const serviceId = parseInt(serviceIdEle.value.trim());
				const serviceNameEle = modal.querySelector('input[data-service-name]');
				const priceEle = modal.querySelector('input[data-service-price]');
				const descriptionEle = modal.querySelector('input[data-service-description]');
				const isValidated = _this.validateInput(serviceNameEle) && _this.validateInput(priceEle) && _this.validateInput(descriptionEle);

				const data = {
					id: serviceId,
					name: serviceNameEle.value.trim(),
					price: priceEle.value.trim(),
					description: descriptionEle.value.trim()
				}

				if (isValidated) {
					_this.sendServiceToSave(data)
					.then(() => {
						location.href = "/manager/service/"
					})
				}
			};
		}
    },
	sendServiceToSave(data) {
		return fetch('/manager/service/saveService', {
			method: 'POST',
			headers: {
				"Content-type" : "application/json; charset=utf-8",
				"x-csrf-token" : csrfToken,
			},
			body: JSON.stringify(data)
		})
	},
	sendServiceToDelete(id) {
		return fetch('/manager/service/deleteService', {
			method: 'POST',
			headers: {
				"Content-type" : "application/json; charset=utf-8",
				"x-csrf-token" : csrfToken,
			},
			body: id
		})
	},
	validateInput(input) {
		let isValid = true;

		if (input.value.trim() == "") {
			isValid = false;
			input.classList.add("error");
		} else {
			isValid = true;
		}

		return isValid;

	},
    loadConfirmModal: function({serviceId, serviceName}) {
		const _this = this;
        this.disableWindowScroll();
        const modalSection = document.createElement('div');
        const duration = 300;
        
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s linear`; 
        modalSection.innerHTML = `
			<div class="modal-section-body">
				<div class="modal__confirm-delete warning-type">
					<div class="modal__confirm-delete-wrapper">
						<h3 class="modal__confirm-delete-heading">
							<i class="fas fa-exclamation-circle"></i>
						</h3>
						<div class="modal__confirm-delete-desc">Do you really want to delete room type?</div>
						<div class="modal__confirm-delete-room">${serviceName}</div>
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
				_this.sendServiceToDelete(serviceId)
				.then(() => {
					location.href = "/manager/service/"
				})
			}
        }
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
    run: function() {
		this.loadSelectElement();
        this.loadSelectOnchange();
        this.loadAddNewBtn();
        this.loadEditBtns();
        this.loadDeleteBtns();
    }
}
manageService.run();
