import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RetweetAccountComponent } from './retweet-account.component';
import { RetweetAccountDetailComponent } from './retweet-account-detail.component';
import { RetweetAccountPopupComponent } from './retweet-account-dialog.component';
import { RetweetAccountDeletePopupComponent } from './retweet-account-delete-dialog.component';

export const retweetAccountRoute: Routes = [
    {
        path: 'retweet-account',
        component: RetweetAccountComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.retweetAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'retweet-account/:id',
        component: RetweetAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.retweetAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const retweetAccountPopupRoute: Routes = [
    {
        path: 'retweet-account-new',
        component: RetweetAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.retweetAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'retweet-account/:id/edit',
        component: RetweetAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.retweetAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'retweet-account/:id/delete',
        component: RetweetAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.retweetAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
