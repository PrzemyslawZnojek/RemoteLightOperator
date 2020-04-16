const publicIp = require('public-ip');
const geoip = require('geoip-lite');
const sunriseSunset = require('sunrise-sunset-js');
const NodeGeocoder = require('node-geocoder');

const geocoder = NodeGeocoder({provider: 'openstreetmap'});

async function getCoordinatesFromAddress(address) {
  const [result] = await geocoder.geocode({
    q: address
  });
  
  if (!result) {
    return null;
  }

  return {
    country: result.country,
    city: result.city,
    lat: result.latitude,
    lon: result.longitude
  };
}

class Config {
  constructor() {
    this.locationFromIp = true;
    this.location = null;

    this.sunriseTime = null;
    this.sunsetTime = null;
  }

  async setupLocation() {
    if (!this.location) {
      await this.setLocationFromIp();
    }
    
    this.setSunriseSunset();
  }

  async setLocationFromIp() {
    const ip = await publicIp.v4();
    const geo = geoip.lookup(ip);

    const location = {
      country: 'Polska',
      city: geo.city,
      lat: geo.ll[0],
      lon: geo.ll[1]
    };

    this.location = location;

    return location;
  }

  async setLocationFromAddress(address, onReady) {
    const res = await getCoordinatesFromAddress(address);
    
    if (!res) {
      return;
    }

    this.location = res;
    this.setSunriseSunset();

    onReady({
      ...this.location, 
      sunriseTime: this.sunriseTime, 
      sunsetTime: this.sunsetTime,
    });
  }

  setSunriseSunset() {
    const {lat, lon} = this.location;

    this.sunriseTime = sunriseSunset.getSunrise(lat, lon);
    this.sunsetTime = sunriseSunset.getSunset(lat, lon);
  }

  async setLocationAutoMode(onReady) {
    this.locationFromIp = true;

    await this.setLocationFromIp();
    this.setSunriseSunset();

    onReady({
      ...this.location, 
      sunriseTime: this.sunriseTime, 
      sunsetTime: this.sunsetTime,
    });
  }

  setLocationManualMode() {
    this.locationFromIp = false;
    this.location = null;
    this.sunriseTime = null;
    this.sunsetTime = null;
  }

  dump() {
    return {
      ...this.location,
      locationFromIp: this.locationFromIp,
      sunriseTime: this.sunriseTime,
      sunsetTime: this.sunsetTime
    };
  }
}

module.exports = Config;