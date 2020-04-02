const socket = io();
const $alert = document.querySelector('.alert-changes');

socket.on('config-changed', () => {
  $alert.classList.remove('d-none');
});