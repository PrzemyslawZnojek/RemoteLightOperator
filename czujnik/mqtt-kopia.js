//instalacja: https://www.npmjs.com/package/mqtt?fbclid=IwAR1tMyP3seQhTgaeRS_zmpJ_sg1B4BCTVZ9VwrHuy_4x5s1Ej6MJBTAEpG4

var mqtt = require('mqtt')
var client  = mqtt.connect('mqtt://192.168.0.14')

client.on('connect', function () {
  client.subscribe('esp8266', function (err) {
    if (!err) {
      client.publish('esp8266', 'Hello mqtt')
    }
  })
})

client.on('message', function (topic, message) {
  // message is Buffer
  console.log(message.toString())
})
