import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterKeywordService,
    TwitterKeywordPopupService,
    TwitterKeywordComponent,
    TwitterKeywordDetailComponent,
    TwitterKeywordDialogComponent,
    TwitterKeywordPopupComponent,
    TwitterKeywordDeletePopupComponent,
    TwitterKeywordDeleteDialogComponent,
    twitterKeywordRoute,
    twitterKeywordPopupRoute,
} from './';

const ENTITY_STATES = [
    ...twitterKeywordRoute,
    ...twitterKeywordPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterKeywordComponent,
        TwitterKeywordDetailComponent,
        TwitterKeywordDialogComponent,
        TwitterKeywordDeleteDialogComponent,
        TwitterKeywordPopupComponent,
        TwitterKeywordDeletePopupComponent,
    ],
    entryComponents: [
        TwitterKeywordComponent,
        TwitterKeywordDialogComponent,
        TwitterKeywordPopupComponent,
        TwitterKeywordDeleteDialogComponent,
        TwitterKeywordDeletePopupComponent,
    ],
    providers: [
        TwitterKeywordService,
        TwitterKeywordPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterKeywordModule {}
