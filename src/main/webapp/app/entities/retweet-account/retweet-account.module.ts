import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    RetweetAccountService,
    RetweetAccountPopupService,
    RetweetAccountComponent,
    RetweetAccountDetailComponent,
    RetweetAccountDialogComponent,
    RetweetAccountPopupComponent,
    RetweetAccountDeletePopupComponent,
    RetweetAccountDeleteDialogComponent,
    retweetAccountRoute,
    retweetAccountPopupRoute,
} from './';

const ENTITY_STATES = [
    ...retweetAccountRoute,
    ...retweetAccountPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RetweetAccountComponent,
        RetweetAccountDetailComponent,
        RetweetAccountDialogComponent,
        RetweetAccountDeleteDialogComponent,
        RetweetAccountPopupComponent,
        RetweetAccountDeletePopupComponent,
    ],
    entryComponents: [
        RetweetAccountComponent,
        RetweetAccountDialogComponent,
        RetweetAccountPopupComponent,
        RetweetAccountDeleteDialogComponent,
        RetweetAccountDeletePopupComponent,
    ],
    providers: [
        RetweetAccountService,
        RetweetAccountPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappRetweetAccountModule {}
