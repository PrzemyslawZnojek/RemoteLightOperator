from flask import Flask
import json

from firebase import FirebaseManager

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello, World!'


@app.route('/api')
def api():
    return json.dumps({'dziala?': 'ta'})


@app.route('/test_firebase')
def firebase():
    fb = FirebaseManager()
    fb.read()


firebase()
