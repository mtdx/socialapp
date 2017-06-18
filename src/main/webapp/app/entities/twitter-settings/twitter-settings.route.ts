import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TwitterSettingsComponent } from './twitter-settings.component';

export const twitterSettingsRoute: Routes = [
    {
        path: 'twitter-settings',
        component: TwitterSettingsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialappApp.twitterSettings.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
