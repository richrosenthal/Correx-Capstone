import {Router, RouterLink} from '@angular/router';
import { NgIf} from '@angular/common';
import { Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../core/services/auth';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [FormsModule, NgIf, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit{
  errorMessage = '';
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private http = inject(HttpClient);

  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  ngOnInit() {
    if(this.authService.isLoggedIn()){
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit(): void {
    this.errorMessage = '';

    const username = this.loginForm.value.username ?? '';
    const password = this.loginForm.value.password ?? '';

    this.http.post('/api/auth/login', {
      username,
      password
    }).subscribe({
      next: () => {
        localStorage.setItem('corrix_logged_in', 'true');
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.errorMessage = 'Invalid username or password.';
      }
    });
  }

}
