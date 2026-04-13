import { Component } from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {AuthService} from '../../core/services/auth';
import {inject} from '@angular/core';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {

  private authService = inject(AuthService);
  private router = inject(Router);
logout(): void {
  this.authService.logout();
  this.router.navigate(['/login']);
}
}
