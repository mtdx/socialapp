import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterAccountService,
    TwitterAccountPopupService,
    TwitterAccountComponent,
    TwitterAccountDetailComponent,
    TwitterAccountDialogComponent,
    TwitterAccountPopupComponent,
    TwitterAccountDeletePopupComponent,
    TwitterAccountDeleteDialogComponent,
    twitterAccountRoute,
    twitterAccountPopupRoute,
} from './';

const ENTITY_STATES = [
    ...twitterAccountRoute,
    ...twitterAccountPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterAccountComponent,
        TwitterAccountDetailComponent,
        TwitterAccountDialogComponent,
        TwitterAccountDeleteDialogComponent,
        TwitterAccountPopupComponent,
        TwitterAccountDeletePopupComponent,
    ],
    entryComponents: [
        TwitterAccountComponent,
        TwitterAccountDialogComponent,
        TwitterAccountPopupComponent,
        TwitterAccountDeleteDialogComponent,
        TwitterAccountDeletePopupComponent,
    ],
    providers: [
        TwitterAccountService,
        TwitterAccountPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterAccountModule {}
