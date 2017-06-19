import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Rx';
import { JhiLanguageService } from 'ng-jhipster';

import { TwitterSettings } from './twitter-settings.model';
import { TwitterSettingsService } from './twitter-settings.service';

@Component({
    selector: 'jhi-twitter-settings',
    templateUrl: './twitter-settings.component.html'
})
export class TwitterSettingsComponent implements OnInit, OnDestroy {
    twitterSettings: TwitterSettings;
    isSaving: boolean;
    success: boolean;

    constructor(
        private twitterSettingsService: TwitterSettingsService,
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.success = false;
        this.load();
    }

    load() {
        this.twitterSettingsService.find().subscribe((twitterSettings) => {
            this.twitterSettings = twitterSettings;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {}

    save() {
        this.isSaving = true;
    }
}
