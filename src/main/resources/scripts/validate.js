import * as CryptoJS from 'https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js';

// Import the crypto-js library

function isEmail(email) {
    return /^\w+@\w+\.\w+$/.test(email);
}

function isPhone(phone) {
    return /^\d{3}-\d{3}-\d{4}$/.test(phone) || /^\d{10,11}$/.test(phone) || /^\+?86\d{11}$/.test(phone);
}

function isUsername(username) {
    return /^[a-zA-Z][a-zA-Z0-9_.-]{5,19}$/.test(username);
}

function isName(name) {
    return /^[\p{L}\s-']+$/.test(name);
}

