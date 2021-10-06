var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const fileInput = $('.management__room-type-file');
const form = $('.management__room-type-form');
const inputs = $$('.management__room-type-input');
const errorMessage = $('.management__room-type-error');


const createRoomType = {
    loadInitializationEvent() {
        if (fileInput) {
            fileInput.onchange = () => {
                const img = $('.management__room-type-image');
                if (img) {
                    img.style.display = 'none';
                }
            }
        }

        if(inputs.length > 0) {
            inputs.forEach(input => {
                input.onfocus = () => {
                    input.classList.remove('error');
                    errorMessage.innerText = "";
                }
            })
        }
    },
    checkFormBeforeSubmit() {
        form.onsubmit = (e) => {
            e.preventDefault();

            const isValid = Array.from(inputs).every(input => this.validateInput(input));
            console.log(isValid);
            if (isValid) {
                form.submit();
            }
            
        }
    },
    validateInput(input) {
		let isValid = true;

		if (input.value.trim() == "") {
			isValid = false;
			input.classList.add("error");
            errorMessage.innerText = "Please fulfill all information";
		} else {
			isValid = true;
		}

		return isValid;

	},
    run() {
        this.loadInitializationEvent();
        this.checkFormBeforeSubmit();
    }
}
createRoomType.run();

