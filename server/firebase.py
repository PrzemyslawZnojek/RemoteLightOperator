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
