const SENSOR_TTL = 10;

class Sensor {
  constructor({mac}) {
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

  addEntry({color, brightness}) {
    this.entries.push({
      brightness,
      color,
      timestamp: Date.now(),
    });
    this.ttl = SENSOR_TTL;
    this.active = true;
  }

  getLastEntry() {
    return this.entries[this.entries.length - 1];
  }

  dump() {
    const lastTimestamp = this.entries.length ? this.entries[this.entries.length - 1].timestamp : null;
    const lastMeasurement = this.entries.length ? this.entries[this.entries.length - 1].brightness : null;

    return {
      lastTimestamp,
      lastMeasurement,
      last100Reads: this.entries.slice(-100),
      mac: this.mac,
      ttl: this.ttl,
      active: this.active,
    };
  }
}

module.exports = Sensor;