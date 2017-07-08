import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TwitterError } from './twitter-error.model';
import { TwitterErrorService } from './twitter-error.service';

@Component({
    selector: 'jhi-twitter-error-detail',
    templateUrl: './twitter-error-detail.component.html'
})
export class TwitterErrorDetailComponent implements OnInit, OnDestroy {

    twitterError: TwitterError;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private twitterErrorService: TwitterErrorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTwitterErrors();
    }

    load(id) {
        this.twitterErrorService.find(id).subscribe((twitterError) => {
            this.twitterError = twitterError;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTwitterErrors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'twitterErrorListModification',
            (response) => this.load(this.twitterError.id)
        );
    }
}
