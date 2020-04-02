const firebase = require('firebase');
require('firebase/auth');
require('firebase/firestore');

const FIREBASE_CONFIG = {
  apiKey: "AIzaSyBzUW24ShfIvVz4O5VefnCJ699CguBf99o",
  authDomain: "smartlampa-ce48c.firebaseapp.com",
  databaseURL: "https://smartlampa-ce48c.firebaseio.com",
  projectId: "smartlampa-ce48c",
  storageBucket: "smartlampa-ce48c.appspot.com",
  messagingSenderId: "371665868257",
  appId: "1:371665868257:web:0ee85596191901c7186ea0",
  measurementId: "G-Y034DTW4CC"
};

class Firebase {
  constructor() {
    this.app = firebase.initializeApp(FIREBASE_CONFIG);
    this.auth = firebase.auth();
    this.db = firebase.firestore();

    this.currentUser = null;
    this.currentUserConfiguration = null;
  }

  async signIn({email, password}) {
    try {
      const response = await this.auth.signInWithEmailAndPassword(email, password);

      this.currentUser = response.user;

      return Promise.resolve({success: true});
    } catch (error) {
      const result = {success: false, error: 'Coś poszło nie tak'};

      switch (error.code) {
        case 'auth/user-not-found':
        case 'auth/wrong-password':
          result.error = 'Zły email lub hasło';
          break;

        default:
          result.error = 'Coś poszło nie tak :(';
      }

      return Promise.resolve(result);
    }
  }

  async readUserConfiguration() {
    if (!this.currentUser) {
      return;
    }
    
    const uid = this.currentUser.uid;
    const userConfigRef = this.db.collection('UserConfiguration').where('uid', '==', uid);

    const snapshot = await userConfigRef.get();

    if (snapshot.empty) {
      console.error('Snapshot empty');
      return;
    }

    this.currentUserConfiguration = snapshot.docs[0].data();

    return this.currentUserConfiguration;
  }

  onUserConfigChange(callback) {
    if (!this.currentUser) {
      return;
    }
    
    const uid = this.currentUser.uid;
    const query = this.db.collection('UserConfiguration').where('uid', '==', uid);
    
    query.onSnapshot(querySnapshot => {
      this.currentUserConfiguration = querySnapshot.docs[0].data();
      callback(this.currentUserConfiguration);
    });
  } 

  signOut() {
    this.auth.signOut();
    this.currentUser = null;
  }

  getCurrentUser() {
    return this.currentUser;
  }

  dumpUser() {
    return {
      email: this.currentUser.email,
      config: this.currentUserConfiguration,
    };
  }
}

module.exports = new Firebase();