const http = require('http');

const interval = 3000;
const mac = '00:00:00:00:00:' + Math.floor(Math.random() * 100);

function sendRequest() {
  const options = {
    method: 'POST',
    host: 'localhost',
    path: '/api/sensor_entry',
    port: 5000,
    headers: {
      'Content-Type': 'application/json'
    }
  };
  
  const postRequest = http.request(options, res => {
    console.log('Request sent with mac: ', mac);
  });

  postRequest.write(JSON.stringify({
    mac,
    measurement: Math.floor(Math.random() * 100)
  }));
  postRequest.end();
}

setInterval(sendRequest, interval);