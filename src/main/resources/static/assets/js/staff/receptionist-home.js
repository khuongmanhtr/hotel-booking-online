var $ = document.querySelector.bind(document);

const selectElement = $('.management__dashboard-search-option');
const inputRow = $('.management__dashboard-search-form.input-row');
const inputElement = $('.management__dashboard-search-input');

var activeRoom = {
	loadSelectElement() {
		const option = selectElement.value;
		if (option.toUpperCase() == "all".toUpperCase()) {
			inputRow.style.visibility = "hidden";
		} else {
			inputRow.style.visibility = "visible";
		}
	},
	loadSelectOnchange() {
		const _this = this;
		if (selectElement) {
			selectElement.onchange = function() {
				inputElement.value = "";
				_this.loadSelectElement();
			}
		}
	},
	run() {
		this.loadSelectElement();
		this.loadSelectOnchange();
	}
}
activeRoom.run();