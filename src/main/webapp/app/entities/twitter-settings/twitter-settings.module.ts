import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    TwitterSettingsComponent,
    twitterSettingsRoute,
} from './';

const ENTITY_STATES = [
    ...twitterSettingsRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterSettingsComponent,
    ],
    entryComponents: [
        TwitterSettingsComponent,
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappTwitterSettingsModule {}
