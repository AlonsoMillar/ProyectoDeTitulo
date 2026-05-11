import { initializeApp, getApps } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyD5L0pwNAVnf5d03FKLNgDZocHWcmexouE",
  authDomain: "programwork-333af.firebaseapp.com",
  projectId: "programwork-333af",
  storageBucket: "programwork-333af.firebasestorage.app",
  messagingSenderId: "902749656527",
  appId: "1:902749656527:web:146139991fbf709cb72eba"
};

//  Inicializa Firebase UNA sola vez
if (!getApps().length) {
  initializeApp(firebaseConfig);
}

//  Exporta auth listo para usar
export const auth = getAuth();