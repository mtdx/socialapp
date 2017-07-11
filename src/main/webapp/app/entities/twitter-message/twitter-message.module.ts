import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterMessageService,
    TwitterMessagePopupService,
    TwitterMessageComponent,
    TwitterMessageDetailComponent,
    TwitterMessageDialogComponent,
    TwitterMessagePopupComponent,
    TwitterMessageDeletePopupComponent,
    TwitterMessageDeleteDialogComponent,
    twitterMessageRoute,
    twitterMessagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...twitterMessageRoute,
    ...twitterMessagePopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterMessageComponent,
        TwitterMessageDetailComponent,
        TwitterMessageDialogComponent,
        TwitterMessageDeleteDialogComponent,
        TwitterMessagePopupComponent,
        TwitterMessageDeletePopupComponent,
    ],
    entryComponents: [
        TwitterMessageComponent,
        TwitterMessageDialogComponent,
        TwitterMessagePopupComponent,
        TwitterMessageDeleteDialogComponent,
        TwitterMessageDeletePopupComponent,
    ],
    providers: [
        TwitterMessageService,
        TwitterMessagePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterMessageModule {}
