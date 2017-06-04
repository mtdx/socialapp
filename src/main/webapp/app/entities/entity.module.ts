import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SocialappAvatarModule } from './avatar/avatar.module';
import { SocialappHeaderModule } from './header/header.module';
import { SocialappProxyModule } from './proxy/proxy.module';
import { SocialappCompetitorModule } from './competitor/competitor.module';
import { SocialappTwitterAccountModule } from './twitter-account/twitter-account.module';
import { SocialappTwitterErrorModule } from './twitter-error/twitter-error.module';
import { SocialappTwitterFollowerModule } from './twitter-follower/twitter-follower.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SocialappAvatarModule,
        SocialappHeaderModule,
        SocialappProxyModule,
        SocialappCompetitorModule,
        SocialappTwitterAccountModule,
        SocialappTwitterErrorModule,
        SocialappTwitterFollowerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappEntityModule {}
