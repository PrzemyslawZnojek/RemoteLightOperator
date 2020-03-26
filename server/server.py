import json
import random

from flask import Flask, request, jsonify, render_template
from firebase import FirebaseManager

from controller import Controller
from sensor import Sensor


app = Flask(__name__)
ctr = Controller()

@app.route('/')
def index():
  return render_template('index.html', data=ctr.dump_data())


@app.route('/sensor/<mac>')
def show_sensor(mac):
  sensor = ctr.get_sensor(mac)
  return render_template('sensor.html', data=sensor.dump_data())


@app.route('/api/version')
def api_version():
    return u'v0.0.1'


@app.route('/api/sensor_entry', methods=['POST'])
def api_sensor_entry():
  data = request.get_json()

  if not data:
    return

  sensor = ctr.get_sensor(data['mac'])

  if not sensor:
    sensor = Sensor(ip=request.remote_addr, mac=data['mac'])
    ctr.add_sensor(sensor)

  sensor.add_entry(float(data['measurement']))

  return jsonify({'success': True})
