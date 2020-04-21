const socket = io();
const $alert = document.querySelector('.alert-changes');

socket.on('user-config-changed', () => {
  $alert.classList.remove('d-none');
});

const $locationAutoCheckbox = document.querySelector('#location-auto-checkbox');
const $locationCountry = document.querySelector('#location-country');
const $locationCity = document.querySelector('#location-city');
const $locationCoords = document.querySelector('#location-coords');
const $locationSubmit = document.querySelector('#location-submit');
const $locationError = document.querySelector('#location-error');

const $sunriseTime = document.querySelector('#sunrise-time');
const $sunsetTime = document.querySelector('#sunset-time');


const $lampEnabled = document.querySelector('#lamp-enabled');
const $lampMode = document.querySelector('#lamp-mode');
const $lampBrightness = document.querySelector('#lamp-brightness');
const $lampColor = document.querySelector('#lamp-color');
const $lampTimeline = document.querySelector('#lamp-timeline');
const $lampSunrise = document.querySelector('#lamp-sunrise');
const $lampSunset = document.querySelector('#lamp-sunset');
const $lampMonitorStart = document.querySelector('#lamp-monitor-start');
const $lampMonitorStop = document.querySelector('#lamp-monitor-stop');

const disable = $el => $el.disabled = true;
const enable = $el => $el.disabled = false;


socket.on('set-config-data', ({country, city, lat, lon, sunriseTime, sunsetTime}) => {
  $locationCountry.value = country;
  $locationCity.value = city;
  $locationCoords.value = `${lat}, ${lon}`;
  console.log(sunriseTime);
  $sunriseTime.innerText = sunriseTime;
  $sunsetTime.innerText = sunsetTime;
});

socket.on('set-lamp-config', ({enabled, mode, brightness, color, sunriseTime, sunsetTime, monitorStartTime, monitorStopTime}) => {
  $lampEnabled.innerText = enabled ? 'włączona' : 'wyłączona';
  if (enabled) {
    $lampMode.innerText = mode === 'AUTO' ? 'automatyczny' : 'manualny';
    $lampBrightness.innerText = brightness;
    $lampColor.innerText = `rgb(${color.r}, ${color.g}, ${color.b})`;
  }
  $lampTimeline.style.display = mode === 'AUTO' ? 'flex' : 'none';
  
  if (mode === 'MANUAL') {
    return;
  }

  $lampSunrise.innerText = sunriseTime;
  $lampSunset.innerText = sunsetTime;
  $lampMonitorStart.innerText = monitorStartTime;
  $lampMonitorStop.innerText = monitorStopTime;
});

$locationAutoCheckbox.addEventListener('change', () => {
  const isChecked = $locationAutoCheckbox.checked;

  if (isChecked) {
    $locationSubmit.style.display = 'none';

    socket.emit('auto-location');

    disable($locationCountry);
    disable($locationCity);
  } else {
    socket.emit('manual-location');

    $locationCountry.value = $locationCity.value = $locationCoords.value = '';
    $sunriseTime.innerText = $sunsetTime.innerText = '';

    $locationSubmit.style.display = 'block';

    enable($locationCountry);
    enable($locationCity);
  }
});

$locationSubmit.addEventListener('click', () => {
  if ($locationCountry.value.length === 0 || $locationCity.value.length === 0) {
    $locationError.innerText = 'Wpisz kraj i miasto';
    $locationError.style.display = 'block';
    return;
  }

  $locationError.innerText = '';
  $locationError.style.display = 'none';

  const address = `${$locationCountry.value},${$locationCity.value}`;

  socket.emit('get-location-from-address', {address});
});
