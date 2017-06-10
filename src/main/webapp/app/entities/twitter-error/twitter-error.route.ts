import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TwitterErrorComponent } from './twitter-error.component';
import { TwitterErrorDetailComponent } from './twitter-error-detail.component';
import { TwitterErrorPopupComponent } from './twitter-error-dialog.component';
import { TwitterErrorDeletePopupComponent } from './twitter-error-delete-dialog.component';

import { Principal } from '../../shared';

export const twitterErrorRoute: Routes = [
    {
        path: 'twitter-error',
        component: TwitterErrorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterError.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'twitter-error/:id',
        component: TwitterErrorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterError.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const twitterErrorPopupRoute: Routes = [
    {
        path: 'twitter-error-new',
        component: TwitterErrorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterError.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-error/:id/edit',
        component: TwitterErrorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterError.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-error/:id/delete',
        component: TwitterErrorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterError.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
