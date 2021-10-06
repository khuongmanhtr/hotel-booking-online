var $ = document.querySelector.bind(document);
var $$ = document.querySelectorAll.bind(document);

const roomHeadingName = $('.room__info-heading');

if (roomHeadingName) {
    const storedName = roomHeadingName.innerText;
    roomHeadingName.innerText = uppercaseFirstLetter(storedName);
}

function uppercaseFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}