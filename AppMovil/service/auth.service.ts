import { Injectable } from '@angular/core';
import { GoogleAuth } from '@codetrix-studio/capacitor-google-auth';
import {
  signInWithCredential,
  GoogleAuthProvider,
  signOut,
  getAuth
} from 'firebase/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private auth = getAuth();

  async login() {

    const googleUser = await GoogleAuth.signIn();

    const idToken = googleUser.authentication?.idToken;
    if (!idToken) throw new Error('No idToken');

    const credential = GoogleAuthProvider.credential(idToken);

    const result = await signInWithCredential(this.auth, credential);

    return result.user;
  }

  async logout() {
    await GoogleAuth.signOut();
    await signOut(this.auth);
  }
}