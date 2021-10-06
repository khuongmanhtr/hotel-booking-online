var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = document.getElementById('modal-segment');
const editBtns = $$('.management__room-type-edit-btn');
const deleteBtns = $$('.management__room-type-delete-btn');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const hiddenElement = $('.management__dashboard-search-hidden');
const csrfToken = $('input[data-csrf-token]').value;

const manageService = {
	allRoleList: [],
	currentRoles: [],
	errorMessage: null,
	modalSection: null,
	loadInitialization() {
		fetch("/admin/getAllRoles", {
			method: 'POST',
			headers: {
				"Content-type" : "application/json; charset=utf-8",
				"x-csrf-token" : csrfToken,
			},
		})
		.then(response => response.json())
		.then(json => this.allRoleList = json)
	},
    loadAddNewBtn() {
        const addNewBtn = $('.management__room-type-new');
        if (addNewBtn) {
            addNewBtn.onclick = () => { 
                this.loadAddNewModal({});
            }
        }
    },
    loadEditBtns() {
        editBtns.forEach(btn => {
            btn.onclick = () => {
				const rowData = btn.closest('tr');
				const username = rowData.querySelector('td:nth-child(1)').innerText;

				fetch("/admin/getUser", {
					method: 'POST',
					headers: {
						"Content-type" : "application/json; charset=utf-8",
						"x-csrf-token" : csrfToken,
					},
					body: username
				})
				.then(response => response.json())
				.then(json => {
					this.currentRoles = json.roles;
					this.loadEditModal(json);
				})
            } 
        })
    },
    loadDeleteBtns() {
        deleteBtns.forEach(btn => {
            btn.onclick = () => {
                const rowData = btn.closest('tr');
                const username = rowData.querySelector('td:nth-child(1)').innerText;
                this.loadConfirmModal(username);
            }
        })
    },
    loadAddNewModal() {
        const modalSection = document.createElement('div');
        const duration = 300;
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s ease-in`;

        modalSection.innerHTML = this.renderHTML`
            <div class="modal-section-body">
                <div class="modal__service-form">
                    <div class="modal__service-form-inputs">
                        <h3 class="modal__rooms-form-heading">user account information</h3>
                        <div class="grid-row modal__service-form-details">
                            <div class="grid-col grid-col-12">
                                <div class="modal__rooms-form-detail">
                                    <span class="modal__service-form-title">Username:</span>
                                    <input type="text" class="modal__service-form-input" data-username="1">
                                </div>
                            </div>
                            <div class="grid-col grid-col-12 ">
                                <div class="modal__rooms-form-detail password">
                                    <span class="modal__service-form-title">Password:</span>
                                    <input type="password" class="modal__service-form-input" data-password="1">
                                </div>
                            </div>
                        </div>
                    </div>
					<div class="modal__service-promotion-error"></div>
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
            modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
            setTimeout(() => modal.removeChild(modalSection), duration);
        }

		// error message
		const errorMessage = modal.querySelector('.modal__service-promotion-error');
		if (errorMessage) {
			this.errorMessage = errorMessage;
		}

		// input onfocus
		const inputElements = modal.querySelectorAll('.modal__service-form-input');
		if (inputElements.length > 0) {
			inputElements.forEach(inputElement =>{
				inputElement.onfocus = () => {
					this.errorMessage.innerText = "";
					inputElement.classList.remove('error');
				};
			})
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

		// save button 
		const saveBtn = modal.querySelector('.modal__rooms-form-save-btn');
		if (saveBtn) {
			saveBtn.onclick = () => {
				const usernameInput = modal.querySelector('input[data-username]');
				const passwordInput = modal.querySelector('input[data-password]');
				const isValid = this.validateInformation(usernameInput, passwordInput);

				if (isValid) {
					fetch("/admin/createAccount", {
						method: 'POST',
						headers: {
							"Content-type" : "application/json; charset=utf-8",
							"x-csrf-token" : csrfToken,
						},
						body: JSON.stringify({
							username: usernameInput.value.trim(),
							password: passwordInput.value.trim(),
						})
					})
					.then(() => {
						closeModal();
						setTimeout(() => location.href="/admin" , duration);
					})
					
				}
			}
		}
    },
	loadEditModal(data) {
        const modalSection = document.createElement('div');
        const duration = 300;
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s ease-in`;

        modalSection.innerHTML = this.renderHTML`
			<div class="modal-section-body">
				<div class="modal__account-form">
					<div class="modal__account-form-inputs">
						<h3 class="modal__rooms-form-heading">user role information</h3>
						<div class="grid-row modal__account-form-details">
							<div class="grid-col grid-col-12">
								<div class="modal__rooms-form-detail">
									<span class="modal__account-form-title">Username:</span>
									<input type="text" readonly class="modal__account-form-input" data-username="1" value="${data.username}">
								</div>
							</div>
							<div class="grid-col grid-col-12">
								<div class="modal__rooms-form-detail">
									<span class="modal__account-form-title">Enabled:</span>
									<select class="modal__account-form-select" data-enabled="1">
										<option value="1">active</option>
										<option value="0">inactive</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="modal__list-role-title">
						<h4 class="modal__list-role-heading">user role list</h4>
						<div class="pill-btn blue-type-btn modal__new-role-btn">
							<i class="fas fa-plus"></i>
							Add New Role
						</div>
					</div>
					<div class="modal__account-list">
						<table class="modal__account-list-table">
							<thead>
								<tr>
									<th>user role</th>
									<th>action</th>
								</tr>
							</thead>
							<tbody>
								${this.renderRoles(data.roles)}
							</tbody>
						</table>
					</div>
					<div class="modal__account-error-message"></div>
					<div class="modal__account-form-btns">
						<div class="pill-btn red-type-btn modal__account-form-save-btn">cancel</div>
						<div class="pill-btn green-type-btn modal__account-form-cancel-btn">save</div>
					</div>
					<div class="square-btn red-type-btn modal__account-close-btn">
						<i class="fas fa-times"></i>
					</div>
				</div>
			</div>
        `;

		this.modalSection = modalSection;
        modal.appendChild(modalSection);
		this.loadAllEditionBtnGroup(data);
		this.loadAllRoleButton();
		
    },
	loadAllEditionBtnGroup(data) {
		// error message
		const errorMessage = modal.querySelector('.modal__account-error-message');
		if (errorMessage) {
			this.errorMessage = errorMessage;
		}

		// input onfocus
		const inputElement = modal.querySelectorAll('.modal__account-form-input');
		if (inputElement) {
			inputElement.onfocus = () => {
				this.errorMessage.innerText = "";
				inputElement.classList.remove('error');
			};
		}

		// select element
		const statusSelection = modal.querySelector('.modal__account-form-select');
		if (statusSelection) {
			statusSelection.value = data.enabled ? 1 : 0;
		}

		// close button
		const closeBtn = modal.querySelector('.modal__account-close-btn');
		if (closeBtn) {
			closeBtn.onclick = () => this.closeModal();
		}

		// cancel button
		const cancelBtn = modal.querySelector('.modal__account-form-save-btn');
		if (cancelBtn) {
			cancelBtn.onclick = () => this.closeModal();
		}

		// add New role button
		const addNewBtn = modal.querySelector('.modal__new-role-btn');
		if (addNewBtn) {
			addNewBtn.onclick = () => this.addNewRole();
		}

		// save button 
		const saveBtn = modal.querySelector('.modal__account-form-cancel-btn');
		if (saveBtn) {
			saveBtn.onclick = () => {
				const tbodyTable = modal.querySelector('.modal__account-list-table tbody');
				const allEdittingRow = tbodyTable.querySelectorAll('.modal__account-list--editting')
				if (allEdittingRow.length > 0) {
					this.errorMessage.innerText = "One of the role above still not save"
				} else {
					const usernameInput = modal.querySelector('input[data-username]');
					const statusSelect = modal.querySelector('.modal__account-form-select');
					
					const data = {
						username: usernameInput.value.trim(),
						enabled: parseInt(statusSelect.value) ? true : false,
						roles: this.currentRoles
					}
					fetch("/admin/update", {
						method: 'POST',
						headers: {
							"Content-type" : "application/json; charset=utf-8",
							"x-csrf-token" : csrfToken,
						},
						body: JSON.stringify(data)
					})
					.then(() => {
						this.closeModal();
						let duration = 300;
						setTimeout(() => location.href="/admin" , duration);
					})
					
				}
			}
		}
	},
	addNewRole() {
		const tbodyTable = modal.querySelector('.modal__account-list-table tbody');
		const newRowData = document.createElement('tr');
		let newRole;

		if (this.allRoleList.length > this.currentRoles.length) {
			for (let i = 0; i < this.allRoleList.length; i++) {
				newRole = this.allRoleList[i];
				const isExisted = this.currentRoles.some(role => role === newRole);
				if (!isExisted) {
					this.currentRoles.push(newRole);
					break;
				}
			}

			newRowData.classList.add('modal__account-list--editting');
			newRowData.innerHTML = `
				<td>
					<span class="modal__account-user-role">${newRole}</span>
					<select class="modal__account-role-selection">
						${this.renderRoleOptions()}
					</select>
				</td>
				<td>
					<div class="modal__account-edit-btns">
						<span class="pill-btn blue-type-btn modal__account-edit-btn">Edit</span>
						<span class="pill-btn red-type-btn modal__account-delete-btn">Delete</span>
					</div>
					<div class="modal__account-save-btns">
						<span class="pill-btn green-type-btn modal__account-save-btn">Save</span>
						<span class="pill-btn red-type-btn modal__account-delete-btn">Delete</span>
					</div>
				</td>
			`;
			const selectElement = newRowData.querySelector('.modal__account-role-selection');
			selectElement.value = newRole;

			tbodyTable.appendChild(newRowData);
			this.loadAllRoleButton();

		} else {
			this.errorMessage.innerText = 'You have reached the maximum of role list'
		}
	},
	loadAllRoleButton() {
		// All Role Selection
		const selectElements = modal.querySelectorAll('.modal__account-role-selection');
		if (selectElements.length > 0) {
			selectElements.forEach(element => {
				element.onchange = () => {
					this.errorMessage.innerText = "";
				}
			})
		}

		// Edit Btns
		const editBtns = modal.querySelectorAll('.modal__account-edit-btn');
		if (editBtns.length > 0) {
			editBtns.forEach(btn => {
				btn.onclick = () => {
					this.errorMessage.innerText = "";
					this.editRole(btn)
				};
			})
		}

		// Save Btns
		const saveBtns = modal.querySelectorAll('.modal__account-save-btn');
		if (saveBtns.length > 0) {
			saveBtns.forEach(btn => {
				btn.onclick = () => {
					this.errorMessage.innerText = "";
					this.saveRole(btn)
				};
			})
		}

		// Delete Btns
		const deleteBtns = modal.querySelectorAll('.modal__account-delete-btn');
		if (deleteBtns.length > 0) {
			deleteBtns.forEach(btn => {
				btn.onclick = () => {
					this.errorMessage.innerText = "";
					this.deleteRole(btn)
				};
			})
		}
	},
	editRole(btn) {
		const rowData = btn.closest('tr');
		const roleSpanEle = rowData.querySelector('.modal__account-user-role');
		const roleSelectEle = rowData.querySelector('.modal__account-role-selection');

		roleSelectEle.value = roleSpanEle.innerText.trim();

		rowData.classList.add("modal__account-list--editting");
	},
	saveRole(btn) {
		let isValid = true;
		const rowData = btn.closest('tr');
		const roleSpanEle = rowData.querySelector('.modal__account-user-role');
		const oldRole = roleSpanEle.innerText.trim();

		const roleSelectEle = rowData.querySelector('.modal__account-role-selection');
		const newRole = roleSelectEle.value;

		if (oldRole !== newRole) {
			const isExisted = this.currentRoles.find(role => role === newRole);
			if (isExisted) {
				this.errorMessage.innerText = "Duplicate user role, choose another role!";
				isValid = false;
			} else {
				const oldIndex = this.currentRoles.indexOf(oldRole);
				this.currentRoles.splice(oldIndex, 1, newRole);
				roleSpanEle.innerText = newRole;
			}
		}
		
		if (isValid) {
			rowData.classList.remove("modal__account-list--editting");
		}
		
	},
	deleteRole(btn) {
		const rowData = btn.closest('tr');
		const roleSpanEle = rowData.querySelector('.modal__account-user-role');
		const oldRole = roleSpanEle.innerText.trim();
		this.currentRoles.splice(this.currentRoles.indexOf(oldRole), 1);
	
		const tbodyTable = modal.querySelector('.modal__account-list-table tbody');
		tbodyTable.removeChild(rowData);

	},
	closeModal() {
		let duration = 300;
		this.modalSection.style.animation = `fadeOut ${duration / 1000}s linear forwards`;
		setTimeout(() => {
			modal.removeChild(this.modalSection);

			this.currentRoles = [];
			this.errorMessage = null;
			this.modalSection = null;
		}, duration);
	},
	renderRoles(roles) {
		if(roles.length > 0) {
			// edit mode modal__account-list--editting
			return roles.map(role => `
				<tr>
					<td>
						<span class="modal__account-user-role">${role}</span>
						<select class="modal__account-role-selection">
							${this.renderRoleOptions()}
						</select>
					</td>
					<td>
						<div class="modal__account-edit-btns">
							<span class="pill-btn blue-type-btn modal__account-edit-btn">Edit</span>
							<span class="pill-btn red-type-btn modal__account-delete-btn">Delete</span>
						</div>
						<div class="modal__account-save-btns">
							<span class="pill-btn green-type-btn modal__account-save-btn">Save</span>
							<span class="pill-btn red-type-btn modal__account-delete-btn">Delete</span>
						</div>
					</td>
				</tr>
			`).join('')
		}
	},
	renderRoleOptions() {
		if (this.allRoleList.length > 0) {
			return this.allRoleList.map(role => `
				<option value="${role}">${role}</option>
			`).join('')
		}
	},
    loadConfirmModal(username) {
        const modalSection = document.createElement('div');
        const duration = 300;
        
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `fadeIn ${duration / 1000}s linear`; 
        modalSection.innerHTML = this.renderHTML`
			<div class="modal-section-body">
				<div class="modal__confirm-delete warning-type">
					<div class="modal__confirm-delete-wrapper">
						<h3 class="modal__confirm-delete-heading">
							<i class="fas fa-exclamation-circle"></i>
						</h3>
						<div class="modal__confirm-delete-desc">Do you really want to delete this account?</div>
						<div class="modal__confirm-delete-room">${username}</div>
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
            yesBtn.onclick = () => {
				fetch("/admin/delete", {
					method: 'POST',
					headers: {
						"Content-type" : "application/json; charset=utf-8",
						"x-csrf-token" : csrfToken,
					},
					body: username
				})
				.then(() => {
					closeModal();
					setTimeout(() => location.href="/admin" , duration);
				})
			}
        }
    },
	validateInformation(inpusernameInput, passwordInputut) {
		let isValid = true;
		const username = inpusernameInput.value.trim();
		const password = passwordInputut.value.trim();

		if (!this.checkNotEmpty(username)) {
			isValid = false;
			inpusernameInput.classList.add("error");
			this.errorMessage.innerText = "Please fulfill all information";
			return isValid;
		} else if (!this.checkNotEmpty(password)) {
			isValid = false;
			passwordInputut.classList.add("error");
			this.errorMessage.innerText = "Please fulfill all information";
			return isValid;
		}

		if (!this.checkValidUsername(username)) {
			isValid = false;
			inpusernameInput.classList.add("error");
			this.errorMessage.innerText = "Your username is not valid";
			return isValid;
		} else if (!this.checkValidPassword(password)) {
			isValid = false;
			passwordInputut.classList.add("error");
			this.errorMessage.innerText = "Your password is not valid";
			return isValid;
		}
			
		return isValid;

	},
	checkNotEmpty(password) {
        const isNotEmpty = password !== "" ? true : false;
        return isNotEmpty;
    },
	checkMinMax(password, min, max) {
        const len = password.length;
        const isValid = len > min && len < max ? true: false;
        return isValid;
    },
	checkValidUsername(username) {
        // const regExp = /^(?=.*\d{2})(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{3,}$/
        // const regExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{3,}$/
        const regExp = /^[a-zA-Z0-9]{1,}$/
        const isValid = username.match(regExp) ?  true : false;
        return isValid;
    },
	checkValidPassword(password) {
        // const regExp = /^(?=.*\d{2})(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{3,}$/
        // const regExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{3,}$/
        const regExp = /^[A-Za-z\d@$!%*#?&]{0,}$/
        const isValid = password.match(regExp) ?  true : false;
        return isValid;
    },
    renderHTML: function([first,...strings], ...values) {
        return values.reduce(
            (acc, value) => [...acc, value, strings.shift()]
            ,[first]
        )
        .filter(x => x && x !== true || x === 0)
        .join('')
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
		this.loadInitialization();
		this.loadSelectElement();
        this.loadSelectOnchange();
        this.loadAddNewBtn();
        this.loadEditBtns();
        this.loadDeleteBtns();
    }
}
manageService.run();
