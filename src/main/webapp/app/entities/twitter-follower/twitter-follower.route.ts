import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TwitterFollowerComponent } from './twitter-follower.component';
import { TwitterFollowerDetailComponent } from './twitter-follower-detail.component';
import { TwitterFollowerDeletePopupComponent } from './twitter-follower-delete-dialog.component';

import { Principal } from '../../shared';

export const twitterFollowerRoute: Routes = [
    {
        path: 'twitter-follower',
        component: TwitterFollowerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterFollower.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'twitter-follower/:id',
        component: TwitterFollowerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterFollower.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const twitterFollowerPopupRoute: Routes = [
    {
        path: 'twitter-follower/:id/delete',
        component: TwitterFollowerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterFollower.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
