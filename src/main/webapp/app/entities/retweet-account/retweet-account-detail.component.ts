import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { RetweetAccount } from './retweet-account.model';
import { RetweetAccountService } from './retweet-account.service';

@Component({
    selector: 'jhi-retweet-account-detail',
    templateUrl: './retweet-account-detail.component.html'
})
export class RetweetAccountDetailComponent implements OnInit, OnDestroy {

    retweetAccount: RetweetAccount;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private retweetAccountService: RetweetAccountService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRetweetAccounts();
    }

    load(id) {
        this.retweetAccountService.find(id).subscribe((retweetAccount) => {
            this.retweetAccount = retweetAccount;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRetweetAccounts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'retweetAccountListModification',
            (response) => this.load(this.retweetAccount.id)
        );
    }
}
