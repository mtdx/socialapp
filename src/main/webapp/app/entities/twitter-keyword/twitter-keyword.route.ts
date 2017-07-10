import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TwitterKeywordComponent } from './twitter-keyword.component';
import { TwitterKeywordDetailComponent } from './twitter-keyword-detail.component';
import { TwitterKeywordPopupComponent } from './twitter-keyword-dialog.component';
import { TwitterKeywordDeletePopupComponent } from './twitter-keyword-delete-dialog.component';

import { Principal } from '../../shared';

export const twitterKeywordRoute: Routes = [
    {
        path: 'twitter-keyword',
        component: TwitterKeywordComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterKeyword.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'twitter-keyword/:id',
        component: TwitterKeywordDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterKeyword.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const twitterKeywordPopupRoute: Routes = [
    {
        path: 'twitter-keyword-new',
        component: TwitterKeywordPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterKeyword.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-keyword/:id/edit',
        component: TwitterKeywordPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterKeyword.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-keyword/:id/delete',
        component: TwitterKeywordDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterKeyword.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
