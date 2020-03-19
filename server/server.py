from pprint import pprint

from flask import Flask, request, jsonify
import json
from firebase import FirebaseManager

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello, World!'


@app.route('/api')
def api():
    return json.dumps({'dziala?': 'ta'})


@app.route('/light_measures', methods=['POST'])
def light_measures():
    data = request.form
    pprint(data)
    return jsonify(data)

@app.route('/test_firebase')
def firebase():
    fb = FirebaseManager()
    print('Wyniki z firebase:')
    fb.read()
    return {}
