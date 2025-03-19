// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getStorage } from "@firebase/storage";
import { getAuth } from "firebase/auth";
import { getDatabase } from "firebase/database";
import { getFirestore } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAv95nCdBzWn7D41PmpMVb8NhmSRN5YZAY",
  authDomain: "toannv1712-c7ea9.firebaseapp.com",
  databaseURL: "https://toannv1712-c7ea9-default-rtdb.firebaseio.com",
  projectId: "toannv1712-c7ea9",
  storageBucket: "toannv1712-c7ea9.firebasestorage.app",
  messagingSenderId: "1053254127862",
  appId: "1:1053254127862:web:5ad5260edce2accbbd1cd7",
  measurementId: "G-R4BTGFRBXC",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
const realtimedb = getDatabase(app);
const db = getFirestore(app);
const storage = getStorage(app);
export { db, storage, realtimedb };
