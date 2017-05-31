import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CompetitorComponent } from './competitor.component';
import { CompetitorDetailComponent } from './competitor-detail.component';
import { CompetitorPopupComponent } from './competitor-dialog.component';
import { CompetitorDeletePopupComponent } from './competitor-delete-dialog.component';

import { Principal } from '../../shared';

export const competitorRoute: Routes = [
    {
        path: 'competitor',
        component: CompetitorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.competitor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'competitor/:id',
        component: CompetitorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.competitor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const competitorPopupRoute: Routes = [
    {
        path: 'competitor-new',
        component: CompetitorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.competitor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'competitor/:id/edit',
        component: CompetitorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.competitor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'competitor/:id/delete',
        component: CompetitorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.competitor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
