import time

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Use a service account

cred = credentials.Certificate('firebase_key.json')
firebase_admin.initialize_app(cred)


class FirebaseManager:
    def __init__(self):
        self.db = firestore.client()

    def read(self):
        lampa_ref = self.db.collection(u'lampa')
        docs = lampa_ref.stream()
        for doc in docs:
            print(u'{} => {}'.format(doc.id, doc.to_dict()))

    @staticmethod
    def on_snapshot(doc_snapshot, changes, read_time):
        for doc in doc_snapshot:
            print(u'Received document snapshot: {}'.format(doc.id))

    def watch_for_snapshots(self):
        doc_ref = self.db.collection('commands').document('test')
        doc_watch = doc_ref.on_snapshot(self.on_snapshot)
        while True:
            time.sleep(1)
            print('processing...')
