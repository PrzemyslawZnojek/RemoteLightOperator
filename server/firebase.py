import time

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

credentials = credentials.Certificate('firebase_key.json')
firebase_admin.initialize_app(credentials)

class FirebaseManager:
    def __init__(self):
        self.db = firestore.client()

    def read(self):
        lampa_ref = self.db.collection(u'lampa')
        docs = lampa_ref.stream()
        for doc in docs:
            print(u'{} => {}'.format(doc.id, doc.to_dict()))

    def read_plant_settings(self):
        lampa_ref = self.db.collection(u'PlantTemplate')
        docs = lampa_ref.stream()
        for doc in docs:
            print(u'{} => {}'.format(doc.id, doc.to_dict()))
