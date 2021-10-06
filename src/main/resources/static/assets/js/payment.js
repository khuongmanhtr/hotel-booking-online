var $ = document.querySelector.bind(document);

const bookingCode = $('.payment__noti-code').innerText.trim();
const copyBtn = $('.payment__noti-copy');

copyBtn.onclick = function() {
    navigator.clipboard.writeText(bookingCode);
    this.innerText = "Copied!";
}
