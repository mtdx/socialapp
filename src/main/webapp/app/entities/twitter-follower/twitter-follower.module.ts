import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterFollowerService,
    TwitterFollowerPopupService,
    TwitterFollowerComponent,
    TwitterFollowerDetailComponent,
    TwitterFollowerDialogComponent,
    TwitterFollowerPopupComponent,
    TwitterFollowerDeletePopupComponent,
    TwitterFollowerDeleteDialogComponent,
    twitterFollowerRoute,
    twitterFollowerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...twitterFollowerRoute,
    ...twitterFollowerPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterFollowerComponent,
        TwitterFollowerDetailComponent,
        TwitterFollowerDialogComponent,
        TwitterFollowerDeleteDialogComponent,
        TwitterFollowerPopupComponent,
        TwitterFollowerDeletePopupComponent,
    ],
    entryComponents: [
        TwitterFollowerComponent,
        TwitterFollowerDialogComponent,
        TwitterFollowerPopupComponent,
        TwitterFollowerDeleteDialogComponent,
        TwitterFollowerDeletePopupComponent,
    ],
    providers: [
        TwitterFollowerService,
        TwitterFollowerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterFollowerModule {}
