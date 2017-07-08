import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TwitterAccountComponent } from './twitter-account.component';
import { TwitterAccountDetailComponent } from './twitter-account-detail.component';
import { TwitterAccountPopupComponent } from './twitter-account-dialog.component';
import { TwitterAccountDeletePopupComponent } from './twitter-account-delete-dialog.component';

import { Principal } from '../../shared';

export const twitterAccountRoute: Routes = [
    {
        path: 'twitter-account',
        component: TwitterAccountComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'twitter-account/:id',
        component: TwitterAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const twitterAccountPopupRoute: Routes = [
    {
        path: 'twitter-account-new',
        component: TwitterAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-account/:id/edit',
        component: TwitterAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-account/:id/delete',
        component: TwitterAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
