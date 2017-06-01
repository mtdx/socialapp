import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterErrorService,
    TwitterErrorPopupService,
    TwitterErrorComponent,
    TwitterErrorDetailComponent,
    TwitterErrorDialogComponent,
    TwitterErrorPopupComponent,
    TwitterErrorDeletePopupComponent,
    TwitterErrorDeleteDialogComponent,
    twitterErrorRoute,
    twitterErrorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...twitterErrorRoute,
    ...twitterErrorPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterErrorComponent,
        TwitterErrorDetailComponent,
        TwitterErrorDialogComponent,
        TwitterErrorDeleteDialogComponent,
        TwitterErrorPopupComponent,
        TwitterErrorDeletePopupComponent,
    ],
    entryComponents: [
        TwitterErrorComponent,
        TwitterErrorDialogComponent,
        TwitterErrorPopupComponent,
        TwitterErrorDeleteDialogComponent,
        TwitterErrorDeletePopupComponent,
    ],
    providers: [
        TwitterErrorService,
        TwitterErrorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterErrorModule {}
