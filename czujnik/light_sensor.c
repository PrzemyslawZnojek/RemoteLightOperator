#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "Adafruit_APDS9960.h"

Adafruit_APDS9960 apds;

const char* ssid = "<ssid>";                   // wifi ssid
const char* password =  "<passwd>";         // wifi password
const char* mqttServer = "192.168.0.14";    // IP adress Raspberry Pi
const int mqttPort = 1883;
const char* mqttUser = "";      // if you don't have MQTT Username, no need input
const char* mqttPassword = "";  // if you don't have MQTT Password, no need input

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {

  Serial.begin(115200);

  //WIFI:
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");

  client.setServer(mqttServer, mqttPort);

  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");

    if (client.connect("ESP8266Client", mqttUser, mqttPassword )) {
      Serial.println("connected");
    } else {
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }


  //ADAFRUIT:
  Serial.begin(115200);

  if(!apds.begin()){
    Serial.println("failed to initialize device! Please check your wiring.");
       client.publish("esp8266", "Prawdopodobnie kable sa zle!!!");
   client.subscribe("esp8266");

  }
  else Serial.println("Device initialized!");

  //enable color sensign mode
  apds.enableColor(true);

}

void loop() {

  //create some variables to store the color data in
  uint16_t r, g, b, c;
//
  //wait for color data to be ready
//  while(!apds.colorDataReady()){
//    delay(5);
//  }

  //get the data and print the different channels
   apds.getColorData(&r, &g, &b, &c);
   char wynik[1000];
   sprintf(wynik, "red: %d, green: %d, blue: %d, clear_light: %d", r,g,b,c);
   client.publish("esp8266", wynik);
   client.subscribe("esp8266");
   delay(2000);
   client.loop();
}
