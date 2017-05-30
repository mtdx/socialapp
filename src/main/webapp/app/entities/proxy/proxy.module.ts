import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    ProxyService,
    ProxyPopupService,
    ProxyComponent,
    ProxyDetailComponent,
    ProxyDialogComponent,
    ProxyPopupComponent,
    ProxyDeletePopupComponent,
    ProxyDeleteDialogComponent,
    proxyRoute,
    proxyPopupRoute,
} from './';

const ENTITY_STATES = [
    ...proxyRoute,
    ...proxyPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProxyComponent,
        ProxyDetailComponent,
        ProxyDialogComponent,
        ProxyDeleteDialogComponent,
        ProxyPopupComponent,
        ProxyDeletePopupComponent,
    ],
    entryComponents: [
        ProxyComponent,
        ProxyDialogComponent,
        ProxyPopupComponent,
        ProxyDeleteDialogComponent,
        ProxyDeletePopupComponent,
    ],
    providers: [
        ProxyService,
        ProxyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappProxyModule {}
