const SENSOR_TTL = 10;

class Sensor {
  constructor({ip, mac}) {
    this.ip = ip;
    this.mac = mac;

    this.ttl = SENSOR_TTL;
    this.active = false;
    this.entries = [];
  }

  decrementTTL() {
    if (!this.active) {
      return;
    }

    this.ttl -= 1;

    if (this.ttl === 0) {
      this.active = false;
    }
  }

  addEntry({measurement}) {
    this.entries.push({
      measurement,
      timestamp: Date.now(),
    });
    this.ttl = SENSOR_TTL;
    this.active = true;
  }

  dump() {
    const lastTimestamp = this.entries.length ? this.entries[this.entries.length - 1].timestamp : null;
    const lastMeasurement = this.entries.length ? this.entries[this.entries.length - 1].measurement : null;

    return {
      lastTimestamp,
      lastMeasurement,
      last100Reads: this.entries.slice(-100),
      ip: this.ip,
      mac: this.mac,
      ttl: this.ttl,
      active: this.active,
    };
  }
}

module.exports = Sensor;