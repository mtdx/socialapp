import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    HeaderService,
    HeaderPopupService,
    HeaderComponent,
    HeaderDetailComponent,
    HeaderDialogComponent,
    HeaderPopupComponent,
    HeaderDeletePopupComponent,
    HeaderDeleteDialogComponent,
    headerRoute,
    headerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...headerRoute,
    ...headerPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HeaderComponent,
        HeaderDetailComponent,
        HeaderDialogComponent,
        HeaderDeleteDialogComponent,
        HeaderPopupComponent,
        HeaderDeletePopupComponent,
    ],
    entryComponents: [
        HeaderComponent,
        HeaderDialogComponent,
        HeaderPopupComponent,
        HeaderDeleteDialogComponent,
        HeaderDeletePopupComponent,
    ],
    providers: [
        HeaderService,
        HeaderPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappHeaderModule {}
