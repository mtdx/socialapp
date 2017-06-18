import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { TwitterSettings } from './twitter-settings.model';

@Component({
    selector: 'jhi-twitter-settings',
    templateUrl: './twitter-settings.component.html'
})
export class TwitterSettingsComponent implements OnInit {
    twitterSettings: TwitterSettings;
    authorities: any[];
    isSaving: boolean;
    success: boolean;

    constructor(

    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.success = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    save() {
        this.isSaving = true;
    }
}
