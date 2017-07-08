import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TwitterErrorComponent } from './twitter-error.component';
import { TwitterErrorDetailComponent } from './twitter-error-detail.component';

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
