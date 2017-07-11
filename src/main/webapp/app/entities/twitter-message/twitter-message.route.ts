import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TwitterMessageComponent } from './twitter-message.component';
import { TwitterMessageDetailComponent } from './twitter-message-detail.component';
import { TwitterMessagePopupComponent } from './twitter-message-dialog.component';
import { TwitterMessageDeletePopupComponent } from './twitter-message-delete-dialog.component';

import { Principal } from '../../shared';

export const twitterMessageRoute: Routes = [
    {
        path: 'twitter-message',
        component: TwitterMessageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterMessage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'twitter-message/:id',
        component: TwitterMessageDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterMessage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const twitterMessagePopupRoute: Routes = [
    {
        path: 'twitter-message-new',
        component: TwitterMessagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterMessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-message/:id/edit',
        component: TwitterMessagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterMessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-message/:id/delete',
        component: TwitterMessageDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterMessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
