var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const modal = document.getElementById('modal-segment');
const confirmBtn = $('.management__change-password-confirm-btn');
const errorMessage = $('.management__change-password-error');
const csrfToken = $('input[data-csrf-token]').value;

const manageService = {
    beforeSubmission: function() {
		if (confirmBtn) {
			confirmBtn.onclick = () => {
				const username = $('input[data-username]').value;
				const password = $('input[data-password-type="password"]').value;
				const retypePassword = $('input[data-password-type="retype"]').value;
	
				if (this.validatePassword(password, retypePassword)) {
					errorMessage.innerText = "";
					fetch("/changePassword",{
						method: 'POST',
						headers: {
							"Content-type": "application/json; charset= utf-8",
							"x-csrf-token": csrfToken,
						},
						body: JSON.stringify({
							username: username,
							password: password,
							retype: retypePassword,
						})
					})
					.then(response => response.json())
					.then(json => {
						if (json) {
							this.loadModal();
						}
					})
				}
				
			}
		}
        
    },
    validatePassword(password1, password2) {
        let isValid = true;

        if (this.checkNotEmpty(password1) && this.checkNotEmpty(password2)) {
            isValid = true;
        } else {
            errorMessage.innerText = "Please fulfill your both password";
            return !isValid;
        }


        if (this.checkMatchedPassword(password1,password2)) {
            isValid = true;
        } else {
            errorMessage.innerText = "Your password and retype password do not match";
            return !isValid;
        }

        const min = 8;
        const max = 20;
        if (this.checkMinMax(password1, min, max)) {
            isValid = true;
        } else {
            errorMessage.innerText = `Your password must contain ${min} to ${max} characters`;
            return !isValid;
        }

        if (this.checkValidPassword(password1)) {
            isValid = true;
        } else {
            errorMessage.innerText = "Your password is not valid, please try again !";
            return !isValid;
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
    checkMatchedPassword(password1, password2) {
        const isMatched = password1 === password2 ? true: false;
        return isMatched;
    },
    checkValidPassword(password) {
        // const regExp = /^(?=.*\d{2})(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{3,}$/
        // const regExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{3,}$/
        const regExp = /^[A-Za-z\d@$!%*#?&]{0,}$/
        const isValid = password.match(regExp) ?  true : false;
        return isValid;
    },
    loadModal() {
        const modalSection = document.createElement('div');
        const duration = 300;
        
        modalSection.classList.add('modal-section');
        modalSection.style.animation = `
            fadeIn ${duration / 1000}s linear
        `; 
		console.log(1);
        modalSection.innerHTML = `
            <div class="modal-section-body">
                <div class="modal__confirm-delete success-type">
                    <div class="modal__confirm-delete-wrapper">
                        <h3 class="modal__confirm-delete-heading">
                            <i class="fas fa-check-circle"></i>
                        </h3>
                        <div class="modal__confirm-delete-desc">You have changed you password successfully</div>
						<a href="/" class="pill-btn green-type-btn modal__confirm-home-link">OK</a>
                    </div>
                </div>
            </div>
        `;
		console.log(2);
        modal.appendChild(modalSection);
    },
    run() {
        this.beforeSubmission();
    }
}
manageService.run();
