import { Routes } from '@angular/router';
import { AppShell } from './layout/app-shell/app-shell';
import { Login } from './features/auth/login/login';
import { Dashboard } from './features/dashboard/dashboard';
import { CapaList } from './features/capas/capa-list/capa-list';
import { CapaForm } from './features/capas/capa-form/capa-form';
import { CapaDetail } from './features/capas/capa-detail/capa-detail';
import { AgingReport } from './features/reports/aging-report/aging-report';
import {authGuard} from './core/guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },

  {
    path: '',
    component: AppShell,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'capas', component: CapaList },
      { path: 'capas/new', component: CapaForm },
      { path: 'capas/:id', component: CapaDetail },
      { path: 'capas/:id/edit', component: CapaForm },
      { path: 'reports/open-capa-aging', component: AgingReport }
    ]
  },

  { path: '**', redirectTo: '' }
];
