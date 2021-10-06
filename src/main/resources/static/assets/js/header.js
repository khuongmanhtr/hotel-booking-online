var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const bookingCodeForm = $('.navbar__part-booking-form');
const bookingCodeElement = bookingCodeForm.closest('.navbar__part-nav-item');
const bookingCodeInput = $('.navbar__part-booking-input');
const username = $('.navbar__part-user-name');
const roomTypeLinkNames = $$('.navbar__part-room-link');
const checkCodeForm = $('.navbar__part-booking-form');

const headerEvents = {
    loadFormInput() {
        checkCodeForm.onsubmit = function(e) {
            e.preventDefault();
            const input = checkCodeForm.querySelector('.navbar__part-booking-input');
            if (input.value.trim() !== "") {
                this.submit();
            }
        }
    },
    loadCheckCodeInput() {
        bookingCodeInput.onblur = function (e) {
            e.stopPropagation();
            bookingCodeElement.classList.add('display-form');
        }
        
        bookingCodeElement.onclick = function (e) {
            e.stopPropagation();
            this.classList.toggle('display-form');
        }
        
        bookingCodeForm.onclick = function (e) {
            e.stopPropagation();
        }

		bookingCodeForm.onmousedown = function (e) {
			e.stopPropagation();
			this.classList.add("drag");
		}
		
		bookingCodeForm.onmouseup = function (e) {
			e.stopPropagation();
			this.classList.remove("drag");
		}
		
		bookingCodeForm.ondragover = function (e) {
			e.stopPropagation();
			this.classList.remove("drag");
		}

        window.onclick = function () {
            bookingCodeElement.classList.remove('display-form');
        }
    },
    uppercaseRoomTypes() {
		const _this = this;
        if (roomTypeLinkNames.length > 0) {
            roomTypeLinkNames.forEach(name => {
                const storedName = name.innerText;
                name.innerText = _this.uppercaseFirstLetter(storedName);
            })
        }
    },
    // uppercaseUsername() {
	// 	const _this = this;
    //     if (username) {
    //         const storedName = username.innerText;
    //         username.innerText = _this.uppercaseFirstLetter(storedName);
    //     }
    // },
    uppercaseFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },
    run() {
		this.loadFormInput();
        this.loadCheckCodeInput();
        this.uppercaseRoomTypes();
        // this.uppercaseUsername();
    }
}
headerEvents.run();







