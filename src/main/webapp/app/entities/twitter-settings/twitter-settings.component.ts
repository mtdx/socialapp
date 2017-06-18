import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal, AccountService, JhiLanguageHelper } from '../../shared';

@Component({
    selector: 'jhi-twitter-settings',
    templateUrl: './twitter-settings.component.html'
})
export class TwitterSettingsComponent implements OnInit {
    error: string;
    success: string;
    twitterSettings: any;

    constructor(
        private account: AccountService,
        private principal: Principal
    ) {
    }

    ngOnInit() {

    }

    save() {
        this.account.save(this.twitterSettings).subscribe(() => {
            this.error = null;
            this.success = 'OK';
        }, () => {
            this.success = null;
            this.error = 'ERROR';
        });
    }
}
