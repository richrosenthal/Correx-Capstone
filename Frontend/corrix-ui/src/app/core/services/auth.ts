import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private storageKey = 'corrix_logged_in';

  login(username: string, password: string): boolean {
    const isValid = username === 'admin' && password === 'admin123';

    if(isValid){
      localStorage.setItem(this.storageKey, 'true');
    }
    return isValid;
  }

  isLoggedIn(): boolean {
    return localStorage.getItem(this.storageKey) === 'true';
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
  }

}
