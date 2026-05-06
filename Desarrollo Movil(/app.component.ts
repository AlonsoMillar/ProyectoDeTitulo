import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { GoogleAuth } from '@codetrix-studio/capacitor-google-auth';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone:false
})
export class AppComponent {

  constructor(private platform: Platform) {
    this.platform.ready().then(() => {
      this.initializeGoogle();
    });
  }

  initializeGoogle() {
    GoogleAuth.initialize({
      clientId: '902749656527-0f8scgl1n3jfmdt11m8u6k8m8du6f923.apps.googleusercontent.com',
      scopes: ['profile', 'email'],
      grantOfflineAccess: true
    });
  }
}