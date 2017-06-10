import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { TwitterAccount } from './twitter-account.model';
import { TwitterAccountService } from './twitter-account.service';

@Component({
    selector: 'jhi-twitter-account-detail',
    templateUrl: './twitter-account-detail.component.html'
})
export class TwitterAccountDetailComponent implements OnInit, OnDestroy {

    twitterAccount: TwitterAccount;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private twitterAccountService: TwitterAccountService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTwitterAccounts();
    }

    load(id) {
        this.twitterAccountService.find(id).subscribe((twitterAccount) => {
            this.twitterAccount = twitterAccount;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTwitterAccounts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'twitterAccountListModification',
            (response) => this.load(this.twitterAccount.id)
        );
    }
}
