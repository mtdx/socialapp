import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialappSharedModule } from '../../shared';
import {
    CompetitorService,
    CompetitorPopupService,
    CompetitorComponent,
    CompetitorDetailComponent,
    CompetitorDialogComponent,
    CompetitorPopupComponent,
    CompetitorDeletePopupComponent,
    CompetitorDeleteDialogComponent,
    competitorRoute,
    competitorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...competitorRoute,
    ...competitorPopupRoute,
];

@NgModule({
    imports: [
        SocialappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CompetitorComponent,
        CompetitorDetailComponent,
        CompetitorDialogComponent,
        CompetitorDeleteDialogComponent,
        CompetitorPopupComponent,
        CompetitorDeletePopupComponent,
    ],
    entryComponents: [
        CompetitorComponent,
        CompetitorDialogComponent,
        CompetitorPopupComponent,
        CompetitorDeleteDialogComponent,
        CompetitorDeletePopupComponent,
    ],
    providers: [
        CompetitorService,
        CompetitorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappCompetitorModule {}
