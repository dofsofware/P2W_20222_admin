import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DashboardComponent } from 'app/dashboard/dashboard.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'authentification',
        data: { pageTitle: 'play2WinAdminApp.authentification.home.title' },
        loadChildren: () => import('./authentification/authentification.module').then(m => m.AuthentificationModule),
      },
      {
        path: 'choix',
        data: { pageTitle: 'play2WinAdminApp.choix.home.title' },
        loadChildren: () => import('./choix/choix.module').then(m => m.ChoixModule),
      },
      {
        path: 'infos-abonne',
        data: { pageTitle: 'play2WinAdminApp.infosAbonne.home.title' },
        loadChildren: () => import('./infos-abonne/infos-abonne.module').then(m => m.InfosAbonneModule),
      },
      {
        path: 'incription',
        data: { pageTitle: 'play2WinAdminApp.incription.home.title' },
        loadChildren: () => import('./incription/incription.module').then(m => m.IncriptionModule),
      },
      {
        path: 'mot-de-passe-setting',
        data: { pageTitle: 'play2WinAdminApp.motDePasseSetting.home.title' },
        loadChildren: () => import('./mot-de-passe-setting/mot-de-passe-setting.module').then(m => m.MotDePasseSettingModule),
      },
      {
        path: 'play',
        data: { pageTitle: 'play2WinAdminApp.play.home.title' },
        loadChildren: () => import('./play/play.module').then(m => m.PlayModule),
      },
      {
        path: 'principes',
        data: { pageTitle: 'play2WinAdminApp.principes.home.title' },
        loadChildren: () => import('./principes/principes.module').then(m => m.PrincipesModule),
      },
      {
        path: 'profil',
        data: { pageTitle: 'play2WinAdminApp.profil.home.title' },
        loadChildren: () => import('./profil/profil.module').then(m => m.ProfilModule),
      },
      {
        path: 'reponse',
        data: { pageTitle: 'play2WinAdminApp.reponse.home.title' },
        loadChildren: () => import('./reponse/reponse.module').then(m => m.ReponseModule),
      },
      {
        path: 'restaure',
        data: { pageTitle: 'play2WinAdminApp.restaure.home.title' },
        loadChildren: () => import('./restaure/restaure.module').then(m => m.RestaureModule),
      },
      {
        path: 'resultat',
        data: { pageTitle: 'play2WinAdminApp.resultat.home.title' },
        loadChildren: () => import('./resultat/resultat.module').then(m => m.ResultatModule),
      },
      {
        path: 'saisie-code',
        data: { pageTitle: 'play2WinAdminApp.saisieCode.home.title' },
        loadChildren: () => import('./saisie-code/saisie-code.module').then(m => m.SaisieCodeModule),
      },
      {
        path: 'abonne',
        data: { pageTitle: 'play2WinAdminApp.abonne.home.title' },
        loadChildren: () => import('./abonne/abonne.module').then(m => m.AbonneModule),
      },
      {
        path: 'gains',
        data: { pageTitle: 'play2WinAdminApp.gains.home.title' },
        loadChildren: () => import('./gains/gains.module').then(m => m.GainsModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'play2WinAdminApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'recette',
        data: { pageTitle: 'play2WinAdminApp.recette.home.title' },
        loadChildren: () => import('./recette/recette.module').then(m => m.RecetteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
