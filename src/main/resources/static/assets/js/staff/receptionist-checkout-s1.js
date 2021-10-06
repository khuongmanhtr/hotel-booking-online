var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = $('#modal-segment');
const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const hiddenElement = $('.management__dashboard-search-hidden');
const checkInBtns = $$('.management__bookingcode-table-view-detail');
const csrfToken = $('input[data-csrf-token]').value;

const checkInApp = {
    loadCheckInBtns() {
        const _this = this;
        checkInBtns.forEach(btn => {
            btn.onclick = function() {
                const rowData = this.closest('tr');
                const bookingCode = rowData.querySelector('td:nth-child(1)').innerText.trim();
                _this.createFormToSubmit(bookingCode);
            }
        })
    },
    createFormToSubmit(bookingCode) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/staff/checkOut/confirm';

        const input = document.createElement('input');
        input.type = 'text';
        input.name = 'bookingCode';
        input.value = bookingCode;
        form.appendChild(input);

        const hiddenField = document.createElement('input');
        hiddenField.type = 'hidden';
        hiddenField.name = "_csrf";
        hiddenField.value = csrfToken;
        form.appendChild(hiddenField);

        document.body.appendChild(form);
        form.submit();
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
        } else if (option.toUpperCase() == "bookFrom".toUpperCase() || option.toUpperCase() == "bookTo".toUpperCase() ||
            option.toUpperCase() == "bookDate".toUpperCase())  {
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
        this.loadCheckInBtns();
    }
}
checkInApp.run();