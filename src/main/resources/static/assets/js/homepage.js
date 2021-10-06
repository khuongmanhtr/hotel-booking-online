var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const dateFromInput = $('#check-date-from');
const dateToInput = $('#check-date-to');
const periodDate = 1; //Amount of day between check in and check out
const dayMillisecond = periodDate*24*60*60*1000;
const roomTypeNames = $$('.room__details-item-title');

const homePageEvent = {
    loadDateInputs() {
        // Input Date event
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
    splitDate(dateString) {
        return dateString.replace(/^(\w+)-(\d+)/, '$2-$1');// convert dd-MM-yyyy to MM-dd-yyyy
    },
    formatDateTypeOne(date) {
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
    formatDateTypeTwo(date) {
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
    uppercaseRoomTypes() {
        const _this = this;
        if (roomTypeNames.length > 0) {
            roomTypeNames.forEach(name => {
                const storedName = name.innerText;
                name.innerText = _this.uppercaseFirstLetter(storedName);
            })
        }
    },
    uppercaseFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    run() {
        this.loadDateInputs();
        this.uppercaseRoomTypes();
    }
}
homePageEvent.run();




