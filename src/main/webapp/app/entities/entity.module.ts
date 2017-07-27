import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SocialappAvatarModule } from './avatar/avatar.module';
import { SocialappHeaderModule } from './header/header.module';
import { SocialappProxyModule } from './proxy/proxy.module';
import { SocialappCompetitorModule } from './competitor/competitor.module';
import { SocialappTwitterAccountModule } from './twitter-account/twitter-account.module';
import { SocialappTwitterErrorModule } from './twitter-error/twitter-error.module';
import { SocialappTwitterSettingsModule } from './twitter-settings/twitter-settings.module';
import { SocialappTwitterKeywordModule } from './twitter-keyword/twitter-keyword.module';
import { SocialappTwitterMessageModule } from './twitter-message/twitter-message.module';
import { SocialappRetweetAccountModule } from './retweet-account/retweet-account.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SocialappAvatarModule,
        SocialappHeaderModule,
        SocialappProxyModule,
        SocialappCompetitorModule,
        SocialappTwitterAccountModule,
        SocialappTwitterErrorModule,
        SocialappTwitterSettingsModule,
        SocialappTwitterKeywordModule,
        SocialappTwitterMessageModule,
        SocialappRetweetAccountModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialappEntityModule {}
