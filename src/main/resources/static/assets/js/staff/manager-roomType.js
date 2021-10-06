var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const hiddenElement = $('.management__dashboard-search-hidden');
const roomTypeTable = $('.management__room-type-table');
const allRoomType = roomTypeTable.querySelectorAll('tbody td:nth-child(1)');
const deleteBtns = $$('.management__room-type-delete-btn');
const editBtns = $$('.management__room-type-edit-btn');
const csrfToken = $('input[data-csrf-token]').value;

var manageRoomType = {
    loadDeleteBtns() {
        const _this = this;
        deleteBtns.forEach(btn => {
            btn.onclick = () => {
                const rowData = btn.closest('tr');
                const roomTypeName = rowData.querySelector('td:nth-child(1)').innerHTML;
				const roomTypeId = rowData.querySelector('.management__room-type-id').value.trim();

				_this.loadConfirmModal({
					rowData: rowData,
					roomTypeId: roomTypeId,
					roomTypeName: roomTypeName
				});
            }
        })
    },
	loadEditBtns() {
		const _this = this;
		if (editBtns.length > 0) {
			editBtns.forEach(editBtn => {
				editBtn.onclick = function() {
					const rowData = editBtn.closest('tr');
					const roomTypeId = rowData.querySelector('.management__room-type-id').value.trim();
					
					const form = document.createElement('form');
					form.action = '/manager/room/createRoomType';
					form.method = 'POST';
					form.style.display = 'none';

					const csrfField = document.createElement('input');
					csrfField.type = 'hidden';
					csrfField.name = '_csrf'
					csrfField.value = csrfToken;
					form.appendChild(csrfField);

					const input = document.createElement('input');
					input.type = 'hidden';
					input.name = "roomTypeId";
					input.value = roomTypeId;
					form.appendChild(input);
	
					document.body.appendChild(form);
					form.submit();
				}
			})
		}
	},
    loadConfirmModal({rowData, roomTypeId, roomTypeName}) {
		const _this = this
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
                        <div class="modal__confirm-delete-room">${roomTypeName}</div>
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
				_this.deleteData(roomTypeId)
				.then(response => response.json())
				.then(responseData => {
					if (responseData) {
						const tbody = roomTypeTable.querySelector('tbody');
						tbody.removeChild(rowData);
						closeModal();
					} else {
						location.href = "/manager/room/allRoomType"
					}
				})
			}
        }
    },
	deleteData(id) {
		return fetch("/manager/room/allRoomType/delete", {
			method: 'POST',
			headers: {
				"content-type" : "application/json",
				"x-csrf-token" : csrfToken
			},
			body: id
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
	uppercaseAllRoomType() {
		const _this = this;
		if(allRoomType.length > 0) {
			allRoomType.forEach(roomType => {
				roomType.innerText = _this.uppercaseFirstLetter(roomType.innerText.trim());
			})
		}
	},
	uppercaseFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    run: function() {
		this.loadSelectElement();
		this.loadSelectOnchange();
        this.loadDeleteBtns();
		this.loadEditBtns();
		this.uppercaseAllRoomType();
    }
}
manageRoomType.run();
