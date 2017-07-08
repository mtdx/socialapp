import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { HeaderComponent } from './header.component';
import { HeaderDetailComponent } from './header-detail.component';
import { HeaderPopupComponent } from './header-dialog.component';
import { HeaderDeletePopupComponent } from './header-delete-dialog.component';

import { Principal } from '../../shared';

export const headerRoute: Routes = [
    {
        path: 'header',
        component: HeaderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.header.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'header/:id',
        component: HeaderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.header.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const headerPopupRoute: Routes = [
    {
        path: 'header-new',
        component: HeaderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.header.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'header/:id/edit',
        component: HeaderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.header.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'header/:id/delete',
        component: HeaderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.header.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
