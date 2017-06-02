import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterErrorService,
    TwitterErrorPopupService,
    TwitterErrorComponent,
    TwitterErrorDetailComponent,
    twitterErrorRoute,
} from './';

const ENTITY_STATES = [
    ...twitterErrorRoute
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterErrorComponent,
        TwitterErrorDetailComponent,
    ],
    entryComponents: [
        TwitterErrorComponent,
    ],
    providers: [
        TwitterErrorService,
        TwitterErrorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterErrorModule {}
