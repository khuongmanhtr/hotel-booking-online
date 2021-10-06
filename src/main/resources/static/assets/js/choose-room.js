var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const dateFromInput = $('.date__form-item-from');
const dateToInput = $('.date__form-item-to');
const periodDate = 1; //Amount of day between check in and check out
const roomList = $$(".result__list-item");
const dayMillisecond = periodDate*24*60*60*1000;
const roomTypeNames = $$('.result__item-info-title');
const nextBtn = $('.search__next-btn');
const messageWarning = $('.search__warning');

const chooseRoomApp = {
    loadCheckBoxPerRoom: function() {
        roomList.forEach(room => {
            room.onclick = function(e) {
                messageWarning.innerText = "";
                const checkboxRoom = this.querySelector(".result__item-checkbox-input");
                checkboxRoom.checked = !checkboxRoom.checked;
                if (checkboxRoom.checked) {
                    this.classList.add('selected');
                } else {
                    this.classList.remove('selected');
                }
            }
        })
    },
    loadDateInputs: function() {
        const _this = this;
        dateFromInput.onchange = function () {
            let fromDate = new Date(dateFromInput.value);
            let toDate;
            if (!dateToInput.value) {
                toDate = new Date(dateFromInput.value);
            } else {
                toDate = new Date(dateToInput.value);
            }
        
            if (toDate - fromDate < dayMillisecond) {
                toDate.setDate(fromDate.getDate() + periodDate);
                dateFromInput.value = _this.formatDateTypeOne(fromDate);
                dateToInput.value = _this.formatDateTypeOne(toDate);
            }
        }
        
        dateToInput.onchange = function () {
            let toDate = new Date(dateToInput.value);
            let minDate = new Date(dateToInput.min);
            let fromDate;
        
            if (toDate - minDate < dayMillisecond) {
                toDate.setDate(minDate.getDate() + periodDate);
                dateToInput.value = _this.formatDateTypeOne(toDate);
            }
        
            if (!dateFromInput.value) {
                fromDate = new Date(dateToInput.value)
            } else {
                fromDate = new Date(dateFromInput.value)
            }
        
            if (toDate - fromDate < dayMillisecond) {
                fromDate.setDate(toDate.getDate() - periodDate);
                dateFromInput.value = _this.formatDateTypeOne(fromDate);
                dateToInput.value = _this.formatDateTypeOne(toDate);
            }  
        }
    },
    loadNextBtn: function() {
		if (nextBtn) {
			nextBtn.onclick = function(e) {
				e.preventDefault();
				const checkedCheckoxes = $$('.result__item-checkbox-input:checked');
				if (checkedCheckoxes.length == 0) {
					messageWarning.innerText = "Let choose at least one room type!";
				} else {
					const form = $('.search-result form');
					form.submit();
				}
			}
		}
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
    uppercaseRoomTypes: function() {
        const _this = this;
        if (roomTypeNames.length > 0) {
            roomTypeNames.forEach(name => {
                const storedName = name.innerText;
                name.innerText = _this.uppercaseFirstLetter(storedName);
            })
        }
    },
    uppercaseFirstLetter: function(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    splitDate: function(dateString) {
        return dateString.replace(/^(\w+)-(\d+)/, '$2-$1');// convert dd-MM-yyyy to MM-dd-yyyy
    },
    run: function() {
        this.loadDateInputs();
        this.loadCheckBoxPerRoom();
        this.loadNextBtn();
        this.uppercaseRoomTypes();
    }
}
chooseRoomApp.run();







