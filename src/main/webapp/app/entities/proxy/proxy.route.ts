import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProxyComponent } from './proxy.component';
import { ProxyDetailComponent } from './proxy-detail.component';
import { ProxyPopupComponent } from './proxy-dialog.component';
import { ProxyDeletePopupComponent } from './proxy-delete-dialog.component';

import { Principal } from '../../shared';

export const proxyRoute: Routes = [
    {
        path: 'proxy',
        component: ProxyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.proxy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'proxy/:id',
        component: ProxyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.proxy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const proxyPopupRoute: Routes = [
    {
        path: 'proxy-new',
        component: ProxyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.proxy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'proxy/:id/edit',
        component: ProxyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.proxy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'proxy/:id/delete',
        component: ProxyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.proxy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
